/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms;

import Items.BasicItem;
import Items.Item;
import Items.CustomItems.LostLocket;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class FarmhousePorch extends Room
{
	private static final long serialVersionUID = 1L;

	public FarmhousePorch(GameState gameState) 
	{
		super(gameState);
	}
	
	@Override
	protected void setName() 
	{
		this.name = "FarmhousePorch";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData(this.FarmhousePorchImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		//=================================================================================
		//This is used to return the main room description.  If any flags alter the main
		//room description, that will be handled here.
		//=================================================================================
		this.description = "This old farmhouse is falling apart.  The windows have been broken out, and the door is standing ajar.  "
				+ "It's obvious it's been abandoned for quite a while.  A well worn path leads south towards the farm house's old mailbox, "
				+ "and another heads east towards the forest.";
		
		return this.description;
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
		//Create directions that move to Old Farmhouse
		//=================================================================================
		this.addMovementDirection("south", "OldFarmhouse");
		this.addMovementDirection("mail", "OldFarmhouse");
		this.addMovementDirection("mailbox", "OldFarmhouse");
		this.addMovementDirection("path", "OldFarmhouse");
		
		//=================================================================================
		//Create directions that move to Edge of Forest
		//=================================================================================
		this.addMovementDirection("east", "EdgeOfForest");
		this.addMovementDirection("forest", "EdgeOfForest");

		//=================================================================================
		//Create directions that move to Backyard
		//=================================================================================
		this.addMovementDirection("north", "Backyard");
		this.addMovementDirection("around", "Backyard");
		this.addMovementDirection("backyard", "Backyard");
		this.addMovementDirection("back", "Backyard");
		
		//=================================================================================
		//Create directions that move to Entryway
		//=================================================================================
		this.addMovementDirection("entryway", "Entryway");
		this.addMovementDirection("foyer", "Entryway");
		this.addMovementDirection("entrance", "Entryway");
		this.addMovementDirection("inside", "Entryway");
		this.addMovementDirection("door", "Entryway");
		this.addMovementDirection("house", "Entryway");
		this.addMovementDirection("farmhouse", "Entryway");
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
		//Create shard of glass
		//=================================================================================
		Item glassShard = new BasicItem(this.gameState, "Shard of Glass", "", "A shard of glass taken from a shattered "
				+ "window.  It's very sharp.");
		this.gameState.addItemSynonyms(glassShard, "glass", "shard");
		this.gameState.addSpace(glassShard.getName(), glassShard);

		//=================================================================================
		//Create lost locket
		//=================================================================================
		Item oldLocket = new LostLocket(this.gameState);
		this.gameState.addSpace(oldLocket.getName(), oldLocket);
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
		this.gameState.addFlag("shard taken", new Flag(false, "", ""));
		this.gameState.addFlag("locket found", new Flag(false, "", ""));
		this.gameState.addFlag("locket taken", new Flag(false, "Sifting through the tattered nest bits and old fur, you uncover an old locket.", ""));
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
			//If go back, but not to backyard, return base room DisplayData.
			//===============================================================
			if (command.unordered("back") &&
				command.unordered("house|yard|farmhouse|farm house") == false)
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
			
		case "take":
			//===============================================================
			//Take the one of the paths.  Changes room.
			//===============================================================
			if (command.unordered("forest", "path") ||
				command.unordered("mailbox|mail box", "path"))
			{
				if (this.checkMovementDirection(command.getMatch(this.movementRegex)) == true)
					return this.move(command);
			}
				
			//===============================================================
			//Take a shard of glass from a window.
			//===============================================================
			if (command.unordered("glass|shard"))
			{
				if (this.gameState.checkFlipped("shard taken") == false)
				{
					this.gameState.addToInventory("Shard of Glass");
					this.gameState.flipFlag("shard taken");
					return new DisplayData("", "Shard of glass taken.");
				}
				else
					return new DisplayData("", "You've already taken that.");
			}

			//===============================================================
			//Take the locket from the nest
			//===============================================================
			if (command.unordered("locket"))
			{
				if (this.gameState.checkFlipped("locket found"))
				{
					if (this.gameState.checkFlipped("locket taken") == false)
					{
						this.gameState.addToInventory("Lost Locket");
						this.gameState.flipFlag("locket taken");
						return new DisplayData("", "Locket taken.");
					}
					else
						return new DisplayData("", "You've already taken that.");
				}
				else
					return new DisplayData("", "You don't see that here.");
			}
			
			return new DisplayData("", "Can't take that.");
			
		case "use":
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
			if (command.unordered("around|porch|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The porch is in poor repair, with multiple holes and loose boards strewn about. "
						+ "It looks like some sort of small animal had a nest built in a corner among some debris, but it is now "
						+ "long abandoned, like the rest of the farmhouse.");
			
			if (command.unordered("door"))
				return new DisplayData("", "The door is standing ajar.  The frame has warped to the point that the door knob no "
						+ "longer latches properly.  Trying to close it simply results in it slowly swinging back open.");
			
			if (command.unordered("window|windows"))
				return new DisplayData("", "The windows are shattered and filthy.  The shards of remaining glass look sharp.");
			
			if (command.unordered("boards|debris"))
				return new DisplayData("", "You find nothing of interest.");

			//===============================================================
			//If look at nest, flip locket found Flag, and modify the
			//descriptive based on whether the locket has been taken or not.
			//===============================================================
			if (command.unordered("nest"))
			{
				this.gameState.flipFlag("locket found");
				String description = "It seems to be an old rodent nest. ";
				description += this.gameState.getFlag("locket taken").toString();
				
				return new DisplayData("", description);
			}

			//===============================================================
			//If look at locket, make sure it is visible, then set it as
			//the innerSpace.
			//===============================================================
			if (command.unordered("locket"))
			{
				if (this.gameState.getFlag("locket found").isFlipped() == true)
					if (this.gameState.getFlag("locket taken").isFlipped() == false)
						return this.setInnerSpace("Lost Locket");
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

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String FarmhousePorchImage = "+ooooooosssssoooooossssssssyyssssssssssssyyyyyyyyyssssososooo+oooosssossssssssyysssssoooooosoooooooooosysssssysssoooooosooooooossssssssssooooooossssooooooooo\r\n" + 
			"hyyyyhddhsyhmdyyhdddsshdddNNNNNNNdsshhmNmNmNNNNysddNNNNNNNNNNdmmNNNNNMNNNNo/NNNNMMMMMMNm+mNNNNNNNNNNmdmNNmmmmmmmNmdddmdmdmmNmyssohdddmmmmmmmmhhddmmmmmmmdhhdm\r\n" + 
			"mhhdNNNNNNdyhdmdmNNNNmhhdddmNNNNNNNhyhymNNNNNNNNysmmNNNNNNNNNhhmNNNNNNNNNNy-mNNNNMNNNNNdoNNmmmmmmNNmdmNNmmmmmmmmdmmhddhmddNmyoosmNNNNNNNdmmmdmNMNNNmdmmddNNMM\r\n" + 
			"hdddhdhmmmmmdyhddddmmmmdyhdddddmmmmmdsyhmmdddddddysmmdddddddmmydhmmmddmdddd.yddmmmmmNmmhsmmddmmmmmmmdmmmmmmmmmmNNdmmmmmmmmdysshmmmmmmNNmmmddmmmmNNmmmdddmmmNN\r\n" + 
			"s+shdhs+yysosyssydmdysssysoydmdhyyyssyyyhdhyssoossssmmhhyssyyyy+syhhhyhyyyh/+dhhhhhyyhdhydhhhhyyyhdmmhhhhhhyhddmdhyyyyhhdmdhydmmddhhdmmmdddddddmdmmmdddddddmm\r\n" + 
			"o++++ss+osooyhdhyyhmsooshdhyyhmddddhhhysshdyssssyyyo+ddhyssyyyyo:syyysssssyo+dhyhhhysshhhdhhhyhyyyddhyyyyyysshdhyysssyyyhmhyddmddhyyhmdhyhhhdhhhdmdhyhhddhhdm\r\n" + 
			"+++++++o+++ooossssss++ooosssshddhhhhhdhyssssssoooso++osoossssssooossosssssssoyyyyyyyyysyysssss+++ooo+ooooooo+oooooooosssssssssssssossssssssssssssssssssssssss\r\n" + 
			"-:::---.--:://::::-:---:-----:--:----:::---.:::--..-----:--::---:-----------::::-::--://///////////::/://///////+/+///////+++++//////++////++++++++////++++++\r\n" + 
			"```..``````.-----..-....--.......-.`..``````.......``.``.`......```..-::-``.:-:/:-::-:/://///+:--.```.-::::::/://:///:::::::::///////:::::://::::::::::/:::::\r\n" + 
			"````....```````....-...`.....--.``---.`.-.-.`--...`..................-::-..-://///+/:::::/+o++-:--:.```-::::://////:://////////////////////+//::::://///:::::\r\n" + 
			".--.----....-:/:::://::::::::::://:/::::-.``..`````````....`-::::::::::::::::::::::::::::::://::///://///////++////+++/+++++++oooooooooooooooo+/////+++//////\r\n" + 
			"...------::.:-::::/o+::+/::/+ooo+o///::-.`...-...``.....--:.`.:///+o+/o++++++/+o+++++//////://////++++++////++++++/ooyo+osssyyhyyssssyhhhhsss+///o+++/+++////\r\n" + 
			".`.......----:/:+syssssssssooossossssooo-........```........`-ooo/:::://///:////////:::/o+++/+///+o++/+++///////+++oyysyooosoooooosssssssss+/::+++////////:/+\r\n" + 
			"---:---::-:.-:/:oyhNNNNNmNmNNmmNNmmNNo++//:://////////////++:/sys/myohmmNNNmMNMNNNNMMNNd/+++:://///o++o++++++++o++/oyhs+++oso++++++oo+ooo/+/s+-/+o+o++++o++++\r\n" + 
			"......``.```.//:+o-:--:::://::::::::-+//+++++++++++++++ooo++//oss/ms+/:+shdmNMMMNNNMMMMd/oo+::::///////+//////:::::oyho/hdhdmNNdddNNNmddy/+/+o::::/:::///////\r\n" + 
			":-``.....`  `::-os:yoosooo+oo+oooooo-ss+/+oooooo++++/////::/:/oyo/m+:mNdyso+syhmMMMMMMMd/+++:::/+//++oo+oo+::///:--/yho/dddhmMMNNNNMMMMMd/++o+----:/://::/++o\r\n" + 
			"..````````  `:+/so-syyososyyys++++++-os+/+++/+++/////////::--/sy+/N+:mMMMMMNmhsyMMMMMMMd/oo+---:/-:::---::::::-----+oss:ydmMMMMNNNNNNMMMd:++y/:---:::::::///:\r\n" + 
			"`........`` `:ooso-+syyhdmmNmNhhyo+/-oo+/o///++///:////:////-/ss+/m+:mMMMMMMMmshMNMNMMMd/oo/-/://:///::://://///:--osso:oNMMMMMNNNNNNmmy+:++o/---:////////://\r\n" + 
			"-:::-.....```/++s+.+ooymNNNMMMMMMMNs-o+/++::://////---::-----:+o/:mo:mNMMMMMMmshMMMMMMMd+o+///++++oooo+oo+oo+////::+oys:/ossyNMNNNNNmsso/:+/oo:::://///+oo++o\r\n" + 
			" `` ```.``` `:++s+-/++omddmNMMMMMMho-+o/++//++o++///::///:/:--++/:m/-NNMMMMMMmsyMMMMMMMd:++////:/::::::::-::--:::-.osyo://+osdNNNNNmhyo+/:/+so:--:::/-:--::--\r\n" + 
			"``.````````  :++o/-:///::oddNmmNMMy/-+o/+sooooo+////:::://+:--+o+:m+-NNNMMMMMmoyMNMMMMMm:++/:////+///////::::::----+sh+::::/+oohmNmyo+///:/+yo----:-:-::/:::/\r\n" + 
			"/+++:::---```-++o/-:/:+-  yyNydmdNs:.//:+//////:::::---.`..``-oo+:d+-NNNNNNNMmosMNMMMMMm/+o+o////o+ooso+oosso++++:-+sdo:-:::/+///+/+++++/://ss--:/+++ooosysoo\r\n" + 
			".``.``````   -//+/-/o/-`  ssh-`+sso:.//:/:::::/:--:--.......`:+o+:d+-NMMMMMMMdssMMMMMMMm/oo+++oooo//+///+/::/::::--+yd+osssyso+oosooosso++++oy+----:-::::::/+\r\n" + 
			":://::::-.-.`-//o/::/+/-..-::..-ooss//+::........````....``..-+o+:d+-NMNMNNNNdosMMMMMMMm/oo/:++++oossosoooosoooo/+:+shoNMMMMMMMNmNNNNNNMMd/osso:+//+ooo+ooooo\r\n" + 
			"`.-....`./` `-/:+//-....---::///::::-/+:/-.......````````````:+++:d+-NMNNNNNNdoyNNmmmNMd:oo+/o++/++/so+++://///:o:-/yhsNMMMMMMMmmNNNNNNMMd/ss+:--+o://///+::+\r\n" + 
			"````....`` ``-/:+:ymNmm.  .dm..:ymNNd//-:----...`....````.```:/++:d+-NNNMMMMMdsyNN/s+NMh:o++//++++ooooo//:-::::::--:sssmMMMMMMMmmmmNNmNNMd/ooy:---::::::::::/\r\n" + 
			"-...---:--```-/:o:yNNNm::::mm..:yNMMd:/:/:------.````````````-://:m/-mNNMMMMMdoyNN.+yMMm:++/++ooo++oossooo///++++::/syodMMMMMMMmmdmNNmMNMd:oys/:::+/+/+/://++\r\n" + 
			" `` `.` `.-- -/:o:ymmmm. `-mm::/hmMNd:/::-.......````````````-/+/:d/--://+syysoyNN`/ommd-+/:::///++/::::-.--.-..-/:/sdsdMMMMMMMNmmmNNmMNNm:syy/-//:..::-----.\r\n" + 
			"````.-. ``   -:-o:yNNNm`  .mm++:hNMNd:/::-..`..`````.````````-/:::h:.::+//++++ohNN.+-.-/-+/::::://///::::----/:----+sdodMMMMMMMmhdmNNmMNNm:oo+/--.---:/-----:\r\n" + 
			".:.....-::.. :::o:yNNNm`  `mm.:/ymMNd//:.````````````````````:///:h/..--::/+++odNN/s+//o-:/:/+++o+oooo+os//::///+/-/os+dMMMMMMMmdmNNmmNMNm:s/+:-//o+////:++/s\r\n" + 
			"--/:-.-`.````:::s:ymNNmssosmm`..omNNd//--..................``.:///h:--:///++o++hNN/s/://:::-::+/++oooo++/++o+:/::--/o+/dMMMMMMMmmmNNmmNNNm:s:/---.-::/:/++++/\r\n" + 
			"-...-..```` `-:-/:ooooo:---+o--:+osoo--.:-----------::::::--.-:+:/h-.:///+++ooosNN.+.` `-::-///+++++oso+///////:---:/+++ooooo+++++++++++++////:--:-:::///////\r\n" + 
			"-/::::-.---....-------........-----:----.----------:::::::/::::o:/h--:::::/+ososNN-+:.`.:/:-:/+ooso+o++oo+o+o+//++://::/+//++/++++++++//++//+////+++/++o+oo+o\r\n" + 
			"::-:/::---:-...-----.---------------------:------:::////////:--::/h--::::://+++oNNmmmmmd:::-//+++++ooo++++o+ooo+++//::::///+/+++//::///://////:/+o+/oooo++o+/\r\n" + 
			"o+--/:/+/::--------:/:-:::-::::::::-:::::/o/+o+++//++o+++//+/::o:/h:-:////+++o+sNNMNMMNm-//:/+oooooo+ooosoy+oossoo/++///+o+o++++os+++/++syyo+++/+oosysos++yso\r\n" + 
			".-:...-......--::::/::----::------:--////ooo+oooo++++++/////:-:+:/h--///:/+++o+sNNMMNMNm://://+++ooo+//+o/++//////:::::-:::-----:/+/:::-.---///:/:///////++/o\r\n" + 
			"..-..-.-.....`..-:////:::::---:--::-://////+/+++++++o+++++///:-+-+m:::::::/+/+osNNMMNNNm:///+++oooooo++o+//+/+++/////::--::-::/::///////:://+++/////+/+//+///\r\n" + 
			"----::---:-..`````........``.............--::::::::::::::://::.-/+h::::::://+oooNMMMMMNm:o+/++o++o++++++/+/+++++o+///////+o+///:::::://////++o+//+o+++o+++/+/\r\n" + 
			"-..---/-.---...----::-------:::::////::/::://///////////++///:.-:+h+:::::://++ooNNNNmmmm/o++o+o+/++++++++//++++//+++//o+////++/////////++oooo+++/++/oo+++//+/\r\n" + 
			"/::/::/:---:.`````..-::::::::://::://////////////+//++/++//:::.-:oho/://///++oooNNNNNNNm//++o+o+oooo+++oosooooso++++oooo+oooo++o+///:::/+ooo//:++++oos+oo+ooo\r\n" + 
			"/-::..:/--://-:---:::::///++++++++++++++++++++++++/+/+++////+:.--sdsoosssyyyyhhhdddddddd/++/++o+++++++/oooo++/oo++oo+/+oo+/:::/++/////o++/////+oo+++o+//ooooo\r\n" + 
			"::::::::::/++o/::::/++ossssssssssssssssssssyyysssssosooooooos/-:/ssooooooooooooooooooooo////yyhhhhhhyyyhhhhhhhhhhhhddhhhhhhhhhhhhhdhhhhhhhhhhhhddhhhdhhhhdhhh\r\n" + 
			"++++++++++oo++///+//++ossyyyyyyyyyyyyyhhhyhhhhhhhhhhyhyyyhhddyyyyyyyyysyyyyyyyyhyyyyhhhyyyyydddddhhyhyyhhhhssssssssssoosooooooo+ooooooooooooooooooooooooooooo\r\n" + 
			"`````````````````.-:/+ooo+ooosoosssoosssyyyyyyyyyyyssssssyhhyyyyysssyssssyyyyyyyyyyyyyyyyhyyyyysyyysoooosyysyyyyyyyyyyysssooo++oo++o+oo++oooooooo++++oooooooo\r\n" + 
			"``````````.---:osssoooo+++osssysssssssssssossyyyysssoosyddhyyyyyssssssssssssssyyssssssssssysyysssssssooo+sssoooosyyyyyysssyyyyyyyyyyssss++++o+osssyo+++ooosso";
}
