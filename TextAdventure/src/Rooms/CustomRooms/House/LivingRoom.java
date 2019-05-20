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

public class LivingRoom extends Room implements GasLeak
{

	private static final long serialVersionUID = 1L;
	
	public LivingRoom(GameState gameState) 
	{
		super(gameState);
		this.initializeGasLeak();
	}

	@Override
	protected void setName() 
	{
		this.name = "LivingRoom";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.livingroomImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "You step into the living room. "
				+ "There's an old, musty couch against one wall, covered with rips. "
				+ "Beneath the couch, covering nearly half the room, is a dank, mildewed rug. "
				+ this.gameState.getFlag("rug moved").toString()
				+ "A small, busted table that probably once held a television is on the opposite side of the room, two of its legs missing. "
				+ "You can see the kitchen through a doorway on one side, and the entryway through another. ";

		return this.description;
	}
	
	@Override
	public void initializeGasLeak() 
	{
		this.addRoomToLeakArea(this.gameState, this.getName(), 1);
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
		//Create copper wire
		//=================================================================================
		Item copperWire = new BasicItem(this.gameState, "Copper Wire", "", "A loose length of copper wire. Why are you picking up garbage? ");
		this.gameState.addItemSynonyms(copperWire, "copper wire", "copper", "wire");
		this.gameState.addSpace(copperWire.getName(), copperWire);

		//=================================================================================
		//Create giant ruby
		//=================================================================================	
		Item rubySkull = new BasicItem(this.gameState, "Ruby Skull", "", "It's a skull carved from Ruby, about the size of a fist. "
				+ "Such a huge gemstone, it must be worth a fortune! ");
		this.gameState.addItemSynonyms(rubySkull, "ruby", "skull", "ruby skull", "gemstone");
		this.gameState.addSpace(rubySkull.getName(), rubySkull);
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
		this.gameState.addFlag("rug moved", new Flag(false, "", "Part of the rug is thrown back, revealing a safe set into the floor. "));
		this.gameState.addFlag("couch searched", new Flag(false, "", ""));
		this.gameState.addFlag("wire taken", new Flag(false, "Pulling the cushions back, all you find hiding underneath is an old, frayed length of copper wire. ", ""));
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
		case "remove":
		case "move":
			//===============================================================
			//Move rug
			//===============================================================
			if(command.unordered("rug|carpet"))
			{
				if(this.gameState.checkFlipped("rug moved") == false)
				{	
					this.gameState.flipFlag("rug moved");
					return new DisplayData("", "You throw back the rug, hoping for a lost item or trapdoor underneath. "
							+ "Instead, what you find is a safe embedded into the floor. "
							+ "What is something like this doing in a farm house? "
							+ "Such a thing seems out of place here, in these rural surroundings. ");
				}
				else
					return new DisplayData("", "You have already moved the rug. ");
			}
			
			//===============================================================
			//Allow 'move' to continue into the 'go' code, but not the other
			//synonyms.  Return a failure message.
			//===============================================================
			if (command.getVerb().matches("fold|remove"))
				return new DisplayData("", "That doesn't work. ");
			
		case "go": 
			//===============================================================
			//'enter' is a synonym for go.
			//If 'enter' is used for safe, redirect command to code entry.
			//===============================================================
			if (command.unordered("enter", "safe|7852"))
				return this.executeCommand(new Command("enter", command.getSentence()));
			
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
			
		case "grab":
		case "pick":
		case "take":
			//===============================================================
			//Take the paperclip from couch
			//===============================================================
			if (command.unordered("copper wire|copper|wire"))
			{
				if (this.gameState.checkFlipped("couch searched"))
				{
					if (this.gameState.checkFlipped("wire taken") == false)
					{
						this.gameState.addToInventory("Copper Wire");
						this.gameState.flipFlag("wire taken");
						return new DisplayData("", "You take the copper wire. ");
					}
					else
						return new DisplayData("", "You've already taken that. ");
				}
				else
					return new DisplayData("", "You don't see that here. ");
			}			

			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData("", "Can't take that. ");
			
		case "7852":
		case "key":
		case "punch":
		case "input":
		case "press":
		case "put":
		case "enter":
		case "open":
		case "unlock":
			//===============================================================
			//Input the correct code into the safe
			//===============================================================
			if (command.unordered("7852"))
			{
				if (this.gameState.checkFlipped("rug moved") == true)
				{
					if (this.gameState.checkFlipped("power restored") == true)
					{
						if (this.gameState.checkInventory("Note with Number") == true)
						{
							if (this.gameState.checkFlipped("ruby skull obtained") == false)
							{
							this.gameState.addToInventory("Ruby Skull");
							this.gameState.flipFlag("ruby skull obtained");
							return new DisplayData("", "You enter the code, and hear a click. It's unlocked! "
									+ "Pulling the door open, you see a glint of red inside. "
									+ "Looking closely, you see a sparkling red skull, about the size of a fist. "
									+ "It looks like it's been carved from a huge ruby. Wow! "
									+ "This thing must be worth a fortune! "
									+ "The moment you touch it, you feel a shock run through you, and a rush of memory. "
									+ "You suddenly remember your purpose in coming here. This was quite a find. ");
							}
							else
								return new DisplayData("", "The safe is already open. It's empty. ");
						}
						else
							return new DisplayData("", "You think you know the code, but it doesn't work. You're going to need to find it somewhere. ");
					}
					else
						return new DisplayData("", "You try to open it, but it's locked. It has a keypad to enter a code, "
								+ "but it doesn't currently have any power. It's unresponsive. ");
				}
				else
					return new DisplayData("", "You don't see that here. ");
			}
			
			//===============================================================
			//Unsuccessful attempt to open safe
			//===============================================================
			if (command.unordered("safe"))
			{
				if (this.gameState.checkFlipped("rug moved") == true)
				{	
					if (this.gameState.checkFlipped("power restored") == true)
					{
						if (this.gameState.checkFlipped("ruby skull obtained") == false)
							return new DisplayData("", "You try to open it, but it's locked. "
									+ "You'll have to find the correct code to open it. ");
						else
							return new DisplayData("", "The safe is already open. It's empty. ");
					}
					else
						return new DisplayData("", "You try to open it, but it's locked. It has a keypad to enter a code, "
								+ "but it doesn't currently have any power. It's unresponsive. ");
				}
				else
					return new DisplayData("", "You don't see that here. ");
			}

			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData ("", "That doesn't do anything. ");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "A thick layer of dust covers everthing. "
						+ "Multiple tracks from what may be a stray dog are visible in the dust, running between the entryway and the couch. "
						+ "Cobwebs fill the nooks and crannies, also covered in dust. ");
			
