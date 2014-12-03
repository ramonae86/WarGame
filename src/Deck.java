import java.util.ArrayList;
import java.util.Random;

/**Ruoyu Zhi
 * CS110
 * Professor: Jackie Horton
 * This class can instantiate a deck of cards.
 * @author Ramon
 *
 */
public class Deck implements DeckInterface
{
	ArrayList<Card> deck= new ArrayList<Card>();
	
	/**establish a deck of sorted cards,s=true to generate a random deck,s=false to generate a deck with 52 same cards(for test use)*/
	public Deck(boolean s)
	{
		if(s == true)
			freshDeck();
		else
		{
			for(int i = 0;i < 52;i++)
			{
				deck.add(new Card(3,6));
			}
		}
	}
	
	/**establish an empty deck of cards*/
	public Deck()
	{
		
	}
	
	/**create a sorted deck*/
	public void freshDeck()
	{
		for(int suit = 1;suit <= 4;suit ++)
		{
			deck.add(new Card(suit,Card.ACE));
			for(int rank = 2;rank <= Card.KING;rank ++)
			{
				deck.add(new Card(suit,rank));
			}
		}
	}
	
	/**remove and return the top card on the deck*/
	public Card dealCard()throws OutOfCardsException
	{
		if(deck.size() == 0)
			throw new OutOfCardsException();
		Card c = deck.remove(0);
		return c;
	}
	
	/**shuffle the deck*/
	public void shuffle()
	{
		int randNum;
		Random r = new Random();
		Card temp;
		for(int i = 0;i <= 51;i++)
		{
			randNum = r.nextInt(deck.size());
			temp = deck.get(i);
			deck.set(i, deck.get(randNum));
			deck.set(randNum, temp);
		}
	}
	
	/**returns if the deck is empty*/
	public boolean isEmpty()
	{
		return(deck.size() == 0);
	}
	
	/**put a card to the bottom of the deck*/
	public void addToBottom(Card c)
	{
		deck.add(deck.size(),c);
	}
	
	/**return how many cards left*/
	public int getSize()
	{
		return deck.size();
	}
	
	/**display all cards of the deck*/
	public void displayAll()
	{
		for(int i = 0;i < deck.size();i++)
			System.out.println(deck.get(i));
	}
	
	/**main method used to test the functionality of the class*/
	public static void main(String [] Args)
	{
		Deck deck = new Deck(true);
		deck.shuffle();
		deck.displayAll();
		System.out.println("**********************");
		Card topCard = deck.dealCard();
		System.out.println(topCard);
		System.out.println("**********************");
		deck.displayAll();
		System.out.println("**********************");
		deck.addToBottom(topCard);
		deck.displayAll();
	}
}
