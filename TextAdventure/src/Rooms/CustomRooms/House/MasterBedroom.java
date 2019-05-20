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
import Structure.GasLeak;

public class MasterBedroom extends Room implements GasLeak
{
	private static final long serialVersionUID = 1L;

	public MasterBedroom(GameState gameState) 
	{
		super(gameState);
		this.initializeGasLeak();
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
		this.description = "This room is cleaner than the others you've seen here. "
				+ "Still, it's obvious no one has been here for a while, looking at the dust covering everything. "
				+ "An old stained mattress leans against the far wall, no bed frame available for it. "
				+ "There's a window on one wall, overlooking the front yard. "
				+ "Underneath the window lies an old rotary phone. ";
		
		if (this.gameState.checkFlipped("phone answered") == false)
			this.description += this.gameState.getFlag("phone observed").toString();

		return this.description;
	}
	
	@Override
	public void initializeGasLeak() 
	{
		this.addRoomToLeakArea(this.gameState, this.getName(), 4);
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Upstairs Hallway
		//=================================================================================
		this.addMovementDirection("hallway", "UpstairsHallway");
		this.addMovementDirection("hall", "UpstairsHallway");
		this.addMovementDirection("leave", "UpstairsHallway");
		this.addMovementDirection("exit", "UpstairsHallway");
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
		this.gameState.addFlag("phone answered", new Flag(false, "Suddenly, the phone starts ringing, startling you. ", ""));
		this.gameState.addFlag("phone observed", new Flag(false, "", "The phone is ringing. "));
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
		case "leave":
		case "exit":
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

		case "pick":
		case "answer":
			//===============================================================
			//answer the phone
			//===============================================================
			if (command.unordered("phone|telephone"))
			{
				if (this.gameState.checkFlipped("phone observed") == true)
				{
					if(this.gameState.checkFlipped("phone answered") == false)
					{	
						this.gameState.flipFlag("phone answered");
						this.gameState.addToInventory("Note with Number");
						return new DisplayData("", "You pick up the phone. A gravelly voice "
								+ "whispers in your ear: 7852. The line then goes dead...  "
								+ "What was that? Maybe it's important. "
								+ "You search around for a scrap of paper and pull out a pen, writing it down. "
								+ "Only after do you wonder how a phone with no line was able to function. ");
					}
					else
						return new DisplayData("", "You pick up the phone, but it's dead. ");
				}
				else
					return new DisplayData("", "You put the phone up to your ear. The line is dead. "
							+ "You notice that it doesn't have a wire. ");
			}
			
			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here. ");
			
		case "search":  //synonyms
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================	
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "Looking around the room, you notice that there isn't a closet. "
						+ "Maybe that's a modern thing. This farmhouse is pretty old. "
						+ "It smells like mothballs in here. ");
			
			if (command.unordered("window|front yard|yard"))
				return new DisplayData("", "The window is so dirty that it's difficult to make anything out through it, not that there's "
						+ "much to see out there anyways. Just the unkempt lawn and the mailbox. ");
			
			if (command.unordered("mattress"))
				return new DisplayData("", "It's huge, king sized. "
						+ "You can see why it was left behind, deep impressions worn into it from years of use, with frayed areas aplenty. "
						+ "It's covered with a multitude of old, unidentifiable stains. ");
			
			if (command.unordered("telephone|phone|rotary"))
			{
				if (this.gameState.checkFlipped("phone observed") == false)
				{
					this.gameState.flipFlag("phone observed");
					return new DisplayData("", "It's an old rotary phone, like you'd see in an old movie. It's heavy. "
							+ "There's no wire for it, but it wouldn't have any service anyways. "
							+ this.gameState.getFlag("phone answered").toString());
				}
				else
				{
					if (this.gameState.checkFlipped("phone answered") == false)
						return new DisplayData("", "It's an old rotary phone, like you'd see in an old movie. It's heavy. "
								+ "There's no wire for it, but it's still ringing, somehow. ");
					else
						return new DisplayData("", "It's an old rotary phone, like you'd see in an old movie. It's heavy. "
								+ "There's no wire for it, but that didn't stop it from working earlier. ");
				}
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