			if (command.unordered("table|stand|tv|television"))
				return new DisplayData("", "An old tv stand, it now lies on its side, splintered. Two of its legs are missing. ");
			
			if (command.unordered("couch|sofa"))
			{
				this.gameState.flipFlag("couch searched");
				return new DisplayData("", "It's covered in rips, stuffing coming out and springs exposed. "
						+ "Caked with years of filth, it has an animal smell about it which almost puts you off of examining it. "
						+ this.gameState.getFlag("wire taken").toString());
			}
			
			if (command.ordered("under|beneath", "rug|carpet"))
			{
				if(this.gameState.checkFlipped("rug moved") == false)
				{	
					this.gameState.flipFlag("rug moved");
					return new DisplayData("", "You throw back the rug, hoping for a lost item or trapdoor underneath. "
							+ "Instead, what you find is a safe embedded into the floor. "
							+ "What is something like this doing in a farm house? "
							+ "Such a thing seems out of place here, in these rural surroundings. ");
				}
				else
					return new DisplayData("", "You have already moved the rug. ");
			}
				
			if (command.unordered("rug|floor"))
				return new DisplayData("", "This rug has seen better days. "
						+ "It's so covered in filth and mildew that you can only guess at its original color. ");

			if (this.gameState.checkFlipped("rug moved"))
			{
				if (command.unordered("safe"))
					return new DisplayData("", "It's a small electronic safe, with a keypad for lock code input. It's an old design. "
							+ "It seems to be set directly into the concrete of the foundation. "
							+ "It may be small, but there's no chance you can move this thing. ");
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
