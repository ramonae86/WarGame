/**Ruoyu Zhi
 * CS110
 * Professor:Jackie Horton
 * This is a Card class that can be used to set up a card instance that holds suits and ranks
 * variables.It also contains the basic method like accessors,toString and equals.
 * @author Ramon
 *
 */
public class Card 
{
	public final static int SPADES = 1;
	public final static int CLUBS = 2;
	public final static int HEARTS = 3;
	public final static int DIAMONDS = 4;
	public final static int ACE = 14;
	public final static int JACK = 11;
	public final static int QUEEN = 12;
	public final static int KING = 13;
	private int rank;
	private int suit;
	
	/**constructor that takes both rank and suit as input to set up a card object*/
	public Card(int suit,int rank)
	{
		this.suit = suit;
		this.rank = rank;
	}
	
	/**no-args constructor that initialize a card to Ace of spades*/
	public Card()
	{
		this.suit = SPADES;
		this.rank = ACE;
	}
	
	/**suit accessor*/
	public int getSuit()
	{
		return suit;
	}
	
	/**rank accessor*/
	public int getRank()
	{
		return rank;
	}

	/**method to compare two cards*/
	public int compareTo(Card otherCard)
	{
		return (this.rank - otherCard.rank);
	}
	
	/**get the filename of a card's corresponding picture*/
	public String getPicName()
	{
		String rankName = "Null",suitName = "Null";
		switch(suit)
		{
			case 1:
				suitName = "s";
				break;
			case 2:
				suitName = "c";
				break;
			case 3:
				suitName = "h";
				break;
			case 4:
				suitName = "d";
				break;
			default:
				break;	
		}
		
		switch(rank)
		{
			case 2:
				rankName = "2";
				break;
			case 3:
				rankName = "3";
				break;
			case 4:
				rankName = "4";
				break;
			case 5:
				rankName = "5";
				break;
			case 6:
				rankName = "6";
				break;
			case 7:
				rankName = "7";
				break;
			case 8:
				rankName = "8";
				break;
			case 9:
				rankName = "9";
				break;
			case 10:
				rankName = "10";
				break;
			case 11:
				rankName = "jack";
				break;
			case 12:
				rankName = "queen";
				break;
			case 13:
				rankName = "king";
				break;
			case 14:
				rankName = "ace";
				break;
			default:
				break;	
		}
		String str = rankName + suitName + ".jpg";
		return str;
	}
	
	/**return that current status of card to a string*/
	public String toString()
	{
		String rankName = "Null",suitName = "Null";
		switch(suit)
		{
			case 1:
				suitName = "Spades";
				break;
			case 2:
				suitName = "Clubs";
				break;
			case 3:
				suitName = "Hearts";
				break;
			case 4:
				suitName = "Diamonds";
				break;
			default:
				break;	
		}
		
		switch(rank)
		{
			case 2:
				rankName = "2";
				break;
			case 3:
				rankName = "3";
				break;
			case 4:
				rankName = "4";
				break;
			case 5:
				rankName = "5";
				break;
			case 6:
				rankName = "6";
				break;
			case 7:
				rankName = "7";
				break;
			case 8:
				rankName = "8";
				break;
			case 9:
				rankName = "9";
				break;
			case 10:
				rankName = "10";
				break;
			case 11:
				rankName = "Jack";
				break;
			case 12:
				rankName = "Queen";
				break;
			case 13:
				rankName = "King";
				break;
			case 14:
				rankName = "Ace";
				break;
			default:
				break;	
		}
		
		String str = rankName + " of " + suitName;
		
		return str;
	}
	
	/**method that compares if two cards equal*/
	public boolean equals(Card otherCard)
	{
		boolean status;
		if(this.rank == otherCard.rank)
			status = true;
		else status = false;
		return status;
	}
}
