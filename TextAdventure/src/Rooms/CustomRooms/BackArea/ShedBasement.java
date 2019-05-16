/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.BackArea;

import Items.BasicItem;
import Items.Item;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class ShedBasement extends Room
{
	private static final long serialVersionUID = 1L;

	public ShedBasement(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "ShedBasement";
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
		this.description = "You stand in the basement of the Toolshed after walking to the bottom of the stairwell. The room is made entirely of concrete, "
				+ "walls, ceiling, and floor. All covered in claw marks. In the center of the room is a large circle containg multiple other symbols and candles "
				+ "inside it. The left wall is covered with numerous bookcases, the right has some sort of table similar to the one from the shed above. The wall "
				+ "in front of you has what appears to be a shattered mirror.";

		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		// Creation of movements to get back out to the toolshed
		//=================================================================================
		this.addMovementDirection("shed", "ToolShed");
		this.addMovementDirection("toolshed", "ToolShed");
		this.addMovementDirection("stairway", "ToolShed");
		this.addMovementDirection("stairs", "ToolShed");
		this.addMovementDirection("stair", "ToolShed");
		this.addMovementDirection("up", "ToolShed");
		this.addMovementDirection("leave", "ToolShed");
		this.addMovementDirection("exit", "ToolShed");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create bronze lantern
		//=================================================================================
		Item BronzeLantern = new BasicItem(this.gameState, "Bronze Lantern", "", "A Bronze Lantern covered in runes and bearing the word 'Almata'. Speaking the phrase ignites "
				+ "a solid silver flame in the lamp.");
		this.gameState.addItemSynonyms(BronzeLantern, "bronze", "lantern");
		this.gameState.addSpace(BronzeLantern.getName(), BronzeLantern);

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

		//=================================================================================
		//Create flag for taking the lantern
		//=================================================================================

		this.gameState.addFlag("lantern taken", new Flag(false, "", ""));
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
		case "leave":
		case "exit":
		case "move":  //doing this will cause move to execute the go code
		case "go": 
			//===============================================================
			//If go back, but not to backyard, return base room DisplayData.
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

		case "take":
			//===============================================================
			//The action for taking the lantern. As long as the "take lantern" flag has not been flipped, the lantern can be taken
			//===============================================================
			if (command.unordered("bronze|lantern"))
			{
				if (this.gameState.checkFlipped("lantern taken") == false)
				{
					this.gameState.addToInventory("Bronze Lantern");
					this.gameState.flipFlag("lantern taken");
					return new DisplayData("", "Bronze Lanter taken.");
				}
				else
					return new DisplayData("", "You've already taken that.");
			}

			//===============================================================
			//Take command not recognized
			//===============================================================
			return new DisplayData ("", "You don't see that here.");

		case "search":  //doing this will cause search to execute the go code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|room|area|basement") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The basement itself is made of concrete covered in claw marks, as if something was not too keen on staying here.  "
						+ "The center of the room has a large circle bearing smaller circles and other symbols in. The sight of it sends chills down your spine. "
						+ "The left wall contains several bookshelves, possibly holding some interesting knowledge. "
						+ "The right wall has a bench pushed up against it, similar to the work station from above, only what kind of 'work' could have hapened down here makes you uneasy. "
						+ "The center wall holds a broken mirror, shattered and bearing some kind of markings on the glass.");

			if (command.unordered("door|trapdoor"))
				return new DisplayData("", "The trapdoor behind you sits wide open. The side which would have been facing the basement is covered in many long claw marks, "
						+ "similar to the rest of the room.");

			if (command.unordered("bookshelf|left|books"))
				return new DisplayData("", "The bookshelves themselves are far from full. Many of their books lay sprawled on the ground leaving only a few are scatterd on the shelves. "
						+ "All the books are written in latin, yet as you look at the texts your head begins to hurt. Your ears start buzzing and a sharp pain forms behind your eyes before"
						+ "dissapearing as fast as it appeared. You suddenly recognize some of the words: 'portal', 'summon' 'blood' 'transformation' 'demon' 'binding'. "
						+ "You suddenly check your nose and realze it is bleeding. The rest of the books on the floor are torn and shredded beyond recognition. You think to yourself, perhaps it's "
						+ "better that they are.");

			if (command.unordered("center|circle|symbol|symbols|floor"))
				return new DisplayData("", "The circle in the center of the room upon a closer look is far more elaborate than it first seemed. There are several circles inside of each other, with symbols inside, between, and "
						+ "overlapping other circles, some in precise areas that you would assume require great practice and skill. The candles are all but melted, as if from frequent use. "
						+ "The symbols seem to be drawn in a red liquid that, while you try to restrain your mind from pondering its origin, fear you already know it.");

			if (command.unordered("right|bench|workbench"))
				return new DisplayData("", "The workbench is scattered with a wide variety of items. Pens, pencils, jars, boxes of bones, feathers, bits of various animals, scrolls, rocks with runes carved into them, "
						+ "and other things you consider not worth picking up. A large bronze lantern does catcb your eye. It has a handle on its top and is covered in runes. "
						+ "A word is etched across the handle, reading 'Almata'. A note next to it reads 'speak it loud and clear when in the barn. lost too many workers already in there'.");

			if (command.unordered("mirror"))
				return new DisplayData("", "The mirror itself no longer hangs on the wall, but is leaning against it. The glass is broken and scattered on the floor, some pieces making it quite far."
						+ "The shards of are covered with symbols similar to the circle in the center of the room. Could something have... crawled out of it?.");

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
	private final String FarmhousePorchImage = "dsmmyy/hmsNMddNNNMMMMMMNMMMNNNNNNNmhmNNdddddmmddddhyyhhysssyhhhyysyyyyysoooossssssysssssysssssssooooossssoooo++++++++++///////++//++hMMMMMNNNyMMydds+sNmhsMNs\r\n" + 
			"dsmdyh-sNyNMhyNNhmMMMMMMMMNNNMMNNNNNmmmmmhhhhdmNdddhyyyhhsssyyysooooyyyhoosoossooosssoosoooooo++++++ooo++++//////:/:::-------::::/omMMMMNddNm+NMymyo+smdyyNdo\r\n" + 
			"hsmdyh-ydsNMsyNNosyMMMMMMMMMNNNNNmNNNNddmNmdhhhddddmhhyhhyooossysooosyys+ossoso/oos++++++////////:/::::::::-------.-----....---./dMMMMMmy/mNm/NMhdyo/smdhsdyo\r\n" + 
			"hsmdhh+ymyNMssmN+oyhymMMMMMMMMNNNmmmmNNmddNNmhhhhdhddmdyysyoosyyyosoosssooo++++/++++/+/////:/::::--.....`````..............``.:yMMMMMyyhy/dmm/NMhdhsoymmhsmmM\r\n" + 
			"MNMdyhosNyNMysmm+oyy+mMmNMMMNNNMNNNNNmmmmmmmmmddddddhdhhyysssssoosssssyys+/++/::///:::-::::/::-...````         `  `````     .yMMMMdNMsoyh/hmm/NMhmdsoymNNMMMM\r\n" + 
			"NNMmNNyyMymMssmN+syh:dN+hmNMMMNMMMNNNNmmmmmmddmmdddmdhhdhysyysysooooossy///+//////::-----/o/-..`````                      `oNMMNmd+mM++hh/hmm:NMymmyhMmNMNMMM\r\n" + 
			"ooMdMMMMMdmNoomNoshh+dM+syhhMMMMMNNmNNNNmmmmmmdmmddmmmmdhyhyyssyyyysooos/+o//+///::-:://:--..````                    `   /MMMMNohs+mMoohh+hmm-mMhNMMMMmmm:ysd\r\n" + 
			"hsNdNmhNMMMMdhmN+osd/dM+yhhyhmMMNNNNNNNNMNNmmmmmddmNmNNNNNNNNmmmddyhhhhhhhyyyyyyyysyyyyyyssssoooo++++++++++o+:--...---`  oMMmhNohy+mM+shs+hmmsNMMMMymNmmmsho+\r\n" + 
			"yoNmdNyyMMMMMMMms+shohMohyyyhyhNNNMMNNMMMMMMMMNNNNNNNNMNMMMMMMMddhyy+........................-------::///://...-://:-.`  +MdshN+hysmM+yyo+dmMMMMMMN/NdNmdody+\r\n" + 
			"y+NmhmsoddNMsyNNMNmhohMshhsyhhsmNMMMMMMNNMMNNNNNNNNNNmmNNNNMMMMNsmddh-``                             `````-:/+oo+:--``   +MysyN+hysmM+sdNMMNN/NMNdy/myNmd+ds+\r\n" + 
			"y+mdhm++osoMoomNMMMNMNMyohshhhomNhhdhhhyyhNyyysyssyhhhhhmNNMNMMMMmhhhs:``/-`.````````.......-----:::://+ooshoo++/:-.```` +MssymohssmMNMMMMMmm.Ndooo-dsmdd+dso\r\n" + 
			"y+mdhm+/+o+M+ommmNN+mmMmNdsyhysmNyhhhyyssyNss+++++++oooo+hmddmNNMMdo+oo///-.`` `````.........-------:///+oshsoo+/:-..````+Mysym+ddmMMmshNNmmm.Nd+oo-dymdh+hss\r\n" + 
			"yomdhm++so+M+odmsyy:hhMhmNMMmysmNNNmddyssyNssooooo++++o++/ohhdmNNNNNydmmd+/--.`..````........---:::::///+oshsoo++/:-.```-sMssdNMMmyMMh/shshdd.Nd/so-dymdhohsy\r\n" + 
			"y+mdmd+oys+M+odmo:/+hhMyyhsNMMNNMMMMMMMNmmNhyooooo++++oyyyyyhmmNMNNNMNsys+//++:-.` ```....-----:::://///++oysooo+/::-..``oMmMMNydyoNMy/o//hdd.Nd/yy/ydmdh/dss\r\n" + 
			"s/mdmhssdh+M+odms//oyhMysyymNMmmMMMMMMMMMMMNNNhhhyyyysosysso+ymmmmNNMMm++//++o/-.`` ..-::::-::://///++++/++yssooo++//:...sMhMMNhhyoNMs:y//hdd.Nd+mh+sdmdh:dso\r\n" + 
			"o/mddysshh+N++dmo/y+hhMys+ymNmhdMNNNNNNmmmNhhhsooooo++++ooo++++dmdmNNMMNhdddds+:::.`.-:::::::///+++++++++++yssssoo+++/:::yMhhNNhosoNMy-yy/hdd`md+my+symdh:hs/\r\n" + 
			"o:mdds/syyoN++dmooo/hyMysoymNdddMdddddhhhhNyyssoooooooo++++ooooohmmNMMMMMNhso+++/++/--------:--::/://////+ohyssso+/+++:::yMhyNNhoyoNMy-osohdd`md/hy/osmmh.yo:\r\n" + 
			"NdNhmyosysoN/+dmsshodyMhyysmNdddMddmdhdhydNyyysssooooooooooshdmmdhmNNNNNMMmso+++/////:-::::::::///+++ooossshhysso++++/+//hMhyNNhsysNMs/ddshdd`mh/hy+sdmmNdMMM\r\n" + 
			"NNMdMMMMMMMMmmNmdhdsdyMhsysmNdmmmdddddhhhmMhhyyyyyyyyhhhhhyssssoo+sdNmmNmMMNhhyysy/::://::-:::::::://++osssyhyyssooooo+++yMdhNNyyysMMs+dddmdNhMMMMMMMMmNNmNMM\r\n" + 
			"./MdMMMMm+NMhdNNMNNMMMMNmmmNNmmmmdddddddddNddddddddhhhyysooo++oooooohmNNNNMMMNyo++:::///:----:::::://++osssyhyyssosssss++yMdhNNmmmNMMMMNmMMmNyNModNMMMmNh-sMN\r\n" + 
			" -mdMNmhd+MM//NNNhmMyyMNNNhNMMNNmNNNNNNNMMMMMMmddhyyssssoooo++++oosyhmNMMMNNMMNs++//::::-------:::///++oosyhhyyysoooooooohMmMMMdNNmMN/MNhNMmd NMydhmNNmmy sMm\r\n" + 
			"`-mdmmmshyMM/:mNNhdMsoMdyydhNmsNMMMMMMMMMMMNmdhyyyyyysssyssoooyyhdmmmdymNNNNmMMMmdsssoo:------::::////+++oshddhhyssysosoodMyhmNhdshMN.MmyNMmh NMmyodmdmds sMN\r\n" + 
			".:mdmmmsssMN/:mNmyyhyoMdysdhmdsmMMMMMMNNNNMmdhyyyyssssyysyyhdmmddhhysooodNmNMNMMMMysss+:::::::/::///++/+++oyhhyyyssysyyssmNydhNhdohNN-myydNmh NNNoodmdmmy hMN\r\n" + 
			"-/NdNdmsssMM+:mNNyyhyoMdyymhmdshMMMNNNNNmmMmdhyyyyyyyhhhdmmmddhysoooooooshmNMMNmMMMhso++/:::/:///+/+///+++oyhysssossssysymNshdNddohMN-mhymMmy`NMNoodmdNmh`hMm\r\n" + 
			":+NmNdmhsoMNo/mNNmsyysMdhsmhmdshNMMNNNNNmmMNmdhhhhhmmmmmmdhhyssssoo+oshdmmNNMMNNmNMMNmyooo+/////+++++++o+ooyhhyyyysssyyyyNNshhNddshMN/m+ddMmy.NNmooNddNNd.hys\r\n" + 
			":+NmNhdsyoMms/mNNNssyoMdmsddNdsdNMMMMMMMNNMNNNNmmmNNNNmdhhyyyssssssyhmmmmdhsdNNMMMMMMMdyydyo+++++oooooosssshhhhhysssyyyyyNNyhdMhhydMm:d+mmMmy-NmmsohddNNd.hys\r\n" + 
			"/oNmNhhydsMNs:mNMNshhoMdsyydNmdNNMMMMMMMMMMMMMMMMNNNmmmhhhhhhyyyyhmmNNmdyssssyNMMMMNMMMNdssosssooooooosssyyhhhhhyyysyhyyydMmmmNyysyMm/momNMmy.MNmsyyhdNNd/hys\r\n" + 
			":+Nmmshhh+mms:mNNdshyoMMNMNMMNMNNMMMMMMMMMMMMMMMNNNNmmddddhhhddmmmNmmdyyyssshmNNMMMMmNMMMmyoooooosooosssssyhyhhhyyssyhhyyhMNMNMNMMNMN/dhydMmy-NddoydhyNNd`hys\r\n" + 
			":+Nmdhhho+hmo+NNNNNMMmMMMmhNMNhmNMMMMMMMMMMMMMMNNNMMNNNNmmmmNNMMMNmdhhhhhhhdNNMNdMMMMMMMMMmdyddyssssssyyyyyhyhhhhyysyhhhhmMymMNddMMMMmMMNNMmh-mhh/odhsNNd.hys\r\n" + 
			":/NmdmNdmNMMMmMNNMMMmdMMNmymMmNmMMMMMMMMMMMMMMMNNNNNmmmNNNMMMMMMNmddhddddmNNMMmhyhNMMMMMMMMMNhsosyyyyyyyyyhdhdddhhhyyhhhhMMdmMmmhNNMNhMMMNNNNNMMMmdNNhNmd`hyo\r\n" + 
			"+yNmMMMMMMMMdsMMNNdMmyMdmNyNMNNmMNMMMMMMMMMMMMMMNNNNNNNMMMMMMMNmddddmmmNMMMMMmdhhdNMMMMMNMMMMNhsooyhhhddddmmddhhhddhyhhhhMMdNMNmhMhMmyMMdMNNdyMMMMMMMMmNm+hdy\r\n" + 
			"MNMNNmNMddMMdsNNNMos+hMmmNsNMNNmMNMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNNmmmNMMMMMMMNddddMMMMMNNNMMMMMNmhhdhysssssyyhdddddhhhddhhmMdNMNdhMhMmooyhMNNdyMMNhmMmmNMMNMMM\r\n" + 
			"dhMNNmNMNmyMhsNMmMo++dMmmNyNMmmhMmMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNNNNNNMMMMMMMNdmddmMMMMMmhhNMMMMNNmhhysyssooyyyydmmddhdddhmMsmMNddNhMNooodMmNhymdmmMMNdNMmyMMM\r\n" + 
			"hhMMMMdMMMhNhyMMmMo++dMmdmyNNNNNMmMMMNNNNNNNNNmmmmddmNmmmNmmmmddddmmmNNNNNNNNNNNmNMMMMMMdhhNMMMMMmdhsoooooosyyyyhhdmmdddNNMmNNmdhmhMNoo+dMmNdhmmmNMmmMMMmsMMM\r\n" + 
			"yyMMMNyMMMhMhyMMmMso+dMmhmyNMMNNMmMNNNNNNNNmmmdmddmmmmmmdddddhhhhhhyhhysyyhhhhyyyydhhhyyhyyhhdddddhysoo+oo+++++oyyyssydmMMMNNMNmhdhMNoo+mMmNdhmmmMMddNMMm+NMM\r\n" + 
			"dhMMddsMMMdMhhMMmNyooyMmmMMMMyhmMdNNNNNNNNmmmmmmmmmmmmmdddmmdddddhhhyssyyyyssssssosso+/:+o+ooo+:/ooo++/+oo+/:////++ossshmMMyhdMMMNdMdoooNNmMdhmNNMMhdyMMmyMMM\r\n" + 
			"dyMMhdsNMMmMmdMMmmh/oyMMMNhysdmdMdNNNNNNmmmmmmmmmmmmmmdhysso+///:/::///::::-.....--.......--.-:-::-:++oooo+++++++++ooosyhmNhdhshmMMMmoo+MmmMmdNMNNMhdsMMmyMMN\r\n" + 
			"mhMMhyyMMMdMdyMMdmmmdMMNhosyhNmdMdNNNNmmmmddhyo+/----::/++ossyhhhyyssyso/-` `-:.  .-//+++oossssoo++//::-....-:/+ossssysyymNdNmyyosdMMmmhMmmNmhNNmNMhd+MMmdMMN\r\n" + 
			"NdMMhhyMMMdMd+MMNMmdNNMdddMddNNdMdmmmmmy+:`   .:+osssoo+++oo+++++++//:.   .:///++-`  `.://++++++++++++++///:-.`   .-/+osymNdNNhNNdsMNMmhMMNNhoNmNMMhm+MMNdMMM\r\n" + 
			"NmMMhdhNNmhMdoMMMdNMMmMhddNdmmmdMmmmmh.    +ddyysso+/:::::://+ooso:` `:oyyssssssyysso/-` .:+o+//:-------:::://+o+.    -ysdNddNdmNdsMmMMNdNMMdoNmhNMhmsMMNNMMM\r\n" + 
			"NmMMhhhMmhNMNMMMNMMMMNNdddMdmddmMmmmmd:    :ydhhhddddhhhhhhhs/.   ./://///:/+++//:::::::-:.   -/osssoooooooo++++:`    -+sdMhhmdNNhyMmMNMMNMMMMMMhdMdmsMMNmMMM\r\n" + 
			"dhMMdyyNNmMMMNMMMMMMMNNdddMhdyMMMddddddho/-` `-/+syhhhyhyyo:`  `-///:::::::::///::::::::::/:.   `-+ssso+++//:.`  `.:/oooodMMdhhmNhyMNMMMMMMMMMMMNmMydsMMmyMMM\r\n" + 
			"yyMMddNMMMMMMNMMMMMMMMNddhmydMMMMddddddddhydhyso+/:------  ..:/+osssyyyssssssssssssssssssoo+//--``  .-....--::/+o++o++oosdMMMNyddhhMNMMMNMMMMMMMMMMNMyMMdoNMM\r\n" + 
			"+:MMMMMMMMMMMmMMMMMMMMNmmhmNMMMNmmdddddddhdhhhhhhhhhhhhyysso+/+///:::/::::::::::::::--:::::::///++++o+oooooooo+++ooo++osyhdNMMNmdddMmMMMMMMMNNMMMMMMMMMMy`NMN\r\n" + 
			"ohMMMMMMMMMMMmMMMMMMMMNNNNMMNmmmmmddddddhhyhhhhhdhhhhhhhhysssyyyyyyyyyyyysssssssssssssoosoosssoosoooooooooooooooosssosoydmdmmNMMNmmMNMMMMMMMNNMMMMMMMMMMNohNm";
}

