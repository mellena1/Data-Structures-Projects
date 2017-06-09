package edu.wit.comp2000.group13.lists;

/** <b>Deck Class</b></br>
 * 	Extends Pile class</br> 
 *  Includes extra functionality such as dealing
 * 
 *  @author Brandon Simpson
 *  @date November 15, 2016
 *  @class COMP2000
 *  @see Pile */
public class Deck extends Pile{
	/**Makes an empty Deck*/
	public Deck(){
		super();
	}
	
	/**Make a new deck that is a copy of the parameter*/
	public Deck(Deck deck){
		super();
		for(int x = 0; x < deck.getSize(); x++){
			super.add(deck.get(x));
		}
	}
	
	/**Deals the card at the top of the Deck*/
	public Card deal(){
		return super.remove(0);
	}
	
	/**Fill up the deck with all 109 cards*/
	public void fillDeck(){
		//adding colored cards
		for(int x = 0; x <= 12; x++){
			if(x == 0){
				addOneCardOfEveryColor(x);
			}
			else{
				addOneCardOfEveryColor(x);
				addOneCardOfEveryColor(x);
			}
		}
		//adding draw 4 and wild
		for(int x = 13; x < 15; x++){
			super.add(new Card("none", x));
			super.add(new Card("none", x));
			super.add(new Card("none", x));
			super.add(new Card("none", x));
		}
	}
	
	/**Clears and refills the deck*/
	public void resetDeck(){
		super.clear();
		fillDeck();
	}
	
	/**Makes one card of every color
	 * @param x  the number on the card */
	private void addOneCardOfEveryColor(int x){
		super.add(new Card("red", x));
		super.add(new Card("blue", x));
		super.add(new Card("green", x));
		super.add(new Card("yellow", x));
	}
	
	@Override
	public String toString(){
		String strToReturn = super.toString();
		strToReturn = "Deck:\n" + strToReturn.substring(6);
		return strToReturn;
	}
	
	//-----------------------------------TESTING METHODS----------------------------------------------
	
	
	/**Tests the Deck Class*/
	public static void main(String[] args){
		testMakingDeck();
	}
	
	/**Test making a deck and sorting it*/
	private static void testMakingDeck(){
		Deck D1 = new Deck();
		System.out.println(D1);
		System.out.println();
		D1.shuffle();
		System.out.println(D1);
		D1.sort();
		System.out.println();
		System.out.println(D1);
	}
}
