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
import Items.BasicItem;
import Items.Item;

public class DiningRoom extends Room 
{

	private static final long serialVersionUID = 1L;

	public DiningRoom(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "DiningRoom";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.diningroomImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "As you step into the dining room, you immediately recognize a large table"
				+ "taking up much of the room, and three large candles illuminating the dark room with a "
				+ "faint orange glow. A small dresser squats in one of the corners. There are doorways to "
				+ "the entryway and the kitchen.";
		
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
		//Create door key
		//=================================================================================
		Item doorkey = new BasicItem(this.gameState, "Door Key", "", "An old key, belongs to a door. ");
		this.gameState.addItemSynonyms(doorkey, "door key", "key");
		this.gameState.addSpace(doorkey.getName(), doorkey);
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
		this.gameState.addFlag("drawer opened", new Flag(false, "", "Drawer opened"));
		this.gameState.addFlag("key taken", new Flag(false, "", ""));
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
			{
				if (this.gameState.checkFlipped("drawer opened"))
				{
					if (this.gameState.checkFlipped("key taken") == false)
					{
						this.gameState.addToInventory("Door Key");
						this.gameState.flipFlag("key taken");
						return new DisplayData("", "Key taken.");
					}
					else
						return new DisplayData("", "You've already taken that.");
				}
				else
					return new DisplayData("", "You don't see that here.");
			}			

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "Can't take that.");
			
