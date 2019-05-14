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

public class LivingRoom extends Room 
{

	private static final long serialVersionUID = 1L;

	public LivingRoom(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Living Room";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.livingroomImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		if(this.gameState.checkFlipped("rug moved") == true)
		{
			this.description = "You step into the living room. Below a window on the far wall a long, "
				+ "broken-in couch rests, across from a low table where surely a television must "
				+ "have been. Along the third wall there is a bookshelf, with few books littered "
				+ "on the shelves. The plush rug is pulled back to the couch, and a steel safe is "
				+ "visible, inlaid into the floor. A four-digit combination lock is on the front. "
				+ "There are two doorways leading to the entryway and the kitchen.";
		}
		else
			this.description = "As you step into the living room, you feel the soft texture of a woven "
				+ "wool rug under your feet, faded with what must be generations of wear. Below a window"
				+ "on the far wall a long, broken-in couch rests, across from a low table where surely "
				+ "a television must have been. Along the third wall there is a bookshelf, with few books "
				+ "littered on the shelves. There are two doorways leading to the entryway and the kitchen.";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Entryway
		//=================================================================================
		this.addMovementDirection("entryway", "Entryway");
		this.addMovementDirection("foyer", "Entryway");
		this.addMovementDirection("entrance", "Entryway");
		
		//=================================================================================
		//Create directions that move to Kitchen
		//=================================================================================
		this.addMovementDirection("kitchen", "Kitchen");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create paperclip
		//=================================================================================
		Item paperclip = new BasicItem(this.gameState, "Paperclip", "", "A paperclip. Why are you picking up garbage? ");
		this.gameState.addItemSynonyms(paperclip, "paperclip", "paper clip", "clip");
		this.gameState.addSpace(paperclip.getName(), paperclip);

		//=================================================================================
		//Create giant ruby
		//=================================================================================	
		Item huuuuugeRuby = new BasicItem(this.gameState, "Giant Ruby", "", "It is a giant ruby. It weighs a million.");
		this.gameState.addItemSynonyms(huuuuugeRuby, "ruby", "gem", "gemstone");
		this.gameState.addSpace(huuuuugeRuby.getName(), huuuuugeRuby);
	}

	@Override
	public void createFlags() 
	{
		//=================================================================================
		//Create Flags and add them to the Flag hashmap.  The first string field in addFlag()
		//is the key name for the Flag in the hashmap.  In the Flag constructor, the first
		//field is the Flag's starting boolean value.  The second field is the string the
		//Flag will return on toString() if it has not been flipped (false).  The third field
		//is the string the Flag will return on toString() if it has been flipped (true).
		//=================================================================================
		this.gameState.addFlag("rug moved", new Flag(false, "", "Rug moved."));
		this.gameState.addFlag("couch searched", new Flag(false, "", "Couch searched."));
		this.gameState.addFlag("paperclip taken", new Flag(false, "", "Paperclip taken."));
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
		case "fold":
		case "move":  //doing this will cause move to execute the go code
			if(command.unordered("rug"))
			{
				if(this.gameState.checkFlipped("rug moved") == false)
				{	
					this.gameState.flipFlag("rug moved");
					return new DisplayData("", "You move the rug to the side. Underneath, you uncover a safe "
						+ "laid into the floor. a four combination lock is visible, next to a large handle.");
				}
				else
					return new DisplayData("", "You have already moved the rug.");
			}
			
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
			//Take the paperclip from couch
			//===============================================================
			if (command.unordered("paperclip|paper clip|clip"))
			{
				if (this.gameState.checkFlipped("couch searched"))
				{
					if (this.gameState.checkFlipped("paperclip taken") == false)
					{
						this.gameState.addToInventory("Paperclip");
						this.gameState.flipFlag("paperclip taken");
						return new DisplayData("", "paperclip taken.");
					}
					else
						return new DisplayData("", "You've already taken that.");
				}
				else
					return new DisplayData("", "You don't see that here.");
			}			

			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData("", "Can't take that.");
			
		case "open":
		case "unlock":
			//===============================================================
			//If default is reached, return a failure message.
			//===============================================================
			if (command.unordered("safe"))
			{
				if (this.gameState.checkFlipped("rug moved") == true)
				{
					if (this.gameState.checkFlipped("phone answered") == false)
					{
						this.gameState.addToInventory("Giant Ruby");
						this.gameState.flipFlag("jewel obtained");
						return new DisplayData("", "Remembering the code you heard from the voice in the "
								+ "telephone in the master bedroom, you enter the passcode 7852. You turn "
								+ "the handle, and it opens with a thunk. Inside, you find the largest ruby "
								+ "anyone has ever seen, and, before you stop to think, you grab it and shove "
								+ "it in your bag. ");
					}
					else
						return new DisplayData("", "You've already taken that.");
				}
				else
					return new DisplayData("", "You don't see that here.");
			}

			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData ("", "Can't do that here.");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "Spiderwebs are visible in every corner of this room. "
						+ "Stains on the wallpaper give away years of water damage, seeping through the foundation.");
			
