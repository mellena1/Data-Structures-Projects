package edu.wit.comp2000.group14.application3;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 *  <b>TrainSimulator</b><br/>
 * 	Uses the Train, Passenger, Station, and TrainRoute classes<br/>
 * 	to create a working train simulation, and then logs it to<br/>
 * 	a file.
 * @author Andrew Mellen
 * @date 11/3/2016
 * @class COMP 2000 - 2016-3fa
 */
public class TrainSimulation {
	//File names
	private static String configFile = "config.txt";
	//Other objects
	private static ArrayList<Station> stations = new ArrayList<>();
	private static ArrayList<Train> trains = new ArrayList<>();
	private static TrainRoute route;
	//Constants (some from config file)
	private static int ENDOFTRACK;
	private static int RATEOFPASSENGERARRIVALFROMFILE;
	private static int MAXTICKS = 72;
	//Variables that depend on time
	private static int time = 0;
	private static int day = 1;
	private static int rateOfPassengerArrival;
	//list of names for passengers and stations
	private static ArrayList<String> peopleNames = new ArrayList<>();
	private static ArrayList<String> stationNames = new ArrayList<>();
	//String to hold log stuff to print
	private static String logText = "<html>\n<body style=\"background-color:#111E50\" text=\"white\">"; //set up html tags
	
	//----------------------------------MAIN SETUP AND TICK---------------------------------------------
	
