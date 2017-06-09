package edu.wit.comp2000.group13.lists;

/** <b>Pile Class</b></br>
 * 	Holds a pile of Cards
 * 
 *  @author Brandon Simpson
 *  @date November 14, 2016
 *  @class COMP2000 */
public class Hand extends Pile {	
	//Draws a card
	public void draw(Card card){
		super.add(card);
		super.sort();
	}
	
	public String toString(){
		String strToReturn = super.toString();
		strToReturn = "Hand:\n" + strToReturn.substring(6);
		return strToReturn;
	}
	
	public static void main(String args[]){
		
	}
}
