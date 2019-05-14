package Items.CustomItems;

import Items.BasicItem;
import Items.BreakableItem;
import Items.Item;
import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;

public class BottleOfWater extends BreakableItem 
{
	private static final long serialVersionUID = 1L;

	public BottleOfWater(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Bottle of Water";
		this.gameState.addItemSynonyms(this, "bottle", "water", "dasani");
	}

	@Override
	protected void initializeDurability() 
	{
		this.setDurability(1, "The bottle is now empty.");
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//Called when item is viewed.
		//=================================================================================
		return new DisplayData("", this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		//=================================================================================
		//This is used to return the main item description.  If any flags alter the main
		//item description, that will be handled here.
		//=================================================================================
		this.description = "A full bottle of water.  Dasani.";
		
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
		DisplayData displayData;
		Item emptyBottle;
		switch (command.getVerb())
		{	
		case "execute usage":
			//=================================================================================
			//Default usage code
			//=================================================================================
			displayData = new DisplayData("", "");
			this.useItem(displayData);
			
			//=================================================================================
			//Create empty bottle, and add it to inventory
			//=================================================================================
			emptyBottle = new BasicItem(this.gameState, "Dasani Bottle (Empty)", "", "An empty plastic Dasani bottle.");
			this.gameState.addItemSynonyms(emptyBottle, "bottle", "dasani");
			this.gameState.addSpace(emptyBottle.getName(), emptyBottle);
			this.gameState.addToInventory(emptyBottle.getName());

			//=================================================================================
			//Default usage code, with blank display data
			//=================================================================================
			return displayData;
			
		case "sip":
		case "swallow":
		case "imbibe":
		case "consume":
		case "drink":
			if (command.unordered(this.regex))
			{
				displayData = new DisplayData("", "You drink the water. ");
				this.useItem(displayData);
				
				//=================================================================================
				//Create empty bottle, and add it to inventory
				//=================================================================================
				emptyBottle = new BasicItem(this.gameState, "Dasani Bottle (Empty)", "", "An empty plastic Dasani bottle.");
				this.gameState.addItemSynonyms(emptyBottle, "bottle", "dasani");
				this.gameState.addSpace(emptyBottle.getName(), emptyBottle);
				this.gameState.addToInventory(emptyBottle.getName());
				
				return displayData;
			}
			
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
