/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Rooms.CountdownRoom;
import Structure.Command;
import Structure.DisplayData;
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
		this.name = "Forest Clearing";
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
				+ "beneath the large tree.  However, neither of these is holding a basin. "
				+ "The path you followed to get here trails back into the forest behind you, heading west. ";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Forest Path
		//=================================================================================
		this.addMovementDirection("west", "Forest Path");
		this.addMovementDirection("tree", "Forest Path");
		this.addMovementDirection("path", "Forest Path");
		
		if (this.gameState.checkSpace("Forest Path") == false)
			new ForestPath(this.gameState);	
	}

	@Override
	protected void createItems() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createFlags() 
	{
		//asdf
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
			if (command.getSubject().contentEquals("back"))
				return this.displayOnEntry();

			//===============================================================
			//Change current room and return new room DisplayData.
			//===============================================================
			if (this.checkMovementDirection(command.getSubject()) == true)
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));

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
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "");

			if (command.getSubject().contentEquals("altar"))
				return new DisplayData("", "XXXX");

			if (command.getSubject().matches("statue|statues|figure|figures|figurine|figurines"))
				return new DisplayData("", "");

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		default: 
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
