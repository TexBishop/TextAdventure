/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.House;

import Items.BasicItem;
import Items.Item;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class MasterBedroom extends Room 
{

	private static final long serialVersionUID = 1L;

	public MasterBedroom(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "MasterBedroom";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.masterbedroomImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		if(this.gameState.checkFlipped("power restored") == true && this.gameState.checkFlipped("phone answered") == false)
		{
			this.description = "Immediately as you enter the master bedroom you are greeted "
					+ "with the clanging sound of a phone ringing. You look across the room "
					+ "and see the culprit; an old rotary dial telephone sitting haughtily on "
					+ "a nightstand. Right next to it, a king-size bed takes up most of the room.";
		}
		else
			this.description = "You step into the master bedroom, and almost stumble into the king-size "
					+ "bed taking up most of the room. Next to the bed, there is a nightstand, with a "
					+ "handsome old-fashioned telephone.";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Upstairs Hallway
		//=================================================================================
		this.addMovementDirection("hallway", "UpstairsHallway");
		this.addMovementDirection("hall", "UpstairsHallway");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create note with code
		//=================================================================================
		Item note = new BasicItem(this.gameState, "Note with Number", "", "A piece of scrap paper with the number '7852' written on it. ");
		this.gameState.addItemSynonyms(note, "note", "number");
		this.gameState.addSpace(note.getName(), note);
	}

	@Override
	public void createFlags() 
	{
		this.gameState.addFlag("phone answered", new Flag(false, "", "Phone answered."));
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

		case "pick":
		case "answer":
			//===============================================================
			//answer the phone
			//===============================================================
			if (command.unordered("phone|telephone"))
			{
				if(this.gameState.checkFlipped("phone answered") == false)
				{	
					this.gameState.flipFlag("phone answered");
					this.gameState.addToInventory("Note with Number");
					return new DisplayData("", "You pick up the phone. A gravelly voice "
							+ "whispers in your ear: 7852. The line then goes dead...  "
							+ "What was that? Maybe it's important. "
							+ "You search around for a scrap of paper and pull out a pen, writing it down. ");
				}
				else
					return new DisplayData("", "You pick up the phone, but it's dead. ");
			}
			
			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");
			
		case "search":  //synonyms
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("bed|mattress"))
				return new DisplayData("", "Such a shame that a huge, high-quality matress "
						+ "such as this would be abandoned as it was. It has been left to "
						+ "the pests that now inhabit the place.");
			
			if (command.unordered("phone|telephone|nightstand"))
				if(this.gameState.checkFlipped("power restored") == true)
				{
					if(this.gameState.checkFlipped("phone answered") == false)
					{	
						return new DisplayData("", "The noise of the phone ring is truly horrible. "
								+ "Answer it before you go deaf.");
					}
					else
						return new DisplayData("", "A classy rotary dial phone. I hear they are indestructible. "
								+ "It sits on a wooden nightstand. ");
				}
				else
					return new DisplayData("", "A classy rotary dial phone. I hear they are indestructible. "
							+ "It sits on a wooden nightstand. ");
			
			//===============================================================
			//look around
			//===============================================================		
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "On the far wall, there is a large curtained window "
						+ "with a sprawling view of cornfields. Beyond them, there a forest begins.");

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
	private final String masterbedroomImage = " ` .``` ``.--`     `             `-`  ```----:-:::++++/////////////:                                                 `-:-/:://+. `-///:`  ````.-......````` `\r\n" + 
			" ` ..`    `--.     `     `.`     .-` `...-:--:::::++++/////////////:                                                  `..--.-::-..-:/:-```````..`..````      \r\n" + 
			" ` ..`      ``     `    `..`     .-` `...::--:-:::+++++//+/////////:                                                          ` ``````````````````````       \r\n" + 
			" ``..`    ` `      `    ```      .-  `...----:::::++++++///////////:                                                                       ``````````        \r\n" + 
			" ``-.`.-...`.      `      `      .-   ``.----:-:::+++++////////////:                                                                          ``..````       \r\n" + 
			" ``-.``.```.:.     ` .`  `..     .-   ```----:::::+++++++//////////:                                                                          `.....````     \r\n" + 
			" ``-.``.:/..``     ``::.`-:-     .-   ```----:::::+++++++++++++///+/               `.-://++oosssoooo++//:--.`                                 `.---.....`````\r\n" + 
			" ``-.` ```        ```:``/:..     .-   ```----:::::+++++++++++++//oyy++     .-/+sydmmmmmmmdmmmmmmddddddddddddddhyo+:-.`                        .-----------...\r\n" + 
			" ``-.`            `` .` --..     .-   ```----:::::+++++++++++++++hmmmm//ohmmmmmmmmmmmmmmdmmmdmmmdmmmdmmmmmmdddddddddddhso/-`      -://:          ```````..--.\r\n" + 
			"``.-.`            ``             .:  ```.-:--:::::+++++++++++++++hmmmmmmmmmmmdmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmmmmdddmddddddmdhs/-`-ddddm:                ``...\r\n" + 
			"``.:-`            ``            `.:  ```.-:--:::::+++++++++++++++hmmmmmmmdmmmmmmmmmmmmmmmmmmdmmmmmmmmmmmmmmmmddddddddmmmdmmmmmmmmmmmmdmo-.`             ```..\r\n" + 
			"`..:-`         ````.            `-:` ```.-::-::/::+++++++++++++++hdmmmmmmdddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddddddmmmdmmmmmmmmdmyso/`              `..\r\n" + 
			"`..:-.         `.`..```    ` `` .-/```...:::-::/::+++++++++++++++ydmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdddddmmmmmmmdmyss+.             ``.`\r\n" + 
			"`.-::-.........-------.........--:/```...::::://::+++++++++++++++ydmdmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmmmmmmdmyss+.`            ``.`\r\n" + 
			"`.-///::::::::::::::::::--------://``...-:/::://::+++++++++++++++ydddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmmmmmmdmhsso-`````      ````.`\r\n" + 
			"`--///////::::::::::::::-------::/+.....-:/::://:/ooo+oo+++++++++hmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmmmmmmdmhsso-`````      ````.`\r\n" + 
			"`--////////:::://:/::::::------://+.....-:/::://:/ooo+oo+++++++++yhyyyyyyyyyhhhhdddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmhsso-``````     ````.`\r\n" + 
			"`-:/+/////////://://::::::::::::/++.....-:/::://:/ooooooo+++oooooo++oo+++o+/+++/+++++oosyhddmmmmmmdddddddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmhsso-```````   `````-`\r\n" + 
			"`-://////////////////:::::::::://++`..-.-////////+ooooooosyyyyyyyyyyyyyyyysssssssooossyhhhhysoso+ooo++ooo+oooo+osssyhhdmmmmmmmmmmmmmmmmhsso:```````   ````.-`\r\n" + 
			"`-://///////////:////:::::::::://+/-::::://+++++ooooooooossysyyyyyyyyhhhhhhhhhhhhhyyyyhyyyyyyhhyyyyyyyyyyyysssssssssssyyyhddmmmmmmmmmmmdhyy+:--..`````````.-.\r\n" + 
			"`-:///////////////////:::::::::/++o+oooooooooo++++++++++++++oooooooooooossssssssssyyyyyyyyyyyyyhhhhhhdhhhhhhhhyyyyyyyhhhhhhhhhdmmmmmmmmmmmmmmmmmmmmdddhhhhyyy\r\n" + 
			"`-://///////////////:::::::://+++oossyyyyyyyyyyyyyhhyyyyyssssssssssssssosooosoooooooooooooosssssssssssssyyyhhdddddhhhhhhhhyyssooydmmmmmmmmmdmmmmmmmmmmmmmmmmm\r\n" + 
			"`-/+////////////:::--..``````  ``````.------::---:////+ossssyyyyyyyyyyyyhhhhhhhyhhhhyyyyyssssssssssssssssssyyyssooosssoooooooooooymmmmmmmmmdddddddddmmmmmmmmm\r\n" + 
			"`:/++////////:::--------------.-----------..---...----.----------::::::://:/ooossooossssssyyhhhhyyssssyyyyyyyyyyys++++oooooooooooymmmmmmmmmmmmmdddmmddmdmmmmm\r\n" + 
			"`:/+++/+++ooooooo+++++/////////////////::///////::::::::::::--------.----------:::-::::://///+oosyhhyyyyyyyyyyyyyyyooooooooooooooymmmmmmmmmddddmmmmmmmmmmmmmm\r\n" + 
			"`://///+sssssssssssssssssssssssssssooooooooooooo++++++++++++++++++/////////:::/++++/+++++++ooosssssyhdyyyyyyyyyyyyysooooooooooossdmmmmmmmmmmdyddmmddmmmmmmmmm\r\n" + 
			".:/+oo+oyyssssyyssssssssssssssssssssssssssssssssyssssssssssoooooooooooo++o+++++ooossssssyyyyyyyhyyyyymdhyhyyyyyyyyyysooosyyhdmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm\r\n" + 
			"./+ymmmdmdsydmdddddhhhhhhyyyyyyyyyyyssssysyyyssssssssssssssssssssssssssssssssssssyyyyhhyyyyyyyyyyyyhhdmhhhhyyyyyyyyyhddmmmmmmmmmmmmmmmmmmddddmmmdmddmmmmmmmmm\r\n" + 
			"-++ymmmmmddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddddhhhhyyyyyyyssysssssyssssssssssssssyyhhhhhhhhhhhhhhhhhhhhdmdhhhhhyyyyyyyhmmmmmmmmmmmmmmmmmmmmmmmdmddddmmmmmmmmmmm\r\n" + 
			"-++ymmmmmmmmmmmddddddddddmddmdddmmmmmmmmmmmmmmmmmmmmmmmmmmmddddddhhhhhhhysyysyyyhhhhhhhhhhhhhhhhhhhhhdmmhhhhyhyyyyyyydmmmmmddhyyyhmmmmmmmmmmmmmmddmmmmmmmmmmm\r\n" + 
			".::smmmmmmmmmmmmmmmmmmmmmmmmdddddddmdmddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdyydmmmmmmhhhhhhhhhhhhhhhhhhdmmdhhhyyyyyyyyyhdhyyyyyyyyyhmmmmmmmmmmdddmmmmmmmmmmNNNm\r\n" + 
			"./:smNNmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddddddddddmmmmddddmmmmhdmmmmmmhhhhhhhhhhhhhhhhhhhmmmhhhhyyyyyyyyyyyyyyyyyyyyhmmmmmmmmmmhdmmmmdmmmNmmmNmm\r\n" + 
			"`--ommmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmdmmmmmdmmmmmmmmdmdmmmmmmmmmmhhhhhhhhhhhhhhhhhhhmNmdhhhhhyyyyyyyyyyyyyyyyyyhmmmmmmmmmmmmmmmmmmmmmmmmmmm\r\n" + 
			"```+mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmNmmmmmmmmmmmmmmmmmmmmmmNmmmmNNmmmhhhhhhhhhhhhhhhhhhhdNmmdhhhhhyhhyyyyhhhhhhyyyymmmmmmmmmmmmmmmmmmmmmmmmmmmN\r\n" + 
			"```+mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmNmmmmmmNmmmmmmmmmmmmNNmmmmmmddmmmmmmdhhhhhhhhhhhhhhhhhhdmmmdhhhhhhhhhyyyyhhyyso+//ommmhhhdhddddddmdmmmmmmmNNdm\r\n" + 
			"`..+mmmmmmmmmmmmmmmmmdmmmdmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmNmmmmmmmmmmmmmmmmmmNddhhhhhhhhhhhhhhhhhdmNmmdhhhhhhhhhhyyso+++////smdmdhhhhhhhhhhhhhhhdmmmhhdh\r\n" + 
			".-.+mmmmmdossyhhdmmmmmmmmmmmmmmmmmmmmmmmmmmmdmmmmmmmmmmmmmmmmmmmmmmmmmmmmNmmmmmmmmNddhhhhhhhhhhhhhhhhddmNNmmdhhhhhhhhhyo++++++++++syysssssyyyyyyyhhhhmmdmhhyy\r\n" + 
			"`..+mmmmNh.-://+oosssyyyyhhhhhddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmNNmmmNNmmNmddhhhhhhhhdhhhdddddmmNmmdhhhyyyyso++++++++++++++++++++++++++++ooosyhyoo++\r\n" + 
			"`-.+mmmmmdoosssssssssssssssssssyyyyyyyyhhhhdddddmmmmmmmmmmmmmmmmmmmmmddddmmmmmmmmmNdmddhhhhhhhhddhhhddddmmmmddhyyyysso+++++++++++++++++++++++++++++++++++//++\r\n" + 
			"`-..---:::::://++++oooossssssssssyyyyyyyyyyyyhhhhhhhhddddddddddmmmmmmmmmmmmmmmmmmmNddddhhhhhhhhhdhhhhhddhhyssoooo++++++++++++++++++++++++++++++++++++++++/++/\r\n" + 
			"`-..``````````........---:://++ooossssyyyyhhhhhhddddddddddddddddddddhhddddddmmmmmmNddmddhhhhddhhyyysssooooooo+++++++++++++++++++++++++++++++/////+++++++++///\r\n" + 
			"`...`````.````....``......----::::////++oooossssyyyyyhhhhhhhhhhhdddhhhhhhhhhdmmmmmNddddddhhysssooooooooooooooo+++++++++++++++++++++++++++//////////+++++/////\r\n" + 
			"`...``````````.........------::::///////+++++++++++++oooooosssssyyyyyyhhhhhhdmmmmmNhhhhyssooooooooooooooooooooooo+++++++++++++++++///////////////////////////\r\n" + 
			"`-..``````````...........-----::::///////+++++++++++++++++++++ooooooooooooossyhhhdhssooooooooooooooooooooooooo+++++++++++++++++//////////////////////////////\r\n" + 
			"`-..`````..```.............---:::::::////+++++++++++++++++++oooooooo++++++ooooooooooooooooooooooooooooooooo+++++++++++++++++++///////////////////////////////";
}
