/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.BackArea;

import Items.BasicItem;
import Items.Item;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class Backyard extends Room 
{
	private static final long serialVersionUID = 1L;

	public Backyard(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Backyard";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.backyardImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "A size-able yard stretches behind the house, packed dirt with sparse grass. "
				+ "An old red barn sits off to the left, the doors closed shut. It looks to be in as poor a state as the farmhouse. "
				+ "Off to the right is a tool shed, the door also closed. It seems newer than the other buildings here. Odd. "
				+ "Behind those two buildings stretches a corn field, which seems to be dead. "
				+ "You see a back door leading into the house. ";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Edge of Forest
		//=================================================================================
		this.addMovementDirection("southeast", "EdgeOfForest");
		this.addMovementDirection("forest", "EdgeOfForest");
		this.addMovementDirection("edge", "EdgeOfForest");
		this.addMovementDirection("path", "EdgeOfForest");
		
		//=================================================================================
		//Create directions that move to Farmhouse Porch
		//=================================================================================
		this.addMovementDirection("south", "FarmhousePorch");
		this.addMovementDirection("porch", "FarmhousePorch");
		this.addMovementDirection("front", "FarmhousePorch");
		this.addMovementDirection("around", "FarmhousePorch");

		//=================================================================================
		//Create directions that move to the Tool Shed
		//=================================================================================
		this.addMovementDirection("toolshed", "ToolShed");
		this.addMovementDirection("tool shed", "ToolShed");
		this.addMovementDirection("shed", "ToolShed");

		//=================================================================================
		//Create directions that move to the Barn
		//=================================================================================
		this.addMovementDirection("barn", "Barn");

		//=================================================================================
		//Create directions that move to the Cornfield Entrance
		//=================================================================================
		this.addMovementDirection("entrance", "CornfieldEntrance");
		this.addMovementDirection("cornfield", "CornfieldEntrance");
		this.addMovementDirection("corn field", "CornfieldEntrance");
		this.addMovementDirection("field", "CornfieldEntrance");

		//=================================================================================
		//Create directions that move to the Back of the Cornfield
		//=================================================================================
		this.addMovementDirection("back", "BackOfCornfield");
		this.addMovementDirection("rear", "BackOfCornfield");
		this.addMovementDirection("north", "BackOfCornfield");
		this.addMovementDirection("trail", "BackOfCornfield");
		
		//=================================================================================
		//Create directions that move to Kitchen
		//=================================================================================
		this.addMovementDirection("kitchen", "Kitchen");
		this.addMovementDirection("back door", "Kitchen");
		this.addMovementDirection("inside", "Kitchen");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create silver knife
		//=================================================================================
		Item silveredKnife = new BasicItem(this.gameState, "Silvered Knife", "", "A kitchen knife which has had its blade suffused with silver. Youd know this process"
				+ "is done by keeping it strong. "
				+ "It should be effective, as long as you dont mind getting up close. Though why its been suffused with silver is beyond you...");
		silveredKnife.setRegex("silvered|knife");
		this.gameState.addItemSynonyms(silveredKnife, "silvered", "knife");
		this.gameState.addSpace(silveredKnife.getName(), silveredKnife);
	}

	@Override
	protected void createFlags() 
	{
		this.gameState.addFlag("Knife Taken", new Flag(false, "", ""));
		this.gameState.addFlag("shed unlocked", new Flag(false, "", ""));

		//===============================================================
		//If the back door flag hasn't been made yet, make it
		//===============================================================
		if (this.gameState.checkFlag("back door unlocked") == false)
			this.gameState.addFlag("back door unlocked", new Flag(false, "", ""));
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
			//If go inside farmhouse, verify the door is unlocked
			//===============================================================
			if (command.unordered("kitchen|back door|inside"))
			{
				if (this.gameState.checkFlipped("back door unlocked") == true)
				{
					if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
						return this.move(command);
				}
				else
					return new DisplayData("", "You try to go inside, but the door is locked. ");
			}
			
			//===============================================================
			//If go into shed, verify the door is unlocked
			//===============================================================
			if (command.unordered("toolshed|tool shed|shed"))
			{
				if (this.gameState.checkFlipped("shed unlocked") == true)
				{
					if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
						return this.move(command);
				}
				else
					return new DisplayData("", "You try to go into the shed, but the door is locked. ");
			}
			
			//===============================================================
			//If go to back of cornfield
			//===============================================================
			if (command.unordered("back", "cornfield|corn field|field"))
				return this.move(command);
			
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
			return new DisplayData("", "Can't go that direction. ");

		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();
			
		case "open":
		case "unlock":
		case "use":
			//===============================================================
			//Unlock the shed
			//===============================================================
			if (command.unordered("toolshed|tool shed|shed|padlock|master lock"))
			{
				if (this.gameState.checkInventory("Plain Key"))
				{
					this.gameState.flipFlag("shed unlocked");
					return new DisplayData("", "You unlock the padlock on the shed door using the key you got from the dining room. ");
				}
				else
					return new DisplayData("", "It seems you don't have the correct key. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You can't do that. ");

		case "grab":
		case "take":
			//===============================================================
			//Take the Silvered Knife.
			//===============================================================
			if (command.unordered("silvered|knife"))
			{
				if (this.gameState.checkFlipped("Knife Taken") == false)
				{
					this.gameState.addToInventory("Silvered Knife");
					this.gameState.flipFlag("Knife Taken");
					return new DisplayData("", "Silvered Knife taken. ");
				}
				else
					return new DisplayData("", "You've already taken that. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here. ");
			
		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The back of the house looks just as bad as the front, though the door seems to be in better repair at least. "
						+ "An old, cast iron barbeque pit is sitting next to the back door.  It looks like a custom job, made out of an old barrel, "
						+ "with a smoking cabinet attached to the top for making jerky. "
						+ "On the other side of the door is a rusty old butane tank. "
						+ "The forest is an inscrutable wall to the east. From here, you can see a path leading into it from the front of the house. "
						+ "You also notice a trail heading around the side of the cornfield, towards its rear. ");
			
			if (command.unordered("barn"))
				return new DisplayData("", "The old structure is still standing, but you aren't sure how much longer that will last, considering "
						+ "the way it's starting to lean. "
						+ "It's painted a typical barn red, but the paint is faded and peeling now. The door is standing open. ");
			
			if (command.unordered("toolshed|tool shed|shed"))
				return new DisplayData("", "The shed itself looks suprisingly well kept. The paint is not peeling, the wood not rotting, nor the hinges rusted. "
						+ "Attempting to peer through the windows proves to be a useless effort as the glass seems to be tinted. "
						+ "Why in the hell would someone tint the windows of a toolshed? "
						+ "There's a padlock on the door. Master Lock brand, good stuff. ");
			
			if (command.unordered("corn|cornfield|field"))
				return new DisplayData("", "The corn stalks all look to be dead, brown and yellow in color, no green to be seen. "
						+ "How strange that is. What could have caused the entire field to die in such a way, all of the corn stalks whole, intact? ");
			
			if (command.unordered("barbeque|bbq|pit"))
				return new DisplayData("", "The BBQ barrel looks well used, but neglected. No succulent meat has come from here in a while. "
						+ "Upon further examination, you spot what appears to be a silvered knife hanging in the smoke cabinet. ");
			
			if (command.unordered("butane tank|tank"))
				return new DisplayData("", "An old, rusty butane tank. These things are still common out in the sticks. "
						+ "Looking at the gauge, it looks like it still has a half a tank or so. ");
			
			if (command.unordered("back|door"))
				return new DisplayData("", "It's closed.  It looks to be of solid oak, not the flimsy paneling most doors are made from nowadays. ");
			
			if (command.unordered("forest"))
				return new DisplayData("", "The edge of the forest is abrupt, immediately dense. It doesn't look like much light is getting"
						+ "through beneath the canopy.  A path enters the wood a few hundred feet away, cutting between the trees bravely. ");

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
			return new DisplayData("", "You don't see that here. ");

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
			return new DisplayData("", "Can't do that here. ");
		}
	}

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String backyardImage = "`````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"``````````````````..---:://++-```````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"``````..---::/ooosyhyhhyhyoshhs-`````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"/oossshhhhhyyyyhyhyyhhysyhNNMNhhs:```````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"yyyyyyhyhyyyyhyyhyhyoyyNMNNmMMMNyh+``````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"hyyyyyyhyhyyyyyhyoshNMNNmdmdmNMMMmyy/````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"yyyyhhhhhhhhhdy+sNNNmddhhhhhhdmNMMMmhy:``````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"hhddhhhhhhdhdy:NMNNmNmmmmmdmdddmmNMMMmhs:````````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"hhhhhhhhhhhhh/dmdhhhhhmNNNNMMmddddmNMMMdy-```````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"hhhhhhhhhhhd+hdhhhhhhhddddddddhhhhddmNMMyh```````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"hhhhdhhhhhhosdhhhhhhhhhdhdhhddhhhhddddNMmy/``````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"hhhhhhhhddoomddhhhhhhhhhhhhhddhhhhhhdddMMhs.`````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"hhhhhhhhhy+ddddhhhhhhhhhhhhhddhdhhhhdhdmMNys`````````````````````````````````````````````````````````````````````````````````````````````````````````````````\r\n" + 
			"hdddddhhh+mddddhhhhhhhhhhhhhddhhdhhhdhddmMdh-````````````````````````````````````````````````````````````````````````````````./shdhyo/:-`````````````````````\r\n" + 
			"hhhhhddh+ydddddhhhhhhhddhhhhddhhhhhhdddddMMhy.``````````````````````````````````````````````````````````````````````````.-+sdmmmmmmmmmddhy+/:.```````````````\r\n" + 
			"hhhhyyyssmdddhhhhhddhddddhdhdhhdhdhhhhdddmMNys///:::::::::::-----...........````````````.............................-+sdmmmmmmmmmmmmmmmmmmddhs+:.```````````\r\n" + 
			"dddmyyhmmdhddhhhhhhhddhhhhhhddhddhhhhhdhddNMmhssssoyssssoosossossssssssssosoooooooooooooooooooooooooooooo+++++++++++ymmmmmmmmmmmmmmmmmmmmmmmmmmmddhsso+++++/+\r\n" + 
			"MMMMmMNddddddhhhhhhhhdmdddddddhhdhhhhhhdddmMMdyyoyssssssssssyysysysssossyosyssoossssoosssososossoossossooooo+ooooooohmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmhsssssos\r\n" + 
			"MMMMNmdhdmmmmmmmmmmmmmmddddddddddddhdddddhdmmsososyyssyooyyssyssssy+ssosyosysssosysss+ooooyssosyosssssssssoosooosssohmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmyssosyos\r\n" + 
			"NNNNdhhhhhdhdddddNMMMMMMMMMMMMMMMMmmmmmmdhhdmysdyhhohhyossysyhhyoyyyyyyyyhhhyoooyhyososyysys+ssysysooyhsy+ssyoossssshmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddmhyssyyyh\r\n" + 
			"mmmmdhdhdhdddddddmMMMMMMMMMMMMMMMNddddddhhhhmdhdhdyyddyyhmdddhdhdmmhysyhdhmhdhyddhdssdddddsyhdhhhhshdhhyhyddhyssdhhshmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddmdhyyhddd\r\n" + 
			"dmdmdhdhhhddhddddmMMMMMMMMMMMMMMMNddddddhhhhdddmmdmmmdhdddymmdhmmNNmddyhsdmmdymmmmmdymNmmmhhhhdddyymNmNyymmdmhyhhddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdhhydyhm\r\n" + 
			"dmmmhhdhhhddhhdddNMMMMMMMMMMMMMMMNdddddhhhhhmmhmmmmmmmhdmddmmNdmmdmmmmyddmNNmdmNNNNdhNmmNmhhmmmNmdhmmmmhyhdNmhshmmmhdmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmmmmhhhNN\r\n" + 
			"dmdmhhhhhhdddhdddNMMMMMMMMMMMMMMMNddddhhhhhhNdhmNNNNNmymmNNNNNydmNNNNmymmNNNNhdNmNNmymNNNmdsNNMNmmmNNNdyhmNNNdhdmdmddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmNmydhmN\r\n" + 
			"dmdmhhhhhhdhhhdhdNMMMMMMMMMMMMMMMmdddddhhhhhNmyhmNNNMdsmmNNNNdydmNNMNNymNNNMmhmNNMNmyddNNNhyhmMNNdNNNmdymNMMNyhmNNNhhmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmNNNymNNN\r\n" + 
			"dmdmhdhhhhdhhhdddmMMMMMMMMMMMMMMMNdddddhhhhhyssyyyhhhsosyyhyysosooossyssyyyysosssoooosssssosssyssoyyyysossssssssyysohmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddhoyhhh\r\n" + 
			"dmdmhdhhhhhhhhdddmMMMMMMMMMMMMMMMNdddddddddhooosossoooossssooo+++++oo+oooooo+++o++++++++ooo+++++o+++o+++//++/++oooo+yddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmy++++///\r\n" + 
			"dmdmhddhhhhhhhdddmMMMMMMMMMMMMMMMNdddddddddho///+////+++++++///+////+++o+++///+++////////:::::::/::://///:////++++++hddmmmmmmmmmmmmmmmmmmmmmmmmmmmmdhs++/+++/\r\n" + 
			"mmdmhdhhdhhhdddddNMMMMMMMMMMMMMMMNdddddddddhoo/++/+//////:--.:--.--:/:/://:/:///:-:::+:::/::://///+++///://ooo+so++osyyyhyhhddhdddddmmmmmmmmmmmmmdysys+//://+\r\n" + 
			"dmdmdddddhhddddddNMMNNNNNNNNNNNNMNdddddddmds+oo++::+//:-.-::----.-:-:-:::::-:---::::++///://:/+/://:::--:-//oososssoooooossooooo+oosoossysosyyyyyssso+o:///:/\r\n" + 
			"sysysyyhhhddhdhhhmNmmmmmdddddddddhyssooo++//:/:-----:-.::-.----:::-.-::::-:/--:-:/:--/+/:///:::::--..--:---:/++o+++++++o++ossoooo+oo+/+oooo+o+ooyo++/:/:/:://\r\n" + 
			"sysosssossoyssssyyyyssooosssyyyyysysssso++//+.......-::-...-:--:::o/:----::--:--.--.--.:/:/::-::-::-::-..--::/://///+//:o/+++osoooo++//o/+++o+oo++/--::::--//\r\n" + 
			"s+oysssooosyyyyyysssooosoooosssoooo+///++::::.--..------..-.----::---.-....------...:/:--:::....-...-::-:::-/::+oo::++/+//os+osoos++o/+o+o/:+/:/:+/:/:::-::--\r\n" + 
			"yyssooossosyyssss+sooo+++o++++o++///:-:::::----/....:---.-..........::/:--.---`..-:::::-.--.---.-::.-/::::-/:--:soo++://:::oo+/ss//+/:/++::-///-::/:+:/:++--:\r\n" + 
			"+ssssss/+o+oo/++ooos++o///://:-::::-:-::/::--.-:-.:--:...:-.--.--:-.--.-----.::::--.-::...-.:--..:--:/::-:--::--:////:///:::::.-++////o/:/-.:-:--.:/-::-:::::\r\n" + 
			"+/osooo/:/+oo++oo+oso/+++:.:--:::-.::.:-:++:--:/.::-:-/://:::::.-+/-////+:-:-/+...++-/./:/--.:-...:-:::::--./---::--./--:::--::-/+/+--://:-..-::-:---/+/:--:-\r\n" + 
			"ooo+osso///:///:/+/:o//:/:./:ooo:.--.--..:/-:/:+/:::--:/--.::--.:o/+:/:://:-:-/::/+o/:::::--:.--.:-:-:..://:/-:/:--:-+:-:..:/..-/+/:-:/+//-::::-.----::::----\r\n" + 
			"/+++++/:/+//::-:/::::-:--..----::-.--....::::-..:+/-::+--.-:+-/:.++/+--//:-.-::/--+-/-/+/--:/::-.:/--:---/:--//+:/:::-+o+:/+/::---//-::/+//:++/o+/::-+-.:::/-\r\n" + 
			"::::-//:/:+/::::::--:--:/:--.--:/-/:-:-.-o-://::/-.-/------.---://--.:--:...--.--:---.-:+//--/:--:::..-:/+/++++o/+/-//++:/:++:o:-/:/+/::/--.-++:o-/:+//-:::-.\r\n" + 
			":/:/:+//+/:://:--.:---/:/:-..---:-//.::---:-/.:-:.:.-:-:/::--.::::.-./:::-:/--::..-::+:-.::.-..:+:--:///so//-:ooss+o+/::---:///:-/://+o+/::/:-+:++o/++o+-://:\r\n" + 
			":+o:/++//:/:.-:`:.++:/.oo+:.:/:.-+:.//------:--:.+-::/+---:-.--:-:+-...-::--+:+---:----.--:o+/:-:-/++::soo-/:/oys/::o+//:-:/o++:/+s/::o+//:+s+::+/oso+/++::--\r\n" + 
			":ssoo/+-::/.-:..+-:+++++/:-+//:/++-::.-::..--:/-.-...:::/o+//-:::-//--.---+/+////:.:--/+/o/::::/.--://::y++ssy+so++::/://.---:o////:-.//s+os////+//os/:+o:-.-\r\n" + 
			":/++/+://-/o.:.-:-:o+o//--:o++oooss/:/--://+++::./:::/:--/-//:/::/+/--+/.-+-+y+s::-.//-.:--:..-:.--:+++ooso+-+://sssss:/:+:/.///++y+//+:o+:ss+//:o/so/ssoo///\r\n" + 
			":/:://::/:/::+//::-/+soy//o+:/oo:.:/::-:/o./-//:-:/:--::+/-:::/:::o//-::.`::-ohdy::///:/++:-::/-.///+o+/+o//+o//ssoo+:/hyoo+::/:hso:o/++/o/:-/oso/odyosyo//::\r\n" + 
			":-+++o/oss/+/+..:+//+y+ysooooss+-.:++:--:+:/--/--`.--/:-::.-/+/+:yy/:/-.-:.-:+oyss://+o/:/:///o/:/++//:.-o//+o:ohs//:+oo:/oo/::::/os+--:/://:/o+o:ossoso+++++\r\n" + 
			"-+oo+sod+o+ooo/-:/+::/yhsoysyoo///:/:::/:///-+:-+--:/.+++:./--://:::-:/:///.:://+/:/::.--.--::-..:/:::--.:+yds+/+//ooy+h+++-::/:::+:os:+/s+:s//yys+///:/+oo/:\r\n" + 
			"--::/-/+os/::-./-::::y+o+oss/so/s/:+/::/:/--/:-:-:/-//++/+/+-:/-:+-::.-+:+/s+./+/-/-./.:/:o:/+//---o+/::.:-///-s+o/::::s//+/o++.-:/+/:-::o/:oss+yys+so++:o+:/";
}
