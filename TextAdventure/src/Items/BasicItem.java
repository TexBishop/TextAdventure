/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Items;

import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;

/**
 * Basic item that doesn't need any internal flags or internal actions.
 */
public class BasicItem extends Item 
{
	private static final long serialVersionUID = 1L;

	public BasicItem(GameState gameState) 
	{
		super(gameState);
	}
	
	/**
	 * Constructor that can be used to fully initialize a basic item.
	 * @param gameState    GameState Game state object.
	 * @param name         String    The name of the item.
	 * @param image        String    The ASCII image.
	 * @param description  String    The description of the item printed to the CommandBox.
	 */
	public BasicItem(GameState gameState, String name, String image, String description)
	{
		super(gameState);
		this.name = name;
		this.image = image;
		this.description = description;
	}
	
	@Override
	protected void setName() 
	{
		//Not needed for basic item
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//Called when item is viewed.
		//=================================================================================
		return new DisplayData(this.image, fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		return this.description;
	}
	
	@Override
	protected void createItems()
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createFlags() 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public DisplayData executeCommand(Command command) 
	{
		//===============================================================
		//Provide a case for each verb you wish to function in this room.
		//Most verbs will require default handling for cases where the
		//given verb has an invalid command.  
		//===============================================================
		switch (command.getVerb())
		{				
		case "search":  //doing this will cause search to execute the go code
		case "look":
			if (command.unordered(this.regex))
				return this.displayOnEntry();
			
		default: 
			//===============================================================
			//Pass the current command to the room commands when default is reached.
			//===============================================================
			return this.gameState.getCurrentRoom().executeCommand(command);
		}
	}
}
