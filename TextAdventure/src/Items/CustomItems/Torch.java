/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Items.CustomItems;

import Items.Item;
import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;

public class Torch extends Item 
{
	private static final long serialVersionUID = 1L;
	private boolean lit;

	public Torch(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		//===============================================================
		//New torch's default state is unlit
		//===============================================================
		this.name = "Torch";
		this.state = "(unlit)";
		this.gameState.addItemSearch(this.name, "torch", "lit", "unlit");
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData ("", this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "It's a wooden torch, around eighteen inches long, and an inch and a half thick. ";

		//===============================================================
		//Add message for lit or unlit
		//===============================================================
		if (this.lit == true)
			this.description += "It is currently lit. ";
		else
			this.description += "It is currently unlit. ";
		
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
		case "douse":
		case "extinguish":
		case "snuff":
		case "put":
			if (command.getSubject().matches("lit|torch") || command.getTarget().matches("lit|torch"))
			{
				if (this.state == "(lit)")
				{
					this.state = "(unlit)";
					return new DisplayData("", "You grind the head of the torch into the ground, extinguishing it. ");
				}
				else
					return new DisplayData("", "The torch isn't lit. ");
			}
		
		case "ignite":
		case "light":
			if (command.getSubject().matches("unlit|torch") || command.getTarget().matches("unlit|torch"))
			{
				if (this.state == "(unlit)")
				{
					if (this.gameState.checkInventory("Holy Zippo"))
					{
						this.state = "(lit)";
						return new DisplayData("", "You light your torch using the Holy Zippo. ");
					}
					else
						return new DisplayData("", "You don't have anything to light it with. ");
				}
				else
					return new DisplayData("", "The torch is already lit. ");
			}
			
		default: 
			//===============================================================
			//Pass the current command to the room commands when default is reached.
			//===============================================================
			return this.gameState.getCurrentRoom().executeCommand(command);
		}
	}
}