			if (command.unordered("table"))
				return new DisplayData("", "A table of solid construction; a thin film of dust lines the surface.");
			
			//===============================================================
			//searches couch, and flips couch searched flag.
			//===============================================================
			if (command.unordered("couch"))
			{
				this.gameState.flipFlag("couch searched");
				return new DisplayData("", "Depressions in the cushions are visible even from a distance. Scratches in the cloth on "
						+ "base of the frame reveal the couch's soft tissue. Reaching in between the couch cushions, you find an old paperclip.");
			}
				
			if (command.unordered("rug|floor"))
				return new DisplayData("", "This rug has seen better days. A corner of the rug is fraying, in an area of heavy traffic. "
						+ "You notice a crease across an area of the rug, where it seems it has previously been folded away.");
			
			if (command.unordered("bookshelf|book|shelf"))
				return new DisplayData("", "Various books are strewn on all shelves, lazily tossed. One recounts of the author's "
						+ "memory of his life during the Roaring Twenties, another is titled 'How to Read a Book', and still "
						+ "another belongs to the Witcher series. Someone was a fan.");

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
	private final String livingroomImage = "d//////yhyyyhhhs//+//smyooooooooooos+++++/+///////////////+/o+/shNNyomh/+hhhNNh++---:::syhoso++ss+/+ooyyyhhhhhddddhdhhN++/ymNmNmhssdmmdosyhmd+----/o/..osssss\r\n" + 
			"m+/////oyyhshddd/////+mhooooooo//oos+++++/+///////////////+/o+/shNmyomh//osyNNd++---:::oyhmho++yy/+ooyhdddhyyhddmddhyym///sshmmhhmdymmhyssyhd:----/o-.-sssoos\r\n" + 
			"mo/////+yyhydddd+////+mdooo+ooo++ooso++++++///////////////+/o+/syNNhomh//syyNNd++---::/yhhyyyssyyooo+shhhddhhhhddddhhhd//+yysymmmdddddddyhhhh----:+o../ssssoo\r\n" + 
			"my/////+hhhyhdddo//+//ddooooooooooooo++++++///////////////+/++/syNNhomh//ysyNNd++---/:osdyyhyyhhyhssoshdddddhhddmddhhdd+++sssssssssssssssysso---.:++..+oooooo\r\n" + 
			"mh//////hhhhhddds//+//hmooooooooooooo++o+++///////////////+/++/oymNdodd//hhhmNd+/---/:::+o+ososyyoooohddmmdddddmdddhhyss+++++++++++++++++ooo/----/+/.-oooooss\r\n" + 
			"mm//o+//yyhdydddy//+//ymsooooooooooos++o++++//////////////+/++/oymNmymmosmmdNNd+/---/:+++++++++++++ooohmmmdddmmmmdhhhsssyossssssssssssssyyyy:----/o-.:ooooooo\r\n" + 
			"Nmo+h+//shdmhmmmd//+//smyoooooooooooso+o+++++/////////////+/+o/oymNNNNNNNNNNNNd+/---/:///++++++++oooohddmmmddddmmmdhhyydhsydmdmmmmdmddddmmmd----:+o../soooooo\r\n" + 
			"mmysy/+/sooy+oooo///+/+mhoooooooooosso+ooo++++////////////+/+o/+ssyyyyyssssssss+/---/:::/yyyyhhhhy/+odmmmmmdddmmmddddhhmmhhmmdmdddddddhddddy---.:++..osssssss\r\n" + 
			"mmdhy/+so/s+////////+/+mdoooooooooossoosydss++++++++//////+/+o/+osssssssoooooos+/---/:::/NNNNNNNNd/+odmmmdmddmmmdy+++o+++++osoooooosyssssss+----:+/.-syysssss\r\n" + 
			"dNmys/sy+s+/////////+++dmooooooossssyssyyhyyssssssyhoo+++////o//osssssssoooooos+/---/:::+NNNNNNmmh/+smmmmmmdmNmmmohhhhyyyyyyysss+sssyoooooo:----/o-.:yyyyysss\r\n" + 
			"dmhyyhdhhddddddddddddhymNsssssyyyydmmmmmNmmmmmmmmmmmmdyyo/+//o//osssoossoooooos+/---//+/oNNNNNmhdy/+ydmmmmmddmmmmodddddddddddddysdddddddddd----:/o..+ssssssss\r\n" + 
			"ddhhydddhNNNNNNNNNNNNNmdhyyyyhyyyydmddddhhysssyyysssso+++++++//+++++o+++oo+++os//---/omhyNNNNNddmy//ymmmmmmddmmmd/syyhhhhhhhhhhsyhddmmmmNNh----:++..ossssssss\r\n" + 
			"ddddhddymmNNNMMMMMMNNNmhyyyyhhhyo+//++++++++///++/////:::/oo+/so++/so/:+o+///++/::+/:-:/+/:-/+o+/:::ssssyddysyyy+ydymNNNmmmmmNNNNmmddddddd+----/o/.-ssssssssy\r\n" + 
			"dddddmhdmmNNNNMMMMMNNNmhyyyhhyyhh+//++////+///////////::::o+oosso+oso//so+/:os+/:/s+/:-/o/:--:+/::--//:----:::--.-://:---/ossso+++oosyhddm/----/o-./yyyyyyyyy\r\n" + 
			"dmdmmhhdmmNNNNMMMMMMNNNmyyyhhhhyho////////////////////////ossoossoooooooooo+ooo+/+o+/::++//::/+/:--:o+::--./o/::-..-///::----:---------..-....-/o..oyyyyyyyyy\r\n" + 
			"mmdmddmmmNNNNMMMMMMMNNNNmhhhhhdyhs+/:://++///++////o///:/+sooo+yyoo+syooossooooooo+oo++/o++++/+++///++//::::++//:-.-/+//:-----.........``````  `+..yhhyyyyyyy\r\n" + 
			"NmmdmmmdmmdsshmNNNNNmmmmmmmmhhdyhyo/////o+///+o//////::://sooo+osoo+oyoo+syoo+ohso++yo+//oso++++++++++++++///+++//::::-----.......````          ...--::::///+\r\n" + 
			"yyy+++/::::--.-smmNNNNNNNNNNhhdyyho+/://:////////////+////oosysoosoooooo+oooo++soo++ys+//+ho++//yo+++++oso+++++//++++++///::-.````              :////::::::::\r\n" + 
			"--:......``````.sNNNNNNNMMMNdhhyyhs+////////////++////////+++++++ooo++ooooooooooso+oooo+/+oo++//sso++++yyo++ooo+so++++++oooooo:`  `   `        `::::::::::///\r\n" + 
			"-.....````````..hNMMMMMMMMMMdhhhyhso+++++++///++++//////////////////////////::::/+o++oooo++soo++/oo+++++oooosoosso++////++//::s/`  `        `  .:::::::::::::\r\n" + 
			"----.........-:hMMMMMMMNmmdmdhhhyhhyyyyyssooo++++++////////////////:::::::::::////:////::///++++++oosoo++oooso+ssoooooosoo+/::s+.              .:::::::::::::\r\n" + 
			"/::::::::::::smNNNmmdhyyhhdmdhhdhdhhhhhhhhyyyyyysssoooo+++++//::---:::::://::::::://////////////////////++osooosso+++syyso+/::o+-              -:::::::::::::\r\n" + 
			"+++////::::/hdmdddddmmdddddmmhhdhhdhhhhhhhhhyyyyyyyyyyyyyyssso+/+//////:::::::::://////////////////////////sooosso+++ohyoo+/-+o+-`             ::::::::::::::\r\n" + 
			"o+///::----ydddmmmmmmmmmmmmmmdhdhhdddddddddhhhhhyyyyyyyyyyysssyssssoo++++///////////::::////:::::::://////+soooyso+++shsoo/:.+o/-`  `         `::::::::::::::\r\n" + 
			"+//::---..:dddddddddmmmmmmmmNmddhhdddddddddddddddddhhhyyyyyyyyysssssssoosooooo+++/////:::::::::::::::::://+soooyso+++shsoo/:-oo/.`            .::::::::::::::\r\n" + 
			"//:---....:mdmdddddddddddddmmmmmyydmmmdddhhhhhhhhhhddddddddddddyyysssssssssssssooooo++++////:::::::::::/+osso+osoo++osysoo/::oo:.`   `        .-------:::::::\r\n" + 
			"/::---....-mddddddddddddddddddddyyhmmmmmmmmmdddhhhhhhhhhhhhhhhddddhhyyssssssssssssooooooooo+++////::::///oyso+syso++oyysoo/::o+:.    `        ---------------\r\n" + 
			"/::---.....dddddddddddddddddddddhhhhhddmmmNNNNNNNmmddhhhyyyyyyyyyyhhhhhhhhyysssssssssoooooooooo++++/////:/ssoossso++oyyso+/-/o+-`            .:--------------\r\n" + 
			"/::---.....hddddddddddhhdhhhhhhhhhhhhhhhhyhddmNNNNNMNNNmmddhhyyyyyyyyyyyyyhhhhhhyyysssssoooooooooooo+++++//yooysoo++oyyso+:-oo/.             /++++///:::-----\r\n" + 
			"//:---::+oyhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhyhhhyhhdmmNNNNNNMMNNmddhyyyysssyyyyyyyyhhhhhyyysssoooooooooo++o/+yooysoo++ohyso+:-so:` `         `:++++++o+++++++++\r\n" + 
			"hhhyhyyyyyhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdmmNMMMMMMNNNmmdhhyyyssssssssyyyyyyhhyyysssooooo++/ssooysoo++sysso+:/s+-` `       `-/++++oooo+++++++++\r\n" + 
			"dddhhhhhhhhhhhhhhhhhhdhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhyhyyyyhhdmNNMMMMMMMMNNmmdhyysssssssssyyyyyyhhhyyyso+odsooyso+++sysoo/-+s+-   `     `/ooo+++++++++++ooo++\r\n" + 
			"ddddhhhhhhhhhhhhhdddddddddddddhhhhhhhhhhhhhhhhhhhyyhhyyyyyyyyhyyyhhhMMMMMMMMMNNNNmddhyyssssssssssssyyyyyysssooyso++osysoo/-+s+-`  `    :ooooooooo++++++++++++\r\n" + 
			"dddddddhhhhhhhddddddddddddhhhhhhhhhhhhhhhhhhhhhhyhhhyyyyyyyyyyssoooyNNMMMMMMMNNNNNNNNNNNmdhhysssssssssssoooooosso++osyso+/-oo/-`     .+oooooooooo+ooo++++++++\r\n" + 
			"dhhhhhddddhhhhhddddddddhhdhddhhhhhhhhhhhhhhhhhhhhyyyyyyyyyssooosydmNNNNNNNNNNNNNNNNNNNNMMNNNNNmddhysssssoooooosso++oyyso+:-so:.    `/ooooooooooo+++++++++++++\r\n" + 
			"dddhhhddddddddhhhdddddddddddhhhhhhhhhhhhhhhyyhhhhhhhhyssosssssyydddhddmmmNNNNNNNNNNNNNNNNNNNNNNNNNNNmddhyssoossoo+osysoo+//so:.   -sysoooooo+++++++oo++++++++\r\n" + 
			"ddddhhhhhhhhddddddddddddddhhhhhhhhhhhhhhhhhhhhhyhhysssossyyyymhysssyhhhhhddddmmmmNNNNNNNNNNNNNNNNNNNNNNmmmmdhhssooosysso+:+so:.  :ysssooooooo++++++++++++++++\r\n" + 
			"ddddddhhhhhhhhddddddddddddhhhhhhhhhhhhhdhddddhysssssyyyyyyyhhNdyoosooosssyyyhhhhddmmmmmmmNNNNNNNNNNNmmmmmmmmmmdhyyyhssso+/+s+-`./yyssooooo+++++++++++++++++++\r\n" + 
			"ddddddddhhhhhhhhhdddddddddddhhhhhhhhdhhddhyysssyyyyyhhhhyyyyhNhyo+++++oosysyssyyyhdhdmmmmmmmmmmmmmmmmmmmmmdmmddddddh++yys+os+:+yyyysooo++++++++++///++++++++/\r\n" + 
			"dddddddddhhhhhhhhhhhhhddddddhhhhhhddhyyssssyyhhhhhhdhhhyyyyyhNdy++oooooo+/+ooosyyyyhddhddddddddmdddddmmmmmmddddddddy/ohhhhhhhhhhyyssooo+++++//////////+++++//\r\n" + 
			"ddddddddhhdhhhhhhhhhhhhhhhhdddddhyyyyyyyyhhhhhhdhhhhhhhyyyyyhNdh//++oo++o+++oooosoyosyyyyyhyhhhyyhhhhdddddddddddddhs:ohhhhhhhhyysssssoo+++//+++++///////+++//\r\n" + 
			"dddddhdhhhhhhhhhhhhhhhhhhhhhhyyyyyhhhhhhhhdddddhhhhhhhyyyyyhhNmh+/+///++++++osoooo+oosyyosyssosssyyyyyyyhhhhhhhhhdds/shhyyyyyyyyssssoo+++///+++///+++++o+////\r\n" + 
			"dhhhhdddddddddddddhhddddyyysssyhhddddhhhhddddhhhhhhhhhhhyyyhhNmh/////+/////+oooooossssssoo++oososyhhyysyhhyyyyyhhhho/yhhhhhyysooooooooo++/////+++++++oo+o++++\r\n" + 
			"hhdddhdddddddddddddhhyyyyyyyhdddddhhhhhhhhddhhhhhhhhhhhhhhhhhNmh++///+/:+//://oo+o+ooss+++++/:/+osssyoo+osoyhyhyyhd+/yyyyyyssssssso+//////++++++////////++++/\r\n" + 
			"dddddddddhhddddhhyyyhyyhhdddddddhhhhhhhhhhdddhhhdhhhhhhhhhhhhNmh+:::////:/:::/oo/++////+//++++o+++o+/+o//+/+++osyyd+//++oosooo++++++++/////////+++++////////+";
}
