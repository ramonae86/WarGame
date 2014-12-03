
/**Exception thrown when either of the player runs out of cards.
 * @author Ramon
 *
 */
public class OutOfCardsException extends IndexOutOfBoundsException
{
	public OutOfCardsException()
	{
		super("Out of Cards Exception");
	}
}
