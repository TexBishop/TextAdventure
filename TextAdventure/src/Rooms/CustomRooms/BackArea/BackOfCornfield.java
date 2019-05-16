/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.BackArea;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class BackOfCornfield extends Room 
{
	private static final long serialVersionUID = 1L;

	public BackOfCornfield(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "BackOfCornfield";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData (this.backOfCornfieldImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "There's a narrow strip of open ground here, between the dense forest and a dead corn field. "
				+ "Over the top of the tall corn stalks, off in the distance, to the south, you can see the farmhouse and a large barn. "
				+ "A worn path leads out of the cornfield, heading east into the forest. ";

		//=================================================================================
		//Different descriptions based on whether the gate has been unbarred yet
		//=================================================================================
		if (this.gameState.checkFlipped("gate unbarred") == false)
			this.description += "The path is blocked by a closed gate at the edge of the wood, the foliage closing in tightly upon it. ";
		else
			this.description += "The gate on the path, at the edge of the wood, is sitting slightly ajar. ";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Forest Exit
		//=================================================================================
		this.addMovementDirection("forest", "ForestExit");
		this.addMovementDirection("east", "ForestExit");
		this.addMovementDirection("path", "ForestExit");
		this.addMovementDirection("gate", "ForestExit");
		
		//=================================================================================
		//Create directions that move to Backyard
		//=================================================================================
		this.addMovementDirection("farmhouse", "Backyard");
		this.addMovementDirection("farm", "Backyard");
		this.addMovementDirection("house", "Backyard");
		this.addMovementDirection("backyard", "Backyard");
		this.addMovementDirection("west", "Backyard");
		this.addMovementDirection("trail", "Backyard");
	}

	@Override
	protected void createItems() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createFlags() 
	{
		//=================================================================================
		//Flag for whether the gate has been unbarred.  Create only if it doesn't already exist.
		//This flag also used in Forest Exit room.
		//=================================================================================
		if (this.gameState.checkFlag("bate unbarred") == false)
			this.gameState.addFlag("gate unbarred", new Flag(false, "The gate is barred closed with a piece of heavy timber. ", 
					"It's sitting slightly ajar, the bar once holding it closed lightly off to the side, on the ground. "));
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
			//If try to enter forest through the gate, verify that the gate
			//is unbarred
			//===============================================================
			if (command.unordered("east|gate|forest|path"))
			{
				if (this.gameState.checkFlipped("gate unbarred"))
					return this.move(command);
				else
					return new DisplayData("", "The gate is barred closed from the other side. You can't get through. ");
			}

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

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The strip of open ground here is narrow and long, the forest and the cornfield running parallel to one another. "
						+ "The grass is overgrown, up to your knees. "
						+ "It looks like there's a trail heading west through the tall grass, skirting around the edge of the corn field, "
						+ "back towards the farmhouse. ");

			if (command.unordered("gate"))
				return new DisplayData("", "The gate is contructed of heavy timber, with several crossbars, and chain link covering the gaps. "
						+ "It hugged so closely by the foliage, that it appears to be a door through the greenery. " 
						+ "There's presumable a fence as well, but it's completely invisible beneath the brush. ");

			if (command.unordered("corn|field|cornfield|stalks"))
				return new DisplayData("", "The corn stalks all look to be dead, brown and yellow in color, no green to be seen. "
						+ "How strange that is. What could have caused the entire field to die in such a way, all of the corn stalks whole, intact? ");

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
	private final String backOfCornfieldImage = "////////::::--...-:------://////////////:/::::/::::://+///////::::--:///////////////:::::-/::yNhmMMMmNMhhmNMMMMMMMMMMMMMNNNNMMMMNdNNmmNNMNNmmhsdmmmMmNNMMMMNm\r\n" + 
			"////:::::-----........-----::::::::::-::---::--.-:::-::::::::-----::://///////////+//oyso::/hNNmNMNNdNmyNNNMNMMMMMMMNMMMMMMNmmmNNdhmNmmmoohddNmmmNddmmhNNmmNm\r\n" + 
			"::::::::--------.........---------------------------.---:----:::::::://///:::::::/oyyyy+oo//hmmNMNdsydNdNNMNNmMMMNMMNNMNNMNmmmmdmmhmNdmMmmmNNMNmNNNmMMNmNyydd\r\n" + 
			"............................----------------..----..------------::::::::::-------:ssdNdhsshyydmmNddmhmNMNNNMMNMMMNNNNNmmNmmNmmdmddNmmNMMMMdhdmshdNNmMNMmNhhmm\r\n" + 
			"...``````....```.``.```...........................`.`..------::::::::::::::-----./oydmmdyodhNNMNMMMMNNmmNmddNNNNdmMMMNNNNNNNNNNmmmdNNMMNNmdhNd+ydyyhyNNmy/+dm\r\n" + 
			"``````....`  `..`...........------------------------.`..-::::::::::::::::::------./yNmNdhhymNdmNMMMNdshmmhNmNmmNmmNMMMMMMNNNNmmmMNNMNNMdNNNmNmdoo:+ssmMmyoshy\r\n" + 
			"`````````.   `..`.-------------:::::::::::::::-------..--.-::---:::::::::::----:-::+ohyyyyhdmddhmNdssodNmymMNNddNNNNNNMMMNNNmmmmNNMMhyNmNMMMMNmy//s+dhdNhhhys\r\n" + 
			"````````  `...--.--------::::::::::::::::::::::-----.....`..--..---::::---...../+syhhdmNmNMNdNNyyosdmmdhdNMMNNmNmddmNMMMMMMMNmddmdmhsymdmNMMMMNmmdy++oymmhyo-\r\n" + 
			".........` .....---::::::::::::::::::::::::::::::---.......-.......----..`.//+osydddNNMMNMMdNMNmNmysmmysodNNNmmmmNNMMMMMMMNmhdmmMMNmdhNhomNMNNNMMNmmNMNNMdohh\r\n" + 
			"...............------::::::::::::::::::::::::::::::----.....``......-o/-:-shdddhmNMMMMMNNNdhMMNMMMMmyoyyhyhdmdNNNMMMMNNNNNNhmmhyddmNN+ydhddydmNMMMMNNMMMNNmmN\r\n" + 
			"..............-------:::::::::::::::::////::::::::::--------......---+yysshdmdhymNMMMMMdhMNmNNhmmdNMddhdyyddmdNNNMMMMNNMNNmNNNdysydNMdymdh+odNdNNMMMMMMMMNNNN\r\n" + 
			"....````.......-------------::::-------------::::-----........```....`+yydddmyshNNMNNdhhhdddddmmmNNMdssshyddNNNmmNNNMMMMNNNMNmmmdhdmddohsshNNMhmNMMNMNNMMhsyd\r\n" + 
			"``````````````....----............```````..------......````````````` `/yyyhdNdyssyhdmhyyyhmmsydmMMMMhsdddhhhNMMMNNmNNNNmmmmmNmhmNMNmhdyhmNNMMMMMMMMMMMMMNd/ym\r\n" + 
			"              ````..............```````````....````````              :+oodhmmhdhhdmmNNmdmdmNdmdhNmNdyhNNdyhmNNMMMMNNmmmmdMMMNhoyNmNmmmddNNNMMMMMMMMMMMMmMMyhm\r\n" + 
			"                        ```````````````                             `/ysymhdddhhddhyyyshddNNNdddNmdosddmddmNMMMNmNNdmhdNdNNNNNdhhmmmhydyhNNMMMMmNMMMMMMNMMmod\r\n" + 
			"                                                                 `-``:/hhhyddhdmmdss+yydmhdyhyyhNmNNdNNNmNNNNNNddmmmdmdmmmmmmNmdmdNmdsdhymNMMMMNMMMMMMMMMMMos\r\n" + 
			"                                                                -/ys+/sysso/ooymmmdhdddmmhdmdmmdNmMNNhdmNNmmNNNNddNMNhddyyNmmNNmmmNNdhhmydNMMMMMMMMMMMMMMMMms\r\n" + 
			"                                                                -+oshhmmdyyddhdddmydmmmNNNNNNmhddNNNNshyhhmNNNMNmNmNNmdmysdmmNNmmmmMmhydyhmMMMMMMMMMMMMMMMMMN\r\n" + 
			"                                                                `///sdmmhhdNNNmddhmmNNNNmmmhydyhdmmmNddydmNddmmNMNNNMNddhhmNdNNNmhdMMNNMdmhNMMMMMMMMMMMMMMMMM\r\n" + 
			"                                                                `:-/sydhyhddmmmNmdmNNNdmyyymNMNNNmdhmmhddddddmNNNNdmNdsmmdmNmmNNMMMMNNNMM+oMMMMMNMMMMMMMMMMMM\r\n" + 
			"                                                                  `sdy+s:://yoymhdydmdyNNNNNMNNhNhhmNmdmhhmmmNNNMMmhhysNmhshsyMMNNMMMMMMMhhNMMMMhNMMMMMMMMMMM\r\n" + 
			"                                                                  -//-/s//o/hysos++oyhdMMMMMMNmsmydMMNmmdNMNNNmmMNNNmymmmmhhh+mMMMNNMMNMMm/hMMMMmMMMMMMMMMMMM\r\n" + 
			"                                                                  :o+osy+/oydddhmmhsddhmNMMNNmysmddNNdmmNNMMNsdmNdmmdydmNNMNdsyMMMMMNNMMMN/oNMMMNMMMMMMMMMMMM\r\n" + 
			"     .    `..`                                                  ::oosyhossydhddmNNdmNshdmNmddmhdMMNmNdNNMMMMMmNMMNNMNddhhmmmNmdNNMMMMMmmMM//hMMMMMMMMMMMMMMMM\r\n" + 
			"``` `.`  `.--/                                               . :oymmhdoyddNNNNNNdddsdodNNMMNMMNmNNNhmddmNMMMNdNMMNdmmmNMMMMMMNsMMMMMMMmmNMs:+mNMMMMMMMMMMMMMM\r\n" + 
			"``.:`--.```.+/-                                              +oodNdhmdddhNNNMmdhddmhhmMNNMMNmmhyymdmmMMMMMMMdoNMMNMMMMMNMMMMMNomNMMMMMmhdMh`:mMMMMMMMMMMMMMMM\r\n" + 
			"`:- -s++./-:o++:.`                                       ``:+shNmNmmNNNMNmmdmhhdmNdddddddmdmdmdoyddMMMNMMMMMosmMMMMMNMMMMMMMMm:smNMMMMMydMm`.yNMMNNNMMMMMMMMM\r\n" + 
			"-/- -+yo/:`oos/::+`                                   ` :s+syhhmdmmNMNMMMMMNNmddmddhoNNMNmNNNMN-hmNMMMMMNmmMyhMMMMMNNMMMMMNNMN.+NNMMMMM+odM`.oMMNdNMMMMMMMMMM\r\n" + 
			"od/`:ohy:/.+yy+sy/:`                       `   `/-/+o+-/+ohmmydddmdmNNMNMNNNdhdmddNNhmMMMNNMMMM/mNmMMNMMMNNN.+MNMMMymMMMMmmMMM-:NMMMMMN++yM:`+NmmomMMMMMMMMMM\r\n" + 
			"hsy+dym+o+-ssssshos/`           :s   `.:o:-yydysddy+oshhyhdmdhhmNmNNNNmmmNmNmNNNNmmMdmhmMMMMMMNyNdNmddmNMMMh.sMNMMmomMMMMNNMMM+-dNMMMMMsssM: .hmMdMMMMMMMMMMM\r\n" + 
			"hhshmhmyhs/hysssdsyy+:/oo-//./+odms++hmmmddNmNhdddddhyymNNNNdhdmdddmmNMMMMMMMMMNNmNmNhdhMMMMMMN+hdNNNddMMmMy-yMMMMmsNMMMMMMMMMh`oNMMMMNyyhm: -+oNMMMMMMMMMMMM\r\n" + 
			"hhyhddmyhooyyo/odsoyosodNNNNNNNNMMMMmNMNhmNdddhdNdmNmNmhddmMNmhmNmdmmNNMMMMMMNMNNMMMMmNsmMMMMNdymmNNNmhNMdM+:hMNMMhsMMMMMMMMMMM.-mMMMMNssdd/ -hdNmmMMMMMMMMMM\r\n" + 
			"ysdmhdmhyhoosy+hdshyhys+hNNMMMMMMMNMNMNmdmdddddNmmmNNMNmmNMNMMNNmNNNmmNNMMmMMMMMMMMMMNyhmMNNNNmohmMMMNysNmM/:dMMMMyyMMMMMNdNMMMs.hMMMMNsohm: -sdMdmMMMMMMMMMM\r\n" + 
			"ssmmNdNmhsyyoshddmdhmddyyNMMMMMMNMmMMMNNmmmdmmmdmmmNNMNNNMMNMMNMMMNmmNMMMMNMMMNmNmNMMM+ooNNNMMN.yhNMMmyomMN.:mMMMM+hMMMMMh+mMMMd.yNMMMMyohNs`-+yNmMMMMMMMMMMM\r\n" + 
			"yhddNmNmdohy+yNNmNddddddmMMMNMMMMNNMMNmmmmmmddhhddmdNNNNNMNNMMNNMNNmmNMMMNNmNNmmmNNmMMo//mNMMMN-somMMdh+mNm.:mMNMN+dMMMMMdymMMMM-/NMMMMdhdmN-``odhMMMMMMMMMMM\r\n" + 
			"mmmNNmmdoymyohNMhdNddmdmdmmmNNmNmNNNNNNmmddmdhmhhsyhNNNNNNMNNMNNNNNNNNNNNNNmNNdmmmmMMMh-:dMNMNN:o+yMMhh+NMh.+NMNMd/mMMMMMh/hMMMMo`sNMMMhoshMh``:dhMMMMMMMMMMM\r\n" + 
			"NmNMNdyy+dNdsdMMdmNmmmmmd++++++++osyyshsyyyhyshsyommmmNNNNNNNMNNMmNNNMMNmNNNNNNNNMNNNNm..yNmNNN/+osMmoh/NMs.+MMNMy+mMMMMMh:yMMMMd`/NMMMy:/yMM: `ydMMMMMMMMMMM\r\n" + 
			"MNMMNdsm+hm/+dMNmmddmmNNy//////++++++++/++++//::/:ooyshsymNNdmmmmyNNMMMNmmdmmNNMMMmmNMN:.yMNNNN//ssMm/d/mM/.sMMNM+oNMMMMMd:yMMMMm.:mMMMs-/sMMy `+hMMMMMMMMMMM\r\n" + 
			"MNNNMdmMyh+:smMNNdmmNNNy++++++++++++++++++oooooo++/:--::/oydshhodshNNMMMhmmmyddNNMNNNmmo.yMMMdm/:osMN:N:mM--yMMmM:oMMMMMMd:yMMMMM.-dMMMs-:oNMm``-yNMMMMMMMMMM\r\n" + 
			"NNNNNNMNyhyymmMdNmMdMNh++++++++++++++++++oooooooooooo+++////::::++/sshddodmyoydNMMMMMhmy-ymdmdm::syNd:m:dd.-hNMmN-oMMMMMMy-oNMMMM..hMMMs.-omMN``.sNMMMMMMMMMM\r\n" + 
			"MNNMmNMmohmhhdNdNdMmMmo+++++oooo++++ooooooooooooooooooooooooooo+++//////::::::/+++ossoh+:dhsyyh-.yyy/+m/h+`:ddmdm.oMNMMMMy-+NMMMM-`sNMMs--odMN.`.+yMMMMMMMMMM\r\n" + 
			"MMMMNNMdsodysyNdNhNddoooooooooooosssoooosssooossooooosooooooooooooooooooooooo+++++/////-:d+//++`.yh/-sd+o:.+mhhyh.ommmmNms:+mNMMN-`odmNo--+dMN- ./+MMMMMMMMMM\r\n" + 
			"MMMMMmNh++hsodMNNhNdhssssssssossssssssssssssssssssssooossssooooooooooo+ooooooo+++++++++.:ds++oo..yso/yyss--sdsoso/osyysso:-+syysy``oosh+--/yMm/  :+NMMMMMMMMM\r\n" + 
			"NNMMMNmsysdoydNMmhNyyyyyyyysyyyyssssssssssssssssoooooooooooooooooooossoosssoo+++///:::::/+/++++..++osoo+/`/ss/+osso+oooo+//ooysso``+ooo/--/hMNs  ./mMMMMMMMMM\r\n" + 
			"mMMMMNmomhmsydNMNddsyssssssssssssssssssossosssssooooooossoooooo++oooo+++oo++++////+++++//////////:://////-/osoosso////+++++++++++.`/+++:--/yMMd  ./dMMMMMMMMM\r\n" + 
			"MMNMmNNyNhhsohdNMyhyyyyyssysssssyysssyyossysssssssosyysssooosso+++/+/+++///+//////++/+/////++/:/:///////+++++++////////+/+//+++o+:.:ooy:-:/sMms` ./hNMMMMMMMM\r\n" + 
			"MMNNmNNhmhyhodmMmyyyhhyyysyhhyyshyyhhyyssyyyyssyyysyssoooosyhoo+++++o+oosoo+++oo++///+/+/::///////////++++//++o++o++++///+++/////:-:hmh//+oohs+.``-hMMMMMMMMN\r\n" + 
			"MmNmNmNhdhshsyNMyhdmdhyyhyydyysyyysyydhyssyyyysyyyoosssssoooossoo++oooo+sssoosyhsyo+//://:/:////++/////o+++++o+oo+:://+++//++////::+yyo++++++++-``.sMMMMMNdy+\r\n" + 
			"NNNNNmNdhhysyymhysssyhyyhhhhhyyhddddhhsssyhhyhhssysoysssooossoysosossyso+oooo+yhssso++//+//:///:///:+///++////++++o++++-/:++o++/o+//+/+/////:/+-.`-+hdmhso+++\r\n" + 
			"mmmNNdNmmhsoyhhoyyhhyhhhmdhhdddhdyyydyyyhyysyyhshdsy+sohyyydyddyhdhhssooso++o+ohhdhyoo+os+////+/+soo++:/++oo++/++++oooo+++++/+//://:///:///:---::/+++++/+++oo\r\n" + 
			"mmdddmNdmhosossysyyyhmdhydyyysysysyoshoyyhhyshhyhhdhhsyooyhdshhhdhhhyhoooosooossoysdmhyyhsssy//+++/..-+o/:/+++///o++o++///+/++++o++o+++/+///+::.--/:::/:::/++\r\n" + 
			"mdhsshmdhy+++shdyyyyhdyydddmdhysssysyssyssddhdddmhyddosssshyyossyhshddsssohhysosysydysosyyysyoo/://-.-://-///++oos+++s+++/+++oo+/++/+o++/+/+o+://-::://://+os\r\n" + 
			"dmhy+oy+/sdhhdddshhmmdmNNmhhyso+ydhsshmmhhyhdhyyooyhdNmhosys+osysyhddmdydsymhyyyhhmdysoyhyyo+ssso////++++/+/////++os+ooo++o+//+//++o///+//++o++o/:///++//++sy";
}
