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

public class GuestRoom extends Room 
{

	private static final long serialVersionUID = 1L;

	public GuestRoom(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "GuestRoom";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.guestroomImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "You open the right door, and step into the guest room. With the light from a large "
				+ "window on the far wall, you can see a bare mattress on a bedframe. To it's side, there is a "
				+ "closet; it's sliding doors have been removed.";

		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Upstairs Hallway
		//=================================================================================
		this.addMovementDirection("hallway", "UpstairsHallway");
		this.addMovementDirection("hall", "UpstairsHallway");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create flextape
		//=================================================================================
		Item tape = new BasicItem(this.gameState, "FlexTape", "", "Flex tape. Can even repair boats, when they are cut in half. ");
		this.gameState.addItemSynonyms(tape, "flextape", "tape");
		this.gameState.addSpace(tape.getName(), tape);
	}

	@Override
	public void createFlags() 
	{
		this.gameState.addFlag("key snatched", new Flag(false, "", "Key snatched."));
		this.gameState.addFlag("chest opened", new Flag(false, "", "Chest opened."));
		this.gameState.addFlag("flextape taken", new Flag(false, "", "Flextape taken."));
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
		case "move":  //doing this will cause move to execute the look code
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
			//Take the key from the drawer
			//===============================================================
			if (command.unordered("key"))
				if(this.gameState.checkFlipped("key snatched") == false)
				{	this.gameState.flipFlag("key snatched");
				return new DisplayData("", "You barely grab the key with the tips of your fingers. "
						+ "As you pull them out you feel something furry against your hand, and the key "
						+ "slip from from your grasp into the darkness between the walls.");
				}
				else
					return new DisplayData("", "The key is gone.");

			if (command.unordered("flextape|tape"))
				if(this.gameState.checkFlipped("chest opened") == true)
					if(this.gameState.checkFlipped("flextape taken") == false)
					{		
						this.gameState.addToInventory("FlexTape");
						this.gameState.flipFlag("flextape taken");
						return new DisplayData("", "You take the roll of FlexTape.");
					}
					else
						return new DisplayData("", "You've already taken that.");

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "Can't take that.");

		case "open":
			if (command.unordered("box|chest"))
			{	
				this.gameState.flipFlag("chest opened");
				return new DisplayData("", "You try to open the latch, and to your surprise, "
						+ "it opens! You didn't even need a key. Perfectly fitting the round "
						+ "shape of the box, you find a gorgeous roll of FlexTape.");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		case "search":  //synonyms
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("bed|mattress"))
				return new DisplayData("", "A musty double mattress. Bedbug spots are visible under the seam. "
						+ "You look under the bed, and find a small circular chest with a keyhole.");

			if (command.unordered("closet"))
				return new DisplayData("", "Nothing remains in this closet, except for a few bare hangers. "
						+ "you notice a small hole in the bottom left corner of the closet, from what must "
						+ "be some sort of rodent.");

			if (command.unordered("hole"))
				if(this.gameState.checkFlipped("key snatched") == false)
					return new DisplayData("", "you find a small key in the hole.");
				else
					return new DisplayData("", "You look into the hole; there is nothing to be found.");

			if (command.unordered("chest"))
				return new DisplayData("", "This circular chest is bound with leather. A tiny keyhole "
						+ "with a latch marks the front.");

