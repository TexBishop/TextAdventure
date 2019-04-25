/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Items.Item;
import Items.CustomItems.Torch;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class ForestExit extends Room 
{
	private static final long serialVersionUID = 1L;

	public ForestExit(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Forest Exit";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData (this.forestExitImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "You walk into a hollowed out area within the forest.  The canopy is thick overhead, allowing little light in, "
				+ "and the foliage around the circular area is dense, difficult to see through. "
				+ "It's like an upside down bowl, formed of trees and brush. "
				+ "On the western end is a gate, the view through it showing a bit of empty field. "
				+ this.gameState.getFlag("gate barred").toString()
				+ "The stone staircase leads back down to the cave entrance on the southern end of the area. ";
		
		if (this.gameState.checkFlipped("troll satisfied") == true && this.gameState.checkFlipped("troll scene ran") == false)
		{
			this.gameState.flipFlag("troll scene ran");
			this.gameState.flipFlag("gate unbarred");
			this.description += "You hear the brush rustling from the direction of the staircase. "
					+ "Turning to find out what is causing the noise, you see the Troll emerging onto the packed dirt. "
					+ "He walks over to the barred gate, lifts the heavy timber from its seating, and throwing it aside. "
					+ "He gives you a glance and a grunt, then turns and heads back down the staircase. ";
		}
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Cave Entrance
		//=================================================================================
		this.addMovementDirection("cave", "Cave Entrance");
		this.addMovementDirection("south", "Cave Entrance");
		this.addMovementDirection("southern", "Cave Entrance");
		this.addMovementDirection("stone", "Cave Entrance");
		this.addMovementDirection("stair", "Cave Entrance");
		this.addMovementDirection("staircase", "Cave Entrance");
		this.addMovementDirection("down", "Cave Entrance");
		
		if (this.gameState.checkSpace("Cave Entrance") == false)
			new CaveEntrance(this.gameState);	

		//=================================================================================
		//Create directions that move to Back of Cornfield
		//=================================================================================
		this.addMovementDirection("west", "Back of Cornfield");
		this.addMovementDirection("western", "Back of Cornfield");
		this.addMovementDirection("exit", "Back of Cornfield");
		this.addMovementDirection("gate", "Back of Cornfield");
		
		if (this.gameState.checkSpace("Back of Cornfield") == false)
			new BackOfCornfield(this.gameState);	
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
		//Flag for whether the gate has been unbarred
		//=================================================================================
		this.gameState.addFlag("gate unbarred", new Flag(false, "The gate is barred closed with a piece of heavy timber. ", ""));

		//=================================================================================
		//Flag for whether the gate has been unbarred
		//=================================================================================
		this.gameState.addFlag("troll scene ran", new Flag(false, "", ""));
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
			//If exit forest to back of cornfield, only allow if gate has
			//been unbarred
			//===============================================================
			if (command.getSubject().matches("west|exit|gate|western"))
			{
				if (this.gameState.checkFlipped("gate unbarred"))
					return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));
				else
					return new DisplayData("", "The gate is barred closed. ");
			}

			//===============================================================
			//Change current room and return new room DisplayData.
			//===============================================================
			if (this.checkMovementDirection(command.getSubject()) == true)
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));

			//===============================================================
			//Go / Move command not recognized
			//===============================================================
			return new DisplayData("", "Can't go that direction.");

		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();
			
		case "unbar":
		case "remove":
		case "open":
			//===============================================================
			//Attempt to unbar the gate.  Results in failure.
			//===============================================================
			if (command.getSubject().matches("gate|bar|timber"))
				return new DisplayData("", "You try to lift the heavy timber barring the gate from its seating, but it's just too heavy for you. ");

			//===============================================================
			//Open command not recognized
			//===============================================================
			return new DisplayData("", "Can't do that. ");
			
		case "grab":
		case "get":
		case "take":
			//===============================================================
			//If take stair|staircase, then move to cave entrance
			//===============================================================
			if (this.checkMovementDirection(command.getSubject()) == true)
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));

			//===============================================================
			//Take a torch
			//===============================================================
			if (command.getSubject().contentEquals("torch"))
			{
				//===============================================================
				//Verify that the player doesn't already have a torch
				//===============================================================
				if (this.gameState.checkInventory("Torch") == false)
				{
					//===============================================================
					//We create the torch here, rather than in CreateItems(), because
					//this is an item that can be removed from inventory, in which case
					//it gets deleted from the space map.  Thus, it needs to be 
					//re-created when taking a second Torch after removing the first.
					//===============================================================
					Item newTorch = new Torch(this.gameState);
					this.gameState.addSpace(newTorch.getName(), newTorch);
					
					this.gameState.addToInventory("Torch");
					return new DisplayData("", "You take a torch from the box. ");
				}
				else
					return new DisplayData("", "You already have one. ");
			}

			//===============================================================
			//Take command not recognized
			//===============================================================
			return new DisplayData("", "Can't do that.");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "This area looks like it saw a fair bit of use at one time. "
						+ "There are still some old bootprints visible in the dirt, and some rather large bare footprints... "
						+ "There are a couple of sawhorses off to one side, with a few old rotted boards, and a smallish wooden box. ");

			if (command.getSubject().matches("gate|barred"))
			{
				//===============================================================
				//Adjust description based on whether the gate is still barred
				//===============================================================
				String gateDescription = "The gate is contructed of heavy timber, with several crossbars, and chain link covering the gaps. "
						+ "It hugged so closely by the foliage, that it appears to be a door through the foliage. "
						+ "There's presumable a fence as well, but it's completely invisible beneath the brush. ";
				if (this.gameState.checkFlipped("gate unbarred"))
					return new DisplayData("", gateDescription + "The piece of timber that was barring the gate closed now lies on the ground, "
							+ "off to the side. ");
				else
					return new DisplayData("", gateDescription + "A heavy piece of timber bars the gate closed. ");
			}

			if (command.getSubject().matches("box|wooden"))
				return new DisplayData("", "It looks like a small wooden crate. Looking inside, you see several old torches. "
						+ "They still look good, probably preserved from the elements by the oil they're soaked with. ");

			if (command.getSubject().matches("saw|horse|horses|sawhorse|sawhorses"))
				return new DisplayData("", "Someone was doing some carpentry here once upon a time. The sawhorses are a bit rickety now, but not rotten. ");

			if (command.getSubject().matches("rotted|boards"))
				return new DisplayData("", "They're old and rotted, no strength left in them. They're of no use. ");
			
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
	private final String forestExitImage = "";
}
