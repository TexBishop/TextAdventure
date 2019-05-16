/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms.House;

import Rooms.CountdownRoom;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;
import Items.BasicItem;
import Items.Item;

public class Kitchen extends CountdownRoom 
{

	private static final long serialVersionUID = 1L;

	public Kitchen(GameState gameState) 
	{
		super(gameState);
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
		this.description = "You step into the kitchen. Like many kitchens, cabinets line the walls above the counter, "
				+ "stove, and oven. Stains indicating water damage mottle the cabinet doors. Below a window showing a "
				+ "view to the backyard, there is a sink. ";

		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Backyard
		//=================================================================================
		this.addMovementDirection("outside", "Backyard");
		this.addMovementDirection("porch", "Backyard");
		this.addMovementDirection("backyard", "Backyard");

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
		Item jiffy = new BasicItem(this.gameState, "Jiffy Pop", "", "A disposable pan covered in foil, filled to the brim with unpopped corn kernels. ");
		this.gameState.addItemSynonyms(jiffy, "jiffypop", "jiffy pop", "pan");
		this.gameState.addSpace(jiffy.getName(), jiffy);

		//=================================================================================
		//Create cooked pop
		//=================================================================================
		Item cookedpop = new BasicItem(this.gameState, "Cooked Popcorn", "", "Delicious, fluffy popcorn. ");
		this.gameState.addItemSynonyms(cookedpop, "cooked", "popcorn");
		this.gameState.addSpace(cookedpop.getName(), cookedpop);
	}

