
/**Ruoyu Zhi
 * CS110
 * Professor:Jackie Horton
 * This Interface regulates the method the Deck Class must have.
 * @author Ramon
 *
 */
public interface DeckInterface 
{
	public void freshDeck();
	public Card dealCard();
	public void addToBottom(Card c);
	public int getSize();
	public void shuffle();
	public void displayAll();
}
