package Rooms;

import Items.BasicItem;
import Items.Item;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;
import Structure.MultiFlag;

public class OldFarmhouse extends Room
{
	private static final long serialVersionUID = 1L;

	public OldFarmhouse(GameState gameState) 
	{
		super(gameState);

		//=================================================================================
		//Don't forget to set the room name and add it to the Space hashmap, or there
		//will be copious errors.
		//=================================================================================
		this.name = "Old Farmhouse";	
		gameState.addSpace(this.name, this);
		
		createMovementDirections();
		createItems();
		createFlags();
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData(this.whiteHouseImage, this.fullDescription());
	}
	
	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//When creating a movement direction, the first string is the keyword to recognize
		//in a typed command, and the second string is the key name of the room to move to.
		//Multiple movement directions can be made for the same room, to create multiple
		//command options to move to that room.  After creating the movement directions for
		//a room / path, check to see if the target room has been instantiated and added
		//to the Space hashmap.  If it has not, instantiate it.
		//=================================================================================

		//=================================================================================
		//Create directions that move to Farmhouse Porch
		//=================================================================================
		this.addMovementDirection("north", "Farmhouse Porch");
		this.addMovementDirection("porch", "Farmhouse Porch");
		this.addMovementDirection("house", "Farmhouse Porch");
		this.addMovementDirection("farmhouse", "Farmhouse Porch");
		
