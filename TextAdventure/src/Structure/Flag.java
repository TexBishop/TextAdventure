/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Structure;

import java.io.Serializable;

/**
 * A boolean flag to keep track of events within the game.  Stores the boolean value, plus a
 * String description for output on toString().
 */
public class Flag implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected boolean toggle;
	protected final boolean polarity;
	protected final String trueDescription;
	protected final String falseDescription;
	
	/**
	 * Constructor.
	 * @param polarity          boolean The starting value of the toggle.  This effects {@link Flag#toString()}
	 * @param falseDescription  String Description to output on toString() when value is false.
	 * @param trueDescription   String Description to output on toString() when value is true.
	 */
	public Flag(boolean polarity, String falseDescription, String trueDescription)
	{
		this.polarity = polarity;
		this.toggle = polarity;
		this.falseDescription = falseDescription;
		this.trueDescription = trueDescription;
	}
	
	/**
	 * Return String value of description for this flag.  Will return the False description when Flag 
	 * is False, and the True description when Flag is True.
	 */
	public String toString()
	{
		return this.toggle ? this.trueDescription : this.falseDescription;
	}
	
	/**
	 * Flip the boolean value of this Flag.
	 */
	public void flipToggle() 
	{
		//===============================================================
		//Only toggle if the Flag is still at default value
		//===============================================================
		if (this.toggle == this.polarity)
			this.toggle = this.toggle ? false : true;
	}

	/**
	 * Check the boolean value of this Flag.
	 * @return boolean value of toggle
	 */
	public boolean isToggle() 
	{
		return this.toggle;
	}

	/**
	 * Check whether this flag has been flipped.  False if it still has
	 * its default value, true if it has changed value.
	 * @return boolean value
	 */
	public boolean isFlipped() 
	{
		if (this.toggle == this.polarity)
			return false;
		return true;
	}
	
	/**
	 * Resets the flag to its unflipped value.
	 */
	public void reset()
	{
		this.toggle = this.polarity;
	}

}
