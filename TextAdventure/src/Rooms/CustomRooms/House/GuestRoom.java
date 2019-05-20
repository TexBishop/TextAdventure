/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.House;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;
import Structure.GasLeak;
import Items.BasicItem;
import Items.Item;

public class GuestRoom extends Room implements GasLeak
{
	private static final long serialVersionUID = 1L;

	public GuestRoom(GameState gameState) 
	{
		super(gameState);
		this.initializeGasLeak();
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
		this.description = "This room seems to be the guest bedroom. "
				+ "There's an old, wooden bedframe against one wall, splintered in several places. "
				+ "A small bedside table sits next to it. "
				+ "The room is lit by a grimy window, which overlooks the back yard. ";

		//=================================================================================
		//If the key has been left on the floor, say so
		//=================================================================================
		if (this.gameState.checkFlipped("key traded sequence ran") == true &&
			this.gameState.checkFlipped("small key taken") == false)
		{
			this.description += "On the floor next to the empty popcorn pan is a small key. ";
		}

		//=================================================================================
		//Key trade sequence, will only run once.  Replaces previous description.
		//=================================================================================
		if (this.gameState.checkFlipped("key traded sequence ran") == false &&
			this.gameState.checkFlipped("room exited") == true)
		{
			this.gameState.flipFlag("key traded sequence ran");
			this.description = "You charge back into the room, planning to catch the mouse snacking on the popcorn. "
					+ "Instead, what you see is an empty popcorn pan. "
					+ "What? How in the world... that was enough popcorn for two or three people! And in such a short time... "
					+ "On the floor next to the popcorn pan is the key that you were hoping to get from the mouse. ";
		}

		return this.description;
	}
	
	@Override
	public void initializeGasLeak() 
	{
		this.addRoomToLeakArea(this.gameState, this.getName(), 4);
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Upstairs Hallway
		//=================================================================================
		this.addMovementDirection("hallway", "UpstairsHallway");
		this.addMovementDirection("hall", "UpstairsHallway");
		this.addMovementDirection("leave", "UpstairsHallway");
		this.addMovementDirection("exit", "UpstairsHallway");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create flextape
		//=================================================================================
		Item tape = new BasicItem(this.gameState, "Duct Tape", "", "Silver duct tape. Famous for being able to repair anything. ");
		this.gameState.addItemSynonyms(tape, "duct tape", "tape");
		this.gameState.addSpace(tape.getName(), tape);

		//=================================================================================
		//Create small key
		//=================================================================================
		Item key = new BasicItem(this.gameState, "Small Key", "", "A key a bit smaller than a typical house key. ");
		this.gameState.addItemSynonyms(key, "small key");
		this.gameState.addSpace(key.getName(), key);
	}

