package edu.wit.comp2000.group13.lists;

import java.util.Scanner;

//TODO: maybe migrate to GUI if time

/** <b>Game Class</b></br>
 * Plays a game of Uno
 * @author Andrew Mellen
 * @date November 15, 2016
 * @class COMP2000*/
public class Game {
	private static Player[] players;
	private static Deck deck;
	private static Deck discardDeck;
	private static Card cardInPlay;
	private static int whoseTurn;
	private static boolean isReversed = false;
	private static Scanner sc;
	private static int roundNumber = 0;
	
	/**Starts a console based game*/
	public static void main(String[] args){
		new Game();
	}
	
	/**Sets up a brand new game (console)*/
	public Game(){
		sc = new Scanner(System.in);
		newPlayersSetup();
		playGame();
		sc.close();
	}
	
	/**Setup the game*/
	public static void newRound(){
		roundNumber++;
		System.out.print("Round " + roundNumber + " starting...");
		
		//Clear all the players hands
		for(Player p: players){
			p.clearHand();
		}
		
		//Make a deck
		deck = new Deck();
		deck.fillDeck();
		deck.shuffle();
		
		//Make discard deck
		discardDeck = new Deck();
		
		//Draw 7 cards
		for(Player p : players){
			for(int x = 0; x < 7; x++)
				p.drawCard(deck.deal());
		}
		
		//Set the card in play
		putACardInPlay(deck.deal());
		
		//Decide who goes first
		whoseTurn = (int)(players.length * Math.random());
	}
	
	/**Ask how many players there will be and get names (Console)*/
	private static void newPlayersSetup(){
		System.out.print("How many players? ");
		int numOfPlayers = sc.nextInt();
		
		if(numOfPlayers > 10){
			System.out.println("Too many players, max is 10.");
			numOfPlayers = 10;
		}
		if(numOfPlayers <= 1){
			System.out.println("Can't play with 1 or less than 1 player. Exiting...");
			System.exit(0);
		}
		
		String[] playerNames = new String[numOfPlayers];
		for(int x = 0; x < numOfPlayers; x++){
			System.out.print("Enter player " + (x + 1) + "'s name (enter \"AI\" for computer player): ");
			playerNames[x] = sc.next();
		}
		newPlayersSetup(numOfPlayers, playerNames);
	}
	
	/**Setups up the array of players with names</br>
	 * <b>Used for GUI</b>
	 * @param numOfPlayers  how many players
	 * @param playerNames  an array of player names*/
	public static void newPlayersSetup(int numOfPlayers, String[] playerNames){
		players = new Player[numOfPlayers];
		for(int x = 0; x < numOfPlayers; x++){
			if(playerNames[x].equalsIgnoreCase("AI"))
				players[x] = new AIPlayer("Hal 9000 (" + (x + 1) + ")");
			else
				players[x] = new Player(playerNames[x]);
		}
	}
	
	/**Plays a game until someone wins (console)*/
	private static void playGame(){
		while(!isAnyPlayerOver500()){
			newRound();
			playRound();
		}
		//Print out the winner
		for(Player p : players)
			if(p.getScore() >= 500)
				System.out.println(p.getName() + " wins!!!");
	}
	
