package Items.CustomItems;

import Items.Item;
import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;

public class PuzzleCube extends Item 
{
	private static final long serialVersionUID = 1L;

	public PuzzleCube(GameState gameState) 
	{
		super(gameState);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setName() 
	{
		this.name = "Puzzle Cube";
		this.gameState.addItemSearch(this.name, "puzzle", "cube", "puzzlecube");
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
		this.description = "";
		
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
			
		default: 
			//===============================================================
			//Pass the current command to the room commands when default is reached.
			//===============================================================
			return this.gameState.getCurrentRoom().executeCommand(command);
		}
	}
}
