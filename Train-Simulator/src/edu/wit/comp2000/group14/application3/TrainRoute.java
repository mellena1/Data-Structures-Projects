package edu.wit.comp2000.group14.application3;
import java.util.ArrayList;

/**
 *  Train Route
 * 	The TrainRoute track the distences between station and sends out information on the fastest routes to take
 * 	
 * @author Anthony McCann
 * @date 11/3/2016
 * @class COMP 2000 - 2016-3fa
 */



public class TrainRoute {
	private ArrayList<Station> stations;
	private ArrayList<Integer> ticks;
	
	// DEFAULT CONTRUCTER
	// FOR MY CLASS TESTING
	public TrainRoute() {
		//stations = new Station[] {new Station(), new Station(), new Station(), new Station(),new Station(), new Station(), new Station()};
		//ticks = new Integer[] { 2, 5, 7, 12, 15, 17, 20 };
	}

	public TrainRoute(ArrayList<Station> stationsFromSim, ArrayList<Integer> ticksFromSim) {
		stations = stationsFromSim;
		ticks = ticksFromSim;
	}

	/** Make a method to return inbound/outbound */
	public boolean atStation(int currentTick) {
		for (int i = 0; i < ticks.size(); i++) {

			if (ticks.get(i).equals(currentTick)) {
				// System.out.println("At a Station");		//TESTING
				return true;
			} // end if
		} // end for
		return false;
	}// end atStation()

	/** Takes the currentTick location and find the next Inbound Station */
	public int distanceToNextInboundStation(int currentTick) {

		// If the current Tick is already @ a station
		int tempTick = currentTick;
		// increment tempTick (because it could be at a station already
		tempTick++;
		// finds the next station
		while (!atStation(tempTick)) {
			tempTick++;
		}
		// distance = next Stations Tick - current Tick
		int distance = tempTick - currentTick;
		//System.out.println("Distance to next Staion: " + distance);		//TESTING
		return distance;

	}
	/** Takes the currentTick location and find the next Outbound Station */
	public int distanceToNextOutboundStation(int currentTick) {

		// If the current Tick is already @ a station
		int tempTick = currentTick;
		tempTick--;
		// finds the next station by decrementing tempTick
		while (!atStation(tempTick)) {
			tempTick--;
		}
		// distance = current Tick - Next Stations Tick
		int distance = currentTick - tempTick;
		//System.out.println("Distance to next Staion: " + distance);		//TESTING
		return distance;

	}

	/**  Tackes the currentTick and returns the closest station(inbound or outbound) */
	public boolean getClosestStation(int currentTick) {
		//gets the distances to the next Inbound and Outbound station
		int inboundDist = distanceToNextInboundStation(currentTick);
		int outboundDist = distanceToNextOutboundStation(currentTick);
		
		//inbound is the closest station
		if (inboundDist < outboundDist)
			return true;
		//outbound is the closest station
		else if (outboundDist < inboundDist)
			return false;
		//If the distances are equal; default closest will be Inbound
		else
			return true;
		
	}
	
	/**  returns the station name (based off its tick location) */
	public Station getStationName(int currentTick){
		
		for(int i=0; i< ticks.size();i++){
			if(ticks.get(i).equals(currentTick)){
				return stations.get(i);
			}// end if
		}//end for
		
		return null;
	}//end getStation Name
	
	public int stationsTickValue(Station s){
		return ticks.get(getStationIndex(s));
	}
	
	
	/** returns the best route to take Inbound/Outbound from one station to another */
	public boolean bestRoute(Station s1, Station s2){
		
		int i = getStationIndex(s1);
		int j = getStationIndex(s2);
		
		int s1Tick = ticks.get(i);
		int s2Tick = ticks.get(j);
		
		int inboundDist = 0;
		int outboundDist = 0;
		
		if(s1Tick < s2Tick){
			inboundDist = s2Tick - s1Tick;
			outboundDist = (maxTick() - s2Tick) + s1Tick;
		}
		else {
			outboundDist = s1Tick - s2Tick;
			inboundDist = (maxTick() - s1Tick) + s2Tick;
		}
		
		if (inboundDist <= outboundDist){
			return true;
		}
		else{
			return false;
		}
	
	}
	
	//===============================================================================
	/** returns the Index number of the station you pass to it */
	private int getStationIndex(Station s){
		int i = 0;
		//counts until it finds the matching station name
		while(!stations.get(i).equals(s)){
			i++;
		}
		// i = Index Number
		return i;
	}
	/** returns the last possible tick value */
	private int maxTick(){
		
		int size = ticks.get(ticks.size() - 1);
		//System.out.println("Max Tick: " + size);		//TESTING
		return size;
	}

	//================================================================================

	public static void main(String[] args) {
		TrainRoute tr = new TrainRoute();
		
		// 0, 5, 7, 12, 15
		/*
		tr.distanceToNextInboundStation(9);
		tr.distanceToNextInboundStation(0);
		tr.distanceToNextInboundStation(7);
		tr.distanceToNextInboundStation(10);
		tr.distanceToNextInboundStation(15);
		tr.distanceToNextOutboundStation(20);

		System.out.println(tr.getClosestStation(7));
		
		System.out.println(tr.getStationName(7));
		*/
		
		
		
		
	}// end main
}
