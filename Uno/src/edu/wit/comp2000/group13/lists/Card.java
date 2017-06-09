package edu.wit.comp2000.group13.lists;

/**
 * Card Class
 * @author Eric Wahlstrom
 * @date 11/17/16
 * @class COMP 2000 - 2016-3fa
 */
public class Card implements Comparable<Card>
{
	private String[] colors = {"red","blue","green","yellow","none"};
	private String[] abilities = {"drawtwo","reverse","skip","drawfour","wild"};
	private int cardNum, pointsWorth;
	private String cardColor, ability;

	// Constructors
	public Card()
	{
		this("none", 0);
	}

	/**
	 * @param color - Color of the card
	 * @param num - The Number of the Card
	 */
	public Card(String color, int num)
	{
		if(num >= 0 && num <= 9)
		{
			cardNum = num;
			ability = "none";
		}
		else if(num > 9)
		{
			cardNum = num;
			ability = abilities[num - 10];
		}
		cardColor = (num > 12) ? "none" : color;
		pointsWorth = (!ability.equals("none")) ? ((ability.equals("drawfour") || ability.equals("wild")) ? 50 : 20) : num;
	}

	// ------------------------------------PUBLIC METHODS---------------------------------------------------

	/**
	 * @param c - The Card to compare THIS card to
	 * @return 1 if the THIS card's color is GREATER THAN the the card to check (c) 
	 * 					or if  the colors are the same but the number is GREATER THAN
	 * 		  -1 if the THIS card's color is LESS THAN the the card to check (c) 
	 * 					or if  the colors are the same but the number is LESS THAN
	 * 		   0 if the same
	 */
	@Override
	public int compareTo(Card c)
	{
		if(this.indexOf(this.cardColor) < c.indexOf(c.getColor()))
			return -1;
		else if(this.indexOf(this.cardColor) > c.indexOf(c.getColor()))
			return 1;
		else
		{
			if(this.cardNum < c.getCardsNumber())
				return -1;
			if(this.cardNum > c.getCardsNumber())
				return 1;
		}
		return 0;
	}

	/**
	 * @return The number of THIS Card
	 */
	public int getCardsNumber()
	{
		return cardNum;
	}

	/**
	 * @return The amount of points THIS Card is worth
	 */
	public int getPoints()
	{
		return pointsWorth;
	}

	/**
	 * @return The String Value of this Cards special move
	 */
	public String getAbility()
	{
		return ability;
	}
	
	/**
	 * @return true if the color is valid, false otherwise
	 */
	public boolean setColor(String newColor){
		if(!(newColor.equalsIgnoreCase("blue") || newColor.equalsIgnoreCase("red") || newColor.equalsIgnoreCase("yellow") || newColor.equalsIgnoreCase("green")))
			return false;
		cardColor = newColor;
		return true;
	}

	/**
	 * @return The color of THIS Card
	 */
	public String getColor()
	{
		return cardColor;
	}

	/**
	 * @param c - Card to check against
	 * @return true if the Cards are equal, false otherwise
	 */
	public boolean equals(Card c)
	{
		return (cardColor.equals(c.getColor()) && cardNum == c.getCardsNumber())
				|| (cardColor.equals(c.getColor())&&(ability.equals(c.getAbility())));
	}

	/**
	 * @return true if THIS Card has a special move, false otherwise
	 */
	public boolean isSpecialCard()
	{
		return cardNum >= 13;
	}
	
	/**
	 * @return the String value of THIS Card
	 */
	public String toString()
	{
		return "Color: " + cardColor + ", Number: " + cardNum + ", Ability: " + ability+", Points Worth: "+pointsWorth;
	}
	// ---------------------------------PRIVATE HELPER METHODS----------------------------------------------

	/**
	 * @param c - the color you want to check
	 * @return the integer value of the specified color
	 */
	private int indexOf(String c)
	{
		for(int i = 0;i < colors.length;i++)
			if(colors[i].equals(c))
				return i;
		return -1;
	}

	//Method Testing
	public static void main(String[] args)
	{
		Card test1 = new Card("red", 3);
		Card test2 = new Card("red", 15);
		Card test3 = new Card("green", 5);
		Card test4 = new Card("yellow", 4);

		System.out.println(test2);

		System.out.println(test2.compareTo(test4));
		System.out.println(test1.compareTo(test3));
		System.out.println(test1.compareTo(test2));
		System.out.println(test1.compareTo(test1));
		System.out.println(test2.compareTo(test1));
		System.out.println(test3.compareTo(test1));
		System.out.println(test4.compareTo(test2));
	}
}
