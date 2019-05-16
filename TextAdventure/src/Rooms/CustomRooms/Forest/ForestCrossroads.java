/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.Forest;

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
		this.name = "ForestCrossroads";
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
		this.addMovementDirection("west", "ForestCliff");
		this.addMovementDirection("cliff", "ForestCliff");
		
		//=================================================================================
		//Create directions that move to Cave Entrance
		//=================================================================================
		this.addMovementDirection("north", "CaveEntrance");
		this.addMovementDirection("east", "CaveEntrance");
		this.addMovementDirection("south", "CaveEntrance");
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
			if (command.unordered("back"))
				return this.displayOnEntry();

			//===============================================================
			//If move, and maze not completed, handle the maze
			//===============================================================
			if (command.unordered(this.movementRegex) && 
				this.gameState.checkFlipped("maze completed") == false)
			{
				//===============================================================
				//Handle movement choice, then flip the maze completed flag, if
				//the sequence has been completed.  Count being equal to eight
				//indicates the sequence has been completed.
				//===============================================================
				this.handleMaze(command.getMatch(this.movementRegex));
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
				if (command.unordered("west") && count == 0)
				{
					this.reentry = false;
					return this.move(command);
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
			if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
			{
				this.reentry = false;
				return this.move(command);
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
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The forest is dense, the light is dim. You peer carefully down each path, but are unable to "
						+ "discern anything of note. The crossroads itself is non-descript. "
						+ "But wait, is that an overgrown sign among the brush there? ");

			if (command.unordered("sign"))
				return new DisplayData(this.signImage, "You push aside the brush and look at the rotting sign. "
						+ "It says, \"BA BA Start - Konami\". What does that mean? ");

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
	private final String forestCrossroadsImage = "NMMMMh/so/s+sy+sMyshdsmMMm+:o+ohMMMN+.//+MMN/o-sMMMMs/sNy/+omNhyysosmNMMMMMdo++++ossNydhoymMMMMMMMh+hdM+shdMdmMMMMMMMMmdhoMNyhosso-oMMNs: :NMMN++sdddds+mMMMN\r\n" + 
			"MMMMMNo//+/:ooymMhssdymMMh++oyyhMMMMo:::oMMMss:NMMMM+hyMdooydmyshhyydNMMMMMssso+sssymssyyhdMMMMMMMds/MMssdodNdMMMMMMMNmmhMMoo+:/:+osMMhso+/dMMMhshhddhhydMMMN\r\n" + 
			"dMMMMNohoso:-:/MNyo/-osMMmoshs:hMMMMy+++sMMMdohMMMMN-/smd++oyd/o+sssdMMMMMMdyysyhhhhmdyssmdNMMMMMMNhhNMmhs/sMhNMMMMMdNo+sNd/ss/yy/:yMMNoohdyNMMNmmdmmddNNMMMM\r\n" + 
			"mMMMMMsy+o+/.+Nh:--/+oyMMNoysy+dMMMMh+o++MMMdoyMMMMN+/omdo+sym+oyhhhmMMMMMMsysossoyhNNmmdmddMMMMMMNNdsMyo+oyNhNMMMMmsmNydMhyhmmyddyyMMNsh/syodMMMdsyhmmMmMMMM\r\n" + 
			"yNMMMMho+o/hhMd:--/sd+sMMMhhhyhdMMMMss/ohMMMmhdMMMMho/sddo+somsshhhdNMMMMMMdy:ssddddNMNmmhhmMMMMMMmNhyNd+//oyNmMMMMmdmmhmM:/+o/osoo+mMM/+./shmNMMMsoyydhyMMMM\r\n" + 
			"ymMMMMsssshMNo/-:+yhmdyNMMshyyydMMMMsys+sMMMNhsMMMMs+ssyyssosmsodydNmNMMMMMdyo/smmdymhyosdmMMMMMMMmdsyNMddshhNMMMMMdoNmsmN:++-`://++NMN++oNyhmNMMMMys+sosMMMM\r\n" + 
			"NMMMMMooyhMy/.ooyyydyhoNMMdhhyhmMMMMhdhsyNMMNsdMMMMhoyhdhsh/ohhydmymmNMMMMMdhyhmyyddNhhhhhdNMMMMMMdyyshmhhdmhNMMMMMdoMNdMy:++/::++odMMdyyyhhsmmNMMMMm/+yyMMMM\r\n" + 
			"hNMMMMysMms/::/dhyys+hshMMNys/yNMMMMdooohNMMMmhMMMNss+sm+/:+/oddhysmNmMMMMMddh+/o:+dhmmdddddMMMMMMhymddNmmNNhNMMMMms+MmNMo+oo+/+sssNMMy+/+ooyyhyymMMMNdyyMMMM\r\n" + 
			"ydMMMMmMMy+o:ohshhsysyyodMMNddsyMMMMmoyddNMMMmyMMMNhdmmms++/-y+ydsyNNhMMMMMmhso/+syyyhdhddmNMMMMMMhdhdmNNmydhMMMMMmhyNhMN/+/+ysoo+sMMd/o:-o/soydhyhNMMMMNMMMM\r\n" + 
			"+hMMMMMMdhs+oo+sy+y+-/sohNMMhymNMMMMNsyssdMMMMmNMMNsdyNMdso-.hyoddhNmhNMMMMMmdhddhdhyyyhmdmmMMMMMMyosysNN+yydMMMMMhydNmMy//:-+o+yymMMy+/o/+yhddsyyssmMMMMMMMM\r\n" + 
			"dmMMMMMms///+::+os+o+ooymmMMNmmmMMMMMmhmdmMMMMmNMMMhdsyMms/:-ddhNmdmmdmMMMMMdmmmmhmsddsoy+syMMMMMMhosohNMoyhdMMMMMNmdmmM+-+:o+oydNMMm+o:shhhooo//ososdMMMMMMN\r\n" + 
			"ydMMMMMhhyyoy+//o//++hhhNmMMMdhmMMMMMddmNMMMMMMMMMMNmddMNyo:sNddmdhhmmmMMMMMNdyhhdyyshysydhhMMMMMMhso+yMMhdmMMMMMMmmdmMM+/oo-.-shMMN++++oo+//+shhyyhdhMMMMMMN\r\n" + 
			"ydMMMMNsss++oo+:++yddsdddmhMMNdyMMMMMmmNMMMMMMMMMMMNmhmMNdd-ommhdNNmmNNMMMMMMNdyhyhshdyyhyydMMMMMMmdmmdNMmmmMMMMMMNmhdMMoo/-.-odNMMdo:-::sooyo++o+sosyyMMMMMM\r\n" + 
			"MyNMMMmysyhsy/s/hdhmhyyhdmyNMMmmMMMMMmNmmNMMMMMMMMMNoyhmhsh.+hhhNmNNNNmMMMMMMNdddysyyhyddmmmMMMMMMmyhyyhmdhmMMMMMMNdmNMNhhs::/oyMMMyoo++++osssos+s+so//dMMMMM\r\n" + 
			"MMMMMMmoso/+++ososhoohhmmdhmMMNdMMMMMNmddNMMNMMMMMMNyydNNym`+dNNdNdNNmNMMMMMMMmdddddyyyysyhNMMMMMMNysosydhhmMMMMMMmdmNMmhsssoo+dMMNhs+++ysyNmdNdhhyoooymMMMMM\r\n" + 
			"dNMMMMhoyyoosho//o:oyNmdohohNMMNMMMMMsyyhmMMMMMMMMMMdhmMMNd.+hNNNNmmmmdMMMMMMMdsNhNymyhdmddNMMMMMMNdNhyymdyNMMMMMMmNNmNmyyyshssmMMMmhyyso+odhhmdyhhy++shMMMMM\r\n" + 
			"hhMMMMNydyymhdo+o+ssyyyysyyymMMMMMMMMyyhhmMMMMMMMMMMmNddNmh`:mNmdmmMMmyNMMMMMMMhNmNmNNNNNNmMMMMMMMMdhdsyMNyMMMMMMMNMNMNmdyssyyhNMMNhddhysyhyyhyyssshysssMMMMM\r\n" + 
			"mhNMMMMdydhmmhyss/oso+sysdhhdMMMMMMMMMNMMMMMMMMMMMMMdhhhMMy`:dNNNNNNNNNmMMMMMMMmNNmmNmNNNNNMMMMMMMMddNdhMMsMMMMMMMNMMMMmhysyyhdMMMNmhhdyhhssmyNdhshmhssoMMMMM\r\n" + 
			"NMMMMMNhhhmmNysso+ysd-+yomyhyMMMMMMMMmhdmMMMMMMMMMMMMMhdMMy.:dMmNmNmNNMNMMMMMMMMMmNNmmNMMNNMMMMMMMMdhmmmMMhMMMMMMMNNMMNhh+hssddMMMhsdyhdmmmhyhNmMsmmmhdmMMMMM\r\n" + 
			"NMMMMMNyyydyNshhyss+s/ssohshsNMMMMMMMMdNMMMMMMMMMMMMMMsyMMs`+hMMddNMNNMMMMMMMMMMddmmdddMNNMMMMMMMMNmdhhhNMMMMMMMMMMMMMMMNyyhyhdMMMNNdyyyhmmmNNMNmyNNmmmdMMMMM\r\n" + 
			"hhMMMMNy+yhhMdmmhdh++++yoymhohMMMMMMMMmmNMMMMMMMMMMMMMssMMo`:dMMNNmNNMMMMMMMMMMMNmmNNdhdddmMMMMMMMMmhhmmdNMMMMMMMMMMMMMMNNMNMMMMMMMmNNmddddmhsNNd+mmyyhhMMMMM\r\n" + 
			"mNMMMMNooyhhh+++/:-:::/+/+++ooNMMMMMMMhoyNMMMMMMMMMMMMosNm/`-yMMMNMmNMMMMMMMMMMMMMNNmdmdNmMMMMMMMMMddmmhhNMMMMMMMMMMMMMMMMMNNNMMNNsoyss++o:-:-.-:..````hMMMMM\r\n" + 
			"mMMMMMMo+++....```-//o/+yddhdsmMMMMMMMm+sNMMMMMMMMMMMhoyNN: `yNNdMMdhdMMmMMMMMMMMNyyhmmmddNMMMMMMMMyyhdmdmMMMMMMMNMMNdmmdmmdmddddmhyo+:+++oooo+/:/++/++MMMmMM\r\n" + 
			"NMMMMMMo.`   -:`  `:-:/+++//s++mNMMMMMMsdMMMNNMMMMMMMsohd```-oymhmMhNdMMmMMMMMMMMdyyydhdhdMMMMMMMMMyyssoomMMMMMMMNyyyyyosoosoossoosshhyss+:-::-::-/ymsymNmNNN\r\n" + 
			"+dMMMMMN:`   .--``  -:/++//+/oo+oyyMNmmddNMMNNMMMMMMMNyhmNh`-oMMNMMhNMNmNMMMMMMMMmsoydNhdyMMMMMMMMMsossyhNMMMMMNmyossoosysoososyyo++:.```...````-ooossyssssyy\r\n" + 
			"dhmMMMMMs+/-. `-:/:.```:++//:+soyssssssshddmmmmNNNNNNMhhmMd-/oNMNNmddmmNNMMMMMMMMNmddmmmmmMMMMMMMMMNmdmdddhhhmddhdmhyhyyhyhhsso/-`.``..---.`..-+yo+o+ooo+osys\r\n" + 
			"shhhhyso://+hys+/++++/////:+syss++/////+/////+osssoosossdho++shydNmhdmdhNMNNMMMMdhhhysssoyMMMMMMMMMd+++++//+++++oo+++syyyys+--..`.:/+o///+/:/yydysysooosyhssy\r\n" + 
			"++/++:--:/o//sh/.---:++osoo+oooshyyy+::-:::/ossosssssssyhss/shhysyssssosssyhyhhddhhyyyhdyhdNMMMNmmdyyysysossohhhddhhyso+--.`-.--+//++o//:::-shysoosysso+osyhh\r\n" + 
			":/-----.:::--+o/``` `//+sys+o/+-.`-:++++-::/://+:+::/+oo+o+soss+oosysysssysssssyysyhyyhsyyhhhddssssosyyhdhhhyhyyyyyoo//.---/://+o+++/+////syyhyosshyyss+++ooy\r\n" + 
			"s+/++//:-...../-```  `.o+so/+/::--`...--++++o++/:-:--:-:/-::///+/+:o://+/++oo+/ooooy+os+ssssso+oossyyhhhyhhhos+++::-:-.-.--.:----://///://yyyhyssssyyhhyhyyhm\r\n" + 
			"///://:::.``.-o/.`    .-.-..--...`.`  ````..:++/+//+//-:+-:/:-:--://-:::::/++:::////+/+///+::/+oo+ysysysso++o:--.--.`.`.-.`.//::.-.:...:/syhysoosooysyydhhhyy\r\n" + 
			"+o:::-`...`---:/` `  ..`--.-----.-`.```   ` `-//s+/o/+////+////////+//:+-://-+/:://+//::/+/o+oosyossoso::..-.``` .``..-::-/--/--++.`-/+/+ossso+oosssshshhhyhs\r\n" + 
			"+++//::-----/oo/-```` ````-::`:-.-...``` ``  ```-:-++++o///+ooo+++oosssososysyssssssshyysosssohysh++++:-``.:..--.-:/::+++--:-o/-:::`./oooooo+o+ooossssyyoo+oo\r\n" + 
			"o+::::/-:++so++-..      `` ..``` ```..`  ..   `  `.://oo+/+++///++oooosoo++oo+oooysossos+sssssys+:/--```..--:.--:-:/+/++oo:-.:-/+::/o+oshhshyhssyyhyyhsssssys\r\n" + 
			"dyooo+++//:/++/...```  ` ````.:/..-:-:..``. .`-`  ``.:://++++++o++osos++osoosssssdsss+ysooo://---.....```. `-//:/+//o+o///:-o/--s/sso+ssyodhyyyyshdddyyoyhyoy\r\n" + 
			"yyysosoo+///++---```` .-`.`+oos/++oo++/--.`.   `.`  `.--+osoyyosssoooyysooso+oysoosyoys+++//-.. . ` ` `` .`--::o+os//+/--+-/--.-:/+ssoyooysssysshhhhyyhssssso\r\n" + 
			"ysssooooo+ooo/+-``` -.``..`:/oyhhyyhsss/:::.` .`` `.-.--::::+/+o+sohs+os+oyssssy///y+++//o-..-`` `.-```:/o::+o/oo/++o::://.-..::+/+ss+ohoshyyyysyyhshyhshhydh\r\n" + 
			"yssoyshyoohy+:`.-.``    `-..-::/+/+++//-/:--..:/-:-``.-+//--/ooohss+/ooyso/+ooyoy++-.--::`````.:````.-.:-/+oosyyssos/-.-:///:-:-+++++/syso+ssssyoymdhhmyhddyh\r\n" + 
			"oyhsoyshsy+o--/-`.```  `:-./-.-/+++oy/+/+/+o/:-:---:.:-.:/+//////oydyyyhhyo++++oo::+/.::..-::--....-.-:::+sooyyyso+/---:/.:..o//+h+-os+yyyooshhyhhdyhyhyydhyy\r\n" + 
			"yyyyysoyho+/s++:-.:.:`./:`.`.-:/ysyyhyysyyyoo/:-..//-`-./-:://oossssyhddyooso++:.-.-::--...-.::./:..`/++soyhyo+/://+-:-.-`.-.-/::+:ossoosyssoossyymmmddysyhhd\r\n" + 
			"ddsyshyhh/+/:-::..`.: -:.`-``-+o-:+++:/+o/:/o+----.....---...+:+s//oys++:+/o//--..-:--/-.--:-``.---:+++hos+soo+:::-....//+--o:/++/+/o+yhNhshyyysydNmmmdoyshhh\r\n" + 
			"/yosss///:+/:/:::...``..:`..:::--:-.:-./+--.-/---..--:-os:.-:oo:--::/+:++.::`..:::://-/-.-.-.-..-:/.::-:oo//o/..--/-:-/:-:---+ss+sshso+y+ddhhmhyosdyhddmyysmm\r\n" + 
			"yyyyyyyo+:o+o+sy+-...--:---.:/.-+s+-++-/:--:-:-:..--:/:::+/.-`.+:-/-::.`::-:.-/:```.-:--....-:-:-..--..`-:.--:---.--/-//-:.-://+/ydysysyys+yshhshyhmmmNmmhNNN\r\n" + 
			"shsoy/oo//+hh++/:++:-:/o+::++-+/+oy/.--:/so:+--...:-:-:-../:.::/--/+:///++:.`.///s-:```.:.:.`````...:/-::-+o/:-::::://:-///-:+++ohsh+yy+ymmmhymmyhydNNNmmNNMM\r\n" + 
			"hh+syy+hs+o//:/:+/://-.o///-/:-//:omo:-:::-oo-.-.::/:/-:-///+:/..`-:::-:.:---`.````..:---`--.`.-//:/+:+o:/:++/-/:++//+//::///o+s//o+yyhyohyydyhmdyddddNmNNNNN\r\n" + 
			"";
	
	private final String signImage = "dmhyddhyyhhhdhhhhhhyhoyyhhyhddsyNhddhmhhhyyysssddmdydmmmmmmdmmmmNmNmmdhdhdhhddddddmmydNmNmmhyddddddmmdmmNNNNNNNNdmNNNmmNNmNNNMNNMMNNMNNdNNNNmNNmNMNMNNMMNNMNN\r\n" + 
			"mddhohyyhmdhhdhhhhhhhdmhyymdho+ydhhhymhyhhyyyyhhdddhddmdddmmmmdhmmmmmNmmdhmhddmdmmhmmhNmNNydmmNmhmNNdmmmNNMNmNmMmmmNNNNMNNMNNMMMMMMMNMNdmNNNNMNmMMNNMNMMMMNNM\r\n" + 
			"hyyhyhyyhdhdyyhhsyhyhyhddmhhhdhhsssssdosydsssydhyyhhdddddyhddmmddmmmmdmmdddmhmhddddddmNmmmdmdmmNmmmddmmNNNNmmmNNMNmmNNNMMNMMMNMMMMMMMMMNmmmNNMMMNMMMNMMMMMMMM\r\n" + 
			"dsoyyoysyydhssyhhhhdmdmmdyyyyshsss+smyysdyshmhsyyhhddhddhhddddmmddhdhhhhsyyyhdhmmmNmdddmmmmmmmdmmmmmNNNMMNNNMNmNMMNMMMMMMMMMMMMMMMMMMMMMNMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"mhdhhddmmhyhdhdmmdddhydmdsosyhyssy+dh+ohsyhhhddyyyyyddyhddddddydmddmmhhhhyhdhhdmdmmmh/.oddmddmmmmmmmmNNNNMMMMMMNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"dhyyhhhysyyhhddhdhshyhhdhyyysshyhymhyhhyhhhyyyhhyyyhddddhhddhdmdmmmdddmdddmmdddhoosh/.``/mmddhddmmddmmmNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"dyhhhhhhdmhhdmhhymmddddhyyyhyhyyhmhhyhhhhhhhhhhyyyhhddmdddhhmdNNmmmhdddddddddddm/:--..:ymmNd++oyhyhhms/+NMMMMMMMNNMMMNMMMMMMMMMMMMMMNNMNMMMMMMMMMMMMMMMMMMMNM\r\n" + 
			"dyhhyhdhhyyhmhohmdyhddmmdhhddyyhddyhhhhyhhyhhyhhhhhdmdhdddmhhhmNmNmdmmddddhmhysso/-``/dmNmmdy+//++oodo/+ooNMNNMMNMNNNNNmNMMMMMMMMMMMNMMMNNNNNmNMMMMMMMMMMMNMM\r\n" + 
			"dhhhydmhhdhdhdshNyhhmmNmdhmmmmmhhdhhhhdhhdddhhhhhdhhhdmmhmmmyhmhNNNhdddyossosmmdmmo:-ddddddyho++osy/+/:--:hMMNNNMNNMNNNMMMMNMMMMMMMMMMMNNMNNMMMMMMNMMMMMMMMMM\r\n" + 
			"ddddmmmmyhhhmmmdmdNdyhyyhyyydmhdhhhyyhhhdddhhhhhdhhhhddmNmmmyshydmhyd+sydmsysssyy//--.-..:.-/-ooooss+o+/sNMMMNNNNNNMNMMMMMMMMMMMMMMNNMMNNMNNMMMMMMMMMMMMMMNNM\r\n" + 
			"mmdmmmdmyhmmmNdhyddo+ohyyhdymhshyyysyyhmdddhhddhmddmmmmmNNNmdsyyyy-yddmNds+sdmmms`-`.`` `-`.-:o/+oso+dNMMMMmmdmNNmdmmMMMMMMMMdMNNNNNNMNNmmmNMMMMMMMNNNNNNNNNM\r\n" + 
			"mhhdmmhhhddddmdh+/.:oyoosoymhsshyhyyyyhdddhhddmmmmdmmNmmmmddyyy+..:hmNdsydmNNmNNy`.``.` `..-:///+oo+/dNNNNMMMNNMMNMMhNNNNNmMNNMdmmMMNMMMNMdmNNNNNNNmNNdmMMMMM\r\n" + 
			"dhhdhsddyhddyysshy/osoohyydsssshyysyyhhyhhhmddmddmddmmmdhddyddhhdsy+yyydhdmmNNNNo .``.` ..`--://os:::smNNMMNMMMMmNmmhmNdNNhyyhhdmmmmdddddddhdhddhyyhhdmdMNmmM\r\n" + 
			"dhyysdhyydhsss+++ooyhshyyhsshhyhysyhdddyhmmhmdhdhmNNddmdyyhmhdhhhhosyddhddmNddmN-`.``.. `.`.::+o//oodddmNmNNdmMMNmNNdhdhsysoooo+o++//:::::--..--/odNMMMMMMNMN\r\n" + 
			"mdyyhhsyyyyyohshyyyhhyddmhyyyyyyyhhhhhdhmdddddhdmmNmddddhhyyo:odmhsdmddmdhMdmmhm-..```...-`.::/::o+:dys+:-/+///::-----.-------------------...--.-:/+hmNMNNMNN\r\n" + 
			"ddhhsyyh+hsddhmydyhyhhhyddhhss+hhdmmdNNdmmmmmdhhhydhhddyyhho/.-dydmdmNNmmdmmhdhd- ````......-.://::-:`.-/:------------------.------:-.-----:-------::/sdNMNNM\r\n" + 
			"dddmhhhssdyhshddhddhdmhhydyyyyshhyhddmddmNNdhhymdhhhyhsyyyooo+/+oo++///:-----.-:/.-`......`....-..--ooss+:------.--::----:::----:--::----::-..----:/:::/odNMM\r\n" + 
			"ddmdhmhysssohhssdddyhmhddmdhhhhddmdys++ooo+:://////://-::::----------.....-...-//```````.``....`..--/:/:-..-----------:::/+://-oo--o+-ss:s-.----:--/-:-:::dMm\r\n" + 
			"mNmhhhhyssys++hddhhhmhmmdNmddhyhyo:--::/----:--::--....-----...---------...-::::- ``.`-.``.``` ..`--+//-------::hohoohohsymyys/ddo-NdohN/N:::------...---hMMM\r\n" + 
			"dhsydyyhhy//+syymyydhddmmdysshh+----:-------:::-:----.------:-.-------------:::--`......`.````....::+/./--:.....ydd+hy:sdohomhhyom/m+m/h/d:-.------::-:+mMNNN\r\n" + 
			"hosydyyyydso+so+yosyyhhyhhyyy/.:-:::/:--..--::----:+:----::os+oyyo/hy./hoh+yNo:/. .:/-`...`.....---:/::/-..-...-++-o:+o/-::--:------------:-----:----:hNNNNmm\r\n" + 
			"ysysosyhssoo+oo+ssosyyyymhhs////syyo--:hh---dydo::sdm::-::/ds+-oh.yhmo/Nsm-:N.++-`--::-..`.......--:-:::------------------:--------::::::/::::::---/hNMMNNNmm\r\n" + 
			"hsshsysssyyssossyyyhyyyhhy/....-ydyh--sdmy-:dhhh::mshh:::-:syh-/h-y::y:s-o/:o///.`-:+/-..`....----:/:/---...--------..------:::::::-:/:::-/:////:odMmNMMMNNNN\r\n" + 
			"hysssoooysyyssoyhysyhyyho//::-::ohsy-.y::y-:+so:--/::/----:-::::---:-----..:////.`-.-:---..`..-----/+-.-.......-:-:-:::---:::/+ooossooooossossosmMMNmmNdmdmdm\r\n" + 
			"dddddhhyyddhhyhhhhyyyhhhssss+:::/:::---:-:-::::-:-::::::-::--.--...--.-..-/+oo/+`::---+:--...`-.-..-:--/oo+osssysosyyyhhdmmmmmmhmdddmmdhhdmmmmdmNmmmmdddddmmd\r\n" + 
			"ddmdyysyyysysssyhysyysysshhhhs+:::/:::::::------..--.::--------.---.------::...:`..//+::---..`......sdodyyyysoohNmmNNhdNNdhmmmmddmmmddmmmmNmyNmmhdmmmdddmmmdm\r\n" + 
			"hohyssosssyhyyyshhdhyddhdhhssdyyo+:----..---------------------::////++++o+/+sh+y``::/:/:o/:-....`.`.yddyyyhyyyyymNmNddhmmNNNdNmmmdmmmmmNmNmdhdmNNNmmNmmdmmmmm\r\n" + 
			"yyyhhyshhyydyyyyhmmmmmyhhhyysyyso+o+:--::///++++ooosyyysssyydmmhhdmmmddmmdNd+/+s``..--/o:://-...`..`ymmmmdhyyddNmddNNNhdNmmdmNNNmNNmNNmmhNNmdhNmNdmNNmNmNNmNN\r\n" + 
			"dhssyysssshyhhdNNNNddNmmdhssssssssddhddddddhhydmmmNNNdmhhhmmNNhmmNmmmmNmmNNo+:y/:o+-.-ss+++/---.``.`hNNmddNNhhyydmmNNmdymmmNNmNNNNNmNmmNmNNNmydmmNNmNNNNNmNNm\r\n" + 
			"mhsosyysyyhdNNNNNmmmmNmdyssdyodddNNmmNmmNmdmdhdmmmNmmmdmmdmMNNNNNNmmmmdhhdhys+d:-...--+------..`````ommdmNhhhhyhdmdyoso/:omdNNNNNmmNMMNmdhoso+:/mmmNNNNmmNNMM\r\n" + 
			"dysyhssssyddhmNNmNdNNmddmhmmmmdmNmNNNmNNNmNmNdddmNNddmNNmNNmNmmmmddmdhdmdhyy:-o-:+so+so:---..`.```..:hdNMNhyyhhdhyo++//:/sydNNNNmNMMMNmys++//::oyhmNNNNNNMMNN\r\n" + 
			"hyhmdydhhyhhmhmddmmmNmmmNmmmmmmNNmmNmhmNNmmdddmmmmmdmdmmmmNNmmdNyhdddNNmdhd/:+d:`:///:o+:///:--.``--.dNmNsyyyyyds++//::::/+oydmNNNNMNMm++//:::://oshdmNNNMNNM\r\n" + 
			"NhhddmhohhhhsddhmdmhhhdmmNmmmddmdmNmdNNmmmmmmmmmNNmmNmmdddmNNmmmhddddhmhhddo+hy-`....-.++o+/..--``..-dNddNdhhhyso++//:::::://+ydNmNmmMd/++//:::::///sdNmNNmNM\r\n" + 
			"NNNmmyyydddmhhsymmhmdddNNNNmdddmdmmdmNmdmmmmNNNNNNNNMmNNmdmmmNmhhmddhhhmdddmmdy:``......:::-/o+:-`..-hhmmmmmmhdhyooo+/:::/ossmmNmNNNmmdsoo+/:::/+ssdmmNNmNNmm\r\n" + 
			"MNhmddddddhdmyyyddddhdNmmmmmdddmmNNNmNmddmNhhNNNNmdmmNmmdmdmdmdyymdddhhdddmm/:y-``....-.-/y++++::.../dmdmmmddNNNmyoso/:::hmNdhmNNmmNddmysos+:::omNmhdNNmmmmdm\r\n" + 
			"NNmymmdhhhhdhdmdmdhNmmmmmNmmNmNmmmmdNNmmmNNysNhyhmdmmdNNhhhhhdNddmdmNNNmNNmhyds```.-.://-:o::///-.../hhNNmdmNNNmmhsss+-oydddmddNmNmNNNNdysso:/shdddNhNmNmmNNN\r\n" + 
			"NNhmmmhhddyddmmddmdNmNmNmNMMNNNNNNmNMmdhyyyddyymmdydmmNNdmmdhmNdhdddNNmmmNNdho:-``..-::::os//so/://-:odNddddmmmdmddhs+:sddhmmdhdmmmdNNmmdhs+:ohdddmddhmmmdNNN\r\n" + 
			"NhdmdhdhyddhhmmmdNmdNNmMNmNMNNNNNNNNNmdhhhhddhsoshdmmdyoyhhhddddhhddmmmNNmmd:/+-``.--/+oo+sy/+:::ooo///+dmmmmmdddmddhs/ohdmdmhmymNMNNNmmdmhy//hhmdmhmhdNNNNNN\r\n" + 
			"NmmmdmmyhddddmNNNNNNNdmNmmNmmmNNNNNmNhhyyyhhysyhyhhhooyhysyydhhhdyyydymNNdy+/hy-`....--::/+/o+oshhyso---+hMddNmmdddNMNhsmNNmddNmhNNNNNmddmNNdodNNmmhNNyNNNNNN\r\n" + 
			"NmmmshyydmddmNmmNNNNmNmNNmNmmNMMMMNNNmNhddhdhyysssyysooyyyyyyhyhdmddNNmddm+:--+..--:-.-/:+//+so/++++//::/+NdmddmdmdmmNdmMMNmhNNNydmNmddmdmmNNhMMMmdmNNdymNmdd\r\n" + 
			"yysyyhhydmNmmmNdmmNmydNNNmNNNNNNNNNNmNmmmmmdmyooyymmyhhyddmmmhhdmdmdhdoydmd--:/``-+/./hy/oo++oo/--.:/mddddNmmmdmmddmNNdNMMNddmNmdhmdddhddmNNdNNMMmdmNmmymmddh\r\n" + 
			"myyhmmhyshmddmdmNNNmmmNNmNNmhdNmmmNhmdddhdddyhhddysssyyhhmmdddddmmmm::+om+/+dd:-`.:.:yss++++///+//://dmddmMNNmNmmNNNMhNNmNNmmmNNNymddddmNNMmhNmNNdmmNNNddmddd\r\n" + 
			"dydddhmdhdddddhymmmmmdddNNNhNNmdmdhhhhyhhdmhysoshhhhhdhmy+hhdhyddmhmo::shh:dd..:..-.:/oso/+/++oyso:/:dNNNNMMMMMMMMMMMhMMMNmMMMMNNhddddmMMMMdNMMMmNMMMMNmhdddd\r\n" + 
			"mdmmddmdddmmdmmdhmmmmmmNmmNmmmddhhhyhhyddysosyohNdNmdhdmdhyyyysyymNh.s-o+d:Nmo+/..--:/+ooosooo++ysssodNNmmMMMmmNNdddhdNNmdddmmmmdyNmddmmdddymNmddmmmmNmymNddh\r\n" + 
			"dhhdmmmNdhddydyhhdddmdyhmmNdmmdhhhhyhhhyssyyhyoyyymmmdddddhhyhmNmNd/-o:ohmmmdoy..--:::+ooossso++ssosyddhdhmNhhdmmNmhydhmyddmddhddmhdmhhmmhydhmhdddddhddddhmdy";
}
