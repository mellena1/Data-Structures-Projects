package edu.wit.comp2000.group14.application3;

import java.util.ArrayList;

/**
 *  <b>Train</b><br/>
 * 	Creates a train object that has properties of a real train.<br/>
 * 	Makes use of Passenger and Station classes in order to <br/>
 * 	communicate data between the classes.
 * @author John Yatco
 * @date 11/3/2016
 * @class COMP 2000 - 2016-3fa
 */

//Train Class
public class Train 
{
	//Integer values to track ticks for location and the max capacity of each train.
	private int location, maxCapacity;
	//Boolean values to see if train is at a station and if the train is inbound or outbound.
	private boolean atStation, clockWise;
	//Station value for what station the train is at.
	private Station currentStation;
	//Collection of Passengers on the train.
	private ArrayList<Passenger> listPassengers = new ArrayList<>();
	private String trainName;
	
	//Default Constructor.
	public Train()
	{
		Station s1 = new Station();
		
		trainName = "Default Train";
		currentStation = s1;
		location = 0;
		maxCapacity = 50;
		atStation = true;
		clockWise = true;
	}
	
	//Train with starting origin and inbound / outbound.
	public Train(Station orig, boolean inbound, String name)
	{
		trainName = name;
		currentStation = orig;
		location = 0;
		maxCapacity = 50;
		atStation = true;
		clockWise = inbound;
	}
	
	//Get name of train
	public String getName()
	{
		return trainName;
	}
	
	//Get location of train.
	public int getLocation()
	{
		return location;
	}
	
	//Set location of train.
	public void setLocation(int loc)
	{
		location = loc;
	}
	
	//Get boolean if train is at a station.
	public boolean getAtStation()
	{
		return atStation;
	}
	
	//Checks if train is full.
	public boolean isFull()
	{
		return listPassengers.size() == maxCapacity;
	}
	
	//Checks if train is empty.
	public boolean isEmpty()
	{
		return listPassengers.isEmpty();
	}
	
	//Boards passenger onto the train.
	public void board(Passenger person)
	{
		//Adds passenger to array list if less than capacity.
		if(listPassengers.size() < maxCapacity)
		{
			listPassengers.add(person);
		}
		else
		{
			//System.out.println("Train is full...");		//TESTING
		}
	}
	
	//Removes passengers from train if train is at passenger's destination.
	public String getOff()
	{		
		//Create temporary array.
		ArrayList<Passenger> tempP = new ArrayList<>();
		//Strings to manipulate returned statement.
		String ret = "";
		String tempName = "";
		//Integer to manipulate string statement.
		int includeAnd = 0;
		
		//If there is a passenger on board.
		if(!listPassengers.isEmpty())
		{
			//For each passenger in the list.
			for(Passenger p : listPassengers)
			{
				//If current passenger destination matches current station of train...
				if(this.currentStation.getName().equals(p.getDest().getName()))
				{
					//Adds person to temporary collection and adds names to string.
					//System.out.println(p.getName() + " has left train.");			//TESTING
					ret += p.getName() + ", ";
					tempP.add(p);
					includeAnd++;
				}
			}
			
			//If a person actually left the train...
			if(includeAnd != 0)
			{
				//If more than one person left the train...
				if(includeAnd != 1)
				{
					//Manipulate String to state correct sentence.
					tempName = tempP.get(tempP.size()-1).getName();
					ret = ret.substring(0, ret.length() - tempName.length() - 2);
					ret += "and " + tempName;
					ret = "\n" + ret;
				}
				else
				{
					//Remove the ", " for grammar.
					ret = ret.substring(0, ret.length() - 2);
					ret = "\n" + ret;
				}
				//Got off at station...
				ret += " got off Train " + this.getName() + " at " + this.getCurrentStation().getName() + ".";
				//Actually removes passengers from list.
				listPassengers.removeAll(tempP);
			}
		}
		
		else
		{
			//System.out.println("Train is empty...");			//TESTING
		}
		
		return ret;
	}
	
	//Returns passenger list size.
	public int getPassengers()
	{
		return listPassengers.size();
	}
	
	//Inbound is true.
	public boolean getInbound()
	{
		return clockWise;
	}
	
	//Set station.
	public void setStation(Station current)
	{
		currentStation = current;
		atStation = true;
	}
	
	//Leave station.
	public void leaveStation()
	{
		currentStation = null;
		atStation = false;
	}
	
	//Gets current station.
	public Station getCurrentStation()
	{
		return currentStation;
	}
	
	//Main for tests.
	public static void main(String[] args)
	{
		Station s1 =new Station("Station 1");
		Station s2 =new Station("Station 2");
		
		Train train1 = new Train(s1, true, "TOM");
		
		Passenger person1 = new Passenger("Joe", s1, s2);
		Passenger p2 = new Passenger("Philip", s1, s1);
		Passenger p4 = new Passenger("Someone", s1, s2);
		Passenger p3 = new Passenger("A Very Long Name", s1, s2);
		
		train1.board(person1);
		train1.board(p2);
		train1.board(p3);
		train1.board(p4);
		
		System.out.println("Passengers: " + train1.getPassengers());
		

		train1.setStation(s1);
		String done = train1.getOff();
		System.out.println(done);
		
		System.out.println("Passengers: " + train1.getPassengers());
		
		train1.setStation(s2);
		System.out.println("Train at: " + train1.getCurrentStation().getName());
		
		String test = train1.getOff();
		System.out.println(test);
	}
}