	/**Continues until someone runs out of cards (plays round for console)*/
	private static void playRound(){
		Player currentPlayer = players[whoseTurn];
		
		/*Checks the first card for special stuff*/
		//Print that a special card has been chosen and what happens
		switch(cardInPlay.getAbility()){
			case "drawtwo":
				System.out.println("First card is a Draw Two! " + currentPlayer.getName() + " has to draw two cards and misses a turn!");
				break;
			case "reverse":
				System.out.println("First card is a Reverse! Play is now reversed!");
				break;
			case "skip":
				System.out.println("First card is a Skip! " + currentPlayer.getName() + " has been skipped!");
				break;
			case "drawfour":
				System.out.println("First card is a Draw Four! It is being put back into the deck and a new card is chosen!");
				break;
			case "wild":
				System.out.println("First card is a Wild!");
				break;
		}
		if(cardInPlay.getCardsNumber() >= 10){
			String color = "";
			if(cardInPlay.getCardsNumber() == 14) //only ask for color on wild
				color = consoleAskForColor(true);
			while(doSpecialActionAsFirstCard(color)); //keeps going until top card isn't draw4
		}
		
		while(true){ //keep going until break
			System.out.println(); //separate each turn by newline
			currentPlayer = players[whoseTurn];
			
			//Print out whos turn it is
			Card chosenCard;
			System.out.println(currentPlayer.getName() + "'s turn.");
			System.out.println("They have " + currentPlayer.howManyCards() + " cards.");
			System.out.println("Card in play is: " + cardInPlay);
			
			//Do the turn
			if(currentPlayer instanceof AIPlayer){ //AI is up
				chosenCard = AIMoves(currentPlayer);
				if(chosenCard == null){ //no move and deck is empty
					System.out.println(currentPlayer.getName() + " had no moves left and there are no cards left, turn skipped.");
					continue;
				}
			}
			else{ //Human player is up
				//Ask for card
				System.out.println(currentPlayer);
				System.out.print("What card number would you like to use?(or type \"0\" to draw) ");
				int pickedCard = sc.nextInt() - 1;
				
				if(pickedCard == -1){ //player requested to a draw a card
					Card drawnCard = drawCard(currentPlayer);
					if(drawnCard == null){ //no cards left in deck
						System.out.println("There are no cards left to pick up, skipping your turn. Sorry.");
						skip();
						continue;
					}
					System.out.println("Card Drawn: " + drawnCard);
					continue;
				}
				//Make sure picked card is a valid play
				chosenCard = playerMoves(currentPlayer, pickedCard);
				if(chosenCard == null){
					System.out.println("Invalid card choice, please choose again.");
					continue;
				}
			}
			
			//Print what the player put down and how many cards they have left
			System.out.println(currentPlayer.getName() + " put down " + chosenCard);
			System.out.println("They now have " + currentPlayer.howManyCards() + " cards.");
			
			/*Print lines for different ability cards*/
			if(cardInPlay.getCardsNumber() == 10){//draw2
				System.out.println(players[nextPlayer()].getName() + " has to draw two cards and is skipped!");
			}
			if(cardInPlay.getCardsNumber() == 11){//reverse card
				System.out.println("Play has been reversed!");
			}
			if(cardInPlay.getCardsNumber() == 12){//skip card
				System.out.println(players[nextPlayer()].getName() + " has been skipped!");
			}
			String color = "";
			if(cardInPlay.getCardsNumber() >= 13){//wilds
				color = consoleAskForColor(false);
			}
			if(cardInPlay.getCardsNumber() == 13){//draw4
				System.out.println(players[nextPlayer()].getName() + " has to draw four cards!");
			}
			
			//Do the special action
			if(chosenCard.getCardsNumber() >= 10)
				doSpecialCardAction(color);
			
			//Check for remaining cards
			if(currentPlayer.getHand().getSize() == 1)
				System.out.println("UNO!");
			if(!currentPlayer.hasCards())
				break;
			
			//Move to the next person
			whoseTurn = nextPlayer();
		}
		
		//----Someone ran out of cards-----
		
		//Give points to the winner of the round
		countPoints(players[whoseTurn]);
		
		//Print winner and points
		System.out.println(players[whoseTurn].getName() + " wins this round!!!");
		System.out.println();
		for(Player p : players)
			System.out.println(p.getName() + " has " + p.getScore() + " points.");
		System.out.println();
	}
	