	/** Main method */
	public static void main(String args[]){
		//Name log file with date and time of runtime
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH.mm.ss");
		String dateTime = sdf.format(cal.getTime());
		
		try(PrintWriter printer = new PrintWriter(new File(dateTime + " log.html"))){
			System.out.println("Simulation starting...");
			
			//Header for the log
			logText += "<b>**********Train Simulation**********</b>";
			print(printer);
			
			setup();
			print(printer);
			
			for (int x = 0; x < MAXTICKS; x++) { //MAXTICKS amount of times
				//Make the time look nice in the log
				logText = "\n<br/><b><u>";
				System.out.print("Made it to ");
				if(time > 12){
					logText += time + ":00 PM";
					System.out.print(time + ":00 PM");
				}
				else if(time == 0){
					logText += "12:00 AM";
					System.out.print("12:00 AM");
				}
				else{
					logText += time + ":00 AM";
					System.out.print(time + ":00 AM");
				}
				logText += " - Day " + day + "</u></b>";
				System.out.println(" - Day " + day);
				print(printer);
				
				//Change rate of passenger arrival depending on the time
				if (time == 7 || time == 8 || time == 17 || time == 18) //Busy times
					rateOfPassengerArrival = RATEOFPASSENGERARRIVALFROMFILE * 2;
				else //normal times
					rateOfPassengerArrival = RATEOFPASSENGERARRIVALFROMFILE;

				tick();
				print(printer);
				
				//iterate time
				if (time == 23){ //hitting midnight
					time = 0;
					day++;
				}
				else
					time++;
			} //end for loop
			
			//End of simulation logging
			logText += "\n<br/>\n</br><b>**********End of the Simulation**********</b>";
			for(Train t : trains){
				logText += "\n<br/>" + t.getPassengers() + " passengers left on Train " + t.getName();
			}
			for(Station s : stations){
				logText += "\n<br/>" + s;
			}
			print(printer);
			
			//close the html tags
			logText = "\n</body>\n</html>";
			print(printer);
		}catch (Exception ex){
			System.err.println("Something happened in the main method.");
			ex.printStackTrace();
		}
		System.out.println("Simulation over.\nOpening " + dateTime + " log.html for relevant information.");
		
		//Open the HTML file after program completes
		try {
			Desktop.getDesktop().browse(new File(dateTime + " log.html").toURI());
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/** Run on startup */
	private static void setup(){
		getNamesFromFile();
		getStationNamesFromFile();
		readFromFile();
		stationNames = null; //stationNames ArrayList no longer needed, garbagecollect
		makeMorePeople();
		for(Train t : trains){ //Board trains
			boardOrGetOff(t);
		}
		logText += "\n<br/>Setup Complete.<br/><br/><hr>";
	}
	
	/** Run every tick of the program<br/>
	 *  1. Make new passengers<br/>
	 *  2. Move the trains<br/>
	 *  3. Check if every train is at a station<br/>
	 *  4. Let passengers off and board new passengers */
	private static void tick(){
		//Spawn people
		String newPeople = makeMorePeople();
		if(!newPeople.equals("")){
			logText += "\n<br/>\n<br/><b>New Passengers Arriving at Stations:</b>";
			logText += newPeople;
		}
		//String to hold trains departing
		String trainsDeparting = "";
		//Move Train locations, get them out of stations
		for(Train t : trains){
			moveTrainForward(t);
			trainsDeparting += getTrainOutOfStation(t);
		}
		//If there were trains that departed, add the header
		if(!trainsDeparting.equals("")){
			logText += "\n<br/>\n<br/><b>Trains Leaving Stations:</b>";
			logText += trainsDeparting;
		}
		
		//Strings to hold trains and passengers coming in
		String trainsArriving = "";
		String passengersBoarding = "";
		//Put Trains in station, board and get passengers off
		for(Train t : trains){
			trainsArriving += tellTrainItsStation(t);
			passengersBoarding += boardOrGetOff(t);
		}
		//If there were trains that arrived, add the header
		if(!trainsArriving.equals("")){
			logText += "\n<br/>\n<br/><b>Trains Arriving at Stations:</b>";
			logText += trainsArriving;
		}
		//If there were passengers that boarded, add the header
		if(!passengersBoarding.equals("")){
			logText += "\n<br/>\n<br/><b>Passengers Boarding and Getting off:</b>";
			logText += passengersBoarding;
		}
		logText += "<br/><br/><hr>";
	}
	
	
	//---------------------------------TICK HELPER METHODS(MOVEMENT)----------------------------------
	
	/** If the train is at a station, take passengers off, put passengers on 
	 *  @param t  a Train
	 *  @return a string with every passenger that got off/on the train
	 */
	private static String boardOrGetOff(Train t){
		String passengersBoarding = "";
		//Get passengers off the train
		if(t.getAtStation()){
			String people = t.getOff();
			if(!people.equals(""))
				passengersBoarding += "</br>" + people; //add line break to it
		}
		
		//Put passengers on the train
		while(t.getAtStation() && !t.isFull()){ //train is at a station and isn't full
			Passenger pass;
			
			if(t.getInbound() && t.getCurrentStation().arePassInb()) //inbound and theres passengers
				pass = t.getCurrentStation().removePassIn();
			else if(!t.getInbound() && t.getCurrentStation().arePassOutb()) //outbound and theres passengers
				pass = t.getCurrentStation().removePassOut();
			else //theres no passengers left
				return passengersBoarding;
			
			t.board(pass); //board the passenger
			passengersBoarding += "\n<br/>" + pass.getName() + " has boarded Train " + t.getName() + " at " + t.getCurrentStation().getName() + ".";
		}
		return passengersBoarding;
	}
	
	/** Let a train know if it is at a station 
	 *  @param t  a train
	 *  @return a String with the train arriving at a station
	 */
	private static String tellTrainItsStation(Train t){
		String trainsArriving = "";
		if(route.atStation(t.getLocation())){ //at a station now
			t.setStation(route.getStationName(t.getLocation()));
			trainsArriving += "\n<br/>Train " + t.getName() + " going " + (t.getInbound()?"inbound":"outbound") 
								+ " has arrived at " + t.getCurrentStation().getName() + ".";
			if(t.getInbound())
				t.getCurrentStation().setInTrain(t);
			else
				t.getCurrentStation().setOutTrain(t);
		}
		return trainsArriving;
	}
	
	/** Let a train know it is no longer at a station */
	private static String getTrainOutOfStation(Train t){
		String trainsDeparting = "";
		if(t.getAtStation() && (route.getStationName(t.getLocation()) != t.getCurrentStation())){ //was at a station, no longer is
			trainsDeparting += "\n<br/>Train " + t.getName() + " going " + (t.getInbound()?"inbound":"outbound") + " departed " 
					+ t.getCurrentStation().getName() + ".";
			
			//Remove train from station
			if(t.getInbound()) //Train going inbound
				t.getCurrentStation().removeInTrain();
			else //train going outbound
				t.getCurrentStation().removeOutTrain();
			
			//Have train leave station
			t.leaveStation();
		}
		return trainsDeparting;
	}
	
	/** Move train forward one tick */
	private static void moveTrainForward(Train t){
		//Get current location
		int currentLocation = t.getLocation();
		if(t.getInbound()){ //train is inbound
			if(currentLocation == ENDOFTRACK) //train is at end of circle
				t.setLocation(0);
			else
				t.setLocation(++currentLocation);
		}
		else{ //train is outbound
			if(currentLocation == 0) //train is at end of circle
				t.setLocation(ENDOFTRACK);
			else
				t.setLocation(--currentLocation);
		}
	}
	
	
	//--------------------------------------GENERATE OBJECTS METHODS---------------------------------------
	
	/** Make array of new passengers with source and destination */
	private static String makeMorePeople(){
		String newPeople = "";
		//How many new passengers to add (between 0 and RateOfPassengerArrival)
		int numNewPassengers = (int)((rateOfPassengerArrival+1) * Math.random());
		
		//Make each passenger
		for(int x = 0; x < numNewPassengers; x++){
			int orgStation = (int)(stations.size() * Math.random());
			int destStation;
			
			do{ //Make sure the destination and origin station aren't the same
				destStation = (int)(stations.size() * Math.random());
			}while(orgStation == destStation);
			
			//Add passenger to station
			int nameIndex = (int)(peopleNames.size() * Math.random());
			//check which direction is closer, inbound or outboud
			boolean inBound = route.bestRoute(stations.get(orgStation), stations.get(destStation));
			if(inBound)
				stations.get(orgStation).addPassIn(new Passenger(peopleNames.get(nameIndex), stations.get(orgStation), stations.get(destStation)));
			else
				stations.get(orgStation).addPassOut(new Passenger(peopleNames.get(nameIndex), stations.get(orgStation), stations.get(destStation)));
			
			newPeople += "\n<br/>" + peopleNames.get(nameIndex) + " has arrived at " + stations.get(orgStation).getName() 
					+ " and is headed "+ (inBound?"inbound":"outbound") +" for " + stations.get(destStation).getName() + ".";
		}
		return newPeople;
	}
	
	/** Generates a certain amount of trains, puts them at stations */
	private static void generateTrains(int numberOfTrains){
		for(int x = 0; x < numberOfTrains; x++){
			//decide direction of the train
			boolean isInbound = true;
			if(x % 2 == 0){
				isInbound = false;
			}
			//Pick station, make sure station spot is not full
			int whichStation;
			do{
				//Put it at a random station
				whichStation = (int)(stations.size() * Math.random());
			}while(stations.get(whichStation).isThereATrain(isInbound));
			
			//Make the train
			trains.add(new Train(stations.get(whichStation), isInbound, "" + (x+1))); //make trains, put at stations
			//Set location of station from the train route
			trains.get(x).setLocation(route.stationsTickValue(stations.get(whichStation)));
			
			//Add the train to the station
			if(isInbound)
				stations.get(whichStation).setInTrain(trains.get(x));
			else
				stations.get(whichStation).setOutTrain(trains.get(x));
			logText += "\n<br/>" + (isInbound?"Inbound":"Outbound") + " train created at " + stations.get(whichStation).getName() + ".";
		}
	}
	
	/** Generates a certain amount of stations */
	private static void generateStations(int numberOfStations){
		for(int x = 0; x < numberOfStations; x++){
			int stationNameIndex = (int)(stationNames.size() * Math.random());
			stations.add(new Station(stationNames.get(stationNameIndex) + " Station")); //make stations
			logText += "\n<br/>" + stations.get(x).getName() + " created.";
		}
	}
	
	//----------------------------------READING AND WRITING FILES METHODS-------------------------------
	
	/** Read the startup data from the file */
	private static void readFromFile(){
		try(Scanner in = new Scanner(new File(configFile))){
			//****Random in.next() calls are to eat up Strings in config file****
			in.next();
			ENDOFTRACK = in.nextInt() - 1; //last index of route
			
			//Get number of trains and stations
			in.next();
			int numberOfTrains = in.nextInt();
			in.next();
			int numberOfStations = in.nextInt();
			
			//Make sure theres no overflow of trains
			if(numberOfTrains > numberOfStations * 2){
				numberOfTrains = numberOfStations * 2;
				logText += "\n<br/>Too many trains requested, defaulting to " + numberOfTrains + " trains.";
			}
			
			//Station locations
			ArrayList<Integer> whereStationsAre = new ArrayList<>();
			in.next();
			for(int x = 0; x < numberOfStations; x++){
				in.next();
				whereStationsAre.add(in.nextInt());
			}
			
			//Make stations
			generateStations(numberOfStations);
			logText += "\n<br/><b>" + numberOfStations + " Stations created.</b>";
			System.out.println(numberOfStations + " Stations created.");
			
			//Make the train route
			route = new TrainRoute(stations, whereStationsAre);
			
			//Rate passengers spawn at
			in.next();
			RATEOFPASSENGERARRIVALFROMFILE = in.nextInt();
			
			//Make trains
			generateTrains(numberOfTrains);
			logText += "\n<br/><b>" + numberOfTrains + " Trains created.</b>";
			System.out.println(numberOfTrains + " Trains created.");
		}catch(Exception ex){
			System.err.println("Something happened in the readFromFile method!");
			ex.printStackTrace();
		}
	}
	
	/** Set up name ArrayList from file */
	private static void getNamesFromFile(){
		try(Scanner in = new Scanner(new File("names.txt"))){
			while(in.hasNextLine()){
				String newName = in.next();
				newName = newName.substring(0, 1) + newName.substring(1, newName.length()).toLowerCase();
				peopleNames.add(newName);
				in.nextLine();
			}
		}catch(Exception ex){
			System.err.println("Something happened in the getNamesFromFile method.");
			ex.printStackTrace();
		}
	}
	
	/** Set up stationName ArrayList from file */
	private static void getStationNamesFromFile(){
		try(Scanner in = new Scanner(new File("stationNames.txt"))){
			while(in.hasNextLine()){
				String newName = in.next();
				newName = newName.substring(0, 1).toUpperCase() + newName.substring(1, newName.length());
				stationNames.add(newName);
			}
		}catch(Exception ex){
			System.err.println("Something happened in the getStationNamesFromFile method.");
			ex.printStackTrace();
		}
	}
	
	/** Print logText to the log file, then flush logText variable */
	private static void print(PrintWriter printer){
		printer.print(logText);
		logText = "";
	}
	
}