	@Override
	public void createFlags() 
	{
		this.gameState.addFlag("key found", new Flag(false, "", ""));
		this.gameState.addFlag("key traded", new Flag(false, "", ""));
		this.gameState.addFlag("small key taken", new Flag(false, "", ""));
		this.gameState.addFlag("popcorn placed", new Flag(false, "", ""));
		this.gameState.addFlag("room exited", new Flag(false, "", ""));
		this.gameState.addFlag("key traded sequence ran", new Flag(false, "", ""));
		this.gameState.addFlag("bedside drawer opened", new Flag(false, "", ""));
		this.gameState.addFlag("duct tape taken", new Flag(false, "", ""));
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
		case "leave":
		case "exit":
		case "go": 
			//===============================================================
			//If go back, return base room DisplayData.
			//===============================================================
			if (command.unordered("back"))
				return this.displayOnEntry();

			//===============================================================
			//Trigger the key traded flag on room exit
			//===============================================================
			if (this.gameState.checkFlipped("popcorn placed") == true &&
				this.gameState.checkFlipped("room exited") == false)
			{
				if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
				{
					this.gameState.flipFlag("room exited");
					this.gameState.flipFlag("key found");
					this.gameState.flipFlag("key traded");
					return this.move(command);
				}
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
			
		case "set":
		case "drop":
		case "position":
		case "put":
		case "place":
			//===============================================================
			//Place the popcorn on the floor next to the mouse hole
			//===============================================================
			if (command.unordered("popcorn", "mouse|hole|floor"))
			{
				if (this.gameState.checkInventory("Popcorn"))
				{
					if (((Item) this.gameState.getSpace("Popcorn")).getState().contentEquals("(popped)"))
					{
						this.gameState.flipFlag("popcorn placed");
						this.gameState.removeFromInventory("Popcorn");
						return new DisplayData("", "You place the popcorn next to the mouse hole and back off to the other side of the room. "
								+ "Surely this buttery popcorn smell will lure it out. "
								+ "You see the mouse stick it's nose out of the hole and sniff the air, then dart back inside. "
								+ "It knows you're here. You're probably going to have to leave the room. ");
					}
					else
						return new DisplayData("", "You place the popcorn next to the mouse hole and back off to the other side of the room."
								+ "You wait for a while, but nothing happens. I guess the mouse isn't interested in unpopped popcorn. "
								+ "You pick it back up. ");
				}
				else
					return new DisplayData("", "You don't have any popcorn. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "That doesn't do anything. ");

		case "grab":
		case "pick":
		case "take":
			//===============================================================
			//Take the key from the drawer
			//===============================================================
			if (command.unordered("key"))
			{
				if (this.gameState.checkFlipped("key found") == true)
				{
					if (this.gameState.checkFlipped("key traded") == true)
					{	
						this.gameState.flipFlag("small key taken");
						this.gameState.addToInventory("Small Key");
						return new DisplayData("", "You take the small key that the mouse left behind. ");
					}
					else
						return new DisplayData("", "It's out of reach. Your hand won't fit through the small mouse hole. ");
				}
			}

			if (command.unordered("tape"))
			{
				if(this.gameState.checkFlipped("bedside drawer opened") == true)
				{
					if(this.gameState.checkFlipped("duct tape taken") == false)
					{		
						this.gameState.addToInventory("Duct Tape");
						this.gameState.flipFlag("duct tape taken");
						return new DisplayData("", "You take the roll of duct tape. Duct tape is always useful. ");
					}
					else
						return new DisplayData("", "You already have that. ");
				}
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here. ");
			
		case "force":
		case "lever":
		case "leverage":
		case "wedge":
		case "pop":
		case "use":
			//===============================================================
			//use the flathead screwdriver to open the drawer
			//===============================================================
			if (command.unordered("flathead|screwdriver", "drawer"))
			{
				if (this.gameState.checkInventory("Flathead Screwdriver") == true)
				{
					if (this.gameState.checkFlipped("bedside drawer opened") == false)
					{
						this.gameState.flipFlag("bedside drawer opened");
						return new DisplayData("", "Wedging the screwdriver into a gap in the bedside table's drawer, you try to force it open. "
								+ "Success! The drawer breaks free from whatever was holding it closed. "
								+ "Pulling it open, you see a roll of silver duct tape. ");
					}
					else
						return new DisplayData ("", "You've already done that. ");
				}
				else
					return new DisplayData ("", "You don't have a screwdriver. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't do that here. ");

		case "open":	
			//===============================================================
			//Open drawer
			//===============================================================
			if (command.unordered("drawer"))
			{
				if(this.gameState.checkFlipped("bedside drawer opened") == true)
				{
					if (this.gameState.checkFlipped("duct tape taken") == true)
						return new DisplayData("", "The drawer is empty. ");
					else
						return new DisplayData("", "There's a roll of duct tape in the drawer. ");
				}
				else
					return new DisplayData("", "The drawer is stuck; you can't get it open. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here. ");

		case "search":  //synonyms
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The room is mostly empty, and covered in dust and cobwebs. "
						+ "You see mouse tracks in the dust around the perimeter of the room, and mouse droppings scattered liberally. "
						+ "There's a mouse hole in the corner. ");
			
			if (command.unordered("bed"))
				return new DisplayData("", "An old, wooden bed frame. It was probably sturdy at one time, but it's now broken, "
						+ "half of its slats splintered, along with one of the length-wise struts. "
						+ "The headboard still looks like it's in good shape though. ");

			if (command.unordered("hole"))
			{
				if(this.gameState.checkFlipped("key traded") == false)
				{
					this.gameState.flipFlag("key found");
					return new DisplayData("", "Getting down on the floor and looking in the hole, "
							+ "you're able to discern a mouse's nest in the darkness within. "
							+ "You see a bit of movement, and notice the mouse, and a glint. "
							+ "It looks like it's holding a small key in its mouth. "
							+ "Why does it have something like that? "
							+ "If you can lure it out somehow, maybe you can get the key from it. Maybe with some food? ");
				}
				else
					return new DisplayData("", "Getting down on the floor and looking in the hole, "
							+ "you're able to discern a mouse's nest in the darkness within. "
							+ "The mouse is there, sleeping, looking fat. ");
			}

			if (command.unordered("table"))
				return new DisplayData("", "It's a small wooden bedside table, not very big. "
						+ "The top of it is all sticky. Gross. What is that? "
						+ "Dust is plastered to it. It has a small drawer. ");

			if (command.unordered("window"))
				return new DisplayData("", "It's filthy, but you can still make out the back yard. There's a barn, a shed, and a cornfield. "
						+ "And there behind the barn, you see an old timey outhouse. How old is this farm? ");

		default: 
			//===============================================================
			//If default is reached, return a failure message.
			//===============================================================
			return new DisplayData("", "Can't do that here. ");
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