			//===============================================================
			//look around
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The room is empty, except for the bed, and the closet. "
						+ "Outside the window, you can see the mailbox.");

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
	private final String guestroomImage = "o+++////////////:-----------------..---::-:-..//:+:::o/o/:--........................`````````````````.ohyysssoo++///+o+///:://+++ooosyyyy:-.----------/////+/\r\n" + 
			"+++/////////////:::::::::::-----..---..--::-..//:/:-:o/+/:--.....................````````````````````.oysssssso++/////:::////+oooooosyyyy:------------/////++\r\n" + 
			"++//////////////::::::/:::://///::::/::::::-`.////:-:o/+/:--.....................`````````````````````oyo++ooss+++++////++ooossssooosyyyy:-..---------//////+\r\n" + 
			"+++/////////////-...-------------:::::://::.``/+//:-:o/+/:--.....................`````````````````````oy+////++:/o+////+++oossssossossyyy:-....-------///////\r\n" + 
			"oyhy+//++//////:`         ```````````....``.``:///:::o/+/:--.....................`````````````````````+yo+/////++oo/://///+oossooosssyyyy:-....-------///////\r\n" + 
			"mddyoyy+///////:`                          `  :///:::+/+/:--.....................`````````````````````+hsso+/+ooo++++++o++oossssssssyyyyy:-.....------///////\r\n" + 
			"mmmmdho////////:`                          `  ://::::+/+/::-.....................`````````````````````+yoo++++osoosyssssssssssssyyyyyyyyy:-.......----///////\r\n" + 
			"dddho+//////////` `                        `  ://::::+///:--....................``````````````````````++   .-`.osyyyyssssssssooossyyhyyyy:-......-----///////\r\n" + 
			"dhho+++/////////` -                        `  ://:-::+///::-.....................`````````````````````oo`  ..`.oyyyysoooossoooosssyyhhhyy:-....-------///////\r\n" + 
			"mmmdhhhdhy+/////`..`                      `` `://:-::////::--....................`````````````````````oo`` -```osssssooosssssyyyyyyyhhhys:-...--------//////+\r\n" + 
			"mmmmddhmmmdyyyo+:o/.                      `` `://:-::////::--....................`````````````````````oo-.-:.-:sssssssssossssssssssssssyo:----------:-///////\r\n" + 
			"mmmdsosoooyhhhy++s:  ``                   `` `://:-::////::--....................```.`````````````````:::-----------------------::::::::///:-------:o://////+\r\n" + 
			"hhhshho+oyoyhhhs-oo.````                  `` `://:-::////::--...................---:+s`````````./ooooooooooooosssssssssyyyyyyyyyyyyyyyyyyyyyhhs::ohdd://///++\r\n" + 
			"mmmdhoooodyoysyysys+:`                    ``  -//:-/-////::--..................-/..-+hs-``````-h+-..````````.....................------------/hhydmmm://///++\r\n" + 
			"mmmdhyso++sos/o+-                         `` `-//:-/::///::---.................-/..+/hdh-`````yy/-.`````````````..................------------+ddmmmm:/++++++\r\n" + 
			"mmdho++++++odh+/-`                        .  `-//:-/::///:::----.............../ho./-//:.````:mdo+/+o:/:/o////o+////o+/+++o++++++o+++++ooooooohNmyyhh:/++++++\r\n" + 
			"dmdmy++shyosNNyys+`                       .` `-//:-+::///:::----...............odd.```````````ho:-.:+.-..s.--.o:.--.y-.---y/--:-+y---::ss-:::/sNd////:/++++++\r\n" + 
			"ddhdmhydmmdNNNs++os- `                    `` `-//--+::///:::------.............-+-..``````````y/-.`-/`.``o....+-.-..s..--.o-----:o-----++---::/mo:::::/++++++\r\n" + 
			"hhNNmdNNmdNMMmsy+os:syy.                 ``` `-//--+:-///:::---------..........--....`````````h/-.`-/..``o....+-.-..s..--.s-----:o-----o/---::/mo:::::+++++++\r\n" + 
			"dmNNmmysydNNmmdmmdddhho`                 ``` `:/:--+/-///:-:-----------........----...````````+-:hsso:-..o...-o/+shdo..---+-----/+---:-o/---::+No:::::+++++++\r\n" + 
			"mNmNNmyshmmhdmNNmhhhho.         `.-...```.-```-/:--//-///:::------------....:++ooo+/::-```````...ymNNNmmmdddmNNNNNNh------------:::::::s/:::::+No:::::+++++++\r\n" + 
			"mNNmddsssyhdmNNNNmddh:`  `     .+/+hdsyhhyoo+oooooooo+oooo++//::::------..../o+osyyo:````````````ymmmNNNNNNNNNNNMMNs+/:::------.-:////////////oNo///::+++++++\r\n" + 
			"mNmmmyso++mNNNNNmm/:-`   oyy+``y++dmdddds-..``:/:--//://+++++ooossssssssssssoo/-:///.`..`.--...--dmmmNNNNNNNNNNMMMNso+/:::---..--.:/+ooysooo++yNs++//:+++++++\r\n" + 
			"mNNmyo++++dNNNNmd:  :sysohddh`.dhdddyso-``..`.::---++-:::-...``.....------:::odh+---.``````````.-dmmNNNNNNNNNNMMMMmdyso+//::::/+///+++oyysssooyNso++/:+++++++\r\n" + 
			"NNMMNyoo++hmmmmh/...-+o+/sdd+`odyo//yh+````.`.-.```           ````````````````-yNs...`````````.-ymmNNNNNNNNNNNMMMMdyo+///:----::////////+syssshNso++//+++++++\r\n" + 
			"NMMMMNhoosmmddmmdmddh:   :ys``/ss--/dso/:/::-.`````````............``````````.`.hNo````........---/++oosyyhdmmNNmmyo+//+/:----.--:://////++ossdMso+/:/+++++++\r\n" + 
			"NNMNhyyydmmmdmmNmmmmmdoo/.`-``.h/```s`  .d. `.hs----+/--::--..--.-..--........../Nm......---.---..---------...........-------------:::::///+sydM/+ss+/+++++++\r\n" + 
			"NNmmooodNNNNmmmmmNyhdhhhhy:`  .h/`  s  ``y ```o:```.d+```.yh:////+//::::---::--./mNs----------------------:::--.-.-----------:::::::////++ooshNM//:::/+++++++\r\n" + 
			"NmmyosodNmmdhhmmmmdhhddhhy:   `y/  `s````y````o:`...y.....so....-mh.--:/sdo++++:smNmo-:///::---------:::::::::::---::::::::://///////////oosymMN////:/+++++++\r\n" + 
			"Nmmmdhdmmmmyysydmmmdhs:-.`  ```yo`  o``  y````o-`...y-....o+.....y/.-.-.om:...-:hNNmo-----::/::-:::::::::::::::::::://////////////+//////ossydNN/+//:/+++++++\r\n" + 
			"mNmdmmmmNNNmNNNmmmmdhhh-  `````ys   o`   s````o-.```y.....o+.--.-y/-----:h------+NNy----:::::/::::::::::::::::::::///////////////////////+syydNMdyys//+++++++\r\n" + 
			"hmmmmmmNNNmmNNNmmhsyydh.`````  ss   o`   s`  `o:.```y:....o+-..-.y/-----:h-:-:::/mN+:::::::::////::::::::::::::/::://///////////////////++syhmMMmdhy//+++++++\r\n" + 
			"NmNNNNNNNNmNNmdhsyhdyo-`````   sy.``o`   y`` `++`   y:.```++.-...y::----:h:-:::::dNo:::/::::///+//:::::::::::::://////////////////+++/++++hmNmMMmhyy//+++++++\r\n" + 
			"NNNMMMNmNNNNNmmmdddhs.`````   `sy-  o.`  y`   ++````y:.```+o-....y/--.--:h-:::-::dN+//::///:://+////::::::://::::///////////////++++++++++hMNmMMmdhh/++++++++\r\n" + 
			"mmNMNNNNmmNNmmmddmddo.````    :os+-.yo ``y`   +/````y/````+s-....yo---.-:h:::---:dNo/:/:////://++////:://::://::://////////////+++++++++++yMNNMMddhh/++++++++\r\n" + 
			"mmNNNNdydmmNmmyoddh/.....`    `/s. `-:::/ds`  oo` ``s:```.+s..``.yo-....:d/------dNo//://///////o+////:://::////:///+/+////+++/+++++++++++oNNNMNhhhh:++++++++\r\n" + 
			"NNNNNNdmmyNNmms++/-...```      :h-       `-::/sd:.`.ho.``.+s....-yo-....:m:---.:-dNs////:///////+o+////::///:///////++++///+++++++++++++ooohddMNdhhh:++++++++\r\n" + 
			"NNNNmNNmmNNmmdo++:-..`         -d+`          ```-:/+hd+-..sh.....yo....-:m:-----:dNy//::/:://////oo+//////////////+/++/++/+++++smdsyhso+ooshdmMNdhhy:++++++++\r\n" + 
			"mNMMMNNmNhossyhhh/             .h`          ``  ````..:/+ohms:---hy--..-:d----:-:dNh+::/::/:/:///+s++//////////+++//++/+++++oshhMNNNNdo+oymmmmNNmddh:++++++++\r\n" + 
			"MMMNNNmmmho++++//-             .h           ```  ````.```.--:+ossmmo:---/m+-----:dNy/:/://://:///+oo++///++///+++//+++++ooommNNNNNNNNmdmmddddddddddd/++++++++\r\n" + 
			"MNNmNmysoosss+///-         ```.+d-     ``.-:/:-.````...``-..`.--.:/+ssssdNh-:-::/dNo::://:/::/://+oso++////+/+++++ooohosdmNNNNmmmmmdddhhhhhyyyyyyhdh/++++++++\r\n" + 
			"NNmyo+++++++++//:`        ````.//::-:://+++oossyso++:-``..-...-......--:/oysysooymN+:://:/::/://:/+syo++/++ooossyhdddmmmmmmddddhhhhyyyysssssssssssso/++++++++\r\n" + 
			"Ndmmdhhyo++++///:.         `````...--::///++oossyyyhhhysoo+...-.......---:---/+sdNNs:/://///://:/:/oossoooososhmNNmmmmmdddhhhyyyyysssssssssssssssss+/++++++++\r\n" + 
			"NMMMMMMNmyo+++///:          ````.....---::://++oossyyyhhhddddy+//---..--------::yNNd::////////://://ooooosyhdmmmmmddddhhhyyyyssssssssssoooooooooooo+/++++++++\r\n" + 
			"MMMMNdyo++++++///:           ```.......----::://++oossyyyhhdmmddmdhso++:::--:://+mm/:///+//+//+///://hhhmmmmmmdddhhhyyyyysssssssssooooooooooooooooo/+++++++++";
}
