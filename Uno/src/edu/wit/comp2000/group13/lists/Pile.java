package edu.wit.comp2000.group13.lists;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

/** <b>Pile Class</b></br>
 * 	Holds a pile of Cards
 * 
 *  @author Kai Zhao
 *  @date November 14, 2016
 *  @class COMP2000 */
public class Pile {
	/**List of Cards*/
	private ArrayList<Card> cards;
	
	/**Make a new Pile*/
	public Pile(){
		cards = new ArrayList<Card>();
	}
	
	/**Add a card to the pile*/
	public void add(Card newCard){
		cards.add(newCard);
	}
	
	/**Add a card to the pile at an index*/
	public void add(int index, Card newCard){
		cards.add(index, newCard);
	}
	
	/**Remove a card at the given index
	 * @param index  the index of the card to remove
	 * @return whatever Card is removed from that spot*/
	public Card remove(int index){
		return cards.remove(index);
	}
	
	/**Remove a given card from the pile (removes first instance of card)
	 * @param cardToRemove  The card to search for and remove*/
	public void remove(Card cardToRemove){
		//Two Iterators, one stays behind the other to remove last element
		Iterator<Card> iter = cards.iterator();
		Iterator<Card> oneBehindIter = cards.iterator();
		
		while(iter.hasNext()){
			Card currentCard = iter.next();
			if(currentCard.equals(cardToRemove)){
				oneBehindIter.remove();
				return;
			}
			oneBehindIter.next();
		}
	}
	
	/**@return the card at given index; null if out of bounds*/
	public Card get(int index){
		if(index > cards.size() - 1){
			return null;
		}
		return cards.get(index);
	}
	
	/**Clears the pile*/
	public void clear(){
		cards.clear();
	}
	
	/**Returns whether a certain card is in the pile
	 * @param cardToSearch  the card to look for
	 * @return  true if the card is in the pile*/
	public boolean contains(Card cardToSearch){
		return cards.contains(cardToSearch);
	}
	
	/**@return index of the cardToSearch*/
	public int indexOf(Card cardToSearch){
		return cards.indexOf(cardToSearch);
	}
	
	/**Shuffles the pile*/
	public void shuffle(){
		Collections.shuffle(cards);
	}
	
	/**Sorts the pile*/
	public void sort(){
		cards.sort(null);
	}
	
	/**@return How many cards are in the pile*/
	public int getSize(){
		return cards.size();
	}
	
	/**@return true if the pile is empty*/
	public boolean isEmpty(){
		return cards.isEmpty();
	}
	
	@Override
	public String toString(){
		String strToReturn = "Pile:\n";
		int counter = 1;
		for(Card c : cards){
			strToReturn += "Card " + counter + ": " + c + "\n";
			counter++;
		}
		return strToReturn.substring(0, strToReturn.length()-1); //remove last \n
	}
	
	//--------------------------------------TESTING METHODS--------------------------------------------
	
	/**Test the Pile Class*/
	public static void main(String[] args){
		
	}
}