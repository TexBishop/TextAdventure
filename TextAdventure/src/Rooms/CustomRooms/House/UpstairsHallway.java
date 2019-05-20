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

public class UpstairsHallway extends Room implements GasLeak
{
	private static final long serialVersionUID = 1L;

	public UpstairsHallway(GameState gameState) 
	{
		super(gameState);
		this.initializeGasLeak();
	}
	
	@Override
	protected void setName() 
	{
		this.name = "UpstairsHallway";	
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.hallwayImage, this.fullDescription());
	}
	
	@Override
	public String fullDescription() 
	{
		this.description = "At the top of the stairs is a small, empty hallway. "
				+ "There's a door to either side, one on the left and one on the right. "
				+ "A small, grimy window is lighting the area weakly. ";

		return this.description;
	}
	
	@Override
	public void initializeGasLeak() 
	{
		this.addRoomToLeakArea(this.gameState, this.getName(), 3);
	}
	
	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Entryway
		//=================================================================================
		this.addMovementDirection("downstairs", "Entryway");
		this.addMovementDirection("down", "Entryway");
		this.addMovementDirection("stairs", "Entryway");
		
		//=================================================================================
		//Create directions that move to Guest Room
		//=================================================================================
		this.addMovementDirection("guest", "GuestRoom");
		this.addMovementDirection("right", "GuestRoom");
		
		//=================================================================================
		//Create directions that move to Master Bedroom
		//=================================================================================
		this.addMovementDirection("master", "MasterBedroom");
		this.addMovementDirection("left", "MasterBedroom");
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
		this.gameState.addFlag("master bedroom unlocked", new Flag(false, "", ""));
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
			if(command.unordered("left|master") && this.gameState.checkFlipped("master bedroom unlocked") == false)
			{
				return new DisplayData("", "You try to go through the door on the left, but it's locked. ");
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
			return new DisplayData("", "Can't go that direction. ");
			
		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();
			
		case "take":
			//===============================================================
			//Move on 'take stairs'
			//===============================================================
			if (command.unordered("stairway|stairs|stair"))
			{
				if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
					return this.move(command);
			}
			
			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData ("", "Can't do that here. ");
				
		case "use":
		case "unlock":
			if (command.unordered("key|door"))
			{
				if (this.gameState.checkInventory("Small Key"))
				{
						if (this.gameState.checkFlipped("master bedroom unlocked") == false)
						{
							this.gameState.flipFlag("master bedroom unlocked");
							return new DisplayData("", "You use the key you got from the mouse. "
									+ "It works. The door swings open, revealing what looks to be the master bedroom. ");
						}
						else
							return new DisplayData("", "You have already unlocked the door. ");
				}
				else
					return new DisplayData("", "It seems you don't have the needed key. ");
			}
			
			//===============================================================
			//Subject not recognized.
			//===============================================================
			return new DisplayData ("", "Can't do that here. ");
			
		case "search":  //doing this will cause search to execute the go code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The hallway is dimly lit, but even so, it's easy to tell that there's nothing of interest here. ");
			
			if (command.unordered("window"))
				return new DisplayData("", "The window is so filthy that you can't see anything through it. "
						+ "At least it's letting in a bit of light. ");
			
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
