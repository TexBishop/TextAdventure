/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.Forest;

import Items.BasicItem;
import Items.Item;
import Rooms.CountdownRoom;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class ForestClearing extends CountdownRoom 
{
	private static final long serialVersionUID = 1L;

	public ForestClearing(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "ForestClearing";
	}

	@Override
	protected void initializeCountdown() 
	{
		//=================================================================================
		//Death after third command
		//=================================================================================
		this.setCountdown(3, "You feel as if some oppressive presence is gazing upon you, disapproving. ", 
				"You're feeling a bit light-headed and woozy. Something isn't right. ", 
				"Your vision begins to go black, and you feel yourself slumping to the ground. As conciousness leaves you, "
				+ "you find yourself wondering... why am I here? ");

		//=================================================================================
		//Count down starts and progresses unless the brooch is in inventory
		//=================================================================================
		this.setTriggers(null, this.gameState.getFlag("brooch in inventory"));
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData (forestClearingImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "You come upon a clearing in the forest. "
				+ "It's better lit than the path you've been following, with the canopy being thinner here. You can actually see the sky. "
				+ "In the center of the clearing is a small altar, made of stone. "
				+ "To either side of it are two statues, identical, that look like the same seated woman as the figurine "
				+ "beneath the large tree.  However, neither of these is holding a basin. ";
		if (this.gameState.getFlag("praying completed").isFlipped())
		{
			if (this.gameState.getFlag("zippo taken").isFlipped() == false)
				this.description += this.gameState.getFlag("zippo taken").toString();
		}
		this.description += "The path you followed to get here trails back into the forest behind you, heading west. ";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Forest Path
		//=================================================================================
		this.addMovementDirection("west", "ForestPath");
		this.addMovementDirection("tree", "ForestPath");
		this.addMovementDirection("path", "ForestPath");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create holy zippo lighter
		//=================================================================================
		Item holyZippo = new BasicItem(this.gameState, "Holy Zippo", "", "A zippo lighter with the \"eye within a pyramid\" symbol on it. "
				+ "You came by it in a rather unusual fashion... Could it possibly have special properities? ");
		this.gameState.addItemSynonyms(holyZippo, "holy", "zippo", "lighter");
		this.gameState.addSpace(holyZippo.getName(), holyZippo);
	}

	@Override
	protected void createFlags() 
	{
		//=================================================================================
		//Flag for whether the basin on the altar has water in it
		//=================================================================================
		this.gameState.addFlag("basin filled", new Flag(false, "The basin has some old, dried out flower petals lying in it. ", 
				"The basin has some water in it, with some old, dried out flower petals floating on top. "));
		
		//=================================================================================
		//Flag for whether the player has prayed yet
		//=================================================================================
		this.gameState.addFlag("praying completed", new Flag(false, "", ""));
		
		//=================================================================================
		//Flag for whether the zippo has been taken
		//=================================================================================
		this.gameState.addFlag("zippo taken", new Flag(false, "A mysterious zippo lighter sits on the lip of the altar. ", ""));
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
			
		case "pour":
		case "fill":
		case "put":
			//===============================================================
			//If command is to pour water into the basin
			//===============================================================
			if (command.unordered("water|dasani|bottle", "basin"))
			{
				//===============================================================
				//Verify that the player has water in inventory
				//===============================================================
				 if (this.gameState.checkInventory("Bottle of Water"))
				 {
					 if (this.gameState.checkFlipped("basin filled") == false)
					 {
						//=================================================================================
						//Flip the switch for basin filled, and set the display data
						//=================================================================================
						this.gameState.flipFlag("basin filled");
						DisplayData displayData = new DisplayData ("", "You pour some water into the basin. "
								+ "The dried petals swirl around, floating. "
								+ "The atmosphere in the small clearing somehow becomes expectant. ");

						//=================================================================================
						//Use the Bottle of Water, which will cause it to break, and create an empty bottle
						//=================================================================================
						DisplayData bottleData = this.gameState.getSpace("Bottle of Water").executeCommand(new Command("execute usage", ""));
						
						return new DisplayData("", displayData.getDescription() + bottleData.getDescription());
					 }
					 else
						 return new DisplayData("", "The basin already has water in it. ");
				 }
				 else
					 return new DisplayData ("", "You don't have any water. ");
			}

			//===============================================================
			//If put command not recognized
			//===============================================================
			return new DisplayData("", "That doesn't seem to do anything. ");
			
		case "pray":
			//===============================================================
			//Praying complete, flip flag and return message.  May only be
			//done if the basin has water in it.
			//===============================================================
			if (this.gameState.checkFlipped("basin filled") == true && this.gameState.checkFlipped("praying completed") == false)
			{
				this.gameState.getFlag("praying completed").flipToggle();
				return new DisplayData("", "You kneel, close your eyes and pray. It feels as if there's some kind of answer, an indistinct feeling. "
						+ "You linger a bit, not sure what happened. But, when you open your eyes, they focus on something new before you. "
						+ "A shiny zippo lighter, sitting on the lip of the altar. It has the same symbol on it as the brooch. ");
			}

			//===============================================================
			//If basin not filled, or already prayed
			//===============================================================
			return new DisplayData("", "Nothing happens. ");
			
		case "grab":
		case "get":
		case "take":
			//===============================================================
			//Take the lighter.  Can only be done if the "praying complete"
			//flag has been flipped, and the "zippo taken" flag has not.
			//===============================================================
			if (command.unordered("zippo|lighter|shiny"))
			{
				if (this.gameState.checkFlipped("praying completed") == true)
				{
					//===============================================================
					//Flip the "zippo taken" flag, and add Holy Zippo to inventory.
					//===============================================================
					if (this.gameState.checkFlipped("zippo taken") == false)
					{
						this.gameState.flipFlag("zippo taken");
						this.gameState.addToInventory("Holy Zippo");
						return new DisplayData("", "You take the mysterious zippo lighter. ");
					}
					else
						return new DisplayData("", "You've already taken that. ");
				}
			}
			
			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|room|area") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "While this seems like a nice, sunny spot in the middle of this shadowy forest, "
						+ "it has a heavy feeling around it, a sense of foreboding, that ruins the image. "
						+ "The encroaching forest at the edges of the clearing seems particularly dense here, for some reason. ");

			if (command.unordered("altar"))
			{
				String description = "It looks to be a construction of a brick column, with a concrete slab on top. "
						+ "In the center of the slab is a hollowed out basin, a couple of hands across. "
						+ "Carved into the slab around the basin are images of worshippers kneeling and praying. "
						+ this.gameState.getFlag("basin filled").toString();
				if (this.gameState.getFlag("praying completed").isFlipped())
				{
					if (this.gameState.getFlag("zippo taken").isFlipped() == false)
						description += this.gameState.getFlag("zippo taken").toString();
				}
				
				return new DisplayData("", description);
			}

			if (command.unordered("statue|statues|figure|figures|figurine|figurines"))
				return new DisplayData("", "These statues are much larger than the one beneath the huge tree, "
						+ "and they have no basins, but seem otherwise identical. They seem to be hewn from concrete. ");


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
	private final String forestClearingImage = "MMMMNMMMMMNNmMmNMNmdhhsdhhdyydyyhyydmhyyssssd+yyNhyyhmmmdmmmdyhyyNMs:s/::-...yy......:/:..--------..-.-::/++++ods/+y++sh:yods:-o/o+-....Ns+s/--ym/Ns/so///++/\r\n" + 
			"MMMNNMMMMMNMMMMMNmdhyhysdyyddhysosdydyo:+ysyhsydmhhhhddmhy+oshydNmNooy:-....+d-.....-/----.......--....---:::oyyy:sy//osdsmmo::s+s+-....my/y/+:sNoMo:so/::://\r\n" + 
			"MMMmNNMMMMMMMMMNmymhhmhshhyhmdyhshmNdyo/osoydyyhm+ooyddhshsoyymNmmmoy+::::-.ho..-..-:..-..........---....---/s++m:/y+:o:omNN+/:+sy::::::dh:h+/+mmyN+:os/:----\r\n" + 
			"NNMNmhNNdNMMMNNNdhhdmhhsyhyddydhydMMhhhsssso/sydmyssymNdhyyysNNhydmss::/:-:.hs./-.-:`.-.....`.`....--:-.`.-///o+ho/ss/ysosMN/:-+oy.--..-dm-o:-:hdyM::++//o+//\r\n" + 
			"hNNMMMMNmdMMMMNmhhhhdhhhdhhosshmmNMNyhssyyhssss+sooyyddyyhyhdNhysdmh+....``.sy.h///:`     ````````.....-:--//:/++h/+h:yhoymN+::oos.-...-sN.o+--smhN.:os+:-.::\r\n" + 
			"NMNdNMMMNmMMMNNddhhhdhhhhhdyyy+hyNmdyhyy+odyddddhos+yssssyhhNdsyhdNh```     :d:o`.-/.      `````.....-----/+o////hssd/hs/omMo//o/s.`...-+N:/+-.oNNNo/oo----..\r\n" + 
			"NNMMNNNNhsmMNNMddhddhhydddddyhsyhMhhydhdhyyyhhdhmyyshdNdhmhmhyyhhyNh`        dm-``: `       `` `   `.-:::::://:-:ohod:h::+hMh-:y/s.`....-Nh/s``:NMmh/o+----::\r\n" + 
			"NNMmhyMMmmNMNMNdddmdddhyhmmdyhddmMNdyyyyyyhdhyhom+oymmMdNdNdhhyhhyNs-        os` `.   ``              .---://:///sh:d/so+odNd-/sos...`...hN:y-`.NM+yos+-::/:/\r\n" + 
			"NNNMMNMMNMMmNMNdmmmddddhdddmmhhdNMNhhyydysssdhhyNysydyNohhMshs/+/oN/``.-``.``d- `.                   ` `.----::::/Nsyd/+::hMMy/soo.......oM:/o--mN::sso-:-::o\r\n" + 
			"NdmMNMNMMMmdMNdmdddmhhdmmmddhhhmNMMdhhhhhyy+hyyomhyyhyhymMNohsyoohdos-::.`` /h ..                      ``...---+/-dmod++..+MMo.s++`..-..`+N:.y..mM+-yhs/++o/+\r\n" + 
			"mmmMMMNmNNmNNmmdmhdmmmmmmdddddddMMNhhhosyhhyyssohmyhshhyNMdhyoos+hmyyo:...:.ss-.                    `.``....-.-//:/dymyhoshMNd+y++...````.No`y-.dMo:yy+++oo+o\r\n" + 
			"mNmNNNmNMNMMMdddNmmmmmdmdmmhhhdhMMNhdysossyhyshshNhdydmyNMmmysydydds++///` `d+.                     `.-:--::+//+o+/yNmdhsymMMmydds-.``````Ns.s+.dMy:ydyyhyyso\r\n" + 
			"MNMMMMNMMNMMNmmmNdmhmmdddNhshddmMMmhdysooysdsysyymyyoydhMNmsdhyosddhyosso:-+N-`.`                  ```..-:::+/yyyyyyNNdhhdmNMNshss::/:--..dm.y:.sNyyymddssoo+\r\n" + 
			"MMMMMNNMNNNMmhhNNdmdmmmmNmmhhhmNMMhhysosyhyhhhhydNhssddhMMmsdhsyymmhsosyssshh::``           ````..-----`.-:++oshyhdhmNdmddNNNNhdhdyo+//:::dN+ho/hNy+smhssyhys\r\n" + 
			"MMMMMNNMNmMMmdhhNmmmmmmhysyyhhNMMMddhossyyyyyyyyhNhyyhdyMNyoyyyhdddyyssyhysds-``````````````````.---.--..//ooydmhydhdMmmddmNMmmmddhsso+/+ohNyhhyNNhsymyyyhyhy\r\n" + 
			"NMMMMNMMMNMmdyhdNmdNNNNNNdhhdddMMNdhyssyyyyhyyyyhMyhyyhmMmhhdyhyoddyyossossh-..````.``````````.`..---/::--/oosssshydyNNddhdMMhNmmdyooo+osoyMmydyMNhssdyyhyhyy\r\n" + 
			"NdMMNNMMNMNdoydNNmNmdmNNmddhhdmMMNshshhdsyddhhdyhMhhddhdMmmddhhdhddssssyo+mo/--...-..```  ```......:.-///-/ooyyhyhhm/hNdhydMMmNmmhysssssyosNNomoNNhyhdmhyhhdh\r\n" + 
			"MMMMNMMNNMmhyddmNNNmdmNNmmhyssmMMmsshhsyyyyhyyhmNMdhdddmMddhyyhhhdsoso+/ohN/:---.--::...`..:.//:--.:-.-/o/:+syhdmNmdhoNmyyyMMdmddsysyshhssydMyhyNMdyhydoyyddd\r\n" + 
			"MMMNMMMNMNmddmNNNNNmdmmmhdhyhdMMMhhdddhyyyyyyyyhyNhdNmmmMMdsyysyhdysys+/+Ny--::+//:+::/:---+:/-:/::/:.-:+s/ssysyyd+hhomNsmhNNdyyhhyyyhhdyshdNdyNMNdhdsdhhhhhm\r\n" + 
			"MMNMMMNdMmdhdhmNNMNNNNNmmhdhdNMMNhmmddyyyyhhhsyysmmhyyhdMNyymdhyyNdhhssoyN////+o+o/+/////.:+//:/-:::/-/+osshhhdNddmyshhMhdmmmdhyyysyhyydysyyNNhmMMmdmyysyyddm\r\n" + 
			"MMMMMMmmMmhhddmmMMNNNNNmmhhmmMMMmdNNddyyhydddhdssmmsyo+sMmhdNhdddNdddhyhNdo++///+soo+/:://.+:/::+oo/++ohhdmmdhhmmdm+ydyNdhmmNmdhyyyyyhyddhsyNNhdMMdhdhdhyhhmm\r\n" + 
			"MMMMMMNMNmmddddNMMMMNNNmmmmNNMMMhhmmddhhhyhhyhddddNyssohMdyyhsoshNyyhhdmNhoosoosssss+o+/:--: /+oooo++ydmdmmmmmNmhdmsymdmmdNNNddhhhyydshhhhsyNMddMMdhdhsyhddmN\r\n" + 
			"MMMNMMmMNNmddmdNMMMMmdmmmmmNNMMNdyyhysyyyhhhyydmyyMhdhymNyyshysymmmddmdNNhssssssosyyyso/--/o-oo++syyhdhdhhhddddmddNhymmNMmNNhhmhhhyshhhmdhshMMmdMMNddhyyhdmmN\r\n" + 
			"MMNNNmNNNdddhmmNMMMNNNmmmNNNMMMNsosssssyhyhdsyNdyhMhdNdMmssmmhhhdddmdmmNmdyssyyysshssyyo+::ossysssshyyyshhhdddhdhdNdhmmNMmNddmdhhdysyhmmdhhmNMmmMMmmdhhddmmNN\r\n" + 
			"MMmmddNNNmmdNNmNMMNNNNNmdmmhMMMmyssssyyydyhmhdmyhymdhMMMNNsmdhhhNmhddmNNdy++yyyhyydyyyyhyyshdddhsyhhyyyysshhddhdhdNddNdNNNNNNNNdhdhdhddmyhhmmMNNMNNNNmddmmmdd\r\n" + 
			"MNmmdmMNmmmNNNmNNmNmNmmdmMNNMMMmyhyyyyshdhdmhyhhssyNhMNMhmyMhyyyNmmmdmNmhhoosydsosdhyssssyyhmdhhhhdNdhyhhhdhddddmdNhmNmNNNmNNmmmdmdyhhdmhyhmNMMNNMNmNmmmNmddh\r\n" + 
			"MNmhddNmNNNmmNmMNNNNNdhdNmdNMMNhyyshhdddddymmsmyyyyMNMMNymsNhoshNNmmdNNhyy+oss.```yddhyssyhddmdhhdmNmhhhhmdmNmo``-dmNNmmdMNMNNmddhddddmdhyyhdNMmNMMNNNNNNmNNm\r\n" + 
			"MNdydmNmNmmdNNmMMNMNNdhmmmhNMMNhdyhdddddmdhNddhhssyNMMMNymyNhooydNddmNmysyo/++/:--/hysssssyddmdddNNmNNNmdhhhhh-.-.sNNNmdhMMMNNmNmdddhhmdhhyhhmMMMMNNNNNNNNmNN\r\n" + 
			"NNdhdNmdmdmNmmmMNNMNNddNmdmMMMmhdhmmmmddNmmNmhhdossmMMMmhmdMdsssdNdhNmmhddhyydh+-::ysshhyyssshdmNMNNNMNhssysy+:-:oMmNmhddNMMMMmmNddmyhNmddddhhNMMMMMNMMNmNNNN\r\n" + 
			"dNdhmNmmmmmmNNNMNNMNmNNMmdMMMMmyhhmddmymddmNhhhdos+dMMMmhNmMhsshmNmdMdmddhhyssh+...-+dmmhyyhysmmmmNNNmmmhhyhs:.-/dNmNmhyhNMMMMmmmddmddNNdddmmhdMMMMMMMMMNNNMN\r\n" + 
			"NddhmNddNmmNNNMNNNMNmNMNNNMMMNmhmmdddmhNhNmmyysdyssmMMMmhNNNhsymmNmNNdmddhsyos+` `---hyssosss++ossyssoooooo/`.   -NmNmddmNMMMMddNdhNhdNNmmmNNmNMMMMMMMMMNNNMM\r\n" + 
			"mddhdmmmmmdmNNMNNMMNmNNmmNMMMNhhNNNmdmdmmhhhyyshyyymMMNddNMmyshhmNNMmyhhmmdmdy/``--                        .`--`.-mmNddmmmMMMNhhmddmddmmddmddmNMMMMMMMNNmmmmd\r\n" + 
			"mhmhmhdmhyshdmMNNNMNNNmmdNMMMmshdmmNmdNNdydhyhhhdhhhMMNhymmhysmmdmNNdyyyyddms--- `--/yyo/////////////:-:yyhs.    .-/NmhhddmNNmhdmdddhhhysyhmNMMMMMMNNNNmddysh\r\n" + 
			"NmdddmdmhhshyyhhmNmNdhysmNMNMmoooyydhmmNhhhyyyyyhsyNMMNyhhhsyhysooos+oossyyyyso.`.//omdh::::::/+/////-.-mmmy:-.`.oooooooooosssyyyyhhhhhddmmddhddhhyyyyyhhhhhh\r\n" + 
			"yhyhhhhhyysssooyydyosyyyodNmmd++:+/ssyyshdddhddhhhhhyhssoooosossssosssssos+`    ``.--:yy.``````.`...-. .yyo:```     ./oooooooooo+o+ooo+++oooo+ooosssssssssyyy\r\n" + 
			"oyoo/+oo+o++osss+sy+oos++ysyoy+o+oo+osooossossooso+++///////+/+o+++o++++ooo:....-.-/++oo       ` `` .`-+yyo:-````````:+//+//++//++++++++++ooooo+oooooooo+sysy\r\n" + 
			"so+syysoo+/ssysosossssoo+++++o/+ooooo++ooooooo++////////++///:+oooo++++sssyysssso++oyhhy++oo+ooo+o+oohhhhhyo//:/:++/oyyo++//+++++/++++oo/ooooooosssoooooossyd\r\n" + 
			"hsosyosysyyyyo++ososso+oooo++oo+++ooo++++o+/++++++++/+o+/+////////////++++osysssyyyhdhhhhhhddhhhdhhhyhhyyyhhyhyyyyhyysso++////++++o+++o++ooo+oooyhysoooshdmNM\r\n" + 
			"hmdhdhhhhdhhhhssyyso++ossyddhysysooooooo+/+///++oo+++oooo+o+++o++/+++o++/+osoooooossyysyssyhhyyyyysssoss++oooosossoos++++oooo+++oooooo++++ooss++o++ooyddhdddm\r\n" + 
			"NNmmdmNmmdmmddyhdhyyhyhhydmdhsyhssso+o+ooosssyosyssso+ooo++o++++++++o++o+/oo+ooossssssyyysyhyhyoyssssoooooossyyyyssoo++oossoooo+o++++++oooshmhyyyhhhdmNmmmmdm\r\n" + 
			"mmmmmmNmNmdddddhdyhhyysyyhhhhyysssyyhhsssossosoooososyyyyyyysossyssoosyysossyhdhhyysyyysyyyhyysooooyysooooosssyyhsssoooooyyssyyysyssssosysyhhhhdhddmNNNNNNMNN\r\n" + 
			"ydmmmmNNNNmmmNNNmmmdhsyhhddddhyyssyyddddhyyyhyyyyyyyyyyyyssysyhyyyyyysssyyshyhhyssssosoooooos++ooooss++ooosyyyyydysoosso++sso+ooooooooso+oooo+oosydNNNNNNMMMN\r\n" + 
			"hhdddmNNmddmmmmmNNmmddhyhyyshhsssyyyysydyhddhyhyysoyysssyyyyyyysyyyssosyyyyyosss+osssysso+ooso+oosossooosyhydmhhhhsoo+oo+os+oooooooooososssyhyhhdmNNNMMNNNMNN\r\n" + 
			"yyhhdddddddmddmNNNmdhdhhyyhhhdyyssyys+sosyyhddyhhdhhhhdyhddddhhhhddhyyhyyyyyssooosossssyhyhdhysyyyyyyyooydyhhhyhhysyssssyooooosssyssshmdmmNNNNNNNNNNNNNNMMNNN\r\n" + 
			"hsyhhhddmdydhmmmNmmhhhmdhdmmddhyoosoysyyooossyhddhyhyhyhhdddddmdsmhdmdhhdddmdmhsshyyymhyddmddhhdmdddddhhdhdddhyhhhoyhhhdhdyhysdhdmmmmNMMNMMMMMMMNNMNNNmNNNmNN";
}
