/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms.Forest;

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
		this.name = "CaveEntrance";
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
				+ "In the center of the sheer rock face is a large, crude wooden door. "
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
		this.addMovementDirection("south", "ForestCrossroads");
		this.addMovementDirection("path", "ForestCrossroads");

		//=================================================================================
		//Create directions that move to Cave Interior
		//=================================================================================
		this.addMovementDirection("west", "CaveInterior");
		this.addMovementDirection("cave", "CaveInterior");
		this.addMovementDirection("door", "CaveInterior");
		this.addMovementDirection("entrance", "CaveInterior");
		this.addMovementDirection("inside", "CaveInterior");
		
		//=================================================================================
		//Create directions that move to Forest Exit
		//=================================================================================
		this.addMovementDirection("north", "ForestExit");
		this.addMovementDirection("stair", "ForestExit");
		this.addMovementDirection("stairs", "ForestExit");
		this.addMovementDirection("staircase", "ForestExit");
		this.addMovementDirection("up", "ForestExit");
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
			if (command.unordered("back"))
				return this.displayOnEntry();
			
			//===============================================================
			//If go into cave
			//===============================================================
			if (command.unordered("west|cave|door|entrance|inside"))
			{
				if (this.gameState.checkFlipped("cave door open"))
					return this.move(command);
				else
					return new DisplayData("", "The door is closed. You aren't sure how to open it. ");
			}

			//===============================================================
			//Change current room and return new room DisplayData.
			//===============================================================
			if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
				return this.move(command);

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
			if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
				return this.move(command);

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
			if (command.unordered("torch|unlit|lit"))
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
			if (command.unordered("torch"))
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
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The outer edges of the area of packed dirt, give way immediately to dense foliage and large trees. "
						+ "The cliff face with its curious oaken door stand out, as the lone break in the greenery. "
						+ "However, on the northern edge of the area, you do see a piece of stone sticking out from under the brush, "
						+ "and it looks squarish, hewn, not a natural shape. ");

			if (command.unordered("door"))
			{
				String doorDescription = "";

				//===============================================================
				//Different descriptions based on whether the door is open or not
				//===============================================================
				if (this.gameState.checkFlipped("cave door open"))
					doorDescription += "It's a giant slab of oak, made all of one piece. It's pretty thick. "
							+ "It's plain, no markings or windows, or a handle even. It's currently sitting open. "
							+ "A lit torch is sitting in a sconce next to the door. ";
				else
				{
					doorDescription += "It's a giant slab of oak, made all of one piece. It seems pretty thick. "
							+ "It's plain, no markings or windows, or a handle even.  You aren't sure how to open it. ";

					//===============================================================
					//Different descriptions based on whether the torch is placed
					//===============================================================
					if (this.gameState.checkFlipped("torch placed"))
						doorDescription += "An unlit torch is sitting in a sconce next to the door. ";
					else
						doorDescription += "An empty sconce is attached to the stone cliff face next to the door. ";
				}
				
				return new DisplayData("", doorDescription);
			}

			if (command.unordered("cliff"))
				return new DisplayData("", "This portion of the cliff is much like the other, sheer and without handholds. "
						+ "It's taller here, though you aren't sure by how much, as the canopy hides the top from view. "
						+ "The door is the only thing of note. ");

			if (command.unordered("stone"))
				return new DisplayData("", "Moving aside the foliage, you find that the stone is the first step of a hewn stone staircase, "
						+ "heading upward, possibly towards the top of the cliff. The foliage almost completely hides it from view at the edges "
						+ "of the area of packed dirt, but it looks to be clear enough to traverse easily beyond that point. ");

			if (command.unordered("sconce|torch"))
			{
				//===============================================================
				//Different descriptions based on whether the torch is placed, and
				//if it's lit or not
				//===============================================================
				if (this.gameState.checkFlipped("torch placed"))
				{
					if (this.gameState.checkFlipped("cave door open"))
						return new DisplayData("", "A lit torch sits in the sconce. ");
					else
						return new DisplayData("", "An unlit torch sits in the sconce. ");
				}
				else
					return new DisplayData("", "It's a bracket for holding a torch about an inch and a half thick. It's currently empty. ");
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
			//Subject is unrecognized, return a failure message.
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
	private final String caveEntranceImage = "hhhhyyyyssosyddhhyyyMMMs++osMMNo++++++oNMMmosooo+o+++o+/+o+/+so+++++sysooo+++osssys+//+o+++oo++/////++o++soooooso+yyysyooosoo+++oy++//oooymMMMMdyysosyydddmmm\r\n" + 
			"hdhyyhsssyyyyhyossooMMNssosdMMh+o+ooosmMMmossoosoooosoooo+os++ososssssoo+ysooosssyysooosyyosooo+/+///oo+oo+ssss++sos+o+oosso+o/+ooo/:/++oyhdMMMMNyohdddddmmNm\r\n" + 
			"dhhddyyoyhhyysyyysooMMNoooyMMNs++oosyNMMN+osssso+ss+sysosssooososyhysssoos+osssysyyyysyyy+++/++//+/++o+soysssys+oso/soo+//ssysyysoooo/+sysshmMMMMNdddmdhhdddd\r\n" + 
			"mddddhyhhhhhyyyyossyMMMyysmMMhsoooomMMMNs+oosssoosoossyyossyyyhyyyyhyddhyhyyyysssyyyyyyy+/++ooooooossssoosssoo+ooss+soossssyyyyyssssyoshhhyyhdNMMMMdhhdddddmm\r\n" + 
			"mddddhhyddhyysssysosMMmsodMMd+soosNMMMms+oo+ossssyyyoyhs+oyhhyhhhhyhhhddddmmNmhhyyyshhhsssysssssssyydddddddhmdhyyyyyyyyyyyyyyyyyyyssosoyhhhdmdhdMMMMdhhdmmddd\r\n" + 
			"yymdhhhhhooyyyyyyosdMMy+yMMmosossdMMMdosoo+oosssssysyssooyhhhhhsyyhddhhyhhdddmMMMmNNNNMNNmNdddhdddhhssossooosshdmmmhhhyyyhhyhhyyyosssyyyhhhddhhhmMMMmhddmdhdd\r\n" + 
			"yhhhhdddhysyysssyysmMMsodMMyosssyMMMdsssosysssyssyyyyhhysosyyyyhhhdmNNNNNNNmdhdddhhhhysssyhddmNNMMMMNNNNmddhdhddddmMMMMNmhhhhysysooyyhhhddddhyyyyhNMMNmddddhd\r\n" + 
			"yyyhhhhyyhhhyo++ssyMMNssNMMso+o/mMMmssyssyyyyhyyyyyhyyyyy+oyyhydmNNmdyhhyhhyhyyyhhyyyhyysyyyys+/+ooydmNMMMMMMMMMMMMMMMMMMNNddhyhysyyhhddhdddhhyysydMMMMmhyhdh\r\n" + 
			"ssshhhhysohyyo++oshMMdsyMMNsssssMMMssyyysoyhyyhhyyyyyysssydmNNmmdhyhhhhhhyyyysyyyyssso+sssyyy++o++oossshyyddhdddhhhhdyyhdNMMMNmdyyyhhhddddddhyyhyhhmMMMMMdhhy\r\n" + 
			"y+osyyyhhhhhhysyssNMMhshMMmsyyhMMMmyhhhysssyyhyysyyhhyydNNmmdyyosssyhhhhyyysysoooooo++oooo+///ooshhyysoo+yhyy+++++osyysooyhdNMMMNmhhdmddddhhyyyhyyhhdMMMMMNmh\r\n" + 
			"yyoyyhdyyhdmdmddhNMMNysdMMdyysmMMMhyyyyyyyyysssssyssdmNdhhhhhyyysooosoosssooss+oooo+o+oo+oo++oosshhyssooshysososso+oysysysoyyhdNMMMNmdhhhhdhyhhdddddhdNMMMMMm\r\n" + 
			"ssyyhhhyyyhddhhhmMMMdyydMMmyydMMMMyhysyhyyyyyyyyhdNMNdyhhhhyyysossoooooooo+++/++oosso/+o+++ooooooosoosyyyyyssssyhs+ssssooyhyyhhddmNMMMmhhyhhddddmmdhhhhmNMMMM\r\n" + 
			"sssyhhdmdddhhhhhNMMdyyhmMMNhmMMMMdssyyyyyhhhyyhmNMNhhssssssoo+++ooo+o++++/++//:/++o++//+///+/+++++oooooosssossssoss+/oosysyyhdhyhysNMMMMmyyhhhddddhhhddhmmMMM\r\n" + 
			"ddhddmNmdyyhyhhMMMmysssdMMmdMMMMMhs+shhyhmmddNMMNdysyyyysso+++oo+////+o++o+/:////++o//o+///+/+++++ooo++o++ooooo+++s+//+o+syyyyyhhhhhdNMMMNdyyhdhdhhdyhhhdddNM\r\n" + 
			"yhhhhddyyyyhshNMMMysoosmMMNNMMMMNhhhddmddhdmmMNh++oosssysooo+++//://::/:///++//+++++/+o+/+++o+++++///+/+++oo+++oosho/+ooooshhhhyyhhhhydNMMMNhhddmddhddhddhsyN\r\n" + 
			"hhyssyso+oshhMMMMmhooosdMMMMMMMMy+sshyyhhhydNmy++ssooooooooo+o+/+ooo++osssssooossoossossoossyysysoo+++++++ooo++++ydyssysoossydNmhhyssyyhmMMMMmddmmmmdhdmdhysy\r\n" + 
			"hhyhyhyhsosmMMMMNdhyosydMMMMMMMdssosyhyydNmmdy+sssossooooooo+o///+///hNmmNNNNNmNNNmNmmNNmNNmNNNNh++o/++/+o+oooso+sysyhyysssyyhhdmmdyhysshmNMMMMmhhhddhydyyyss\r\n" + 
			"hdhhhhyyhydMMMMMddhhyhhdMMMMMMmhyyhhhhNNmdydho+soooo+oooooooooo+++++/ymmmNmmNNNNNNmmmmmNmmNNNNNNy/++++/o+o+oooyyhdyssyyyhyooyhyhyyhhy+/+osdmMMMMmdhyhyyyhsyyh\r\n" + 
			"dhyhhdhyyhNMMMNyhyyshdhNMMMMMNdyhyhyyydhhsyhyssooooooo+/++oo+++o+++oohdhmmddmmmNNNmdhdmmdmmmmNNNy:+/::/+:+osoosyhhyyysyhhysyyysyyysyyso++oydNNMMMNdhhsyyhhhhy\r\n" + 
			"ddddhddhyhMMMmddhyhhhhmMMMMMNyhhhysssoyhhshysossssoooo+o++++++////+//sdddmdmmdmmmmdyhmdmdmdmmmmmh//+/://://ossyhhhyyyhhhhhhyyhyhhhhhhhhhhyyydNMMMMMNdyhhhyshh\r\n" + 
			"mmmdddhhdNMMmshmdhyhyhMMMMMMhyyyysyhyyyyshhhyhysysooooo+o+/++///+++//sddmmmmdmmmdmmddmmmmmdmmmdmh++++:///ooososyyydhhhddmddddddhydhhhhyyhyhyhyhNMMMMMMNhhhhyh\r\n" + 
			"dhhhhdhymMMMhshsssyhhdMMMMNdhyhhyyyhyyssssssyyyysyyhyso+++//////++//:sddmmmmdmmmmmddhmmmdmhmmmmmy/++//++++osooosssyysyhhdhdmddddmmmdysyhhddhhhyssmNMMMMMMmddh\r\n" + 
			"syhddddyNMMMdyssydhddNMMMMmhyhhhyhhhhhyysosossyssyhysoosss+ooo+///://ymdmmmmmmmmmmmmdmmmmmdmNmmmh////+++/++oo++oyyhssyhhhyhhhhdhdmdddhyyyhdhdysssyyhdNNMMMMMm\r\n" + 
			"yyhhhddmMMMNhshdddhhhNMMMMdshyhhhhhhyyyossyhhhyyhhsss//oo++/+:oo+sso+ymmmNNNmmNmmmdmmmmNNNdNmmdmh/++++/+///++++ossyhyhyyyhhshddmddddhhhdhhhyyhyhdydyhhddmNNNd\r\n" + 
			"hssysssMMMMdhdddddhhdMMMMNhdhhhhhddhhhyssssssyyyysss+/+++++/+-:/-/+oshmdmNNmNmNmmmdmmmNNmNmNmmmmy+:/+//+//:/++oooossysyyyyhhyyyhmmdddhyhhoyyyhdddydhddhhdhdhd\r\n" + 
			"syyyysyMMMNdddhddddNMMMMMmhydhdyssyhhhyyyhhyhyysyyss//+++++//:/:/+/+ohmmNNNNNmNmmmmmmmNNNNmNNmmmh/::o+o/////oo+++/osyyhydhhhhhhhhmhdhysyyydddddhhyhhyhhhdhdhd\r\n" + 
			"ysyhyhmMMMNmdhyhhhdMMMMMMdyhhhyyosoyyyyyyhhhhyysyyys:/+////:///+/+/++hNmNNNNNNNmmmmmNmNNNNNNNmmmh+++o+++///+oo+++oooyyyhhdddhdyhmmdhddhhdddddmmdhyhyhhyhhyyhh\r\n" + 
			"sssyhddMMMNyyyhyhddMMMMMNmhhhydhysyyhhyyyyyyyyyyssysoso+/++/++/:osyyydNmNNNmNNNmmmmNNNNNNNNNNmmNdoo+oo+o+/+//+o+++ossssyhyyhdddddhhyhhdydhdmddddhdddhhhddhhhh\r\n" + 
			"oyhyhdmMMMNyyhhyhydMMMMMMNyhyhhsyhdmhyysossssyyyyyyyyyso+/+++/:.-:-++hNmNNNNNNNmmNmNNNMNNNNNNmNNh/+o++:/+++++++/+o+oysyyhhyhddddhyshhddhdhddhdddhmdhhyhhhhhhh\r\n" + 
			"+syhdmMMMMNshhyhdhmMMMMMMNdssyyshyhmysysssoooyssyssyssyossooo//-:-:++hNmNNNNNNNNNNmMNNMNNNNNNNNNho++/oo+/o+//+o+/++ossyyhhyhdmdhhyyhdddmdhmddmNmdNdhdhhdmdmmd\r\n" + 
			"yyydmmMMMMmhhyyhdyNMMMMMMMhhhhyddhmmddhhyyyhys+oyysosysssso+oso++-:++hMNMNNNMNMNNNNMNNNNNNNNNNNNd/////+o+oo++ossosossyyyhsyhhdddddhddhhdNNmmdmmmdmmhddddmdmdd\r\n" + 
			"ysymdmMMMMdhyysyhhNMMMMMMMmdddddddhhdddmhmmmmmmdmmmdmdddhsyssssoo//oohNmMNNNMMNNNNNNNNMNNMMNNNNNdo//+ooss+/+/oyyoosossyyhhhhhdmddhddmdhdmNmNdmmNNNddmdddmmmmd\r\n" + 
			"yyhhdNMMMNhhhyyyhdmMMMMMMMNhdmmdmddhdmhhdhhddddddddhddddysssyyosysss+yNNNNNNNMNNMNNMNNNNNNMNNNNNd+oo++ooyso+/+yyysysssysossyhddyydmNNdddmMMNNmdMdmhhdhddddmmm\r\n" + 
			"ddddNMMMMNmmddddddmMMMMMMMMhydddmmmdhhdddddmmmmmddyyhysssyoossossssyshNmNmmmNNmNNNNNNmNmmmNmmNNmdsoooossyssoosyhyssssssoossyyyyyhdhhdmmmmmNNNNNNhdmddddmdddmd\r\n" + 
			"dddNMMMMMmmmmmmNNNNNNNMMMNNdddddmmmmmmmNmNmdsyyssosososssssysoooossooyhhddhhdddddhdddhhdddddddddhooo++ooyssssoyhhysysshsssssyyyyydmddddddhdddddmmmmmmmmmmNNNN\r\n" + 
			"ddmMMMMMmddhhdmmmmmNmmddmmmmmmmmmNNmmmmdhhhyyyyyssyssssssssssssssssosssssyssssssssssssssssssssssssssssssssssssyyyyyyyyyyyyyhyhhhhdddddhdmmmmmNmNmmNmmmmmmmmmN\r\n" + 
			"NNNNNMMNNmmmdmmmmmmNmmmmNmmmNmmmmddddhyyssssssssssssssysssssssssosoosssssssssssssssssssssssssssssssssssssssssssssssssssssyyysyyhhhddmddddmmmNNNNNNNNmmmNNNNNN\r\n" + 
			"mNNmNNNNNmmmmmmdmmmmmmmmmmddddhhhyyssssssssssssssssysssssssssssssssssssssssssssssssosssssssssssssoooooooooooosssssoossooooooooossssssyhyddmmmddddddmmdddmdmdd\r\n" + 
			"dmdmmmmmmmmNNNNNmmmmmdhhhhyyysssssssssssssssssssyysssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssoooooooooossooooooooosyyyyyyyyyyyyyyyhhhhh\r\n" + 
			"hdddddddmmmmmmmddhhyyyyyyyysssssssssssyssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssysssssssssssssssssssyhhddmmddddmmmmmmmmm\r\n" + 
			"dddddddddddhhyyysssssssssssssssssssssssssssssssssssssssssssssssssoooossssssssssssssssssssssssssssssssssssossssssssssssssyyyyyyssssssssssyyyyyyyhdmmmmmmmNmNmm\r\n" + 
			"dddmmdmhyssssoooooooooooooooooosssooooooooooooooo++++++oooooooooooooooooooooooooossssoosssssoooossssssssosossssssssssssssssyyyssssssssssssssssssssyyhyhddmmmm\r\n" + 
			"mhdhyssssssoooooooooooooooooooosssoooooooo+++oooooooooo+ooooooo+++ooooooooo++++++++oooooooooooooooooo+o+oooooooooooooooossssssssssssssssssssssssooosssssssyyh\r\n" + 
			"hhyysssssoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo++++ooooooooooooooooooooooooo++++oooooooooosssoooooooooooooooooo\r\n" + 
			"sssssssssoooossssssssssssssoosssssssssssssssooosooooooooooo+oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo+oooooooooooooooooooooo+++++++++ooo";
}
