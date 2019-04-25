/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class ForestCrossroads extends Room 
{
	private static final long serialVersionUID = 1L;
	private final String[] sequence = {"north", "north", "south", "south", "west", "east", "west", "east"};
	private boolean reentry;
	private int count = 0;

	public ForestCrossroads(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Forest Crossroads";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData (this.forestCrossroadsImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "You come to a crossroads, where the path you are following is intersected by another path. "
				+ "Both paths are blessedly clear of foliage, and easily traversed, but there is nothing noteworthy to be seen down any "
				+ "of the available path options. The branches run north, east, south and west. ";
		
		//=================================================================================
		//When re-entering this same room, append extra messages to the room description
		//=================================================================================
		if (this.reentry == true)
			this.description += "This looks familiar. ";
		if (2 < this.count && this.gameState.checkFlipped("maze completed") == false)
			this.description += "You think you might be lost. ";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Forest Cliff
		//=================================================================================
		this.addMovementDirection("west", "Forest Cliff");
		this.addMovementDirection("cliff", "Forest Cliff");
		
		if (this.gameState.checkSpace("Forest Cliff") == false)
			new ForestCliff(this.gameState);	
		
		//=================================================================================
		//Create directions that move to Cave Entrance
		//=================================================================================
		this.addMovementDirection("north", "Cave Entrance");
		this.addMovementDirection("east", "Cave Entrance");
		this.addMovementDirection("south", "Cave Entrance");
		
		if (this.gameState.checkSpace("Cave Entrance") == false)
			new CaveEntrance(this.gameState);	
	}

	@Override
	protected void createItems() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createFlags() 
	{
		//===============================================================
		//Flag for whether the maze has been successfully completed
		//===============================================================
		this.gameState.addFlag("maze completed", new Flag(false, "", ""));
	}
	
	/**
	 * Determine if the correct direction was picked for the maze
	 * @param direction  String   The direction that was chosen
	 * @return           boolean  The boolean value of whether the choice was correct
	 */
	private void handleMaze(String direction)
	{
		//===============================================================
		//If move direction matches the sequence, increment count. 
		//Else, reset count to zero.
		//===============================================================
		if (this.sequence[this.count].contentEquals(direction) == true)
			this.count++;
		else
			this.count = 0;
	}

	@Override
	public DisplayData executeCommand(Command command) 
	{
		//===============================================================
		//Provide a case for each verb you wish to function in this room.
		//Most verbs will require default handling for cases where the
		//given verb has an unrecognized subject.  In this example, that is
		//  return new DisplayData("", "Error message.");
		//===============================================================
		switch (command.getVerb())
		{
		case "move":  //doing this will cause move to execute the go code
		case "go": 
			//===============================================================
			//If go back, return base room DisplayData.
			//===============================================================
			if (command.getSubject().contentEquals("back"))
				return this.displayOnEntry();

			//===============================================================
			//If move, and maze not completed, handle the maze
			//===============================================================
			if ((command.getSubject().matches("north|east|south|west|northern|eastern|southern|western") || 
					command.getTarget().matches("north|east|south|west")) && 
				this.gameState.checkFlipped("maze completed") == false)
			{
				//===============================================================
				//Handle movement choice, then flip the maze completed flag, if
				//the sequence has been completed.  Count being equal to eight
				//indicates the sequence has been completed.
				//===============================================================
				this.handleMaze(command.getSubject());
				if (this.count == 8)
				{
					this.gameState.flipFlag("maze completed");
					return new DisplayData("", "You once again come to the crossroads, but something is different this time. "
							+ "It feels like something intangible just clicked into place. ");
				}

				//===============================================================
				//If move west on the wrong sequence choice, return to forest
				//cliff, else re-enter this room.  Count being equal to zero
				//indicates the direction choice was incorrect.
				//===============================================================
				if (command.getSubject().contentEquals("west") && count == 0)
				{
					this.reentry = false;
					return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));
				}
				else
				{
					this.reentry = true;
					return this.displayOnEntry();
				}
			}

			//===============================================================
			//Change current room and return new room DisplayData.
			//===============================================================
			if (this.checkMovementDirection(command.getSubject()) == true)
			{
				this.reentry = false;
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));
			}

			//===============================================================
			//Go / Move command not recognized
			//===============================================================
			return new DisplayData("", "Can't go that direction.");

		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "The forest is dense, the light is dim. You peer carefully down each path, but are unable to "
						+ "discern anything of note. The crossroads itself is non-descript. "
						+ "But wait, is that an overgrown sign among the brush there? ");

			if (command.getSubject().contentEquals("sign"))
				return new DisplayData("", "You push aside the brush and look at the rotting sign. "
						+ "It says, \"BA BA Start - Konami\". What does that mean? ");
			
			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		default: 
			//===============================================================
			//If default is reached, return a failure message.
			//===============================================================
			return new DisplayData("", "Can't do that here.");
		}
	}

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String forestCrossroadsImage = "";
}