		case "force":
		case "use":
			//===============================================================
			//use the flathead screwdriver to open the drawer
			//===============================================================
			if (command.unordered("flathead|screwdriver"))
			{
				if (this.gameState.checkInventory("Flathead Screwdriver") == true)
				{
					if (command.unordered("dresser|drawer"))
					{
						if (this.gameState.checkFlipped("drawer opened") == false)
						{
							this.gameState.flipFlag("drawer opened");
							return new DisplayData("", "Wedging the screwdriver under the top drawer, "
									+ "you manage to pry it open. Within the drawer, you find a key.");
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
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't do that here.");
			
		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("table|candle|candles"))
				return new DisplayData("", "A film of dust lines the surface of the table. Various dead insects are littered on the surface; "
						+ "it is obvious it has not been cleaned in a hot minute. And yet, the candles are lit. You begin to wonder if you truly are alone...");
			
			//===============================================================
			//If look drawer is unopened/opened
			//===============================================================
			if (command.unordered("dresser"))
				if(this.gameState.checkFlipped("drawer opened") == true)
					return new DisplayData("", "An old wooden dresser, its empty drawers remain pulled.");
				else
					return new DisplayData("", "A sturdy wooden dresser; You try to open the top drawer, but it won't budge... "
						+ "You manage to open the bottom two drawers, only to find strewn within them dead cockroaches and moths.");
			
			//===============================================================
			//look around
			//===============================================================	
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "Moths litter the table around the candles. A chain hangs from the ceiling from the "
						+ "center of the table, where a chandelier used to be.");

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
	private final String diningroomImage = "                        ```.``````````````````          ` `````  `````````````..````````---:+:..```````....----:/+oooooosssssyyyssoo+//::::--.......``.``.```\r\n" + 
			"       `````````.................`````````````         ``````````````````````-dm.```````://:/-.``````````````````..--://++ossyyyyysssoooooo+////:.....-......\r\n" + 
			"       `````.....................`````````````         ``````````````.``````.+ddo+/:-.``ymy:/-.````.//````````.``:+/::-.........----..................-....--\r\n" + 
			"  `   ``````......................`````````````  ````  ``````````.....``````.ymhyhddddhsdmo:/-.````+dd````````:+/omddhyhhhyy.```:............................\r\n" + 
			"````` ````.......................``````````````  ```````.``............````.-dm:......-+mm/:/-.````ymdso+/:.`.hmdsyyyyyhdmmm-```:````````..........-.........\r\n" + 
			"/-``` ```........................```````````````````````.............-..```./mm/:-..```omd-:/-.```-dmsyhddhyoodmhyyhdmmdhyyy+/+hd.``````````````````..-...---\r\n" + 
			"mm.``````........--------........``````````````````````....-..-..-----......smmmmmmdysodms-:/-.```odd`````.--ydmmmmdhyyyyyyyhhsmy.```````:os-`````````.``````\r\n" + 
			"hms`+mh........-----------.......```````````.``````````.-------------:......dmo.--:/++smm+::/-..`.hmy-.``````hmmmmmddddhhhhhdhymmmhyo/:-.yhm.`````````.``````\r\n" + 
			"/dhoomm+.../+/-------------.......`````````....```.........------::--:-..../mm/-......+mm:::/:...:dmmmmdys+/+dmmmmmdhhdmmmmmmydmyosyys++symy``````````.``````\r\n" + 
			".hmhhdmd...+dmo--------------..................`````.``..........----:-....smmmmdhyso/hmh-::+:...odm-:/+ossshdmmmmmmmmdhhhhhhymm/````.-:yhm/``````````.``````\r\n" + 
			"-+mm::mN+..-hmd:---.oyy/..----........................`....................oys/++osyhdmms::/+:...dmy```````.dmmmmmmmmmdmmmmmhdmm+.`````.hmd.````.````.......`\r\n" + 
			":-dmy-ymd:/:smdhyo+/+dmd...--................................................------../mm+::/+:../mNdyo+/-.`/dmNNNmmmmNNNNNmmhmmmddyo/:-+hms`.................\r\n" + 
			"::omdyymN+--/dmmmmmmmdmm+.-----dddo..........................................---------:/::::+:..smmsydmmddhhmmNNNNNmmmmmmmmdhmmmssyyysyhdm:..................\r\n" + 
			"///mmmdmmd---ymmoosyhdmmh-----.smmm----.......-:-.............................------------------+ys....-:+smmmddmNNNNNNNNNmdmmNN:...-:shmd...................\r\n" + 
			"+//sNm-/mN+--+mNd::+oymmmo/::--/mNNhs+/-----..+mmm:..............-..-.........--......-------:::::///::---/mms/++oyyyhhdmNmdmmmm-.....sdmo...................\r\n" + 
			"o+++mds-dNd-::dNmyo+ohdmmh//++++mmNmmmmmdhyssoommms..................------..--------.-----------:::://////:/:---...://++ohmmmmmdhs+::dmm-...................\r\n" + 
			"oooosmdhdmNo::oNmmmmdddmmm/:--:/ymmmddmNmmNNNNNmmmm-----...........---.....----------------------------:::://///::::------dmm+osshdddddmh............-.......\r\n" + 
			"sooo+mmdhmNd:::mmNsymmNmmmy//////mmmyooosyyyyyhdmmm/...------......-------------------:---:-:::--------------::::::::::::::/+----..:+dmm+............-.......\r\n" + 
			"ssss+dNm:oNNo//ymNy/yyhhmmmooosssmmmmyyssooooo++mmmy.........--------.------------------::::::-----:::-----------:::::::::::://///:::syh-............-.......\r\n" + 
			"ssssoyNdy+mNdoosmNmsyyyymmmdyyssshNNNmdhyyssssoommmm/:-...........----::---------:::::---:---::::::::------::-::---:----:::::::://////////::----.....-.......\r\n" + 
			"yyyysymNmdmNNdhhdNmmmdhhdmNmsyssssmNNmmmmmmmddhhdmmmyooo+/:-..........----:::::-----:::::::::::::::::::::::::----:::::------------:::::////////:::...-.......\r\n" + 
			"hhhhhhmNNddNNmyyhmNmmmmmmmmNhyyysodNmmyhdmNNNNNNNmmmmdhhsssso+/:---------------::::::::::::::///////::::::::::::::-:---------------:://////////:::...-.......\r\n" + 
			"hhhhhhdNNhsmNNyssmNNmmmmdmmNmhhhhhyNNNhhyssyyhhyydNmmhhysyyysssssoo+/:-------------::::::::::::///////:::::::---:::::::::::::////++++oo+oo+/----------......-\r\n" + 
			"hyyyyyhmmhohNNdyyhNNmhmmddmNNdhhdddmNNmdhyyssssyyhNmmy+sosddyysoooosssoo+/::::::------::::///:::::::::/:::::::::::::::/+oooo++o+++sdmmmdhyyysso+//:----------\r\n" + 
			"yyyyyyyNmmhhNNmyyhmNmmdddhmNNmNNNNNNNNmmmdhyyssssyNNmmoo++oyhhyysoosdhsssoso++/:::::::::---:::////:::::::::://+++++/+osyhhdhyyy+++yNmmddddddddhhyyysso+/:----\r\n" + 
			"yhhhhhhmNmmmNNNmNNmNNNmmmNmmNNNNNmmmmNNmmmmNNmmmmdmNmmhyyyyso+/oyyyssyyyyyysssooo++///:////:::::::::/:::://++syyyhddmmmmmmmdyyy+++yNmmhhhhhdhddddddddhhyyysso\r\n" + 
			"yyyyyyymNNhdmNNNNNmNNNmmmmmmNNmmmmmmmNNNmmmmmmNNNNNNmmhssyyyyyyssyyyyyyyyyyhhhddysoooo++///++oo/////+oo+odmmNNNmmmmmmmmmmmmmyyyo++oNNmyyyyyhhhhhhhhhhddddmddh\r\n" + 
			"yssssyymNNyymNNhyshmNNdddddmNNNmmmmmmNNNmmmmmmmmNNNNNmmoyhdmdhyyyyyyyyyyyhdmdmNNhyysooooo+oossyyhdssNNNssmNNdmNNNNNNNNNNNNNNhyyo+ooNNmssyyyyyyyhhhyhhhhhhhddd\r\n" + 
			"hyyssssmNNssmNNho+hmmmdhdddmNNNmmmmmmmNNmmmmmmmNNNNNNmmhyhhddhyyyyyyyyyyyyssshdmmsss+o+oyhmNdyyhNNysNNmosmNNhyhdmNNNmmmdNNNhyyys+oomNmyyyyyyyyyyyyyyyyyyhhhhd\r\n" + 
			"hhhyysomNNssmNNho+hmNmmmddddmNNmmmmmmmNNNmmmmmmmmNNNNmmdmNNNmmhhhhhhhyyyyyyyyyysssso++++ydNNmhhhNNdhNNNmNNNNhyyymNNdyyyyNNNyyyyy++ohNmsssssyyyyyyyyyyyyyhhhhh\r\n" + 
			"yhhhhyyNNmoodNNhssdmNNmmmmmmmNNNmNNNNNNNNNNNNNNNNNmmNNmmNNNNNNNNNmmdhhyhddhhyyyyysso++++ydNNmmmmNNNmNNmyyhNNdyyymNNdyyyyNNNyyhyy++oyNmyyyyyyssssyyyyyyyyyyyyy\r\n" + 
			"ssyyyyhNNmhdmNNNNNNmNNdyhmmmmNNNNNNNNNmNNNmhdmmNNNNNNNmmNNNNNNNNNNNNNNNNmmmmddhhysso++++ssshdmddddhhNNmyyyNNmhhhmNNdhhddmNNhshyyo+osNmyyyyyyyyyyyyyysyyyyyhhh\r\n" + 
			"sssssshNNmhhmNNsshdmNNdoooshmNNNmdhNNNmNNNmmmmmmmmNNNNmmNNNNNNNNNNNNNNNNNNNNNNNmhsso+/++yyyssoosyhhhmNNdmmNNNmmNmNNNNNmmmNNdyyyys+oommsyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yssssodNNo++mNN+oyhmNNdssyyhmNNNmmNNNNNNNNmmmmmmmmmmNNNmNNNNNNNNNNNNNNNNNNNNNNNmhsos+++/yyhhhhyysoosydmmmmmmmmddmNNdhhhhdNNmhhyys+oodNyyyysyyyyhhhhyyyyyyyyyy\r\n" + 
			"yysssomNm//oNNNddmNmNNNNmmmmmNNNhhymNNmNNNmmmmmmmmmmmNNmmNNNNNNNNNNNNNNNNNNNNNNmhsss+++/hdddhhhhhhyysooosyhddhddmNNdddddhdhyooooso+ooosyyyyyyyyyyyyhhhhhhyyyy\r\n" + 
			"oossssNNh+/sNNmyyhdmNNdssyyhmNNNhddmNNNNNNNNNNNNmmmmmNNmmmmmmNNNNNNNNNNNNNNNNNNdhssso++/hhddddddhhyyyyyyysooosyhNmdhyso++++++++++o+oohhyyyyyyyyyyyyyyyyhhhhhh\r\n" + 
			"++++++hmd+/yNNy+oydmNNdhhddmmNNNNNmmNNmNNNNmmNNNNNNNNNNNNNNNNNNNNNNNNmmddddmNNNmyssoo++/hdddddddddddhhhyyyyyyso++++++++++++++++++ooosNdyyyyyyyyyyyyhyyhhyyhhh\r\n" + 
			"///+//ohdyomNNhyyhdmNNNNNNNmmNNNo++hmmmNNNmssyhdmmNNNNNNNNNNNNmmmddhhdddddddNNNmhsso++++hhdddddddddddhysoosyyyyooo++++++++oossyyysoooNmhhhhhyyhyyyyyyhhhhhhhh\r\n" + 
			"//+///+shhymNmoooshmNNmdhsosmNNm///hmmmNNNmooossyyhmNNNNNNNNdhhhhhhhhhdddddmNNNdysss++//hhhdhhhyyso+++++++osyyysoooooosyhdmmddhhyyooodmhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"://////syy+ydd+:/+dNNmyysoosmNNmooohmdmNNNNddmNNNNNmNNNNNNNNdhhhhhhhhhhhdddmNNNmhsso++/+ysso++++++++++++++ooooosyhddmmmmmmmddddhhyooshdhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"::::::/oys+ohhs/+omNNmdhhhhhmNNmdmmNNNNNNNNdhhyyyyyhNNNNNNNNdhhhhhhhhhhhdddmNNNmysso++/++++++++++++++++++syyhdddNNNddddmmmmddddhhyoosydddddhhhhhhhhhhhhhhhhdd\r\n" + 
			"::::::/+sso+yhhsshmNNdddddhdmNNNmmddmdmNNNdsoosssyhdNNNNNNNNdhhhhhhhhhhhhddmNNNmooso+++++++++++++oosyyhddddddddddmmddddmmmdddddhhysoosddddddddhhhhhhhhhhhhhhd\r\n" + 
			"-::::::+sssoshhyoodmmdhhhhhdmNNsooshddNNNNdsoshdmNNNNNNNNNNNmdddhhhhhhhhhhhdNmyooooo+o++++++osyhdddddddddddddddddddddddmmmmmdddhhyyoosddddddddddddddddhhhhhhd\r\n" + 
			"----:::/+oooshhyo+ydddhhhhhmNNNhhddddmNNNNmmmNNNmddhmNNNNNNNmddddddhhhhhhhhdNNhsoossoo+ohdmmmmmmdddddddddddddddddddddddmmddddmmdhhyooshddddddddddddddddddddhh\r\n" + 
			"::---::/ooosyhdhssyhdhhhhhdmNNmdddddddNNNNNNmhyyyhhdmNNNNNNNmddddddddddddmmNNNNmyssooo+smddmmmmmmmddddddddddddddddddddddddddddddmmdhddddddddddddddddddddddddd";
}
