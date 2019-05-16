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
import Structure.MultiFlag;
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
		this.description = "You stand in an entry hallway. The door leading to the front porch stands open, no longer wanting to close properly. "
				+	"An old, busted up grandfather clock stands just beyond the door, its once grand countenance now sadly destroyed. "
				+   this.gameState.getFlag("painting removed").toString()
				+	"Directly ahead of you is a door leading to the living room. On the right, a open archway leading to the dining room. "
				+	"On the left, warped, weathered stairs lead up to the second floor. ";

		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Farmhouse Porch
		//=================================================================================
		this.addMovementDirection("outside", "FarmhousePorch");
		this.addMovementDirection("porch", "FarmhousePorch");
		this.addMovementDirection("leave", "FarmhousePorch");
		this.addMovementDirection("exit", "FarmhousePorch");

		//=================================================================================
		//Create directions that move to Living Room
		//=================================================================================
		this.addMovementDirection("livingroom", "LivingRoom");
		this.addMovementDirection("living room", "LivingRoom");
		this.addMovementDirection("straight", "LivingRoom");
		this.addMovementDirection("forward", "LivingRoom");

		//=================================================================================
		//Create directions that move to Dining Room
		//=================================================================================
		this.addMovementDirection("diningroom", "DiningRoom");
		this.addMovementDirection("dining room", "DiningRoom");
		this.addMovementDirection("right", "DiningRoom");

		//=================================================================================
		//Create directions that move to Upstairs Hallway
		//=================================================================================
		this.addMovementDirection("upstairs", "UpstairsHallway");
		this.addMovementDirection("stairs", "UpstairsHallway");
		this.addMovementDirection("stairway", "UpstairsHallway");
		this.addMovementDirection("left", "UpstairsHallway");
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
		this.gameState.addFlag("screwdriver taken", new Flag(false, "", ""));
		this.gameState.addFlag("wire installed", new Flag(false, "", ""));
		this.gameState.addFlag("master switch on", new Flag(false, "", ""));
		this.gameState.addFlag("painting removed", new Flag(false, "On the wall beyond the stairway lies a painting. ", 
				"On the wall beyond the stairway a breaker box is visible, with a painting resting against the wall below. "));

		//=================================================================================
		//Flags used in multiple rooms beyond this point
		//=================================================================================
		this.gameState.addFlag("generator started", new Flag(false, "", ""));

		//=================================================================================
		//This multiflag represents the house receiving working power
		//=================================================================================
		MultiFlag power = new MultiFlag(this.gameState, false, "", "");
		power.addFlag("wire installed");
		power.addFlag("master switch on");
		power.addFlag("generator started");
		this.gameState.addFlag("power restored", power);
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
			//Take stairs, move accordingly
			//===============================================================
			if (command.unordered("stairway|stairs|stair") == true &&
				command.getVerb().matches("grab|pick") == false)
			{
				if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
					return this.move(command);
			}
			
			//===============================================================
			//Take the screwdriver from the clock
			//===============================================================
			if (command.unordered("screwdriver|flathead"))
			{
				if (this.gameState.checkFlipped("screwdriver taken") == false)
				{
					this.gameState.addToInventory("Flathead Screwdriver");
					this.gameState.flipFlag("screwdriver taken");
					return new DisplayData("", "Careful not to cut yourself on the broken glass, you fish the screwdriver out of the old clock. ");
				}
				else
					return new DisplayData("", "You've already taken that.");
			}			

			//===============================================================
			//Intentionally allow Take commands to bleed into "remove", so
			//that "Take painging off of wall" will catch the painting code.
			//===============================================================

		case "remove":
			//===============================================================
			//Remove the painting from the wall
			//===============================================================
			if (command.unordered("painting"))
			{
				if (this.gameState.checkFlipped("painting removed") == false)
				{
					this.gameState.flipFlag("painting removed");
					return new DisplayData("", "You remove the painting from the wall, setting it on the floor. "
							+ "Behind the painting is a breaker box. ");
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
			if (command.unordered("master|fuse"))
			{
				if (this.gameState.checkFlipped("painting removed") == true)
				{
					if (this.gameState.checkFlipped("master switch on") == false)
					{
						this.gameState.flipFlag("master switch on");
						if (this.gameState.checkFlipped("generator started") == false)
							return new DisplayData("", "You flip the Master switch to the 'On' position. Nothing noticable happens. ");
						else
							return new DisplayData("", "You flip the Master switch to the 'On' position. "
									+ "You see a spark come from the exposed wire. ");
					}
					else
					{
						this.gameState.getFlag("master switch on").reset();
						return new DisplayData("", "You flip the Master switch back to the 'Off' position. ");
					}
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
		case "fix":
		case "bridge":
		case "use":
			//===============================================================
			//Use copper wire to repair cut wire
			//===============================================================
			if (command.ordered("copper wire|copper|wire", "wires|wire|breaker|box") ||
				command.ordered("wires|wire|breaker|box", "copper wire|copper|wire"))
			{
				if (this.gameState.checkFlipped("wire installed") == false)
				{
					if (this.gameState.checkInventory("Copper Wire") == true)
					{
						if (this.gameState.checkFlipped("master switch on") == true &&
							this.gameState.checkFlipped("generator started") == true)
						{
							return this.gameState.death("You realize your mistake as soon as you touch the exposed wire. "
									+ "It feels hot as the electricity courses through you, and your eyeballs warm as they cook. "
									+ "Your sight fades. ");
						}
						else
						{
							this.gameState.removeFromInventory("Copper Wire");
							this.gameState.flipFlag("wire installed");
							return new DisplayData("", "You use the bit of copper wire you found to fix the cut wire, bridging the gap. "
									+ "It should have a proper connection now. ");
						}
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
				return new DisplayData("", "The entryway is full of dead leaves and refuse. This front door must have been standing "
						+ "open like this for quite a while now, for this much debris to make it's way in here. "
						+ "The wallpaper is faded and spotted with black mold. This place is in bad shape. ");

			if (command.unordered("painting"))
				return new DisplayData("", "It's a reproduction of the classic 'American Gothic' painting, by Grant Wood. "
						+ "It was probably nice at one point, but some vandal has since defaced it with a marker, "
						+ "and it's torn in several places. You see a glint of metal through one of the rips. ");

			//===============================================================
			// description of clock whether or not screwdriver has been taken
			//===============================================================
			if (command.unordered("clock|grandfather"))
			{	
				String description = "Once grand, this old clock now sits in shambles. The glass is busted. The frame is splintered. "
						+ "The face is gone, and the mechanism is half taken apart, pieces rusted and strewn about. ";

				if (this.gameState.checkFlipped("screwdriver taken") == false)
					description += "In the debris at the bottom of the clock's interior, you notice an old screwdriver. ";

				return new DisplayData("", description);
			}

			//===============================================================
			//inspecting the circuit breaker, with or without paperclip installed
			//===============================================================
			if (command.unordered("fuse|circuit|breaker|box"))
			{
				if (this.gameState.checkFlipped("painting removed") == true)
				{
					String description = "It's a typical breaker box, full of fuses. Each fuse has a label, marking what it controls. "
							+ "Off to the side of the rows of fuses is one that's larger than the others, labeled 'Master'. ";
					
					if(this.gameState.checkFlipped("wire installed") == true)
						description += "Through the hole in the panel, you see the wire you repaired. It looks intact. ";
					else
						description += "A hole in the panel exposes a bundle of wires that are likely important. "
								+ "It looks like someone has cut one of the wires for some reason, its two ends left dangling. ";
					
					return new DisplayData("", description);
				}
				else
					return new DisplayData("", "You don't see a breaker box here. ");
			}

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