	/**Plays a card given from the GUI
	 * @param pickedCard  the index of the card that was chosen, ignore for AI
	 * @param color  color chosen for wilds, just ignore if not a wild card or its AI player
	 * @return  Card, null if the play wasn't valid*/
	public static Card playCardForGUI(int pickedCard, String color){
		Player currentPlayer = players[whoseTurn];
		Card chosenCard;
		
		if(currentPlayer instanceof AIPlayer){ //AI player
			chosenCard = AIMoves(currentPlayer);
		}
		else{ //Human player
			chosenCard = playerMoves(currentPlayer, pickedCard);
			if(chosenCard == null){//play wasn't valid
				return null;
			}
		}
		
		//Check to see if it was a special card
		if(chosenCard.getCardsNumber() >= 10){
			if(chosenCard.getAbility().equals("wild") && currentPlayer instanceof AIPlayer)
				color = ((AIPlayer)currentPlayer).chooseColor();
			doSpecialCardAction(color);
		}
		
		whoseTurn = nextPlayer();
		return chosenCard;
	}
	
	/**@return the card in play right now*/
	public static Card getCardInPlay(){
		return cardInPlay;
	}
	
	/**@return the reference of the player so the GUI can have access to it*/
	public static Player getCurrentPlayerForGUI(){
		return players[whoseTurn];
	}
	
	/**If the deck is empty, shuffle the discard and use that\
	 * @return false if there are no cards left at all*/
	private static boolean checkIfDeckIsEmpty(){
		if(deck.isEmpty()){
			if(discardDeck.getSize() <= 1){ //out of cards in both, 1 means only the cardInPlay is left
				return false;
			}
			discardDeck.shuffle();
			deck = new Deck(discardDeck);
			discardDeck.clear();
		}
		return true;
	}
	
	/**Gets the color for the wild cards, asks human through the console if player is a human
	 * @param isFirstTurn  true if its the beginning of a game (prints the users hand)
	 * @return  the color as a String*/
	private static String consoleAskForColor(boolean isFirstTurn){
		Player currentPlayer = players[whoseTurn];
		String color = "";
		
		if(currentPlayer instanceof AIPlayer){ //ai chooses color
			AIPlayer ai = (AIPlayer)currentPlayer;
			color = ai.chooseColor();
			System.out.println(ai.getName() + " chose " + color + " for the wild card.");
		}else{ //human chooses color through console
			while(!cardInPlay.setColor(color)){
				if(isFirstTurn)
					System.out.println(currentPlayer.getName() + " here is your hand:\n" + currentPlayer);
				System.out.print(currentPlayer.getName() + ", what color would you like for the wild? ");
				color = sc.next();
			}
		}
		
		return color;
	}
	
	/**Does a move for the player
	 * @param currentPlayer  the currentPlayer
	 * @param pickedCard  the card that the player picked
	 * @return the card that was chosen if it was valid, null if not valid play*/
	private static Card playerMoves(Player currentPlayer, int pickedCard){	
		Card chosenCard = currentPlayer.peekAtCard(pickedCard);
			
		if(playIsValid(cardInPlay, chosenCard)){ //Make sure the pick is a valid play
			putACardInPlay(currentPlayer.putDownCard(pickedCard));
		}
		else{ //invalid play
			return null;
		}
			
		return chosenCard;
	}
	
	/**Draw a card for the given player (puts it in their hand)
	 * @return the drawn card or null if the deck is now empty*/
	public static Card drawCard(Player player){
		if(checkIfDeckIsEmpty()){
			Card drawnCard = deck.deal();
			player.drawCard(drawnCard);
			return drawnCard;
		}
		return null;
	}
	
	/**Does a move for an AI player</br>
	 * Auto draws cards until a valid play
	 * @return the card that was chosen or null if theres no play and the deck is empty*/
	private static Card AIMoves(Player currentPlayer){
		Card chosenCard = null;
		
		AIPlayer ai = (AIPlayer)currentPlayer;
			
		while(chosenCard == null){ //still has nothing to play
			chosenCard = ai.chooseCard(cardInPlay);
			if(chosenCard == null){ //had nothing to play
				if(drawCard(ai) == null){ //no cards left, skip past AI
					skip();
					break;
				}
			}
		}
		if(chosenCard != null) //no more cards if chosenCard == null
			putACardInPlay(chosenCard);
		
		return chosenCard;
	}
	
