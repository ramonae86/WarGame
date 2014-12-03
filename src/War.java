import java.util.Scanner;
import java.util.ArrayList;
/**Ruoyu Zhi
 * CS 110
 * Professor:Jackie Horton
 * This program is the logical part of war game.One card each is dealt to a dealer and to a player. 
 * If the player's card is higher, he or she wins the wager they bet. 
 * However, if the dealer's card is higher, the player loses their bet.
 * 
 * A tie occurs when the dealer and the player each have cards of the same rank. In a tie situation, the player has two options:
 * The player can surrender, in which case the player loses half the bet.
 * The player can go to war, in which case both the player and the dealer must place an additional wager the same size as the first wager
 * @author Ramon
 *
 */
public class War
{
	/**
	 * main method that simulates all the process
	 * @param Args
	 */
	public static void main(String [] Args)
	{
		Deck deck = new Deck(true);
		deck.shuffle();
		Deck playerHand = new Deck(false);
		Deck dealerHand = new Deck(false);

		deck.displayAll();
		
		System.out.println("*********************************************");
		
		//evenly hand out the cards to the player and dealer/computer
		for(int i = 0;i <= 51;i++)
		{
			if(i <= 25)
				playerHand.addToBottom(deck.dealCard());
			else
				dealerHand.addToBottom(deck.dealCard());
		}
		
		//input wager amount
		Scanner keyboard = new Scanner(System.in);
		System.out.println("What is the wager?");
		int wager;
		do
		{
			wager = keyboard.nextInt();
		}while(wager > 25 || wager <= 0);
		
		try
		{
			while(true)
			{
				//start of a round
				ArrayList<Card> cardsOnDesk = new ArrayList<Card>();
				Card playerCard = playerHand.dealCard();
				cardsOnDesk.add(playerCard);
				Card dealerCard = dealerHand.dealCard();
				cardsOnDesk.add(dealerCard);
				
				//player is larger
				if(playerCard.compareTo(dealerCard) > 0)
				{
					System.out.println("player's card is " + playerCard);
					System.out.println("dealer's card is " + dealerCard);
					while(cardsOnDesk.size() != 0)
					{
						System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
						playerHand.addToBottom(cardsOnDesk.remove(0));
					}
					System.out.println("player has " + playerHand.getSize() + " cards left");
					System.out.println("dealer has " + dealerHand.getSize() + " cards left");
				}
				//dealer is larger
				else if(playerCard.compareTo(dealerCard) < 0)
				{
					System.out.println("player's card is " + playerCard);
					System.out.println("dealer's card is " + dealerCard);
					while(cardsOnDesk.size() != 0)
					{
						System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
						dealerHand.addToBottom(cardsOnDesk.remove(0));
					}
					System.out.println("player has " + playerHand.getSize() + " cards left");
					System.out.println("dealer has " + dealerHand.getSize() + " cards left");
				}
				//equals
				else
				{
					System.out.println("player's card is " + playerCard);
					System.out.println("dealer's card is " + dealerCard);
					//into a war
					do
					{
						System.out.println("Let's have a war!");
						//deal the cards as wager
						for(int i = 0;i < wager;i++)
						{
							playerCard = playerHand.dealCard();
							cardsOnDesk.add(playerCard);
							dealerCard = dealerHand.dealCard();
							cardsOnDesk.add(dealerCard);
						}
						
						//deal cards to be compared
						playerCard = playerHand.dealCard();
						cardsOnDesk.add(playerCard);
						dealerCard = dealerHand.dealCard();
						cardsOnDesk.add(dealerCard);
					}while(dealerCard.equals(playerCard));//keep dealing cards until not equal
					
					
					if(playerCard.compareTo(dealerCard) > 0)
					{
						while(cardsOnDesk.size() != 0)
						{
							System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
							playerHand.addToBottom(cardsOnDesk.remove(0));
						}
						System.out.println("player's card is " + playerCard);
						System.out.println("dealer's card is " + dealerCard);
						System.out.println("player has " + playerHand.getSize() + " cards left");
						System.out.println("dealer has " + dealerHand.getSize() + " cards left");
					}
					else
					{
						while(cardsOnDesk.size() != 0)
						{
							System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
							dealerHand.addToBottom(cardsOnDesk.remove(0));
						}
						System.out.println("player's card is " + playerCard);
						System.out.println("dealer's card is " + dealerCard);
						System.out.println("player has " + playerHand.getSize() + " cards left");
						System.out.println("dealer has " + dealerHand.getSize() + " cards left");
					}
				}
			}
			
		}
		catch(OutOfCardsException e)
		{
			System.out.println("deck out of cards,game over");
		}
		finally
		{
			keyboard.close();
		}
		
	}
}
