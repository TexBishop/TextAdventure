/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms.House;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;
import Items.BasicItem;
import Items.Item;

public class Entryway extends Room 
{

	private static final long serialVersionUID = 1L;

	public Entryway(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Entryway";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.entrywayImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "You stand in the entryway. A ragged screen door leads to the front porch, with a rusty coat hanger on the left. "
				+	"An ornate grandfather clock stands on the other side of the door; the delicate carvings decorating the clock "
				+	"seem out of place for such a decrepit home. In front of you is a door leading to the living room. On the right "
				+	"is the dining room. On the left, crooked, weathered stairs lead up a dark hallway. ";

		if(this.gameState.checkFlipped("painting removed") == false)
			this.description += "On the wall beyond the stairway lies a painting.";
		else
			this.description += "On the wall beyond the stairway a breaker box is visible, with a painting resting against the wall below.";

		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Farmhouse Porch
		//=================================================================================
		this.addMovementDirection("outside", "Farmhouse Porch");
		this.addMovementDirection("porch", "Farmhouse Porch");
		this.addMovementDirection("leave", "Farmhouse Porch");
		this.addMovementDirection("exit", "Farmhouse Porch");

		//=================================================================================
		//Create directions that move to Living Room
		//=================================================================================
		this.addMovementDirection("livingroom", "Living Room");
		this.addMovementDirection("living room", "Living Room");
		this.addMovementDirection("straight", "Living Room");
		this.addMovementDirection("forward", "Living Room");

		//=================================================================================
		//Create directions that move to Dining Room
		//=================================================================================
		this.addMovementDirection("diningroom", "Dining Room");
		this.addMovementDirection("dining room", "Dining Room");
		this.addMovementDirection("right", "Dining Room");

		//=================================================================================
		//Create directions that move to Upstairs Hallway
		//=================================================================================
		this.addMovementDirection("upstairs", "Upstairs Hallway");
		this.addMovementDirection("stairs", "Upstairs Hallway");
		this.addMovementDirection("stairway", "Upstairs Hallway");
		this.addMovementDirection("left", "Upstairs Hallway");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create flathead screwdriver
		//=================================================================================
		Item flathead = new BasicItem(this.gameState, "Flathead Screwdriver", "", "A flathead screwdriver. ");
		this.gameState.addItemSynonyms(flathead, "flathead", "screwdriver");
		this.gameState.addSpace(flathead.getName(), flathead);	
	}

