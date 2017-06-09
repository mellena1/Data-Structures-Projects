package edu.wit.comp2000.group14.application3;

import com.pearson.carrano.*;
 /**
  * Station Class
  * @author Eric Wahlstrom
  * @date 11/3/2016
  * @class COMP 2000 - 2016-3fa
  */
public class Station
{
	private String stationName; //name of the Station
	private QueueInterface<Passenger> passengersIn, passengersOut; //In-bound platform and Out-bound platform
	private int numInboundPass, numOutboundPass; //Number of passengers on the In-bound platform and Out-bound platform
	private boolean outboundTrain, inboundTrain; //Indicates if there is a Train at the corresponding platforms 
	private Train inTrain, outTrain; //Train at the corresponding platforms
	
	//Default Constructor
	public Station()
	{
		this("Default Name");
	}
	
	/**
	 * Initializes the fields of the class
	 * @param sN - The Station's Name
	 */
	public Station(String sN)
	{
		stationName=sN;
		passengersIn=new ArrayQueue<>();
		passengersOut=new ArrayQueue<>();
		numInboundPass=0;
		numOutboundPass=0;
	}
	
	/**
	 * @param train - The Train being place at the Out-bound platform
	 */
	public void setOutTrain(Train train)
	{
		if(!outboundTrain)
		{
			outTrain = train;
			outboundTrain=true;
		}
		else
			System.err.println("There is already an Outbound Train here");
	}
	
	/**
	 * @param train - The Train being place at the In-bound platform
	 */
	public void setInTrain(Train train)
	{
		if(!inboundTrain)
		{
			inTrain = train;
			inboundTrain=true;
		}
		else
			System.err.println("There is already a Inbound Train here");
	}
	
	/**
	 * Removes the Train form the Out-bound platform
	 * @return Train from the In-bound platform
	 */
	public Train removeOutTrain()
	{
		if(outboundTrain)
		{
			outboundTrain=false;
			return outTrain;
		}
		else return null;
	}
	
	/**
	 * Removes the Train form the In-bound platform
	 * @return Train from the In-bound platform
	 */
	public Train removeInTrain()
	{
		if(inboundTrain)
		{
			inboundTrain=false;
			return inTrain;
		}
		else return null;
	}
	
	/**
	 * @param inOut - true to get in-bound, false to get out-bound
	 * @return whether or no there is a in-bound or out-bound Train in the Station
	 */
	public boolean isThereATrain(boolean inOut)
	{
		return(inOut)?inboundTrain:outboundTrain; 
	}
	
	/**
	 * @return The Name of the Station
	 */
	public String getName()
	{
		return stationName;
	}
	
	/**
	 * @param passIn - Passenger being added to the In-bound platform
	 */
	public void addPassIn(Passenger passIn)
	{
		passengersIn.enqueue(passIn);
		numInboundPass++;
	}
	
	/**
	 * @param passOut - Passenger being added to the Out-bound platform
	 */
	public void addPassOut(Passenger passOut)
	{
		passengersOut.enqueue(passOut);
		numOutboundPass++;
	}
	
	/**
	 * @return The last Passenger in the In-bound queue
	 */
	public Passenger removePassIn()
	{
		numInboundPass--;
		return passengersIn.dequeue();
	}
	
	/**
	 * @return The last Passenger in the Out-bound queue
	 */
	public Passenger removePassOut()
	{
		numOutboundPass--;
		return passengersOut.dequeue();
	}
	
	/**
	 * @return true if there are still passengers on the In-bound platform, false otherwise
	 */
	public boolean arePassInb()
	{
		return !passengersIn.isEmpty();
	}
	
	/**
	 * @return true if there are still passengers on the Out-bound platform, false otherwise
	 */
	public boolean arePassOutb()
	{
		return !passengersOut.isEmpty();
	}
	
	/**
	 * @return The Name of the Station along with the number of passenger on the In-bound/Out-bound Platforms and  
	 * if there is a Train at each platform 
	 */
	public String toString()
	{
		return "\t\t\t\t\t<b>"+getName()+"</b>\n<br/>Inbound Passenegrs: "+numInboundPass+" | Inbound Train: "+inboundTrain+" |"
				+ "  Outbound Train: "+outboundTrain+" |  Outbound Passenger: "+numOutboundPass; 
	}
	
	//Unit Tests
	public static void main(String[] args)
	{
		Passenger pOne = new Passenger(), pTwo = new Passenger();
		Train tOne = new Train(), tTwo = new Train();
		Station s = new Station();
		System.out.println(s.getName());
		s.addPassIn(pOne);
		s.addPassOut(pTwo);
		s.setInTrain(tOne);
		s.setOutTrain(tTwo);
		System.out.println(s.toString());
		s.removePassOut();
		s.removeInTrain();
		System.out.println(s.toString());
		System.out.println("Is there an out-bound Train: "+s.isThereATrain(false)+" | Is there an in-bound Train: "+s.isThereATrain(true));
		
	}
}