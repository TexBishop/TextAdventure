/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Items.BasicItem;
import Items.Item;
import Items.CustomItems.BottleOfWater;
import Items.CustomItems.PuzzleCube;
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
		this.description = "Going inside, you see that this is a natural cave, that someone simply fitted a door to. "
				+ "It appears that someone has been living here. "
				+ "You see a wooden rocking chair, a table, a bedroll lying on the ground, and a large cookpot with a barrel near it. "
				+ "Everything is oversized, as if for a giant. The area is lit by a couple of torches in sconces on the wall. "
				+ "It takes you a couple of moments to notice the large figure sitting in a shadowed corner. "
				+ "It's a huge troll-like figure! ";
		
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
		Item puzzleBox = new PuzzleCube(this.gameState);
		this.gameState.addSpace(puzzleBox.getName(), puzzleBox);
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
			return new DisplayData("", "Can't go that direction. ");
			
		case "leave":
			return this.gameState.setCurrentRoom("Cave Entrance");

		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();
			
		case "eat":
			//===============================================================
			//Eat a potato
			//===============================================================
			if (command.getSubject().matches("potato|potatoes"))
				return new DisplayData("", "You eat one of the potatoes. It's gritty. The dirt that's still on it doesn't help matters. ");

			//===============================================================
			//Eat a carrot
			//===============================================================
			if (command.getSubject().matches("carrot|carrots"))
				return new DisplayData("", "You eat one of the carrots. It's nice and crunchy, but the dirt soils the flavor a bit. ");

			//===============================================================
			//Eat command not recognized
			//===============================================================
			return new DisplayData("", "You can't eat that. ");
			
		case "drink":
			//===============================================================
			//Drink some of the water from the barrel.
			//===============================================================
			if (command.getSubject().contentEquals("water"))
				return new DisplayData("", "You drink some of the water from the barrel. It's cool and refreshing. ");

			//===============================================================
			//Drink command not recognized
			//===============================================================
			return new DisplayData("", "You don't see that here.");
			
		case "put":
		case "fill":
		case "refill":
			//===============================================================
			//Use the water in the barrel to turn the empty dasani bottle
			//into a full dasani bottle.
			//===============================================================
			if (command.getSubject().contentEquals("bottle") ||
				command.getSubject().contentEquals("dasani") ||
				(command.getSubject().contentEquals("water") && (command.getTarget().contentEquals("bottle") || command.getTarget().contentEquals("dasani"))))
			{
				if (this.gameState.checkInventory("Dasani Bottle (Empty)") == true)
				{
					//===============================================================
					//Remove empty bottle from inventory
					//===============================================================
					this.gameState.removeFromInventory("Dasani Bottle (Empty)");

					//===============================================================
					//Create and add a new bottle of water to inventory
					//===============================================================
					Item refilledBottle = new BottleOfWater(this.gameState);
					this.gameState.addSpace(refilledBottle.getName(), refilledBottle);
					this.gameState.addToInventory(refilledBottle.getName());

					return new DisplayData("", "You refill your bottle from the barrel of water. ");
				}
				else
					return new DisplayData("", "Your bottle of water is already full.");
			}

			//===============================================================
			//Use / Fill / Refill command not recognized
			//===============================================================
			return new DisplayData("", "Not sure what you're trying to do.");
			
		case "get":
		case "grab":
		case "take":
			//===============================================================
			//Try to take a potato or carrot.  Reject attempt.
			//===============================================================
			if (command.getSubject().matches("potato|potatoes|carrot|carrots"))
				return new DisplayData("", "You don't really want that. They aren't even clean. ");

			//===============================================================
			//Take command not recognized
			//===============================================================
			return new DisplayData("", "You can't take that. ");
			
		case "offer":
		case "hand":
		case "give":
			//===============================================================
			//Give brooch to troll
			//===============================================================
			if (command.getSubject().matches("brooch|religious|illuminati"))
			{
				//===============================================================
				//Verify the brooch is in inventory
				//===============================================================
				if (this.gameState.checkInventory("Religious Brooch"))
				{
					//===============================================================
					//Flip flags, remove brooch from inventory, and add puzzle cube
					//to inventory.
					//===============================================================
					this.gameState.flipFlag("troll satisfied");
					this.gameState.removeFromInventory("Religious Brooch");
					this.gameState.addToInventory("Puzzle Cube");
					
					return new DisplayData("", "You offer the odd brooch you found in the tree to the troll. "
							+ "He accepts it, looking satisfied. "
							+ "Reaching into a pouch hanging at his waist, he pulls out a puzzle cube of some sort, and hands it to you. "
							+ "A proud look settles on his face. He seems to think he just did something great. ");
				}
				else
					return new DisplayData("", "You don't have that. ");
			}

			//===============================================================
			//Give command not recognized
			//===============================================================
			return new DisplayData("", "He doesn't want that. ");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "Someone put some care into creating this living space. "
						+ "Everything looks hand made, and you can see scars on the rock surfaces from where stalagmites and stalagtites were removed. "
						+ "Who would do such a thing for this troll? ");

			if (command.getSubject().contentEquals("troll"))
			{
				String trollDescription = "This troll looks just like the small figurine you found beneath the large tree earlier, holding the torch. "
						+ "It's obvious that statue was meant to be a representation of this creature here, in all his grotesque largeness. ";

				//===============================================================
				//Different description based on whether the count down has been stopped.
				//===============================================================
				if (this.gameState.checkFlipped("troll satisfied") == false)
					trollDescription += "He's holding a great wooden club, covered with random spiky bits, nails mostly. It doesn't look pleasant. ";
				else
					trollDescription += "He has a satisfied look on his face. ";
				
				return new DisplayData("", trollDescription);
			}

			if (command.getSubject().matches("cookpot|cook|pot"))
				return new DisplayData("", "It reminds you of an old witch's cauldron. Cast iron. "
						+ "Where in the world do you find something like that in this day and age? "
						+ "Nothing seems to be in it at the moment. ");

			if (command.getSubject().matches("rocking|chair"))
				return new DisplayData("", "It's a hand made rocking chair of extra large proportion. "
						+ "Your legs wouldn't reach the ground if you sat in it. ");

			if (command.getSubject().contentEquals("table"))
				return new DisplayData("", "It's a large, plain wooden table. Could this be what the carpentry materials near the gate were for? "
						+ "A bowl filled with potatoes and carrots sits in the center. ");

			if (command.getSubject().contentEquals("torches"))
				return new DisplayData("", "They look the same as the one you fitted outside the cave entrance. ");

			if (command.getSubject().matches("bedroll|bed|roll"))
				return new DisplayData("", "It looks to be constructed of multiple layers of blankets, with each blanket being multiple other "
						+ "blankets sewn together. It has the largest pillow you've ever seen. Everything looks hand made. "
						+ "And, unfortunately, not very clean. ");

			if (command.getSubject().matches("barrel|water"))
				return new DisplayData("", "It's a banded wooden barrel, filled with fresh, clean water. It's nice and cool. ");

			if (command.getSubject().matches("bowl|potatoes|potato|carrots|carrot"))
				return new DisplayData("", "There isn't anything special about the bowl or the veggies. ");
			
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
