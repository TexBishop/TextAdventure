package Rooms.CustomRooms.House;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class UpstairsHallway extends Room
{
	private static final long serialVersionUID = 1L;

	public UpstairsHallway(GameState gameState) 
	{
		super(gameState);
	}
	
	@Override
	protected void setName() 
	{
		this.name = "Upstairs Hallway";	
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.hallwayImage, this.fullDescription());
	}
	
	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Entryway
		//=================================================================================
		this.addMovementDirection("downstairs", "Entryway");
		this.addMovementDirection("stairs", "Entryway");
		
		//=================================================================================
		//Create directions that move to Guest Room
		//=================================================================================
		this.addMovementDirection("guest", "Guest Room");
		this.addMovementDirection("right", "Guest Room");
		
		//=================================================================================
		//Create directions that move to Master Bedroom
		//=================================================================================
		this.addMovementDirection("master", "Master Bedroom");
		this.addMovementDirection("left", "Master Bedroom");
	}
	
	@Override
	protected void createItems() 
	{
		//=================================================================================
		//When creating items, in addition to instantiating the object, you also need to
		//set the synonyms for the item, which are words that it can be recognized by in
		//typed commands.  Once that is done, add the item to the Space hashmap.
		//=================================================================================
	}
	
	@Override
	public void createFlags() 
	{
		this.gameState.addFlag("door unlocked", new Flag(false, "", "Door unlocked."));
	}
	
	@Override
	public String fullDescription() 
	{
		if(this.gameState.checkFlipped("power restored") == true)
			this.description = "You stand at the top of the stairs in the center of the hallway. There is a door "
					+ "at each end of the hallway to your left and to your right.";
		else
			this.description = "You stand at the top of the stairs in dark hallway, with paths leading to your left and your right.";
		
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
		case "move":  //doing this will cause move to execute the look code
		case "go":
			//===============================================================
			//If the master bedroom door is still locked
			//===============================================================
			if(command.unordered("left|master") && this.gameState.checkFlipped("door unlocked") == false)
			{
				return new DisplayData("", "You go left. At the end of the hallway, you come "
						+ "across a door; it is locked.");
			}

			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			if (command.unordered("back"))
				return this.displayOnEntry();

			//===============================================================
			//Change current room and return new room DisplayData.
			//===============================================================
			if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
				return this.move(command);

			//===============================================================
			//Direction not found
			//===============================================================
			return new DisplayData("", "Can't go that direction.");
			
		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();
				
		case "use":
		case "unlock":
			if (command.unordered("key"))
			{
				if (this.gameState.checkInventory("Door Key"))
				{
					if (command.unordered("door"))
					{
						if (this.gameState.checkFlipped("door unlocked") == false)
						{
							this.gameState.flipFlag("door unlocked");
							return new DisplayData("", "The door has been unlocked. It leads into the master bedroom.");
						}
						else
							return new DisplayData("", "You have already unlocked the door.");
					}
					else
						return new DisplayData("", "You cannot use this item on that.");
				}
				else
					return new DisplayData("", "You do not have a key.");
			}
			
			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData ("", "Can't do that here.");
			
		case "search":  //doing this will cause search to execute the go code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				if(gameState.checkFlipped("power restored") == true)
					return new DisplayData("", "With the power restored, the corridor is well lit. ");
				else
					return new DisplayData("", "Surrounded by darkness, you cannot see where the paths lead.");

			//===============================================================
			//If default is reached, check to see if the command contained an 
			//inventory item.  If yes, run the command through it.  If the
			//result is not null, return it.
			//===============================================================
			DisplayData displayData = this.inventoryTest(command);
			if (displayData != null)
				return displayData;
			
			//===============================================================
			//No command matches, return failure.
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
	private final String hallwayImage = ".-/hdddddddddddddddddddds/------------------:::::::::/++:::/:::::::::::--.``````````````..-::/::::::///////////////////////+++++++++oo+++////:::::::::-------\r\n" + 
			"....yddddddddmdddddhhhh+-------------------:-:::::-::::::::/:::::::::::--.````....```````/syyds:::::///////////////////////+++++++++++++++////:::::::--------\r\n" + 
			"....-:yddddddddyhhhhdo:----------------------:::::-::::::-:/:::::::::::--.`              .hohdo::::////////////////////////++++++++++++++/////::::::::-------\r\n" + 
			"yysssoyddddddddhhhhhh+-----------------------:::::-::::::-:/::::::::::+oyhhs/-           .ysddo::::///////////////////////++++++++++++++++////::::::::-------\r\n" + 
			"dhhhdhddddhddhdhhyyyy+----------------------::::::-::::::-:/::::::::+hmdmmmmdhs.         -sydho:::://////////////////////+/+++++++++++++++////:::::::::------\r\n" + 
			"hhhhhddddddddddhhyyyh/---------.------------::::::-::::::-:/:::::::::+mmmmmmdd-          :sdmds-::://////////////////////++++++++++++++++//////:::::::-------\r\n" + 
			"yyyyyhdhhhhdhdhyyyyhh/---------.------------::::::-::::::-:/::::::::::/dddhhm.           +mmmmmo:::://///////////////////++++++++++++o+++/////:::::::::------\r\n" + 
			"`    -hmmmmdddhyyhyhh/----------:++---------::::::-:/::::-://:::::::::/dhhhhdhhyyssoo++//sydddy::::////////////////////+++++++++++++oo+++++////::::::::------\r\n" + 
			"     .hdddddddhhhhhhh/---------.-//---------:::::::+o:::::////:/::::::/dhhhhdysyysyyyyhhhhhhmddhdhhhhs///////////////////+++++++++++oo+ooo/+o//::::::::------\r\n" + 
			"     shdddddddddhhhhhhy+---------/+---------:::::::/o::::://///////::/sddddddo.`..........-.ymy---:::////////////////////+++++++++++oo+oo+/+s///:::::::------\r\n" + 
			"     hdddddddddddddddddo---------//------:--:::::::/+:::::/+/////////+hmmmmdmo.`.`..........ydo----::////////////////////++++++++++ooo+ooo/os//::::::::------\r\n" + 
			"     +hddddddddhddddd/-----------//--------::::::::++:::::/+++////////+dhddhm..`............yd+----::////////////////////++++++++++ooo+ooo/oo///:::::::------\r\n" + 
			"     .hdddhdhhhhhdddd/----------://--------:::::::://:::::++++++++++++ohhhhdm..`............yd+----::///////////////////+++++++++++ooo+ooo/oo////:::::::-----\r\n" + 
			"     .yddhhhhhdhhdddd/---------.-::------:-:::::::::::://:++++++++++++ohhhdhm..`............yd+----:://////////////////++++++++++++ooo+ooo+oo////:::::::-----\r\n" + 
			"     .ydhdhhdhdhhdhhd:-------------------:-::::::::::::///+o+++++++ooosddddhm..`..........-.yd+---::://////////////////++++++++++++ooo+ooo+oo/////::::::-----\r\n" + 
			"     .hhhhhhdhhhhhhhh:-----------------:-:-:::::::::://///+ooooooooooosdddddm..`..........-.hd+---::://///////////////+++++++++++++osyhysooss+/++//::::::----\r\n" + 
			"     .yhhdhhdhhhhhhhh:----------------::::-::::::::://////+ooooooooooosdddddm..`........-.--hd+---::://////////////////+++++++++++ooshNhshyyhhhhdds::::::----\r\n" + 
			"     .yhhdhhdhhhhhhhh:---------------:::::-::::::::///////+oooo+oooooosdhdddm..`..........-.hd+-:-:::////////////////+++++++++++++ooyhNdymhdmmmmmms/:::::----\r\n" + 
			"     .hddhhhhhhhhhhhh:-----------:--::::::-::::::::///////+ooooooooooosdhdddm..`..........--hm+-:-:::////////////////++++++++++++oosshNddNNmyhyhdy+:::::::---\r\n" + 
			"     .yhhhhhdhhhhhhhh:-----------:::::::::-::::::::///////oooooooooooosdddddm..`........----hmy/::::://////////////+++++++++++++ooosshNdmNNN++ssm+/::::::----\r\n" + 
			"     -yhdddhddhhhhhhh:-----------::::::::::::::://:///////oo+ooooooooosdddddm..`..`.....----dmm:::::://////////////++++++++++++oooosshNdmNNm++soo//:::::::---\r\n" + 
			"     -hhdhhhhhhhhhhhh:-----------:::::::::::::////:///////+o+++++ooooosdddddm..`..`......---dmm:::::////////////++++++++++++++ooooosshMmmNNhoos++//:::::::---\r\n" + 
			"     -hhddhhddhhhhhdh:-----------:::::::::::://///://///++osssyyyyyyyyhddmmdm..`........----dmm:::::+yyo++/+++/+++++++++++++ooooooosshMmNNNyoso+///:::::::---\r\n" + 
			"     -hddddhddhhhhhdh:-------::--:::::::::::////////++oyhhdddddddddddddddmmmm..`...--...----dmm:::+yhyo+++++++++++++++++++++oooooossshNmNNmhyho+///::::::::--\r\n" + 
			"     -hddddhddhhhhhdh:------:::--::::::::::::////osyhddddhhdhddddddhhhdmmmmmm..`...--..-----dmm:/shhsoo++++++++++++++++++++oooooossssyNNNNhssdo+///:::::::---\r\n" + 
			"     -hhdddhddhhhhhhh/----:::::-:::::::::///+syhdddhhhhmmmmdhysssssssydddmmmdhs`...--...----dmmsyhyoooo++++++++++++++++++++ooooosssssshNNNsoso++//::::::::---\r\n" + 
			"     -hhddddddhhhhdhh/----:::::-::::://+osydddhhhhhdmNNmmhyo+/::::::+dmmmmmmmm:`...--..-----mmmmhoooooo+++++++++++++++++++oooooosssyssyNNN+os++////::::::::--\r\n" + 
			"     -hdddddddhhhhdhd/------:::::/+osyhdhhhhhhddmNNmmhs+/::::::::://+dmmmmmmmm:`-..--..---:omNms+oooooo+++++++++++o+++ooooooooossssyyshNNN+ss++////:::::::---\r\n" + 
			"     -hddddddddhhdddd:---::::/osyhhhhhhhhhddmNNmdhs+/:::::::::://+shmmmmmmmmmm/`-..--..--+yhdy++oooooooo++++++++++ooooossooooossssyyysdNNdoyo++/////::::::---\r\n" + 
			"     -hddddddddhhddhd/::/osyhhhhhhhhhhdmmNmmhyo/::::::-::::::/+shdmmNmmmmmmmmm/`-..--../syyho++++oooooooo+++++++++ssooossooooosssyyyysmNNooyo++////:::::::---\r\n" + 
			"     -hddddddddhhdddhsyhhhhhhhhyhhdmNNmmhyo+::--:--:----:::+shmNNNNNmmmmmmmmmm/`-..--:syyho++++++oooooooooooooo+ooosyyyysoooossssyyyyyNNNoss+++/////::::::---\r\n" + 
			"     -hddddddddhhdddhhhhhhhhhddmNNmdhyo/:------------::/oyhmmNNNNmdhhdmmmmmmmm/.-../oyyhs//+++++ooooooooooooooooooooooooooooosssyyyyyhNNNoyo++//////:::::::--\r\n" + 
			"    `-hddddddddhhdddhhyhhdmNNmmdhys+:-------------::+oyhdmmNNNNmdhhhydmmmmmmmm/.--syyhy+///++++oooooooooooooooooo+ooooooooooosssyyyhhdNNNsyo++//////:::::::--\r\n" + 
			" `  `+dddddddddhddddddmmmmmdho/::---------------:/+shdmmNNNNmdddhhhhhmmmmmmmmm/:oyyhho/////+++oooooooooo++o++ooooossyyyyyysssssyyyyhhmNmsyyo+++/////:::::::--\r\n" + 
			" `  `hdddddddddhdddddmddhso/:---------------::+oydmNNNNNNmdddddhhhhhydmmmmmmmNyyyyhs:://///++ooooooooooooooooooooyMNNNNNMNsssssyyyhhhNNdyds+++//////:::::::--\r\n" + 
			" `  `ydddddddddhdddddy+::----------------:+oyhdmmNNNNNmddddddddhhhhyhmmmmmmmmmmmdy/:::////+++ooooooooosyyssooooooyMNNNNNMNsssssyyyhhdNNysds++++//////::::::--\r\n" + 
			"``  `hdddddddddhddddm/----------------:+sydmmNNNNNNmddddddddddhhhhhhmmmmmmmmmmmh+:::://///+++oooooooosyhhhssoooooyNNNNNNNNsssssyyyhhmNNsyys+++//////::::::::-\r\n" + 
			"``  `mdddddmmdddddddm/------------::/+osyhdmmNNNmmddddddddddddhhhhhdmmmmmdhdddo:::::://///+++oooooooosssdssssooosyMNNNNNMNssysyyyhhhmNdshyoo+/:://///:::::::-\r\n" + 
			"``  `hmdmddmmdddmmmmmdddddhhhyyyyyyhddmmNNNNNNmdddddddddddddddhhhhhdmmmd/````.:::::://///++++ooooooossssyssssooosymmNNNNNmssysyyyhhhNNhyhsoo++:://///:::::::-\r\n" + 
			"``  `hddddmmmmmddmddddhhhhhhhhhhhhhddddddddddddhddddddddddddddhhhhhddd+.`````.:::::://///++++osssosssssssssssossosssssssssssyyyyyhhdNNyhysoo++/-:////:::::::-\r\n" + 
			"``  `hddddddddddddhhhhhdddhhhhhdhdhhhdhdhhdddddddddddddddddddddddhhds--.`````.::://:////++++ooysssssssssssssssssssyyyyyyyyyyyyyyyhhdNmydysso+++/-:////::::::-\r\n" + 
			"``  `hhhdddddddddhhhhddmdhhhhdddhhhdddddddddddddddddddddddddddddddy:---......:oooooo+ooooooooodNNNNmssssssssssssssssyyyyyyyyyyyyyhhmNdhdysso+++/:-:///::::::-\r\n" + 
			"``  `ddddddhhhhhyhhdmmmmdddddddhhdddddddddddddddddddddddddddddddy:-----ohyyyyhdddddddddddddddddddddddddddddddddddddddddddddddddddddmNddmhhhyyo+++--///:::::::\r\n" + 
			"``  `mmmmmmmmmmddddmmdmdddddddddddddddddddddddddddddddddddddddy/-----+hhhyyyyhdddddddddddddddddddddddddmmmmddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmho++/-://:::::::\r\n" + 
			"``  `mmmmmmmdddddddmmmmdddhdddddddddddddddddddddddddddddddddh/-----:ydhhhyyyhhdddddddddddddddddddddddddddddddddmddddmmmmmmmmmmmmmmmmmmmmmmmmmms+++:-:/:::::::";
}
