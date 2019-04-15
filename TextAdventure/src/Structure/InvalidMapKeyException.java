/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Structure;

/**
 * Flag either does not exist, or there was an attempted duplicate key entry.
 */
public class InvalidMapKeyException extends RuntimeException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidMapKeyException()
	{
		super("Key either does not exist, or there was an attempted duplicate key entry.");
	}
}
