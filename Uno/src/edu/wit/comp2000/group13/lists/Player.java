package edu.wit.comp2000.group13.lists;

/** <b>Pile Class</b></br>
 * 	Holds a pile of Cards
 * 
 *  @author Damien Sardinha
 *  @date November 14, 2016
 *  @class COMP2000 */
public class Player {
	/**Name of the player*/
	private String name;
	/**Hand of the player*/
	private Hand hand;
	/**Score of the player*/
	private int score;
	
	//Used to know which cards are shown for the GUI
	private int lowIndexGUI = 0;
	private int highIndexGUI = 6;
	
	/**Player constructor
	 * @param name  the player's name*/
	public Player(String name){
		this.name = name;
		score = 0;
		hand = new Hand();
	}
	
	/**Draw a card
	 * @param cardDrawn  The card drawn from the deck*/
	public void drawCard(Card cardDrawn){
		hand.draw(cardDrawn);
	}
	
	/**Put down a card
	 * @param index  which card to put down
	 * @return the Card being put down*/
	public Card putDownCard(int index){
		return hand.remove(index);
	}
	
	/**Peek at a card
	 * @param index  which card to peek at
	 * @return the card you peeked at*/
	public Card peekAtCard(int index){
		return hand.get(index);
	}
	
	/**@return true if the player has cards left*/
	public boolean hasCards(){
		return !hand.isEmpty();
	}
	
	/**@return how many cards the player has*/
	public int howManyCards(){
		return hand.getSize();
	}
	
	/**Clear a hand*/
	public void clearHand(){
		hand.clear();
	}
	
	/**@return the name of the Player*/
	public String getName(){
		return name;
	}
	
	/**@return the player's hand*/
	public Hand getHand(){
		return hand;
	}
	
	/**@return the Player's score*/
	public int getScore(){
		return score;
	}
	
	/**@param score  how much to add on*/
	public void addToScore(int score){
		this.score += score;
	}
	
	/**@param index  the index*/
	public void setLowIndexGUI(int index){
		lowIndexGUI = index;
	}
	
	/**@param index  the index*/
	public void setHighIndexGUI(int index){
		highIndexGUI = index;
	}
	
	/**@return lowIndexGUI */
	public int getLowIndexGUI() {
		return lowIndexGUI;
	}
	
	/**@return highIndexGUI*/
	public int getHighIndexGUI() {
		return highIndexGUI;
	}
	
	@Override
	public String toString(){
		return name + "'s " + hand.toString();
	}
}