	@Override
	protected void createFlags() 
	{
		this.gameState.addFlag("screwdriver taken", new Flag(false, "", "Screwdriver taken."));
		this.gameState.addFlag("painting removed", new Flag(false, "", "Painting removed."));
		this.gameState.addFlag("paperclip installed", new Flag(false, "", "Paperclip installed."));
		this.gameState.addFlag("power restored", new Flag(false, "", "Power restored."));
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
			if (command.unordered("back"))
				return this.displayOnEntry();

			//===============================================================
			//Change current room and return new room DisplayData.
			//===============================================================
			if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
				return this.move(command);

			//===============================================================
			//Go / Move command not recognized
			//===============================================================
			return new DisplayData("", "Can't go that direction.");

		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();

		case "grab":
		case "pick":
		case "take":
			//===============================================================
			//Take the screwdriver from the clock
			//===============================================================
			if (command.unordered("screwdriver|flathead"))
			{
				if (this.gameState.checkFlipped("screwdriver taken") == false)
				{
					this.gameState.addToInventory("Flathead Screwdriver");
					this.gameState.flipFlag("screwdriver taken");
					return new DisplayData("", "You open the clock's glass door, and take the screwdriver.");
				}
				else
					return new DisplayData("", "You've already taken that.");
			}			

		case "remove":
			//===============================================================
			//remove the painting from the wall
			//===============================================================
			if (command.unordered("painting"))
			{
				if (this.gameState.checkFlipped("painting removed") == false)
				{
					this.gameState.flipFlag("painting removed");
					return new DisplayData("", "You take the painting off the wall and set it on the floor, to reveal "
							+ "a breaker box from behind it.");
				}
				else
					return new DisplayData("", "You've already done that.");
			}			

			//===============================================================
			//No command matches, return failure.
			//===============================================================
			return new DisplayData("", "Can't do that.");

		case "flip":
		case "pull":
			//===============================================================
			//pulling the switch
			//===============================================================
			if (command.unordered("lever|switch"))
			{
				if (this.gameState.checkFlipped("painting removed") == true)
				{
					if (this.gameState.checkFlipped("paperclip installed") == true)
					{
						if (this.gameState.checkFlipped("power restored") == false)
						{
							this.gameState.flipFlag("power restored");
							return new DisplayData("", "You pull the switch down. With a eerie whurr and a few sparks, power surges throughout the house!");
						}
						else
							return new DisplayData("", "Power has already been restored.");
					}
					else
						return new DisplayData("", "You pull the switch down; nothing happens.");

				}
				else
					return new DisplayData("", "You don't see that here.");
			}			

			//===============================================================
			//No command matches, return failure.
			//===============================================================
			return new DisplayData("", "Can't take that.");

		case "place":
		case "repair":
		case "use":
			//===============================================================
			//Use paperclip on wire
			//===============================================================
			if (command.unordered("paperclip|paper clip|clip", "wire|wires|breaker|box"))
			{
				if (this.gameState.checkFlipped("paperclip installed") == false)
				{
					if (this.gameState.checkInventory("Paperclip") == true)
					{
						this.gameState.removeFromInventory("Paperclip");
						this.gameState.flipFlag("paperclip installed");
						return new DisplayData("", "You twist the paperclip around the ends of both wires. This won't start a fire.");
					}
					else
						return new DisplayData ("", "You don't have that item.");
				}
				else
					return new DisplayData ("", "You have already done that.");
			}

			//===============================================================
			//No command matches, return failure.
			//===============================================================
			return new DisplayData ("", "Can't do that here.");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room|hall") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "Looking more closely at the painting, you notice that it isn't resting flat against the wall.");

			if (command.unordered("painting"))
				return new DisplayData("", "It is an image of a galleon ship, sailing on open sea. A square red cross "
						+ "is displayed on the main sail.");

			if (command.unordered("coat|hanger|coathanger"))
				return new DisplayData("", "A sturdy iron structure, its rustic design feels right at home. Not sure "
						+ "how much use it would get on a farm, however.");

			//===============================================================
			// description of clock whether or not screwdriver has been taken
			//===============================================================
			if (command.unordered("clock|grandfather"))
			{	
				String description = "The ornate design of this grandfather clock feels out of "
						+ "place in a ramshackle house such as this. ";

				if (this.gameState.checkFlipped("screwdriver taken") == false)
					description += "Through the glass of the door on the front, you notice "
							+ "a flathead screwdriver resting inside.";

				return new DisplayData("", description);
			}

			//===============================================================
			//inspecting the circuit breaker, with or without paperclip installed
			//===============================================================
			if (command.unordered("circuit|breaker|box") && this.gameState.checkFlipped("painting removed") == true)
				if(this.gameState.checkFlipped("paperclip installed") == true)
					return new DisplayData("", "The paperclip is now coiled around the ends of each side of the wire; "
							+ "the connection looks secure.");
				else
					return new DisplayData("", "You open the breaker box. Inside you find a large switch, and a severed wire, "
							+ "it's two sides barely separated from their frayed ends.");

			//===============================================================
			//If default is reached, check to see if the command contained an 
			//inventory item.  If yes, run the command through it.  If the
			//result is not null, return it.
			//===============================================================
			DisplayData displayData = this.inventoryTest(command);
			if (displayData != null)
				return displayData;

			//===============================================================
			//No command matches, return failure.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		default: 
			//===============================================================
			//If default is reached, check to see if the command contained an 
			//inventory item.  If yes, run the command through it.  If the
			//result is not null, return it.
			//===============================================================
			displayData = this.inventoryTest(command);
			if (displayData != null)
				return displayData;

			//===============================================================
			//If default is reached, return a failure message.
			//===============================================================
			return new DisplayData("", "Can't do that here.");
		}
	}

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String entrywayImage = "dddmdmmmddddddddddddmddddddddddddddddddddmdddddddmmmddhhhyyyyyyyysyyyssssssssssssssssyyyyyyyyyyyyhhhhhhdddmmmddddddddddddddddddddddddddddddmddddddddddddddddd\r\n" + 
			"ddddddddmmddddddddddddddddmdddmdddddddddmddddddddmmmhhhyyysyhddhyssssssssoooosssssosssssssssyyyyyyyyhhhdddddddmddddddddmddddmdmmdddddddmdddddddddddmddddddddd\r\n" + 
			"mdddddmddddddddddddddddmdddmdddddddmddddddddddddmmmdddhhsssssssssooooooooooooooooooooooossssssssyyyyyhhhdddhhddddddddddddddddddddddddmdddddmdddddmddddddddddd\r\n" + 
			"dddddddddddmdddddddddddddddmdmddddddmddddddmddmdmmddddhhyysssoooooo+++++++++++++//+++ooooooosssssssyyyhhhhhhddddddddddddddddddmddddddddddmddddddddddddddddddm\r\n" + 
			"dddddddddmdddddddddddddddddddddddddmdddddddddddddmdddhhhyyhsoo++++++////////////////++++++ooooossssyyyyhyyhdddddddddddddddddddddmdddddddddddddddddddddddddddd\r\n" + 
			"ddddddddddmddmdddmdddmdmddddddddddmddmmddddddddddmddyhhdssyyyo++///////////////////////++++++ooooosssyysyhhdddddddddddddddddddmdddddddddmddddddddddddddddmddd\r\n" + 
			"ddddddddmddmdmddddddddddddddddddddddddddddddmddddddddhhdsooooss///:::::::::::::::::::://////++++ooosssyyyhhdddddddddddddddddmdddddddddddmdddddddddddddddddddd\r\n" + 
			"dddddddddddddddddmdddmddddddddddmddddddddddddddddddhddhhooo+++ss::/::/::-::::--:::-:::::::////+++ooosssyyhhddddddddddddmddddddmddddddmddmmdddddddmdddmdddmddd\r\n" + 
			"mddddmdddddddmddddddddddddddmddddddddddddmddddddddddhhyho+++/++++::://::-----------:::::::://+/+++soossyhhhhdddmddddddddmdddddddmddddddmddddddddddddddddddddd\r\n" + 
			"dddddddddddddddddddddddddmdddddddddddddddddddddddddhhhyho++////:+/:-/+////:/:::::::::///////////+osooossyhhhhmddddddddmdddddmddddddddmddddddddmddddddmddddddd\r\n" + 
			"dmddddddddddmdddddddddddddddddddddddddddddddddddddhhhyyhyo+////://::::-::::::::::::::::::///////+so+oossyyyhdddddddmdddddddddddddddddddddddddddddddddddddmdmd\r\n" + 
			"dddddddddddddddddddddmdddddmdddddddddddddddddddddhhhhyyyysooo/:-/:-:---/:/:::::::-:::::////++::/oos++oyyhddmmddddddmmdddddddmdddmdddddddmdddddddddmddmdddddmd\r\n" + 
			"dddddddmdddddddddddddddddddddddddddddddmdddddddddhhhyyy+.-/os/:`::-::--/::::////:::://++++//+::/o+o++oddddddddddddddddddddddddddmmdddddmdddddddmdddddddddmddd\r\n" + 
			"ddddddddddmdddddddmdddddddddddddddddmdddddmddddddhhyyys+---oo+:`-:-.--::::--........------://::/+so/+odmmmmmddddddddddddddddddddddddddddddddddddddddddddddddd\r\n" + 
			"ddddddddddddmdddddmddddddddddddddddddddddddddddddhhyyys/---oso:`---`-----..............----//::++oo//oddmdddddddddddmddddddddddddddddddddddddmddddddddddddddd\r\n" + 
			"dddddddddddddddddmddddddddddddddddddddddddddmddddhhyyss/-::sso:`---`--.-.......`.......------:.+yyo+/odddhhdddddddmdddddddmdddddddddddddmdddddmdddddddddddddm\r\n" + 
			"dddddddddddddddddddddddddmddddddddddddddddddmddddhhysso/---syo:`----:---.-:::.```.---.`--::/--`/hho+/ohhmyyhddmddddddddddddddddddddmddddddmdddddddddddddmmddd\r\n" + 
			"dddddddddddmdddmdddddddddddddmdmdddmddmddddddddddhdhysy+--/sso:`---.-..-.--::.```.:--``..:/-.-`/hh++/ohdddhyddddddddddmddmddmddddddddddddmdddddddddddmddddddd\r\n" + 
			"dddddmdmmmdddddddddddddddddmdddddddddddmdddddddmdhhhyyy+--:ooo:`---`-.`-.-:::.```.:--.`-.:/-`-`/hh+//odmdddmdmdmmddddddddddddddddddddddddmdddmdddmddddddddddd\r\n" + 
			"ddddddddddddddddddddddddddddddmddddddddddddddddddhhyyyy+-:/o++:`-:-`-..-.-::::---:/:::::://-`-`/hh///odmddddmmdddddddddddddddddddddddddddddmdddddddddddmddddd\r\n" + 
			"ddddmdddddddddddddddmddddddddddddmdmddddmddmdddddyyyyss+o:+so/:`-:-`-..-.-/::::---/:-----:/. -`/hh:::+ddhdddddmdddmdddddddddddddddddddddddddddddddmmdddmddddd\r\n" + 
			"dddmdddddmddmddddddddddddddmdddddddddmdddddmdmddhyysssss//:os/:`-:-`--.-.-/::::::/+:-:::://``.`/hh:-:/dmdddddmdddmdddddddddddddddddddddddddmddddddddddddddddd\r\n" + 
			"ddddddmddddmdmddddddddddddddddmdddddddddddddddddhhyyyooo///o+/:`-/-.--:/:-///::::/+/::::://``..:yh--:+ddddddddddddddddddddddddddddddddddddddddddddddddddddddd\r\n" + 
			"dddddddmdddddddmddmdddddddddddddddddddddddddddddhhyssoo++-:o++:`-+-:/-/::-//////++///:////o--..+hh---/dddddddddddddddmdddddddddddmdddddmddddddmdddddddddddddd\r\n" + 
			"dddddmdddddddddddddddddddddddmdddddddddmddmmddddhyysso+o+--o//+:+o-:/-+/::++++//++/++++++++:-..oho.-:/dddddddddddddddddddddddddddmdddddddddddddddddddddddmmdd\r\n" + 
			"dddddddddddddddddddddddddddddddddddddddddddddmddhyssso+/:::+//+o+o-:/:+o+/oo+++::::++++o+o+:-.-shs.--/dddddddddddddddddddddmddddddddmddddmdmddddddddddddddddd\r\n" + 
			"ddddddddddddddddddddddddddddddddddddmmddddddddddhyssss++o+:++//+++-:/:+oso+/////:-:::-:::/::-..sho.--+mmdddmddddddddmddddddddddddddddddmddddddddddddmdddddddd\r\n" + 
			"ddddmdmddddddmddddddddddddddddddddddddddddmdddddhysyyss+++:+o/+o++:/+/ossoo++///:--..--:::/+:..+do..-+ddddmddddmdddddddddmdddmmdddddddddddddmddddddmddddddddd\r\n" + 
			"ddmdddmddmddddddddddddddmddddddddddddmmddddddddmdhhyo/++-:-+++++++:/o+syo+sso++/:::::------:-..+ho`.-odddddhdddddmddmdddddddddddddddmdddddddmdddddddmdddddddd\r\n" + 
			"dddddddddddddddmdddddddmddddddddddddddddddddddmdhso++/o+:--+++/oo+:+ssyyyssoo+//:-:::::-/-::/../h+..-ommhhddmddddddddddddddddddddddddddddmdddddddmdddddmddddd\r\n" + 
			"dmdddddmmddddddddddddddmmddmdddddddddddddddmddddhso++/++:--++++oso:syyysyysoo+::--------:::::/`/h+`.-odhhdmmmddddddddddddddmmddddmdddddddddddddddddddddddmmdd\r\n" + 
			"dddddmdddddddddddddddddddmddddmdddddddmddmdmddddhsoos+/+-:-+oooyyyyyhyyyyso++/--------::-://-/-/h/..-odmdmmmmddddddddddddddmddddddddmdddddddddddddddddddddmdd\r\n" + 
			"dddddddddddmdddddddddmddddddddddddddddmddddddmddhysso++/:--+oosyyhyohyyysoo+/::-------::::://::+y/..-ommhmmmdmddddmdddddmdddddddddddddddddddddddddddddddddddd\r\n" + 
			"mddddddmdddddmdmdddddmddddddddddddddddddddddddddhhsso++/:::+ssyhhyyhyyso+++/::::::::///////+/+o+/:..-+hmhmmmdmddddddddddddddddddddmdddddddddddddddddddddddddm\r\n" + 
			"dddddddddddddddddddddddddmdddddddddddddmdddddddddysso+++:::+sooyysoooo+++//::::::::///+///+/:///+:..-+dmhdmmddddddddddddmddddddmdddddddddddddmddddddddddddddd\r\n" + 
			"dddddddddddddddddddddddddddmdddddddmmddddmddddddhssso+ss--:::--:-------..----.----:-::://///:/+//:..-+dmdddddddddddddmdddddddddmdddddddddddddddddddddddddddmd\r\n" + 
			"ddddddddddddddddddddddddddddddddddddddddddddddddhyssysso`.--......-.--...-....-----::::/://+++o+++:--ommdddddddddddmmddmdddddddddddddddddddmdddddddddmddddddd\r\n" + 
			"mmmdddddddddddddddddddddddddddmddddddddddddmddddhyyyyyhyo//::-...----..----:/::-::://::::/++/+/::++--ymmhdddhddddddmddddddddddddddddmddddddddddddddddddddddmd\r\n" + 
			"ddddddddddddddddmdddddddddddddddddddddmdmdddmdddhyyyhhdhdhyyo+//:-::+:---::::://////////:://+//o+++o-odmddddhmddddddddddddddddmddddddddddddddddddddddddddmmdm\r\n" + 
			"ddddddddmdmdmddmdddmdddddddddddddddddddddmdddddddhhhhddddddhyyso+o+++o+/////////////+/+///+oo+so+++o/omNmdddhdmddddddddddmddddddddddddddddddddmmddddmdddddddd\r\n" + 
			"dddddddddddddddddddmdddddddddddddddddddddddddddmdhhddhdddddddhhsyyysyssooo+++oo+o+++oooossosss++//shyshhddddhddddddmddddddddddddddddddmddddddddmddddmmddddddd\r\n" + 
			"dddddddddddddddddmddddmdddddddddddddddddddddddddddddhdysyddddmddhhhyyyyyyyyyyyyyyyyyyyyyysyyhhhyssyhdhdydyyyddddddddddddddddddmdddmddmddddddddmdddddddddddddd\r\n" + 
			"dmddddddddddddddddddddddddddddddddddddddddddddddmmmdddddhhmmmmmmddhhhhhhhdhhdhhyyhhddddhhddhdhddddddhhddmmdyhdddddddmddddddmdddmdmdddddddddddddmddddddddmdddd\r\n" + 
			"ddddddddddddddddddddmdmddddddddddddddddddmddddddddmdmmmmmNmmNmmmmmddddddddddhhhdddddmdddmmdddmmddddmmmdmmdddddddddmddddddmddddddddddddddddddddmdddddddddddddd\r\n" + 
			"ddddmddddddddddddddddddddddddddddddddddddddddddddddmmmNNNNNNNNNNmmmmmmdmmddddddddmmmmmNmmmNNNmmmmmNmmmmmmNNmddmdddddddddmdddddmddddddddddddddddmddddddddddddm";
}
