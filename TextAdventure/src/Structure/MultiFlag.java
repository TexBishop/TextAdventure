/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A boolean Flag that is conditional on other Flags.  When all checked flags have been flipped
 * from their starting value, this Flag flips its value.
 */
public class MultiFlag extends Flag
{
	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private Map<String, Boolean> flagList = new HashMap<>();

	/**
	 * Constructor.
	 * @param polarity     boolean The starting value of the toggle.  This effects {@link Flag#toString()}
	 * @param description  String Description to output on toString()
	 */
	public MultiFlag(GameState gameState, boolean polarity, String falseDescription, String trueDescription) 
	{
		super(polarity, falseDescription, trueDescription);
		this.gameState = gameState;
	}
	
	/**
	 * Add a Flag to monitor to this MultiFlag. 
	 * @param name  String   The key for the Flag to add to this MultiFlag's list.
	 * @param name  Boolean  The value of Flag.checkFlipped() that you want this multiflag to flip on.
	 */
	public void addFlag(String name, Boolean value)
	{
		this.flagList.put(name, value);
	}
	
	@Override
	public String toString()
	{
		//===============================================================
		//Check if this flag has been flipped.  Return false string if not,
		//true string if true.
		//===============================================================
		if (this.flagList.isEmpty() == false) 
		{
			if (this.isFlipped() == true)
				return this.trueDescription;
			
			return this.falseDescription;
		}
		return "";
	}

	@Override
	public boolean isFlipped() 
	{
		//===============================================================
		//If toggle is still at starting value, check to see if it is
		//ready to flip.  If it is, flip it and return true.  If one
		//of the dependent Flags is still not correct, return false.
		//===============================================================
		if (this.toggle == this.polarity)
		{
			if (this.flagList.isEmpty() == false) 
			{
				for (Entry<String, Boolean> entry: this.flagList.entrySet()) 
				{
					if (this.gameState.checkFlipped(entry.getKey()) != entry.getValue().booleanValue())
						return false;
				} 
				this.flipToggle();
			}
		}
		return true;
	}
}