	@Override
	protected void createFlags() 
	{
		this.gameState.addFlag("jiffypop taken", new Flag(false, "", "JiffyPop taken."));
		this.gameState.addFlag("valve turned", new Flag(false, "", "Valve turned."));
		this.gameState.addFlag("flextape installed", new Flag(false, "", "FlexTape installed."));
		this.gameState.addFlag("gas connected", new Flag(false, "", "Gas connected."));
		this.gameState.addFlag("stove on", new Flag(false, "", "Stove on."));
		this.gameState.addFlag("cooked popcorn", new Flag(false, "", "Cooked popcorn."));
		this.gameState.addFlag("gas released", new Flag(false, "", "Gas released."));
		this.gameState.addFlag("gas contained", new Flag(false, "", "Gas contained."));
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

		case "turn": //synonyms
		case "twist":
			//===============================================================
			//turn gas valve // calling this while the countdown is triggered turns the countdown off
			//===============================================================
			if (command.unordered("valve"))
			{
				if (this.gameState.checkFlipped("flextape installed") == true)
				{
					this.gameState.flipFlag("gas connected");
					return new DisplayData ("", "You turn the gas valve. With your repairs to the pipe, gas is flowing smoothly "
							+ "to the stove.");
				}
				else if(this.gameState.checkFlipped("gas released") == false)
				{
					this.gameState.flipFlag("gas released");
					return new DisplayData ("", "As you turn the gas valve, you hear a hiss as you get blasted with the smell "
							+ "of rotten eggs. If things stay as they are, you don't have much time before this turns into a "
							+ "dangerous situation.");
				}
				else
				{
					this.gameState.flipFlag("gas contained");
					return new DisplayData ("", "You quickly turn the valve back to where it was, turning off the gas flow. You are safe. ");
				}
			}

			//===============================================================
			//turn on stove
			//===============================================================
			if (command.unordered("stove|knobs|knob"))
			{
				if (this.gameState.checkFlipped("gas connected") == true)
				{
					if (this.gameState.checkFlipped("power restored") == true)
					{
						this.gameState.flipFlag("stove on");
						return new DisplayData ("", "Gas hisses from the stove and is ignited. ");
					}
					else
						return new DisplayData ("", "Without electricity to spark the gas, gas hisses from the stove. You quickly turn the stove off.");
				}
				else
					return new DisplayData ("", "Nothing happens. The gas appears to be disconnected.");
			}

			//===============================================================
			//Oven is broken, return a failure message.
			//===============================================================
			if (command.unordered("oven"))
				return new DisplayData ("", "Nothing happens, the oven is broken.");

			//===============================================================
			//Command unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't do that here.");

		case "grab":
		case "pick":
		case "take":
			//===============================================================
			//take jiffypop
			//===============================================================
			if (command.unordered("jiffy pop|jiffypop|popcorn"))
			{
				if (this.gameState.checkFlipped("jiffypop taken") == false)
				{
					this.gameState.addToInventory("Jiffy Pop");
					this.gameState.flipFlag("jiffypop taken");
					return new DisplayData("", "Jiffy Pop taken.");
				}
				else
					return new DisplayData("", "You've already taken that.");
			}
			
			//===============================================================
			//Command unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't take that.");

		case "repair":
		case "patch":
		case "use":
			//===============================================================
			//use flextape on pipe
			//===============================================================
			if (command.unordered("flextape|tape", "pipe|crack"))
			{
				if (this.gameState.checkFlipped("flextape installed") == false)
				{
					if (this.gameState.checkInventory("FlexTape") == true)
					{
						this.gameState.removeFromInventory("FlexTape");
						this.gameState.flipFlag("flextape installed");
						return new DisplayData("", "You wrap the FlexTape over the crack on the pipe. It looks airtight.");
					}
					else
						return new DisplayData ("", "You don't have that item.");
				}
				else
					return new DisplayData ("", "You have already done that.");
			}

			//===============================================================
			//Oven is broken, return a failure message.
			//===============================================================
			if (command.unordered("oven"))
				return new DisplayData ("", "Nothing happens, the oven is broken.");

			//===============================================================
			//Command unrecognized, return a failure message.  Excludes the
			//verb 'use', allowing it to pass into the next set of verbs.
			//===============================================================
			if (command.getVerb().matches("repair|patch"))
				return new DisplayData ("", "That doesn't work.");

		case "pop":
		case "cook":	
			//===============================================================
			//cook the jiffy pop
			//===============================================================
			if (command.unordered("jiffy pop|jiffypop|popcorn"))
			{		
				if (this.gameState.checkFlipped("cooked popcorn") == false)
				{	
					if (this.gameState.checkInventory("Jiffy Pop") == true)
					{	
						if (this.gameState.checkFlipped("stove on") == true)
						{
							this.gameState.removeFromInventory("jiffy pop");
							this.gameState.addToInventory("Cooked Popcorn");
							this.gameState.flipFlag("cooked popcorn");
							return new DisplayData("", "Your pan of jiffy pop has transformed into a lovely pan of cooked popcorn!");
						}
						else
							return new DisplayData("", "The stove is not on.");
					}
					else
						return new DisplayData("", "You don't have that item in your inventory.");
				}
				else
					return new DisplayData("", "You have already done that.");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't do that here.");

		case "search":  //synonyms
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room|kitchen") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "You notice a pipe jutting from the wall leading behind the stove and oven. "
						+ "You notice a small crack on the pipe, just below a turnable valve.");

			if (command.unordered("pipe|valve"))
				return new DisplayData("", "This gas line pipe has a crack in it. It would be best to leave that valve "
						+ "well alone, as it is now.");

			if (command.unordered("sink|faucet"))
				return new DisplayData("", "This steel sink has long since rusted through.");

			if (command.unordered("window|backyard"))
				return new DisplayData("", "Through the window above the sink, you can see a view of the "
						+ "backyard, and the buildings and farmland beyond. ");

			if (command.unordered("oven"))
				return new DisplayData("", "An old gas oven. It looks like it is broken.");

			if (command.unordered("stove|stovetop"))
				return new DisplayData("", "A gas stove. There are knobs below it to control the gas flow. ");

			//===============================================================
			//description of cabinet with/without jiffy pop
			//===============================================================
			if (command.unordered("cabinet|cabinets"))
			{	String description = "Cabinets are littered with clutter and dead bugs.";
			if(this.gameState.checkFlipped("jiffypop taken") == false)
				description += " Except in one of them, you find a jiffy pop. How long has that been there?";
			return new DisplayData("", description);
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
	//=================================================================================
	//Plan to have two death conditions. One from turning the valve without flextape,
	//and another from turning the stove on(once the gas is connected) without the power.
	//=================================================================================

	protected void initializeCountdown() 
	{
		//=================================================================================
		//Death after third command
		//=================================================================================
		this.setCountdown(2, "It reaaaally smells like eggs...", 
				"With a fire sparking from the dining room candles, an explosion blows the walls out "
						+ "of the building, and you.");

		//=================================================================================
		//Count down starts and progresses unless the brooch is in inventory
		//=================================================================================

		this.setTriggers(this.gameState.getFlag("gas released"), this.gameState.getFlag("gas contained"));
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
