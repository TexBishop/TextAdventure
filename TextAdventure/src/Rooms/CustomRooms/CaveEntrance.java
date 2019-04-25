/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Items.Item;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class CaveEntrance extends Room 
{
	private static final long serialVersionUID = 1L;

	public CaveEntrance(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Cave Entrance";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData (this.caveEntranceImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "The trail widens out into an area of flat, packed dirt, though it remains shadowed, the canopy still thick. "
				+ "You see the cliff face here again, taller than it was before, on the western edge of the trail. "
				+ "The only path away from this area, is the path back towards the crossroads to the south. "
				+ "In the center of the sheer rock is a large, crude wooden door. "
				+ this.gameState.getFlag("cave door open").toString();

		//=================================================================================
		//Add torch text, if torch is placed.  Torch is lit if the door is open, unlit if
		//it is not.
		//=================================================================================
		if (this.gameState.checkFlipped("torch placed"))
		{
			if (this.gameState.checkFlipped("cave door open"))
				this.description += "Next to the door is a lit torch, sitting in a sconce. ";
			else
				this.description += "Next to the door is an unlit torch, sitting in a sconce. ";
		}
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Forest Crossroads
		//=================================================================================
		this.addMovementDirection("south", "Forest Crossroads");
		this.addMovementDirection("path", "Forest Crossroads");
		
		if (this.gameState.checkSpace("Forest Crossroads") == false)
			new ForestCrossroads(this.gameState);	

		//=================================================================================
		//Create directions that move to Cave Interior
		//=================================================================================
		this.addMovementDirection("west", "Cave Interior");
		this.addMovementDirection("cave", "Cave Interior");
		this.addMovementDirection("door", "Cave Interior");
		this.addMovementDirection("entrance", "Cave Interior");
		this.addMovementDirection("inside", "Cave Interior");
		
		if (this.gameState.checkSpace("Cave Interior") == false)
			new CaveInterior(this.gameState);	
		
		//=================================================================================
		//Create directions that move to Forest Exit
		//=================================================================================
		this.addMovementDirection("north", "Forest Exit");
		this.addMovementDirection("stone", "Forest Exit");
		this.addMovementDirection("stair", "Forest Exit");
		this.addMovementDirection("staircase", "Forest Exit");
		this.addMovementDirection("up", "Forest Exit");
		
		if (this.gameState.checkSpace("Forest Exit") == false)
			new ForestExit(this.gameState);	
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
		//Flag for whether the door has been opened
		//=================================================================================
		this.gameState.addFlag("cave door open", new Flag(false, "", "The oaken door to the cave is standing open. "));

		//=================================================================================
		//Flag for whether the torch has been placed
		//=================================================================================
		this.gameState.addFlag("torch placed", new Flag(false, "", ""));
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
			//If go into cave
			//===============================================================
			if (command.getSubject().matches("west|cave|door|entrance|inside"))
			{
				if (this.gameState.checkFlipped("cave door open"))
					return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));
				else
					return new DisplayData("", "The door is closed. You aren't sure how to open it. ");
			}

			//===============================================================
			//Change current room and return new room DisplayData.
			//===============================================================
			if (this.checkMovementDirection(command.getSubject()) == true)
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));

			//===============================================================
			//Go / Move command not recognized
			//===============================================================
			return new DisplayData("", "Can't go that direction. ");

		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();
			
		case "climb":
		case "take":
			//===============================================================
			//If take or climb up|stair|staircase, then move to forest exit
			//===============================================================
			if (this.checkMovementDirection(command.getSubject()) == true)
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));

			//===============================================================
			//Take command not recognized
			//===============================================================
			return new DisplayData("", "Can't do that. ");
			
		case "seat":
		case "fit":
		case "place":
		case "put":
			//===============================================================
			//Place a torch in the sconce
			//===============================================================
			if (command.getSubject().matches("torch|unlit|lit"))
			{
				//===============================================================
				//Verify that there isn't already a torch in the sconce
				//===============================================================
				if (this.gameState.checkFlipped("torch placed") == false)
				{
					//===============================================================
					//Verify that the player has a torch
					//===============================================================
					if (this.gameState.checkInventory("Torch"))
					{
						//===============================================================
						//Torch can be either lit or unlit when placed
						//===============================================================
						if (((Item) this.gameState.getSpace("Torch")).getState().contentEquals("(unlit)"))
						{
							this.gameState.flipFlag("torch placed");
							this.gameState.removeFromInventory("Torch");
							return new DisplayData("", "You place the unlit torch in the sconce. ");
						}
						else
						{
							//===============================================================
							//If the torch is already lit when placed, the cave door opens
							//===============================================================
							this.gameState.flipFlag("torch placed");
							this.gameState.flipFlag("cave door open");
							this.gameState.removeFromInventory("Torch");
							return new DisplayData("", "You place the lit torch in the sconce. "
									+ "A hearbeat after doing so, the great oaken door begins to slowly open, of its own accord. ");
						}
					}
					else
						return new DisplayData("", "You don't have a torch. ");
				}
				else
					return new DisplayData("", "You've already done that. ");
			}

			//===============================================================
			//Put command not recognized
			//===============================================================
			return new DisplayData("", "Can't do that. ");
			
		case "ignite":
		case "light":
		case "use":
			//===============================================================
			//If the command is to light the torch using the holy zippo
			//===============================================================
			if (command.getSubject().contentEquals("torch") ||
				(command.getSubject().matches("holy|zippo") && command.getTarget().contentEquals("torch")) ||
				command.getSubject().contentEquals("holy") && command.getTarget().contentEquals("zippo"))
			{
				//===============================================================
				//Verify that the torch has been placed in the sconce
				//===============================================================
				if (this.gameState.checkFlipped("torch placed") == true)
				{
					//===============================================================
					//Verify that the torch isn't already lit
					//===============================================================
					if (this.gameState.checkFlipped("cave door open") == false)
					{
						//===============================================================
						//Verify that the player has the holy zippo lighter
						//===============================================================
						if (this.gameState.checkInventory("Holy Zippo") == true)
						{
							this.gameState.flipFlag("cave door open");
							return new DisplayData("", "You light the torch. "
									+ "A hearbeat after doing so, the great oaken door begins to slowly open, of its own accord. ");
						}
						else
							return new DisplayData("", "That doesn't work. ");
					}
					else
						return new DisplayData("", "The torch is already lit. ");
				}
				else
					return new DisplayData("", "There isn't a torch in the sconce. ");
			}

			//===============================================================
			//If use command not recognized
			//===============================================================
			return new DisplayData("", "That doesn't seem to do anything. ");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "The outer edges of the area of packed dirt, give way immediately to dense foliage and large trees. "
						+ "The cliff face with its curious oaken door stand out, as the lone break in the greenery. "
						+ "However, on the northern edge of the area, you do see a piece of stone sticking out from under the brush, "
						+ "and it looks squarish, hewn, not a natural shape. ");

			if (command.getSubject().contentEquals("door"))
				return new DisplayData("", "It's a giant slab of oak, made all of one piece. It seems pretty thick. "
						+ "It's plain, no markings or windows, or a handle even.  You aren't sure how to open it. "
						+ "To the right of it, on the cliff face, you see an empty torch sconce. ");

			if (command.getSubject().contentEquals("cliff"))
				return new DisplayData("", "This portion of the cliff is much like the other, sheer and without handholds. "
						+ "It's taller here, though you aren't sure by how much, as the canopy hides the top from view. "
						+ "The door is the only thing of note. ");

			if (command.getSubject().contentEquals("stone"))
				return new DisplayData("", "Moving aside the foliage, you find that the stone is the first step of a hewn stone staircase, "
						+ "heading upward, possibly towards the top of the cliff. The foliage almost completely hides it from view at the edges "
						+ "of the area of packed dirt, but it looks to be clear enough to traverse easily beyond that point. ");
			
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
	private final String caveEntranceImage = "";
}
