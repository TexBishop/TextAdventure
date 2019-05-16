/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Items;

import java.util.LinkedList;

import Structure.DisplayData;
import Structure.GameState;

/**
 * Item that has a limited number of uses.  Messages can be set to print after each use.
 * After the last use, it is removed from inventory.
 */
public abstract class BreakableItem extends Item 
{
	private static final long serialVersionUID = 1L;
	protected int uses;
	protected int durability = 0;
	protected LinkedList<String> messages = new LinkedList<>();

	public BreakableItem(GameState gameState) 
	{
		super(gameState);
		this.initializeDurability();
	}
	
	/**
	 * Use this method to call setDurability(), to initialize uses and use messages.
	 * The number of use messages should match the number of uses.  The final message
	 * should be the break message.
	 */
	protected abstract void initializeDurability();
	
	/**
	 * Sets number of uses, and usage messages.
	 * The number of use messages should match the number of uses.  The final message
	 * should be the break message.
	 * @param uses      int    The number of uses before this item breaks.
	 * @param messages  String The messages to use for each use.
	 */
	protected void setDurability(int uses, String ...messages)
	{
		this.uses = uses;
		
		for (String message : messages)
		{
			this.messages.addLast(message);
		}
	}
	
	/**
	 * Process a usage of this item.  Break it if max number of uses is reached.
	 * @param displayData  DisplayData The display data to alter.
	 * @return             DisplayData The altered display data.
	 */
	public DisplayData useItem(DisplayData displayData)
	{
		try 
		{
			displayData.appendToDescription(" " + this.messages.get(this.durability));
			this.durability++;
			
			//=================================================================================
			//Break the item if it has reached the max number of uses
			//=================================================================================
			if (this.durability == this.uses)
				this.breakItem();
		} 
		catch (Exception e) 
		{
			System.out.println("Likely the result of number of uses being greater than the number of supplied messages, in BreakableItem " + this.getName());
			e.printStackTrace();
		}
		
		return displayData;
	}
	
	/**
	 * Remove item from inventory.
	 */
	private void breakItem()
	{
		this.gameState.removeFromInventory(this.name);
	}
}
