package edu.wit.comp2000.group14.application3;

/**
 *  <b>Passengers</b><br/>
 * 	Creates a passenger object that has contains a <br/>
 * 	starting point and ending point. Each passenger has a name.
 * @author Brandon Simpson
 * @date 11/3/2016
 * @class COMP 2000 - 2016-3fa
 */

//Passenger class.
public class Passenger
{
	//Name.
	private String passName;
	//Station origin and destination.
	private Station origin, destination;
	
	//Default Constructor
	public Passenger()
	{
		this("Joe", null, null);
	}
	
	//Passenger constructor with name, origin, and destination.
	public Passenger(String name, Station org, Station dest)
	{
		passName = name;
		origin = org;
		destination = dest;
	}
	
	//Get origin station.
	public Station getOrig()
	{
		return origin;
	}
	
	//Get destination station.
	public Station getDest()
	{
		return destination;
	}
	
	//Set origin station.
	public void setOrig(Station s)
	{
		origin = s;
	}
	
	//Set destination station.
	public void setDest(Station s)
	{
		destination = s;
	}
	
	//Get name of passenger.
	public String getName()
	{
		return passName;
	}
	
	//Tests class.
	public static void main(String[] args)
	{
		Station s1 = new Station("Station 1");
		Station s2 = new Station("Station 2");
		
		Passenger p1 = new Passenger("Andrew", s1, s2);
		Passenger p2 = new Passenger("Anthony", s1, s1);
		Passenger p3 = new Passenger("Brandon", s1, s1);
		Passenger p4 = new Passenger("Eric", s1, s2);
		Passenger p5 = new Passenger("John", s1, s2);
		
		System.out.println(p1.getName() + ", " + p2.getName() + ", " + p3.getName() + ", " + p4.getName() + ", " + p5.getName());
		System.out.println(p1.getName() + "'s destination is: " + p1.getDest().getName());
		System.out.println(p2.getName() + "'s destination is: " + p2.getDest().getName());
		System.out.println(p3.getName() + "'s destination is: " + p3.getDest().getName());
		System.out.println(p4.getName() + "'s destination is: " + p4.getDest().getName());
		System.out.println(p5.getName() + "'s destination is: " + p5.getDest().getName());
	}
}

