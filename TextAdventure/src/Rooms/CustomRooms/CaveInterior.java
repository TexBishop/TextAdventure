/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Rooms.CountdownRoom;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class CaveInterior extends CountdownRoom 
{
	private static final long serialVersionUID = 1L;

	public CaveInterior(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Cave Interior";
	}

	@Override
	protected void initializeCountdown() 
	{
		//=================================================================================
		//Death after third command
		//=================================================================================
		this.setCountdown(4, "The troll stands up, beckoning. ", 
				"He begins to look irritated, and hefts his club. ", 
				"The troll's obviously short temper seems to be running out. He brandishes his dangerously spiked club at you menacingly. ",
				"He's had enough. He swings at you violently, and you feel pain lance all through your body. "
				+ "As your vision fades, you remember the troll's large cookpot... ");

		//=================================================================================
		//Count down starts and progresses unless the brooch is in inventory
		//=================================================================================
		this.setTriggers(null, this.gameState.getFlag("troll satisfied"));
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData (this.caveInteriorImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "XXXXX";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Cave Entrance
		//=================================================================================
		this.addMovementDirection("cave", "Cave Entrance");
		this.addMovementDirection("outside", "Cave Entrance");
		this.addMovementDirection("entrance", "Cave Entrance");
		this.addMovementDirection("east", "Cave Entrance");
		
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
		//=================================================================================
		//Flag for having given the troll the brooch
		//=================================================================================
		this.gameState.addFlag("troll satisfied", new Flag(false, "", ""));
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
		case "exit":
		case "go": 
			//===============================================================
			//If go back, return base room DisplayData.
			//===============================================================
			if (command.getSubject().contentEquals("back"))
				return this.displayOnEntry();

			//===============================================================
			//Change current room and return new room DisplayData.
			//===============================================================
			if (this.checkMovementDirection(command.getSubject()) == true)
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));

			//===============================================================
			//Go / Move command not recognized
			//===============================================================
			return new DisplayData("", "Can't go that direction.");
			
		case "leave":
			return this.gameState.setCurrentRoom("Cave Entrance");

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
				return new DisplayData("", "");

			if (command.getSubject().contentEquals("cliff"))
				return new DisplayData("", "");
			
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
	private final String caveInteriorImage = "";
}