	/**Puts a card in play and adds it to the discard deck*/
	private static void putACardInPlay(Card card){
		discardDeck.add(card);
		cardInPlay = card;
	}
	
	/**@return true if any player has over 500 points (won)*/
	public static boolean isAnyPlayerOver500(){
		for(Player p : players){
			if(p.getScore() >= 500){
				return true; //someone won
			}
		}
		return false;
	}
	
	/**@return true if the play is valid*/
	public static boolean playIsValid(Card cardInPlay, Card cardToCheck){
		return  cardToCheck!=null&&(
				cardInPlay.getColor().equals(cardToCheck.getColor())||
				cardInPlay.getCardsNumber()==cardToCheck.getCardsNumber()||
				cardToCheck.isSpecialCard() || cardInPlay.isSpecialCard());
	}
	
	/**Count the points at the end of a round,
	 * give them to the winner
	 * @param winner  winning player*/
	public static void countPoints(Player winner){
		int totalPoints = 0;
		
		for(Player p : players){
			while(p.hasCards()){
				totalPoints += p.putDownCard(0).getPoints();
			}
		}
		
		winner.addToScore(totalPoints);
	}
	
	/**@return the index of whose turn is next*/
	private static int nextPlayer(){
		int nextTurn;
		
		if(isReversed)
			nextTurn = whoseTurn - 1;
		else
			nextTurn = whoseTurn + 1;
		
		return Math.floorMod(nextTurn, players.length);
	}
	
	//----------------------------------SPECIAL CARDS-----------------------------------------------
	
	/**Do the actions for the special cards
	 * @param color  color for wild cases, ignore otherwise*/
	private static void doSpecialCardAction(String color){
		switch(cardInPlay.getAbility()){
			case "skip":
				skip();
				break;
			case "reverse":
				reverse();
				break;
			case "drawtwo":
				drawMultipleCards(2);
				skip();
				break;
			case "drawfour":
				drawMultipleCards(4);
				wildCard(color);
				break;
			case "wild":
				wildCard(color);
				break;
		}
	}
	
	/**Do the actions for a special card if it is drawn as the first card</br>
	 * <b>NOTE: MAKE SURE YOU UPDATE THE CARD IN GUI IF ITS A DRAW FOUR</b>
	 * @param color  color for wild cases, ignore otherwise
	 * @return true if a new card is drawn for draw4 cards (rerun if true)*/
	public static boolean doSpecialActionAsFirstCard(String color){
		switch(cardInPlay.getAbility()){
			case "skip":
				skip();
				break;
			case "reverse":
				reverse();
				break;
			case "drawtwo":
				drawMultipleCards(2);
				skip();
				break;
			case "drawfour":
				putACardInPlay(deck.deal());
				//put the draw4 back in a random spot in the deck
				deck.add((int)(deck.getSize() * Math.random()), discardDeck.remove(0));
				if(cardInPlay.getCardsNumber() >= 10)
					return true;
				break;
			case "wild":
				wildCard(color);
				break;
		}
		return false;
	}
	
	/**Skip card: skip over next person*/
	private static void skip(){
		whoseTurn = nextPlayer();
	}
	
	/**Reverse card: Reverse the order of play*/
	private static void reverse(){
		isReversed = !isReversed;
	}
	
	/**For Draw2 and Draw4: tell next player to draw that many cards*/
	private static void drawMultipleCards(int howMany){
		for(int x = 0; x < howMany; x++)
			drawCard(players[nextPlayer()]);
	}
	
	/**Wild Card: Pick the color*/
	private static void wildCard(String newColor){
		cardInPlay.setColor(newColor);
	}
}