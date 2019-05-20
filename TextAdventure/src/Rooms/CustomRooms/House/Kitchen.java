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

public class Kitchen extends Room implements GasLeak
{
	private static final long serialVersionUID = 1L;

	public Kitchen(GameState gameState) 
	{
		super(gameState);
		this.initializeGasLeak();
	}

	@Override
	protected void setName() 
	{
		this.name = "Kitchen";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.kitchenImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "This small-ish kitchen is typical of a farmhouse. "
				+ "Two of the walls are lined with cabinets. There's a sink with a window over it, which looks out onto the back yard. "
				+ "At one end of the cabinets is an old gas stove. "
				+ "On the opposite side of the room, there's an empty set of shelves build into the wall, to act as a pantry. "
				+ "There's a door that leads outside, to the backyard, and another larger window next to it. "
				+ "There are also open doorways leading to the dining room and the living room. ";

		return this.description;
	}
	
	@Override
	public void initializeGasLeak() 
	{
		this.addRoomToLeakArea(this.gameState, this.getName(), 0);
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Backyard
		//=================================================================================
		this.addMovementDirection("outside", "Backyard");
		this.addMovementDirection("backyard", "Backyard");
		this.addMovementDirection("back yard", "Backyard");
		this.addMovementDirection("north", "Backyard");

		//=================================================================================
		//Create directions that move to Living Room
		//=================================================================================
		this.addMovementDirection("livingroom", "LivingRoom");
		this.addMovementDirection("living room", "LivingRoom");

		//=================================================================================
		//Create directions that move to Dining Room
		//=================================================================================
		this.addMovementDirection("diningroom", "DiningRoom");
		this.addMovementDirection("dining room", "DiningRoom");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create jiffypop
		//=================================================================================
		Item jiffy = new BasicItem(this.gameState, "Popcorn", "", "A disposable pan covered in foil, filled to the brim with unpopped corn kernels. "
				+ "It says 'Jiffy Pop' across the top. ");
		jiffy.setState("(unpopped)");
		this.gameState.addItemSynonyms(jiffy, "jiffypop", "jiffy pop", "popcorn");
		this.gameState.addSpace(jiffy.getName(), jiffy);
	}