		if (this.gameState.checkSpace("Farmhouse Porch") == false)
			new FarmhousePorch(this.gameState);	
	}
	
	@Override
	protected void createItems() 
	{
		//=================================================================================
		//When creating items, in addition to instantiating the object, you also need to
		//set the synonyms for the item, which are words that it can be recognized by in
		//typed commands.  Once that is done, add the item to the Space hashmap.
		//=================================================================================
		
		//=================================================================================
		//Create welcome flyer
		//=================================================================================
		Item welcomeFlyer = new BasicItem(this.gameState, "Welcome Flyer", "", "It's a flyer.  It says, \"Welcome to our game! "
				+ "Term Project for: Tex Bishop, Jacob Holzmann, Ian Wickham\"");
		this.gameState.addItemSearch(welcomeFlyer.getName(), "flyer", "welcome");
		this.gameState.addSpace(welcomeFlyer.getName(), welcomeFlyer);

		//=================================================================================
		//Create string
		//=================================================================================
		Item balloonString = new BasicItem(this.gameState, "String", "", "An old string that used to hold some balloons to the mailbox.  "
				+ "It is a few feet long.");
		this.gameState.addItemSearch(balloonString.getName(), "string");
		this.gameState.addSpace(balloonString.getName(), balloonString);
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
		this.gameState.addFlag("mailbox open", new Flag(false, "", ""));
		this.gameState.addFlag("flyer taken", new Flag(false, "", ""));
		this.gameState.addFlag("string taken", new Flag(false, "There is a piece of string tied around the post of the mailbox.  "
				+ "It looks like a leftover from some long dead balloons. ", ""));
		
		//=================================================================================
		//When creating a MultiFlag, the Flags that the MultiFlag tracks need to be added
		//to it.  On a toString(), it will only return the true statement if all tracked
		//Flags have been flipped.
		//=================================================================================
		MultiFlag mailboxEmpty = new MultiFlag(this.gameState, false, "Inside the mailbox is a flyer. ", "The mailbox appears to be empty. ");
		mailboxEmpty.addFlag("mailbox open");
		mailboxEmpty.addFlag("flyer taken");
		this.gameState.addFlag("mailbox empty", mailboxEmpty);
	}
	
	@Override
	public String fullDescription() 
	{
		//=================================================================================
		//This is used to return the main room description.  If any flags alter the main
		//room description, that will be handled here.
		//=================================================================================
		this.description = "An old, dilapidated white farmhouse stands in front of you.  It "
				+ "appears to be in poor repair, and has an abandoned feel about it.  A well worn path "
				+ "heading north leads to the front porch.  The old farm road heads off to the east and west "
				+ "from here.  There's an old, rusty mailbox next to you. ";
		
		if (this.gameState.checkFlipped("mailbox open"))
			this.description += this.gameState.getFlag("mailbox empty").toString();
		
		return this.description;
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
			//Else, move room and return new room DisplayData.
			//If the subject of the command is unrecognized, return
			//a failure message.
			//===============================================================
			if (command.getSubject().contentEquals("back"))
				return this.displayOnEntry();
			if (this.checkMovementDirection(command.getSubject()) == true)
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));
			return new DisplayData("", "Can't go that direction.");
			
		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();
			
		case "open":
			//===============================================================
			//Open the mailbox. Flip the mailbox flag. Return success message.
			//If the subject is not recognized, return a failure message.
			//===============================================================
			if (command.getSubject().contentEquals("mailbox") || command.getSubject().contentEquals("mail"))
			{
				if (this.gameState.checkFlipped("mailbox open") == false)
				{
					this.gameState.getFlag("mailbox open").flipToggle();
					return new DisplayData("", "You opened the mailbox.  Inside is a flyer.");
				}
				else
					return new DisplayData("", "Mailbox already open.");
			}
			return new DisplayData("", "Can't open that.");
			
		case "take":
			//===============================================================
			//Take the flyer.  Only possible if the mailbox open flag has
			//been flipped.
			//===============================================================
			if (command.getSubject().contentEquals("flyer"))
			{
				if (this.gameState.checkFlipped("mailbox open") == true)
				{
					if (this.gameState.checkFlipped("flyer taken") == false)
					{
						this.gameState.addToInventory("Welcome Flyer");
						this.gameState.flipFlag("flyer taken");
						return new DisplayData("", "Flyer taken.");
					}
					else
						return new DisplayData("", "You've already taken that.");
				}
				else
					return new DisplayData("", "You don't see that here.");
			}
			//===============================================================
			//Take the balloon string.  Can't be done.
			//===============================================================
			if (command.getSubject().contentEquals("string"))
				return new DisplayData ("", "The old string is knotted tightly in place, and you can't get it loose.");
			
			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData("", "Can't take that.");
			
		case "use":
			//===============================================================
			//Use the shard of glass on the string.  Make sure the string
			//hasn't already been taken when doing so.
			//===============================================================
			if (command.getSubject().contentEquals("shard") || command.getSubject().contentEquals("glass"))
			{
				if (this.gameState.checkInventory("Shard of Glass") == true)
				{
					if (command.getTarget().contentEquals("string"))
					{
						if (this.gameState.checkFlipped("string taken") == false)
						{
							this.gameState.addToInventory("String");
							this.gameState.flipFlag("string taken");
							return new DisplayData("", "You cut the string free of the mailbox.  It might be useful.");
						}
						else
							return new DisplayData ("", "You've already done that.");
					}
					else
						return new DisplayData ("", "That doesn't work.");
				}
				else
					return new DisplayData ("", "You don't have that item.");
			}
			
			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData ("", "Can't do that here.");
			
		case "search":  //doing this will cause search to execute the go code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "The path leading north to the farmhouse is well worn, but it is slowly disappearing beneath "
						+ "the tall grass and weeds.  The mailbox is nearly overgrown itself.  The old farm road looks deserted in "
						+ "both directions, no traffic visible.");
			
			if (command.getSubject().contentEquals("house") || command.getSubject().contentEquals("farmhouse"))
				return new DisplayData("", "The abandoned farmhouse has a hollow, abandoned atmosphere about it.  You wonder what "
						+ "may be left inside.");

			//===============================================================
			//If look at mailbox, adjust description based on the "mailbox open",
			//"mailbox empty", and "string taken" flags.
			//===============================================================
			if (command.getSubject().contentEquals("mailbox") || command.getSubject().contentEquals("mail"))
			{
				String mailboxDescription = "This mailbox has seen better days.  It's rusty and leaning, and the weeds "
						+ "around its base are tall and dense. ";
				mailboxDescription += this.gameState.getFlag("string taken").toString();
				if (this.gameState.checkFlipped("mailbox open"))
					mailboxDescription += this.gameState.getFlag("mailbox empty").toString();
				
				return new DisplayData(mailboxImage, mailboxDescription);
			}

			//===============================================================
			//If look at flyer, make sure it is visible, then set it as
			//the innerSpace.
			//===============================================================
			if (command.getSubject().contentEquals("flyer"))
			{
				if (this.gameState.checkFlipped("mailbox open") == true && this.gameState.checkFlipped("flyer taken") == false)
					return this.setInnerSpace("Welcome Flyer");
			}

			//===============================================================
			//If look at string, make sure it is visible, then set it as
			//the innerSpace.
			//===============================================================
			if (command.getSubject().contentEquals("string"))
			{
				if (this.gameState.checkFlipped("string taken") == false)
					return this.setInnerSpace("String");
			}

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
	private final String whiteHouseImage = "......---------------............````````````                 ````````````````````````````             ``.`` `` .`.``   `.-+syyyosyo+yhhyosyhhmhyyyhhyyhyhhhh\r\n" + 
			"..........--------.................``````````````           ``````````````````````````````````      ```:++--`./-:o:-+`````-:syhsssysoso+/syydddhdddhddhhhddhy\r\n" + 
			"....................................``````````````````````` `````````````````````````````````     ``.-:-+ss+:+s+:+so:.:```-oyhhyhyhhyyooosyyyddhdhdddyyhdhhhy\r\n" + 
			"````````  `````````````` ````````...```````````````````````````````````````````````````````        `-oys+sdy/syhsshyo+/:.:+sssyyhhyhhyysysydhhhdhhhhhhhhdyhhd\r\n" + 
			"``````       ````````         `````````````````````````````````````````````````````````   ````    `.:+yyyshhhhhhshhh+o-::oosyyyyyhhhhyhyhhdmdhyhyyyhddhhhhhyh\r\n" + 
			"`               `````           `````````  ``````````````````````````````````````````   `.::/:.``--/ooyyyhhdhhdhhhdysss+oyoyhyyhhhhhhyyyhhhdhhhhhhyyhhhhhdhhm\r\n" + 
			"`                                `````````` ````````````````````````````````````--``  `.-/oso+-//syyoysoyhdddhhhhyhys++so/oshhhhhddhdyyydddhhhydddddhdhyhmhdm\r\n" + 
			"```       `         ```````` ` `````````````` ```````````````````````````````.``/y+`..`-/+oyyyhhhhyyyssyyhdddddhhydddshysysyhhhhyhhhhhdhhhdhdhshddhdmhddhhymN\r\n" + 
			"`````   `````         `````````` ```````````````````````````````````.:///.`./o+/+yho/s//ssyyyyhhhyhhyhyhhhddhhyddyhdhyhyhsosyhyhdhoohydddhhhdddhdhddhdddyhydh\r\n" + 
			"``````   ``````    ```   ````````````````````````````````:-```` ``::+oyhy//:o+:--:-:+yyyyyyyhyhddhyhyyyhhhyhhhdhhhhdddddyyyhyyhhysyyhhdhdhdhhyoydyhhydmNdmddh\r\n" + 
			"```...```` ```````````   ````````````````````````````````..``````:/oso+o/:..`    `://+yhyyyhdhhyyyhsosssyyyhyyhdmmmdddmhhhssssshhyyyhyyyyhdhyyhhhddhhhdhdmmmm\r\n" + 
			"`````````.```````````` `.````````````````````````````````.```..-.-..`           -/:/:/+yhyshhhhhdddhhhyyhdhhhhddmdhhdmdyhyyyyyysyhhhdhhyhddshdddhhhdhddhhmmNd\r\n" + 
			"```````````````````````...`````.``.......````...`````````.`                   .::://:///yhhyyhhdhdddhhhddmdmddhdsyhdddddhsyyyyyyyyhhhhhhddddhyhmmmdhddmmydmmd\r\n" + 
			"``````````````````````.---.```````````````..``........`                     `::://///:::/ohyhhyhyhhhyyyyyhddhdhddddmmmmmdyyyyyyyysyhyyhddhddyhyyhdshhsymdmmNm\r\n" + 
			"````````````````.......-::-.```````````````````````````                    -/:::::::::::::ohyohhhhyhyyyhyyhhhhddddhhhddyyysyhhhyydyhyhyshdyddhddhdh+s-ymmmmNm\r\n" + 
			"```````````..```-::-..-::::-..````````````````````````                   ./::::::::::::::::+syyyyhhhyyyhhddhhdmdddddmdhhhdhdyyhhhyhddddhhmddmmdhhmmmdosmhdmmm\r\n" + 
			"````.....-+s+::+sss/.-::::-.-..``````````````````````                  `::::::::::::::::::::/shyyhdddyydmddhdmNmmddddmNmddhyyydyyhhmdhohddNmNdhydhdhmNmmddydm\r\n" + 
			"//++ssso+sysysssyy/.-::::-`..-..````````````````````                 `-/:::::::::::::::::::://shhddhhsyhyhhhhdmmNNmhhhyossyyssyysyhhyyyhhhmdmNmddmmmNNmmdddmm\r\n" + 
			"yssyhyysossyyysyy+---:::-`````-..```````````````````                ./::::::::::::::::::::::://oydhshhdyhdmmmmmmmmhhyymyysyysssyyyhyyyhhhymmdmmdmmmNmmNNmdddh\r\n" + 
			"yhhyyhyysyhhysyyo-.-:::++:sso.....`````````````````               `:::::::::::+mddsshyy/:::::://ohhdhyddhdmmmmmhdmdssoydyyhhhshhhohyyhhhddmddmmhyhmmNNmdddddh\r\n" + 
			"yhyhysyyyhhyyyys-.-:::/dd+dds.....-.```````````````          ````./:::::::::::+ddmyymmm/::::::://+hddddddhhddmdmmdmdsyysyydmmhmmmyysshhhdyyohhhdmmdmNddhdhhhd\r\n" + 
			"hhhyyyhhhhyyyyy-.-:::-:hy/ys+.```.................................-:::::::::::/yyhssdyd/::/:/:::///syhhhyddhhhmmmmmNmddhydddmmddoyhsdmdhhyyoshhdhhdmhmNmNhydh\r\n" + 
			"dddhhyhhyyyhhh/.-::--.:yy/hh+`````............----:::::----...-...-:::::::::::+mmmyyNNm//////:///++odmddhyssdddmmmmmNNNmyysyddyy+/hdmNdyhdyyhhyddddmmNNNNdooy\r\n" + 
			"ddhhysyyyyhhyyyys::-..:hh:hh+``````......-..-----:::/::----.......-::::::::://+mmmyyNNm//////////hdhhhddsssymddmdmmmmmNNmmdmhshs+/soyhyhhhsshdhmdmdmNNNNMdmmh\r\n" + 
			"hhyssyyyyyyysssys--.``-//.:-.``````...----------:::///:------..`.`-::://///////+++++oos//////////oyysoo+/-/odddmNNmmdmdNNNdhyddyhhyhdddmddsydmmdddsdddmhNdsyy\r\n" + 
			"dhhyysyyysyyyyyys-.````````````````..-------::::/:::--.```````````-/++///////////////////////////sso+soys+ssyhhhhmdmddmdhyyhhdmmdhdhhdddyyhhdhdyysddhhdmmy-:+\r\n" + 
			"dhhyyyyyyyyyyhhys.`````````````````.--::::::-..`````````````        `..-::///++++++oooooo+++++++ooo//+/o+++sdyyhymhyyyyyyyyyyyhhhhmmdyoyhyydhhdyyhddddhNNd/+s\r\n" + 
			"dhhyyyysoo//:-.````````````````````.......-----::::::://::----------............`.......-----::::::::::-`   `..-:+/+ossyyssyhdddydNNmmdydhhdsshsddddhhodmNmdd\r\n" + 
			"+/:--....-.-------::::::::////////+++///////////++++++++++++////////////////////////////////////////////////////::::::://+hdNmNNNMMMMNmdNNmddddmmmNmmmddNMMNN\r\n" + 
			":::::::://///////+++++++++++++++++++++++++++++++++oooooooooo+////////////////////////////////+++++++++++++++++++++++ymmmNNNNNNmNNMMNMNNNNNmddmmmmmmNNNNNNMMNN\r\n" + 
			"/osos/:///////////////////////////////////////+++ooooooooooo+/oossssssssssssssdNmNy+ommysssssssssssssoossssssddmmddsoNNNNmmmNNNMMMMNNNNNNmmhdmmmmmNNNNNNNMNNm\r\n" + 
			"/dmmh:::::://///+ss+//////////////+sss+ssso///+++oooooooooys+/hshhhhhssssssdhsdNNNy+sNNysyhysssydddyyosssssssdmmmdNsoNmNNNmmmNNNNMNNmmMNMNmhmmdmmdNNNmdmNNNdy\r\n" + 
			"/NNNd:::::::::::+dm+///////://////+ddmommdy///++++oooooooods+/NymNNNmsssssssssdNNNy+sNNysssssssyNmmhhosssssssdmdmmNsommNNNNNNMMNMMMNNNNMMNNdmNmmmmmmNmmmNNNmd\r\n" + 
			"/Nmmh```........:yy-``````````....:dmN/mmdo--.+++++ooooooods//NsmNNNmoooosssssdNNNhosNNyssssssssmmmyhosssssssdmNNNNyomNNNNNNMMMMMMMMMMMMMMNdmNmdmdmdmNmmNMNMN\r\n" + 
			"+ddds````````...:yh-..........````:syy:yyy/..`+++++++oooooho//dsddmmhooooooooodNNNhosNNyssssssssNNNydssssssssdNMMNMyoNNNmddmNNNNMMMMMMNMMMMdmNNMNdmdmNNNNMMMM\r\n" + 
			"/ooo/```............``````........:ddd:dmdo...++++++++oooods//NsmNNNdooooooooshNmNhosmNhsssssssyNNNhdsssydhhsdNNNmNyoNNNmmmNNNNNNMNNNMNMMMNddmmMNNNNNNMMNNNNN\r\n" + 
			"::--.``....``````.........````..``:hdd:dmd+..`++++++oooooods++NymNNNmoooossssshNNNhssNNhssssssyyhmdyysoyyhdmydmmmmmysdhhhyhhhhhhhhhdddNNmdddhdmmmdmmmmNNmmmmm\r\n" + 
			"oo:-.``.````.....````````........`-///-+oo:...+++++oooooosho++mmdddyysssssssssdNNNdssNNhssssssyyyhhhyysyhdhdhhdssooysooosssssysooooooossyyyyydhdddddddddmmmmm\r\n" + 
			"--/ooo++/::-..````````...````.``............``++++oooooossyo++dmddhhssyssssssshNNNhssmmdyyyhhhhhddddhyshhhhdhhddddddddhhhhhhyhyyyysssoossssooooooosyyyhyhhdhy\r\n" + 
			"+++++++oosssssysssso+++//:-..`````````````````+++oooooossyyo++dmdddmdddhhhhhhhhdddhsshhhhhhhhhhhddddmmmmmmmmmmmmdddhhyyyyssssosssoosooososssososooooooossoo++\r\n" + 
			"+o++++++++++++++++ooossosssyyyyyyysoo++/::-...+osyyyyyyhhhho+oddddhhhyhhhhhhhhhddddddmmmmmmmmmmmmdddhhhyyyssssoooooooooooooooooooooooooooooooooo+++/::--.....\r\n" + 
			"oooooooooooooo+ooo++++++++///+++oooooossssyyyyddhddhhoossssosyhdddddmmmmmmmmmmmmmdddddhhhyyssssooooooooooooooooooooooooooooooooooo+o++///:---................\r\n" + 
			"ooooooossosoooooosoooosooooooo+ooo++++++++++++++o+oo++oooooosyyyhhdddddddhyyyyssssooooooooooooooooooooooooooooooooooo+ooo+++///::--..........................\r\n" + 
			"sssososoosssoossooosoosooooooooooooooooooooooooooooooo++ooooooooooooooooooooooooooosoooooooooooooooooo++oo++ooo+++//:::-.....................................\r\n" + 
			"ssssosoososssossosooooooosoosooooooosoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo++///::--......:.......................--.-.............";

	private final String mailboxImage = "ssssoooooooooo+++++++++++++++/////////////////////////////////////////////////////++++++++++++++++++oooooooooooosssssssssyyyyyyyyyyhhhhhhhhdddddddddddddmmmmm\r\n" + 
			"soooooooooo+++++++++++++////////////////////////////////////////////////////////////////++++++++++++++++ooooooooooosssssssssyyyyyyyyyhhhhhhhhhddddddddddddmmm\r\n" + 
			"ooooooo+++++++++++++///////////////////////////:/::::://////////////////////////////////////+++++++++++++++ooooooooooossssssssyyyyyyyyyyhhhhhhhhddddddddddddm\r\n" + 
			"oooo+++++++++++++////////////////////:::::::::::::::::::::::::::::::////////////////////////////+++++++++++++++ooooooooosssssssssyyyyyyyyyhhhhhhhhddddddddddd\r\n" + 
			"oo+++++++++++//////////////////:::::::::::::::::::::::::::::::::::::::::::://////////////////////////+++++++++++++ooooooooosssssssssyyyyyyyyhhhhhhhdddddddddd\r\n" + 
			"++++++++++///////////////::::::::::::::::::::::::::::::::::::::::::::::::::::::::///////////////////////+++++++++++++oooooooosssssssssyyyyyyyyhhhhhhhdddddddd\r\n" + 
			"+++++++//////////////::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://///////////////////++++++++++++ooooooooossssssssyyyyyyyhhhhhhhhdddddd\r\n" + 
			"++++/////////////::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://///////////////////+++++++++++oooooooosssssssssyyyyyyhhhhhhhddddd\r\n" + 
			"+////////////::::::::::::::::::------------------------------::::::::::::::::::::::::::::::::::://////////////////+++++++s+++oooooooossssssssyyyyyyyhhhhhhddd\r\n" + 
			"///////////::::::::::::::::------------------------------------------::::::::::::::::::::::::::::::::///////////////++ohsho++++ooooooooossssssyyyyyyyhhhhhhhd\r\n" + 
			"////////::::::::::::::::--------------------------------------------------:::::::::::::::::::::::::::::://///////sdNMMMMNmhy+++++ooooooooossssssyyyyyyyhhhhhh\r\n" + 
			"//////::::::::::::::-----------------------------------------------------------::::::::::::::::::::::::::::///++mMMMMMMMMMMMMh+++++++oooooossssssyyyyyyyhhhhh\r\n" + 
			"////::::::::::::::-----------------------------------------------------------------:::::::::::::::::::::::/+++/sMMMMMMMMMMMMMMNo+++++++oooooossssssyyyyyyhhhh\r\n" + 
			"//::::::::::::-------------------------------------------------------------------------:::::::::::::::://+++///hMMMMMMMMMMMMMMMNo++++++++ooooossssssyyyyyyhhh\r\n" + 
			":::::::::::---------......................--.-.....---------------------------------------::::::::::/++//+/:::/hMMMMMMMMMMMMMMMMm+++++++++ooooossssssyyyyyyyh\r\n" + 
			":::::::-----------......................................---------------------------------------://++//://:+/:/+dMMMMMMMMMMMMMMMMN+//+++++++oooooossssssyyyyyy\r\n" + 
			":-------------------.........................................-------------------------------/+o+//:::::://+::+odMMMMMMMMMMMMMMMNN+////+++++++ooooossssssyyyyy\r\n" + 
			"---------------......................................................----------------------/+//::::::-:-//+:-+ohMMMMMMMMMMMMMNNNN+//////++++++oooooosssssyyyy\r\n" + 
			"-----------------......................................................-.------------------://::----:-::/+/:-:ohMMMMMMMMMMMMMNNNm+///////++++++oooooosssssyyy\r\n" + 
			"---------------..................................................................---------/-/+:-------::///:--oyMMMMMMMMMMMMMNNNmo:///////+++++++ooooossssssy\r\n" + 
			"---------------...................................................................--------s:/+/::-:---::://::-/yMMMMMMMMMMMMNNNmmo:://////++++++++ooooossssss\r\n" + 
			"------------.......................-......................................................y:://---::--::://---.yMMMMMMMMMMMMNNNmms::/://///////+++++ooooossss\r\n" + 
			"...--................................----.---.............................................y://----::::::-+/---/hNNNNNNNNNNNNNNmmmy:::://///////+++++++++++ooo\r\n" + 
			".-.--.-.........````````````````````..``.--....---........--..............................y++:::::::/:/:-+oo/+hmmmmmNNNNNNNNmmmddh:::://////////////+++++++oo\r\n" + 
			"--.---....-.........`..``````````````.....``..`````..-........-........-.................-h//:-::::+/+o+oyyhmNMNNNMNNNNNNNNNmdh+::::::////////////+++++oooooo\r\n" + 
			":-:-::---.--............`...`````````.````.---.`.-.`.`....`.------......................-.y-/:-/+//smhdNMMMNNmdyssNNNNNNNNNNd-----:-::+//::://////++++++ooooo\r\n" + 
			"-::::----:::-:...---.-:--.....``````-`````.....`.`.......-///----...`-....-...........-...o:+oosymmNmNNNdhso/////:mNNmNMMMMN+------::+://::://////+++++++oooo\r\n" + 
			":////://+//::-----.---:..```````...-...``..`..`...--:-://--.-::-------....-..........--.--..:++oyhhhyo+/:::::/:/oyNMmMMNNmmh+::/:::/:+/+/::://+//++++++++oooo\r\n" + 
			"dmddhhhdhhdhhhsss+//--:...........:..`.--.-//-::-///://::+:--//::--.....:.--...-----:-.:...:-:::-:::-:/+/+oyhhNNNMMNNMNmmNNmo---:-:/+:+///::///++++o+ooo+osso\r\n" + 
			"mmmmddddhdhhdhhhhhhhhyso++///+++/oy+/ooo+ososyyssssssyhsso+///:---:::::::---..:--.-:::/:---:/:---::/syhddmmNmmmmddhhyssoso++:---:::+//+/////++/+oooooooossssy\r\n" + 
			"ddmmmmmmdddhhhddhhyyhyyyyyyyyhyyyyyyyhhyyyyyyyyssyyyyyyyyyyhyyyysooooooso+////+//+osyyyyyyhyyysosyyyyso+ohmdddhdmhhyyyyoyyyssssosyyyhhhyyyyyhyyhhhhhhhdhhdhdd\r\n" + 
			"mmmmmmmmmdddddhhhhyyysssssssysooossosossosyyysyysssssssyyysssshssosshysssssssysssysyyysssssssyyssssyys//oydddhmmhddhhdmhhhhyyyssyyyhyhhhddhhdhhdddhdhddddmdmm\r\n" + 
			"Nmmmmmmmmmmmdmmdddddddddmddhhhhdhhhhhhyhyhhyyyhyhyyyyyysyyysyyhyyyyyyyysyyyssyyysssssssoooosssyssyssys+/osmmdmmdddyyhhyhhyysysssssyyyyyhhhhhhhhhdddhdddddmmmm\r\n" + 
			"NNNmmmmmmmmdmmdddhdddhdhhhhyyyyyyyhhhydyhyyyhhhhyhyyyyyyyyyyyhhyhyyyssyyysyoosssssssssssyyyyssyyssssyso+oydddmmmdhyhhdhhhyyyyyyyyyhhhhhhhhhhdhhhhdddddddmmmmm\r\n" + 
			"mNNmmNNmNmmmmmmmmdhdhhhhdhhddhhhhhhhyssyysyhhysyssyhyyhyyyyyyhhhhhhyyyyysyssssyhssssyyyyysoosossysosssoooydddmmmdhyyyyhyyyyyyyssyyhhhdhhhhhhhhhhdhddddmdmmmmm\r\n" + 
			"NNNNNNNNNmmNNmmmmmmddddddhhdhddhhdhhhhhhdhhhyyyhhhhhyhyhyhyhyddhhyyyyyyyyyysyyyhssssysssyssssyyyyyyyyyysssmdmmmmdhhhyhdhdhhhhyyhhhhhhhhhhhdddhdddddddddmmmmmm\r\n" + 
			"NNNNNNNNNmmNmmmNNNmmmmmmmmmdddddhdddhhhdddhhhyhhhhhdhhhhddhhhdhhhhhhyhyyyyyhyyhysyyyyyyhyyyyyyyyyhyhydhosshmmdmddmhyhhdhyhhyhhyyhdhhhddhhddhhdddddmdmmmmmmmmm\r\n" + 
			"NNNNNNNNNmNNNNNNNmNmmmmmmmmmmmmddddddddddddhhhhdhdddhdhddhhhdhhhhhyhhhhyhhhhhyhdyyyyyhyhyhyyyhyyyyddhhdshydmmdmddmhhhdhyhhhhhyhhhhhhdhdddddddmmmdddmmmmmmmmmm\r\n" + 
			"MNNNNNNNNNNNNNNmmNNNNmmmmmmmmdmmmmdmdmddddhddddddddddddddddhdhdddhhhdddddhhhdyhmhyyhhhhhhyhhhhdhhhdhdhhyhydmmdhdmmhddhhhdhhhhhhdddddddddhddddmmmddmmmmmNmmmmN\r\n" + 
			"NNNNNNNNNNNNNNNNNNmmNmmNmmmNddmmmmmmmmmmmdddddmmdddddddmddddddhhddhhhddddhhhdhhmhhhdhhdyhhhydhhdddhddhhyhhddddmmmdhhddhhdhhhhhhhhhhdddmddmdmmmmmmmmmmmmmmmmNN\r\n" + 
			"mNNNMNNNNNNNNNNNNNNNNNNNNNNmmmmmmmmmmmmmmmmmmdmdmmddmddddddhddhddhddddddmdddddhmddddddhddhhhdhhdddddhddhsyddmmddddddhdhdddhhhhhhdddhdddddddmmmNmmNmmmNmmmmNNN\r\n" + 
			"MMMMMMNNNNNNNNNNNNNNNNNNNNNmmmmmmmmmNmNmmmmmmmmmmmmmmddddmdmmmhdmddddddddddhhdddddhhhhhhdhdddhhhdhhhmdmdhydmmddhddhmddddddhddddddddmdmdmmmmmmmmmNmmNNmmNmNNNN\r\n" + 
			"MMMMMNMNNNNMNNNNNNNNNNNNNmmNNNNmNmmmNmmmmmmmmmmmmmmmdmdddddmddhdmmmdhmdmdddddddmdmmmdhddddddhddhhhdddddmhdddhddmmmddddmmdddddhddmdmmmmdmmmmmmmmNmmNmNNNNNNNNN\r\n" + 
			"MMMMMMNNNNNNNMMNNNNNNNNNNmmNNNNNNNNNNmmmmmmmmmmmmmmddmmmmmdNddddmmmdmmdddddddmmmdmmmdmNmdddddhdhdhddmmdmdddmNmmmdmmmdmmddddddhdmmmmmmmmmmmNmmNNmNNNNNNNNNNNNN\r\n" + 
			"MNMMMNNNNMNMNNNNNNNNNNNNmNNNNmNNNNNmmmmmdmmmmmmmmmmmmmmmdmmmmmdmmmddmdddmmdddddmddmdmmddddmmddhddmdmmmmddddmNmmmmmmmmmmdmddmddmmmdmmmmmmmmmmmmmNNNNNNNNNNNNNN\r\n" + 
			"";
}
