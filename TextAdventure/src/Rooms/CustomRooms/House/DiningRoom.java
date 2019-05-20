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

public class DiningRoom extends Room implements GasLeak
{
	private static final long serialVersionUID = 1L;

	public DiningRoom(GameState gameState) 
	{
		super(gameState);
		this.initializeGasLeak();
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
		this.description = "You step into the dining room. "
				+ "You're surprised to find that there's actually a table here, and it isn't broken. "
				+ "Its surface isn't in great shape, but it's all in one piece, and looks sturdy. "
				+ "Built into one wall is a china cabinet, with shelves up top and a couple of drawers beneath. "
				+ "You see the kitchen through a doorway on one side, and the entryway through another. ";
		
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
		//Create door key
		//=================================================================================
		Item doorkey = new BasicItem(this.gameState, "Plain Key", "", "A plain, generic key. It says 'Master' on it. ");
		this.gameState.addItemSynonyms(doorkey, "plain key", "master key");
		this.gameState.addSpace(doorkey.getName(), doorkey);
	}

	@Override
	public void createFlags() 
	{
		this.gameState.addFlag("drawer opened", new Flag(false, "", ""));
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
			//Take the key from the drawer
			//===============================================================
			if (command.unordered("key"))
			{
				if (this.gameState.checkFlipped("drawer opened"))
				{
					if (this.gameState.checkFlipped("key taken") == false)
					{
						this.gameState.addToInventory("Plain Key");
						this.gameState.flipFlag("key taken");
						return new DisplayData("", "You take the key. You wonder what it might go to. ");
					}
					else
						return new DisplayData("", "You've already taken that. ");
				}
				else
					return new DisplayData("", "You don't see that here ");
			}			

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "Can't take that. ");
			
		case "force":
		case "lever":
		case "leverage":
		case "wedge":
		case "pop":
		case "use":
			//===============================================================
			//use the flathead screwdriver to open the drawer
			//===============================================================
			if (command.unordered("flathead|screwdriver", "drawers|drawer"))
			{
				if (this.gameState.checkInventory("Flathead Screwdriver") == true)
				{
					if (this.gameState.checkFlipped("drawer opened") == false)
					{
						this.gameState.flipFlag("drawer opened");
						return new DisplayData("", "Wedging the screwdriver into a gap in the bottom drawer, you try to force it open. "
								+ "Success! The drawer breaks free from whatever was holding it closed. "
								+ "Peering inside, you find that your efforts may have proven useful. You see a key. ");
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
			return new DisplayData ("", "Can't do that here.");
			
		case "open":
			//===============================================================
			//Open drawer
			//===============================================================
			if (command.unordered("drawers|drawer"))
			{
				if(this.gameState.checkFlipped("drawer opened") == true)
				{
					if (this.gameState.checkFlipped("key taken") == true)
						return new DisplayData("", "Both drawers are empty. ");
					else
						return new DisplayData("", "The top drawer is empty, but there's a key in the bottom drawer. ");
				}
				else
					return new DisplayData("", "The top drawer seems to be empty. The bottom drawer is stuck; you can't get it open. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "Can't do that here. ");
			
		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//look around
			//===============================================================	
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "Leaves and debris have made their way in here as well, but there isn't as much mildew here. "
						+ "Dust covers everything, including the multitudes of cobwebs. Other than the table, there really isn't much here. ");
			
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("table"))
				return new DisplayData("", "It's surface is in poor shape, scratched, scraped and well worn in areas, along with a liberal coat "
						+ "of dirt and grime. Despite that, it still seems solid and sturdy. "
						+ "Trying to lift one corner, to get a feel for its weight, you find that it doesn't move. "
						+ "Looking more closely, you see that it's been nailed to the floor. That would explain why it's still here. "
						+ "They didn't leave any chairs for it though. ");
			
			//===============================================================
			//Look at cabinet or drawer
			//===============================================================
			if (command.unordered("china|cabinet|shelf|shelves|drawers|drawer"))
			{
				String description = "It's a set of shelves set into the wall, with two drawers also set into the wall, beneath the shelves. "
						+ "You see some hinges that indicate that there were some cabinet doors for the shelves at some point, but they're gone now. ";
				if(this.gameState.checkFlipped("drawer opened") == true)
				{
					if (this.gameState.checkFlipped("key taken") == true)
						return new DisplayData("", description + "Both drawers are empty. ");
					else
						return new DisplayData("", description + "The top drawer is empty, but there's a key in the bottom drawer. ");
				}
				else
					return new DisplayData("", description + "The top drawer seems to be empty. The bottom drawer is stuck; you can't get it open. ");
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