	@Override
	protected void createFlags() 
	{
		this.gameState.addFlag("popcorn taken", new Flag(false, "And a pan of stovetop popcorn. ", ""));
		this.gameState.addFlag("pipe fixed", new Flag(false, "", ""));	
		this.gameState.addFlag("gas on", new Flag(false, "", ""));

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
			//If go outside, verify the door is unlocked
			//===============================================================
			if (command.unordered("outside|backyard|back yard|north"))
			{
				if (this.gameState.checkFlipped("back door unlocked") == true)
				{
					if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
						return this.move(command);
				}
				else
					return new DisplayData("", "You try to go outside, but the door is locked. ");
			}

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
			
		case "throw":
		case "open":
		case "unlock":
			//===============================================================
			//Unlock back door
			//===============================================================
			if (command.unordered("door|deadbolt|dead bolt"))
			{
				this.gameState.flipFlag("back door unlocked");
				return new DisplayData("", "You unlock the back door. ");
			}

			//===============================================================
			//Command unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't do that here.");

		case "turn":
		case "twist":
			//===============================================================
			//toggle gas valve
			//===============================================================
			if (command.unordered("valve"))
			{
				if (this.gameState.checkFlipped("gas on") == false)
				{
					this.gameState.flipFlag("gas on");
					
					String description = "You turn on the valve. ";
					if (this.gameState.checkFlipped("pipe fixed") == false)
					{
						this.initializeLeak(gameState);
						description += "You smell a hint of natural gas. ";
					}
					
					return new DisplayData("", description);
				}
				else
				{
					this.gameState.getFlag("gas on").reset();
					this.terminateLeak(gameState);
					return new DisplayData("", "You turn off the valve. ");
				}
			}

			//===============================================================
			//Command unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't do that here. ");

		case "grab":
		case "pick":
		case "take":
			//===============================================================
			//take popcorn
			//===============================================================
			if (command.unordered("jiffy pop|jiffypop|popcorn"))
			{
				if (this.gameState.checkFlipped("popcorn taken") == false)
				{
					this.gameState.addToInventory("Popcorn");
					this.gameState.flipFlag("popcorn taken");
					return new DisplayData("", "You take the popcorn. Maybe you'll get hungry later. ");
				}
				else
					return new DisplayData("", "You've already taken that. ");
			}
			
			//===============================================================
			//Command unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't take that.");

		case "repair":
		case "patch":
		case "fix":
		case "seal":
		case "use":
			//===============================================================
			//use duct tape on pipe
			//===============================================================
			if (command.unordered("duct tape|tape", "pipe|crack"))
			{
				if (this.gameState.checkFlipped("pipe fixed") == false)
				{
					if (this.gameState.checkInventory("Duct Tape") == true)
					{
						this.gameState.flipFlag("pipe fixed");
						this.terminateLeak(gameState);
						return new DisplayData("", "You wrap the duct tape around the pipe, making sure to completely cover the crack with "
								+ "multiple layers. You think that should work. Duct tape fixes everything, after all. ");
					}
					else
						return new DisplayData ("", "You don't have any tape. ");
				}
				else
					return new DisplayData ("", "You have already done that. ");
			}
			
			//===============================================================
			//Case for 'use stove' without popcorn in the command
			//===============================================================
			if (command.unordered("use", "stove|oven") == true &&
				command.unordered("jiffy pop|jiffypop|popcorn") == false)
			{
				if (this.gameState.checkFlipped("gas on"))
				{
					if (this.gameState.checkFlipped("power restored"))
					{
						//===============================================================
						//If there are lethal levels of gas, trigger death
						//===============================================================
						if (this.isLevelLethal(gameState, this.getName()))
							return this.gameState.death("You turn on one of the burners. "
									+ "You have only a second to realize your mistake, before the gas you've been smelling in the air ignites. "
									+ "The room is enveloped in flame, blasting you with hellish fury. "
									+ "Thankfully, the pain only lasts a moment... ");
						
						return new DisplayData("", "You turn a few dials, checking the oven out. It works. ");
					}
					else
						return new DisplayData("", "You try to turn on the stove, but it doesn't seem to have any power. "
								+ "The igniter isn't working. ");
				}
				else
					return new DisplayData("", "You try to turn on the stove, but it doesn't seem to have any gas. ");
			}

			//===============================================================
			//Command unrecognized, return a failure message.  Excludes the
			//verb 'use', allowing it to pass into the next set of verbs.
			//===============================================================
			if (command.getVerb().matches("repair|patch|fix|seal"))
				return new DisplayData ("", "Can't do that here. ");

		case "pop":
		case "cook":	
			//===============================================================
			//cook the jiffy pop
			//===============================================================
			if (command.unordered("jiffy pop|jiffypop|popcorn"))
			{		
				if (this.gameState.checkInventory("Popcorn") == true)
				{	
					if (this.gameState.checkFlipped("gas on") == true)
					{
						if (this.gameState.checkFlipped("power restored") == true)
						{
							if (((Item) this.gameState.getSpace("Popcorn")).getState().contains("unpopped"))
							{
								//===============================================================
								//If there are lethal levels of gas, trigger death
								//===============================================================
								if (this.isLevelLethal(gameState, this.getName()))
									return this.gameState.death("You turn on one of the burners. "
											+ "You have only a second to realize your mistake, before the gas you've been smelling in the air ignites. "
											+ "The room is enveloped in flame, blasting you with hellish fury. "
											+ "Thankfully, the pain only lasts a moment... ");

								//===============================================================
								//Else, cook the popcorn
								//===============================================================
								((Item) this.gameState.getSpace("Popcorn")).setState("(popped)");
								return new DisplayData("", "You turn on one of the burners, and set the popcorn pan on it. "
										+ "A few minutes later, you have some succulent smelling popcorn! ");
							}
							else
								return new DisplayData("", "The popcorn has already been popped. ");
						}
						else
							return new DisplayData("", "You try to turn on one of the burners, but it doesn't have any power. "
									+ "The igniter isn't working. ");
					}
					else
						return new DisplayData("", "You try to turn on one of the burners, but it doesn't have any gas. ");
				}
				else
					return new DisplayData("", "You don't have any popcorn. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Nothing happens. ");

		case "search":  //synonyms
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room|kitchen") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "Even aside from the filthy state it's in, this kitchen isn't great. "
						+ "It's severely lacking in modern features. "
						+ "No hot water for the sink, no outlets above the counters for appliances, "
						+ "no refrigerator, though you do see a spot where one likely used to be. ");

			if (command.unordered("line|pipe|valve"))
				return new DisplayData("", "An old copper gas line, with a valve. It looks like it's shut off right now. "
						+ "The copper line has a large crack in it. ");

			if (command.unordered("sink|faucet"))
				return new DisplayData("", "The sink is an old porcelain piece, still in fair condition underneath the filth. "
						+ "It has an old timey faucet with a single knob. You try turning it, but nothing comes out. ");

			if (command.unordered("window|backyard|back yard"))
				return new DisplayData("", "The window is filthy. You are barely able to make out the back yard through it. "
						+ "You see a barn, a shed, and a cornfield. ");

			if (command.unordered("oven|stove"))
				return new DisplayData("", "An old, natural gas model, it's covered in dust, grime and rust. "
						+ "It's pulled half away from the wall for some reason. "
						+ "Looking behind it, you see a copper gas line. The valve appears to be shut off. ");

			if (command.unordered("pantry"))
				return new DisplayData("", "It's a set of shelves built into the wall. Nothing but dust and dead bugs there. ");

			if (command.unordered("door"))
			{
				String description = "Unlike the front door, this back door still looks sturdy, and closes properly. ";
				if (this.gameState.checkFlipped("back door unlocked") == false)
					description += "The deadbolt is thrown, locking it. ";
				
				return new DisplayData("", description);
			}

			if (command.unordered("cabinets|cabinet"))
				return new DisplayData("", "They're old wooden cabinets, covered with peeling paint and cobwebs. "
						+ "Looking through them, you find a lot of dead bugs, mouse poop, and dust. "
						+ this.gameState.getFlag("popcorn taken").toString());

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
	private final String kitchenImage = "mmddhdhddhhso+////////////+++ydmmmdddhyoooooo+++++++++++++oy+//////////////////////////////////////////+/+/+ssyhdddddddddmddmmddmdyyyyhhyyhddysssssssyyyyyyys\r\n" + 
			"NNNNNmmdhydhmdyys+////////+++dmmmmmmmmdmmmmmmmmdmdhhhhhhhhhddyyyyyyyyyyyyyyysysyssssssssssyssssssyyyyssssssyhdmshhhhhdddyyyyyyyyyhdddddmmmmmmmmmmmmmmNNNdhhyy\r\n" + 
			"mmNNNNNNNNmmddhyyyhyys+///+++dmddddddmhNNNNNNNmhmdhdmmdmmmyhhmmmhmmmyhydmmhmmdsyydddhddhsyyhhdyhdhyyyyyyyyyyymhohhhdhdmhohddhdmdossohhhddmmmNNNmdNMMMMNNdmdhy\r\n" + 
			"NNmmmmmmNNNNNNmmdddddyhyyyso+dmdddddddhmNNNmmmmmmdhdddhdmmyhhdmmhmmdyyydddhdddsyshhhyhhysysyyhsyyhssyysysssysmyohyhhhhdyohhdydmdooyyyhhdddmmmmmNNNMMNNNNhmdhy\r\n" + 
			"mmmmmmmmmmmmmNNNNNNmmdddhhyyhmmdddddhdhmNNmmmmmmmdhdhhyyddyhhdddhdddyyydddyhhhsyshhhyyyyossyyhsyyysssysysssysmhohhhhhhdyohhhyhhh+oyyyyhhdddmmmmmNNMMNNNmymdhy\r\n" + 
			"mmddhmNNNNNmmdddmmmNmmmNNmmmdddmmmddhdhmNNmmmmmmmdsshyyymmyhhdmmhmmdyyydddyddhosohyhsyyyosoyyysyyyooossysssyomy+hhhhhddyohhhshhh++yyyyhhhdddmmmNNNMMNNNmsdhyo\r\n" + 
			"mdhdhdmddNmhdd+/syhdddmmddddddmmmmmmhdhmNNNNNdNddyshhmhmmmyhydmmymmdsyshdhyhhhosoyyyosss+o/++o+oos+oossyssyyomy+hhyhhhds/+yy++sy/+yyhhddhdmmmmNNNMMMMNmdsyyo+\r\n" + 
			"ddhhyddddMdhdh+::::::+hdhhdmmmmmmdddhdhmNmmmdhydsyyhddhdmmyhydddydddsyshhysyyyoo+ss-/.-/:/:---::-/:+oo+o++osods+sssyyyho/oo+//ss:oyysssyyyyhhddddmmmNNmy+so+o\r\n" + 
			"ddhhyddhdNdyhh/:---:::yhh+//sddmdhhhhdhdNmmdyyyhddydddhdmmyyyhddydhhsysyyysssyoo/s+./``::+:..://:+/+o++++++sody+ssssssyo/+++/oss:dmy///++++ooyddddhhhyo+++oo+\r\n" + 
			"ddhhyddhdmdhhh:------:yh+::/+ddddddddmdmNmmddhddmdyhmmhdmmhhhhddshhhsysyyysysyyyos+-+-:o/+/-..//:osss++++++ssdy+yssyssys+++o+sys/:---:::///+++ooosssyy+++++++\r\n" + 
			"dhhhyddhdNhhhh:------:so::::/hdmmmmmmmdmmmmmmmmmmmhdddddddhhyhhhhhhhsysyyyyyyysssso+o+ss+o+++++oooosssossssyymd+ysssssssoo+o+++o/:-----:::////+oosooysooooooo\r\n" + 
			"dhhhyhdydNhyhy:----------::::/sddNNNNNmmmmmmmmmmmmmmmmmmmmmmmmddddddddddhhhhhhyyysssyyhhhyyyyysysyyyyyyyyyyyyyyyyysssoosssyhdNNo++/::////++ooo++osssso+++////\r\n" + 
			"dhhhyddydNhyhy:---------:::::/hmdmmmmmmmmmmmmmmmmmmmddddddddddddddhhhhhhhhhhhyyyyyyyyhhhhyyyyyyyyyyyyyyyyyyyyyyyyysssssssyyhmNNdss+os++++oooo+:::+///////////\r\n" + 
			"dhhhyddydmhyhy--------:::::::/dmddmmdmmmmdddddddddddddddddddddhddhdhhhhhhhhhhhhhhhhhhhhhhyyyyyyyyyyyyyysyyyyyyyyysssssssssyhhhdyoooos/+++o+oo+:::////////////\r\n" + 
			"hhhhyyyyhdhyhy-------.--------:/+//:/ysss+---:::--/----:---/o+++/.-.-....-......--:-...-...--.`..```.`..........`..``........````````````----:-::////////////\r\n" + 
			"ddmmdhhyhdhyhy-------..--------:/---:yooo/..---...:....-..-oms+/:..`.````````````````````````````   ``````````    `````   `````````````..-------://///:::::/:\r\n" + 
			"hhhyyhhyhdhyys------..-----------...-:----...------------:sds-.-.```````````````````````````    `   ` `  `   `    `...    ````````````...-------:///:::::::::\r\n" + 
			"hhhyyyhyhdhyys------..-..................................-:-.``````````````````````       ``    `                          ````````````..--------::::::::::::\r\n" + 
			"hhhyyyhyhdhyys---...........................................``````````````   ```````   ` ````   `                         `````````````..--------/:::::::::::\r\n" + 
			"dhhyyhdyhdhyys-......`.........````......................``````````````` `    `````    `````                               ````````````....------::::::::::::\r\n" + 
			"ddhyyhdyhdhyys.......`.........````.............:+.../-...`.``......`---.`    `````    `````    `   `                    ```````````.`...--------::::::::::::\r\n" + 
			"ddhyyhdhdmhyyo.......`........``````............----.................---.`   ````````````````   `   `                   ````````````.`...-.------:::::::::///\r\n" + 
			"hdhyyhdydmhyho.......`.........`................---:.........-:y----.shs:.``````````.-.`.``..`````````````````````...........-.```.....------:---/::::::::/::\r\n" + 
			"dhhyhddymNhhho......``....-/+//+ossoo++++yyhysyhhhhhhhhhhyo+++hhsossssyss++++oooooo+++os++os////++/::::::::::::///::---:::::::///::..-:--::::+/:/++++o++//:::\r\n" + 
			"mdhyhhdyddhyyo.....```:-::/++/////::::::::::::::::::::::::::://::::::::::::-.--------..............`.````....----```` `````````   `` `------://///+//osssso//\r\n" + 
			"ddhyhhhhhhhhy+.``````.-------..................`...````.`````````..```````````````.```````````````````````````````` ```               .-----:::::////::///:::\r\n" + 
			"hddhhhhhhhhhs/```````.-------/+/+//+++++///////////+//++/+++++++++++++++++++++++++++++++++++//////////////////////+///////////.``````.------:::::////::::::::\r\n" + 
			"hhhhhhso/:-```````````--::---shhyyyyyyssssssssssssyysyyssssssssssssssssyoyyssyysssyssssssssssosooososssosossyssyssssssssyyyyys.``````.-----::::://///::::::::\r\n" + 
			"so/:..````````````````-------shsossss+/////////////+/os/++////////++++++++/:/y+-./ooo++++/////////////+++++ymysyo+sssymhhhddys.``````.-----::::://///::::::::\r\n" + 
			"``````````````````````-------sds/+ooo+///:::://:////:os+/+/::::///////++o+:-:yo``-+++////////////////////++ymdyso+ssshmdhyyhys.``````----::::::://////:::::::\r\n" + 
			"``````````````````````-------sd+:/++yh+:::::::::/:://os////:::::::://+++oo:..y/``.+/////::/:::://////////++smyyyo+++++soossyys```````----::::::////////::::::\r\n" + 
			"``````````````````````.--.---yd+:+yhy+::::::///::::::os////::::-:::/+++++o/..y:``./::::::::::::::::::://///ymhsso+ssshmhyyhdhy```````---:::://////+/////::::/\r\n" + 
			"``````````````````````.------ydsyy+:/:::::::://::///:os////:::-:::///++++s/..y:```/:::::::::::::::::::////+ymhhoo+ooohmdhyyhhy```````-::::://////+++++yo////:\r\n" + 
			"```````````````````` `.------hdo:--::--::::::///+o+/:os////:::::////+++++o:..y:```/:::::::::::::::::://///+smhsyoo++osssossyhy.``.```::////////+++o+++o+/////\r\n" + 
			"``````````````````````.---..-hd/----:-:---:::/:oossssss+++/::::////+++oooo:..y:```/:::::::::::::://////////smdyhoosssdmdyyhdhh.......:///+++++++ooooooo+/////\r\n" + 
			"``````````````````````..--.--hd/---:/--:-:::///oyyyhysshso+/+////+sdmmmmmm/..y:```///:/+++++//////++///++++ymyyyooosshdddhyhhs.......osyyyyysyhyshhssoo++++//\r\n" + 
			"````````    ``````````..-.---hd/---:++oosoyyysoosyhhdyshdddddmdddddsddmmmN+..y/``.sssyhddmddhyyyyyyyssssyyydNmmmssyyyyyysoosss.......syyyyyyyyyyshhyhyysoo+++\r\n" + 
			"```````` `````````````.------hd+--:sdhyosssho/:+ys:+yyymmddhyhdmNNNmmmmddmo.:y+`.-y+++/ssyosdhhddhhhshdddddmmmmmysNNNNmmhyyhyy.......//++/++ooossyyyhyhhyyso+\r\n" + 
			"`   ```  `````````` ``------/dm+:+hhyshddhhs///+ss+osysddhhyyhhhddddddddddy//h+---o:/+/+:/ososysyso/osyyyyyyhhddyyddydyyyyhddh.......:::////+++++ossyyyyyyhys\r\n" + 
			"        ```````````` `------:hdh/--....:/:--://oyyyyyyyyyyyyyyyyyyyssssssyyysyyyyyssssssssssssssssooooosssssssssssoo+++oossydm--....---://///+++oooossyyhhyyh\r\n" + 
			"        ``````````````-::::-:ddy...--.-ssyy-..-syyhhsssoyyyo//+soyyyshdyyyyhysoyssooyyo+//ooo++/+sssysooossy+sssssyossyysossdm////////oo+++ossyyyyyydyhhyhhhh\r\n" + 
			"    ````      ``````-:++////+ss+///+++/++oo//+syyshyyyossysy++oos++sssoo++ooo+yo::+ssoo+:::/://+:o+::+:/+oss:++oso/oo/::/+ooys//-----:://+//sssshddddhhyyyyhh\r\n" + 
			"  ```        ````-:+++/++//+/+//++++o+oooooo++sosyso+++o//++//++so+/+oyy+o+sy+/+:/ss+::::///++:::/://::::---//:::/:://+/:/+++:-:.../:--/++//++ssssyyhoosssyhh\r\n" + 
			" `   `      ```-oh+++oooo+/////+++oso++++++++++++oo++oys+:++//////////++o/osdhhyo+//::+/-------:/:/:.-:```...------.--:////////:-...--:-:/+/+ossooshhyhhdyyys\r\n" + 
			"   ` `   ````.:++++/:///:-:::/so+++//++////+/++/://///+/+++///::////////+oossos+:-:+o+//+/-/-.----:-`.-..`--.--........---:::::---::.---.....-::://sosos+///+";
}
