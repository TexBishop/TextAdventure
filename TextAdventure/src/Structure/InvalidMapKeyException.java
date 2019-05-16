/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Structure;

/**
 * Flag either does not exist, or there was an attempted duplicate key entry.
 */
public class InvalidMapKeyException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	public InvalidMapKeyException()
	{
		super("Key either does not exist, or there was an attempted duplicate key entry.");
	}
}
