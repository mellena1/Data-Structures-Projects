package edu.wit.comp2000.group13.lists;

/**
 * AIPlayer Class
 * @author Eric Wahlstrom
 * @date 11/17/16
 * @class COMP 2000 - 2016-3fa
 */
public class AIPlayer extends Player
{
	private Hand hand = super.getHand();

	/**
	 * @param name - name your AI
	 */
	public AIPlayer(String name)
	{
		super(name);
	}

	/**
	 * This is Hal. Hal hates Dave and Humanity, but Dave specifically
	 */
	public AIPlayer()
	{
		this("Hal 9000");
	}

	/**
	 * @param cardOntop - The Card on top of the deck of used cards
	 * @return The Card that is best choice for the AI to use
	 */
	public Card chooseCard(Card cardOntop)
	{
		int i;
		Card check, maxValid = new Card();
		for(i = 0;i < hand.getSize();i++)
		{
			check = hand.get(i);
			if(check.getPoints() >= maxValid.getPoints() && Game.playIsValid(cardOntop, check))
				maxValid = check;
		}
		return (maxValid.equals(new Card())) ? null : putDownCard(hand.indexOf(maxValid));
	}

	/**
	 * This method is for Wild card where the Player needs to choose a color
	 * @return The color the AI has the most Cards of
	 */
	public String chooseColor()
	{
		int b = 0, r = 0, y = 0, g = 0, max;
		for(int i = 1;i < hand.getSize();i++)
			switch(hand.get(i).getColor())
			{
			case "blue":
				b++;
				break;
			case "red":
				r++;
				break;
			case "yellow":
				y++;
				break;
			case "green":
				g++;
				break;
			}
		max = Integer.max(Integer.max(b, r), Integer.max(y, g));
		return (max == b || max == r) ? ((max == b) ? "blue" : "red") : ((max == y) ? "yellow" : "green");
	}

	public static void main(String[] args)
	{
		Deck deck = new Deck();
		Card topCard = new Card();
		AIPlayer hal = new AIPlayer();
		deck.fillDeck();
		deck.shuffle();
		for(int x = 0;x < 7;x++)
			hal.drawCard(deck.deal());
		System.out.println(hal.hand.toString());
		System.out.println();
		System.out.println("Top Card: " + topCard.toString());
		Card choosen = hal.chooseCard(topCard);
		System.out.println("Card Play Put Down: " + choosen);
		if(choosen!=null&&choosen.getAbility().equals("wild"))
			System.out.println(hal.chooseColor());
	}
}