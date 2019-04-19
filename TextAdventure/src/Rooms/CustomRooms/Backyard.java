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
				+ "An old red barn sits off to the left, the door standing wide open. It looks to be in as poor a state as the farmhouse. "
				+ "Off to the right is a tool shed, the door closed. It seems newer than the other buildings here. Odd. "
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
		this.addMovementDirection("southeast", "Edge of Forest");
		this.addMovementDirection("forest", "Edge of Forest");
		this.addMovementDirection("edge", "Edge of Forest");
		this.addMovementDirection("path", "Edge of Forest");
		
		if (this.gameState.checkSpace("Edge of Forest") == false)
			new EdgeOfForest(this.gameState);	
		
		//=================================================================================
		//Create directions that move to Farmhouse Porch
		//=================================================================================
		this.addMovementDirection("south", "Farmhouse Porch");
		this.addMovementDirection("porch", "Farmhouse Porch");
		this.addMovementDirection("front", "Farmhouse Porch");
		this.addMovementDirection("around", "Farmhouse Porch");
		
		if (this.gameState.checkSpace("Farmhouse Porch") == false)
			new FarmhousePorch(this.gameState);	
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

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "The back of the house looks just as bad as the front, though the door seems to be in better repair at least. "
						+ "An old, cast iron barbeque pit is sitting next to the back door.  It looks like a custom job, made out of an old barrel, "
						+ "with a smoking cabinet attached to the top for making jerky. "
						+ "The forest is an inscrutable wall to the east. From here, you can see a path leading into it from the front of the house. ");
			
			if (command.getSubject().contentEquals("barn"))
				return new DisplayData("", "");
			
			if (command.getSubject().contentEquals("tool") || command.getSubject().contentEquals("shed"))
				return new DisplayData("", "");
			
			if (command.getSubject().contentEquals("corn") || command.getSubject().contentEquals("cornfield") || command.getSubject().contentEquals("field"))
				return new DisplayData("", "The corn stalks all look to be dead, brown and yellow in color, no green to be seen. "
						+ "How strange that is. What could have caused the entire field to die in such a way, all of the corn stalks whole, intact? ");
			
			if (command.getSubject().contentEquals("barbeque") || command.getSubject().contentEquals("bbq") || command.getSubject().contentEquals("pit"))
				return new DisplayData("", "");
			
			if (command.getSubject().contentEquals("door") || command.getSubject().contentEquals("back"))
				return new DisplayData("", "It's closed.  It looks to be of solid oak, not the flimsy paneling most doors are made from nowadays.");
			
			if (command.getSubject().contentEquals("forest"))
				return new DisplayData("", "The edge of the forest is abrupt, immediately dense. It doesn't look like much light is getting"
						+ "through beneath the canopy.  A path enters the wood a few hundred feet away, cutting between the trees bravely. ");

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
