/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class ForestCliff extends Room 
{
	private static final long serialVersionUID = 1L;

	public ForestCliff(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Forest Cliff";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData (this.forestCliffImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		//=================================================================================
		//Description is different the first time this room is entered
		//=================================================================================
		this.description = this.gameState.getFlag("ForestCliff first visit").toString()
				+ "You find yourself standing at the bottom of a small cliff, about twelve feet high. "
				+ "There's a small clearing here, but not much light, the forest closing in tightly overhead. "
				+ "The path continues on, heading east. ";

		this.gameState.flipFlag("ForestCliff first visit");
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Forest Crossroads
		//=================================================================================
		this.addMovementDirection("east", "Forest Crossroads");
		this.addMovementDirection("path", "Forest Crossroads");
		
		if (this.gameState.checkSpace("Forest Crossroads") == false)
			new ForestCrossroads(this.gameState);	
	}

	@Override
	protected void createItems() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createFlags() 
	{
		//===============================================================
		//Flag to determine if this is the player's first time visiting
		//this room.
		//===============================================================
		this.gameState.addFlag("ForestCliff first visit", new Flag(true, "", "The path begins to narrow, the foliage closing in. "
				+ "It steadily worsens, until it becomes necessary to push through the brush, the path barely visible. "
				+ "Suddenly, you feel yourself falling, the ground dropping out from under you. "
				+ "After a few stunned moments, you pick yourself up off the ground. "
				+ "Thankfully, the fall doesn't seem to have caused you any injury, just a few bumps and bruises. "));
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
		case "climb":
		case "go": 
			//===============================================================
			//If try to climb cliff, return failure
			//===============================================================
			if (command.getSubject().contentEquals("up") ||
				command.getVerb().contentEquals("climb"))
				return new DisplayData("", "You don't see any way up. ");
			
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
				return new DisplayData("", "The path is visible again, thankfully, but it doesn't look like you can go back the way you came. "
						+ "You'll just have to find another way back. The forest seems even thicker and darker here, unfortunately. ");

			if (command.getSubject().contentEquals("cliff"))
				return new DisplayData(this.cliffCarvingImage, "It's a sheer rock face, no handholds visible. "
						+ "It looks like there used to be an old rope ladder. You can see bits of it left dangling at the top of the cliff, "
						+ "but it has long since rotted away.  A few old bits of it are lying among the scrub.  They crumble apart at your touch. "
						+ "You notice something carved on the face of the cliff, but you aren't sure what it is. ");
			
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
	private final String forestCliffImage = "MMMMMMMmhdhhhyhhMMMMyhhyysNMMNhsssyshddmddNNNmmmddmmmmmmmmdddddmmddddhhhhyhhyyyhhddhhyooosyyhhyssyssyyyyhhyyNMMNhyyyooysosyooysysssyyyyysyyyoshhddNMMMMMMMMNN\r\n" + 
			"MMMMMMMNhyhsyssyMMMNshhyyhMMMMhysyyddhmdmNNNNmddydmmmmmmmmmmmmmmmmmmmNmdmmddhhddhhmmdddo+ooyyyyyyyhosyyyhyoyymNMMmhyhhs/+oosssssoosyyssosyyssyyhhNMMMMMMMMMMM\r\n" + 
			"MMMMMMNsosyyyhsyMMMMhyhhydMMMMsosoohmmmdNmmmmNNmmhmddhdddddmmmmmmdyysooosyyyhhddhdmmdddyosyhyssyhsyyyyyyhhhdmNNmNNMMMMMmmdhhyooossyyyssysysssoosymMMMMMMMMMMh\r\n" + 
			"MMMMMMmssyyyyhhyNMMMNyyyyNMMMM+osss+-:////++oyddmdy/:::///+oo+/:-.``.`.........-:/:::/::::ymdysyyyyyyhhyyyhdhhhosyyddmMNMMMMMNdysyyssssyssyyysyyyNMMMMMMMMMNm\r\n" + 
			"MMMMMMNhyhyysyssmMMMmyhNMMMMMdssooso///-.........-.---:-::/:::::::::-:----:-::::::-::::::::os+/:/yyyyyyyyssyyysoooyyyhysyhdmMMMMNmdhdddys+shhyyyyhMMMMMMMMMmh\r\n" + 
			"MMMMMMMdsoo++o+yhMMMNhymMMMMMysyyyossyyhs------..-------:::----:::::::::-:---::::::::::::::::---+ysssssssyosyhssoyyysssooyyyhyydNMMMMNNmhhyhhhhyyydMMMMMMMMNm\r\n" + 
			"MMMMMMNdyo+syyyyyMMMMdhdMMMMMyso+ossyyyys:::/::::::::/:::::-:-:::::::/::::::::/:::::::::::::-:+syyhssysshysysysooyyysossosyhyyhdmydNMMMNmyshdhyyyhdMMMMMMMMNN\r\n" + 
			"MMMMMMNdhhyysyyhdMMMMNhdMMNMMs+ossyyyo///:::////://////::::::::::-::::::---::://:::::::-::::::yyyhysssyyyssyyhyyyysyyysosyyyoyyhhyyhdNMMMMMNmdyshsdMMMMMMMMMd\r\n" + 
			"MMMMMMdhhhhhhhhhhMMMMMdMMMMMNyossyyyyo:::::/:::::::::://:/:://///::----:------::----.------.--shhys+yssssyyysyyyysssyhysyhyyyhhyyhyhmhydNMMMMMNhddNMMMMMMMNdh\r\n" + 
			"MMMMMMNhhhhhhhhhhmMMMMMMMMMMmyyyyyyyyyo/////////:://:::////:::::------.-......----------------:+syysoosssyyyyssosyyssshyyhyyhhhhyyyhhhymmNNMMMMMMNMMMMMMMMmhh\r\n" + 
			"MMMMMMyyyhhhyhhhhdMMMMMMMMMMhyyyyssyhy+/+++//:///:////////:/::::::::::::------:-----::::::::::::::/++yyyyyhyyyyyyyyysossyhhhhhhhhyyyhhhhhyddMMMMMMMMMMMMMNhhy\r\n" + 
			"MMMMMMssyyyyyhyhhyMMMMMMMMMmsyhhhysys////////////////////////+/////////::::::::::::::::/:::://:::::/ssyhyyhhyhyhhyyysssssyyyhhhhyyyyyyyhysyhmMMMMMMMMMMMMNhyh\r\n" + 
			"MMMMMNsyhysyyyhyyymMMMMMMMMmysshhyyyhy+://:-::--:://///+///+//+++///////:::/::/://-://///:/://///:/sosyhhhhyyyyyyyssoyyssyyysysysyyhyyyysyyyydMMMMMMMMMMMNhsh\r\n" + 
			"MMMMMNhhhhhyyhhhysNMMMMMMMMmhyysyhhssho++:-::----:::://++/+++++++/////+++/////:://:-:////::///+++/:+ysyhyyhyyyyysyssysoyyyyssyyyyyhhyysyyyyyyydMMMMMMMMMMmyyy\r\n" + 
			"MMMMMNhhhhhhhhhyyhyMMMMMMMMdhysssyss+/++/:///////////////:/++++++///////////+//////:-//////://+///::+yyhhyyyyyyyyyyssooyhhhhyyysssyysyysyyyhysyMMMMMMMMMMmyyh\r\n" + 
			"MMMMMNhhyhhhhysyyhhdNMMMMMMhyyyyoooo///+//+++++///++++++//:///++////////+o+/+++/////--/////://///////+yysyyyyysssyyyyyyyhhyhysss+oyyyyyooyhhhyhNMMMMMMMMMmyyh\r\n" + 
			"MMMMMmyyyyhyyyhyysyddMMMMMMhyhhso/++///////////////+////////+////:::-:///+//+////+/:://+++////////////+sysyyysyyysyyyhhhyhyso++ssssoossssshhhhhdMMMMMMMMMmhyh\r\n" + 
			"MMMMMdyyyhyyhhyo+++NMMMMMMd+osoo++//////////////////////////++////////////////+///::://+++/::://+++/////yhyysyyo++ohdmNNmh:::::/+oyhyyhyhyshhhhhmMMMMMMMMNhhh\r\n" + 
			"MMMMMmhhyssoshyo//sNNMMMMMho+oo+/:::://////+//++////////////+///////////////+++///--://++++/////++///////oo+//////+ohdmmds/////:+/+ooshyhhyyyyymMMMMMMMMMNyhh\r\n" + 
			"MMMMMdshhyysyyosoyhNNMMMMMhsyys///:///:////::////////////////////////////////////:://////+////+/++////////+///////////++//++//:://///+yhhhhysysomMMMMMMMMmyyh\r\n" + 
			"MMMMNoyssyyyyhyhhhhMMMMMMMmssyyyysyyyo///+//////////////////+////////////////////::+//////////////::://+:/+//+////+///////++///:://///ohhhhhyysyMMMMMMMMMNhhh\r\n" + 
			"MMMMmoosyhsshyyysddMMMMMMMhoyyyyyyhhhddo++++///+//+////////////+/////////////////-://///+///:::///////////+///////:///:////////:///+++ohhyhhyyyhMMMMNMMMMmsyh\r\n" + 
			"MMMMy+//yssyhoooshmNMMMMMMdyhhyyshhhhydy+///+/++/////++++//++/+++//+/////////////-://///////://////////////////////:+//::::////////++/+hhhhyssymMMMMMMMMMNhhh\r\n" + 
			"MMMMs++osssshssyyssNMMMMMMmyyyysyyhhyyd+/+++++++++/++/////+++++/+////////////////:-/////////::///////+/////////////:///::-:::/:/oysso+shhyyyyyhMMMMMMMMMMMhso\r\n" + 
			"MMMMdssysysyhyyyssdMMMMMMMNdysyysysyhd+//++++/+++++/++////+++++++////////////::://--////////::////////:////+/+//////:/:-://///+soyyyyyyhhhyyymNMMMMMMMMMMMhyy\r\n" + 
			"MMMMmoohhhhhhhhhyhMMMMMMMMdo++oo++//s+//+//+////////+//////////////////////::/::::-:///////::://////::::///++//+////://::://shyossyyysyhhyhyhmMMMMMMMNMMMMNdy\r\n" + 
			"MMMMNyyhhhhhhhhhyhMNMMMMMMNhhyyshho+/////////////////////////////::::////://:::/::::////////::://///::////////////////:/::/odysyyyysyyyyhyyhdMMMMMMNNMMMMMMNm\r\n" + 
			"MMMMNyhyyhhhyyhhdmMMMMMMMMNhhhyhyhy+///////+//++///://+/////:::::::::////////////:://///////////////:::::////:////////::///yhsyssyhyyyyyyydmNMMMMMMMMMMMMMMMd\r\n" + 
			"MMMMMy+oyyhhhyhsmMMMMMMMMMdsyhyhhhyo//+//+////+/+////////:::/::::::::///:/://////::/:////////////////::///://///+++////:++//::/+oyhhhyyyyhdMMNMMMMMMMMMMMMMMd\r\n" + 
			"MMMMMNmyyyyhhyhhNMMMMMMMMMdyhhhhhhhyo:////+/////+//////////:////////:::::://///:/:::::://///////////////++//++/++oosysshddhh++++++syysyyyyNMMMMMMMMMMMMMMMMMN\r\n" + 
			"MMMMMNhyysshhyyydMMMMMMMMMhyyhhhhhhyy+////++++/////::///:://///::::::::::::/+ooooo+://:////////////+/+++++/+osyhhhhhdhhddmdhyhyhys+oyossyNMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMNhyhyyhhyyyyMMMMMMMMNyyyyyyyhhyh+//////+osssss+://///////+++//////::++oo+ososys+/://///////osyhsoosssshhhdddddddddmmmdhhhhhyysshyyodMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMNhhhhhhhhhmMMMMMMMMmysyyyyyysoo://+sshyyysooosys+////+oshyys++/+++ooo+/ooooss+o++//://++oysyhhyysyyyyyyyyyyhhhyhyyhhhhddddddddhhyyNMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMdyyhhhhhyNMMMMMMMMMhyo++oss+ohhhddhhyyyyssssossso++ossssyyysooo++++/:::/+syyyhsosoyyysssooosooosossysooso+/sossyshyhhhyhhdhhhhdyhmMMNMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMNhhhhhhyNMMMMMMMMMNdhyyssysyhhhhhhyhyyyyhhhyyyyysssyddddhhhsyyyyssssossssssssssssyyhhhs//:+/::/++oo+/syyo+ososo:+ssyyyyhhhdhhdhhhdNmmNMMMMMMMMMMMMNNN\r\n" + 
			"MMMMMMMNMmNNmmmmNMMMMMMMMMmNmdhyddyyyosdddhyhhhyyyyyyyyyysyysyyydddddyys+//+++ooooo++/+//++//+/:/+so+--:::/++osysysysyyoso+oysooosoyyhyhddNNNNNMMMMMMMMMMNMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMmMMdNmhhddddmmmdhhhhdddhyhdyyysyyyhhyydmddsoyhoyssso//--..-----:/://ossyo:-::/o/:+//++/ssso///::--:/o+/+:/syydydNNMMNMNmNMMMNNMmm\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMmdmNmddmmmNMNNMNNNdhsyoydhdyhyysyhyysshddyooyossso+/+/:----..-::/oo+/:::osooss:.:+--------.....-..-++/+yssyysohdydddhhhmhddydmdm\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMdmMMNMMMMMMMMNMmysydhsdddddhsosyhhhysshysoy/+ooo///----.---::::////:----:----//o+os+s/:+/..-----::++/sso/soossossysysyysshyyyys\r\n" + 
			"MMMMMNMMMMMMMMMMMMNMMMMMMMMMMMMMMMMNMMMMMMNmmddddddddddhhyso+hhhhyhhhhsyy+/:+++/-:/+/++::-.........--......-://::/:+oyyos+/:-:/://://::+/::--:/-/soo+ooo/oosy\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNmddddddddhhddy+/osshhhshhhhdhhyho+/-:::://:::-//o:-.............----.--+hysssyysyyyoo//:-..--:::::-:/.--...-:::/+sy\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNdddddh+:ssyddddhhhsyshyhhhyso::::soo+:-----:--..................--:-::..-//+syyhyyhhyyyo+//////yyyooo+osssosysy\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNMMNMMMMMmmmmmdhhyddy+shhhddhhhhhhhhhyhddhhooyho+sss+::/::/--............................-:::ossyyyyyhyyyhhhyyyyyyyhyyyyysyhhhh\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmhmddmmmdddhhdddddddhdhdddhhhhhdhyyyoso+yso+::-..................................---:/ossoossyhhhhhhyyyyhyyhyhyhhhhh\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMNMMMMNMMMMMMMMMMMMMMNMMMNMMNNmmdddddddddddddhdhhhhyhyssoso:+yso+/---.--......-.-----.-...-./--:o.---:/::://:/+oososyssyyyyhhyyhhhhhhyyh\r\n" + 
			"";
	
	private final String cliffCarvingImage = "hdmmmhhdmdmmmddhhdyhhdhdhdddhyhhyhhhhddmmddddNdsMdohsoyhhyyyhhhhdddhdddddhdNNmNNddmMMhdhhhmmmNddyyddddNNdmydmmdmddmmhmddsdmdNmdmhddNmmmmNNmNNNNmmNNNddmhhyyhy\r\n" + 
			"mdMdddNNmdddhyyyyhdmNhdhydmddhyysydhddNMMmmdhdddMmh/d/oshhhmhNmmdhddhdhyhooydhdNNhmMMmhmmmdmNNNmhyydhhNmdddddhdmymmmymydyyddmmdhdydNmmmddNmmNNddmMMMMmdmNmmNN\r\n" + 
			"hyddddmdhddmddhhyhhyhdhsshdyssyoyhdydhyyhhhyyhhddmmhddyshddmhdhhhydyyyhdsyydddshhdhMMNdmdddmmNNdyyyddmmmNmdNyydsydmmymsddshhhdmyhdhmhdmmmNNmNNyNNNMNNmddmNmNM\r\n" + 
			"hhhdmddyhdhdddddhhyoyyyyhdhhhyyyyhhhhhysoydhyhhhmNddddhshmyhyhhdhhyyshhhysyyhdhddmdMMMddddmmNmmyhdddmmNmmydmddhsshmdhmhddyyyymNNyyddddmdmNNMMNhmNmNNmmhhhhNmm\r\n" + 
			"hddNdyyyhdhdddmdmhhydmmdddhdmhhhhmmddhyyhddhyyhhhyyyhmyydNMmddNmddNhy/shosyhyhddddmMMMmddhhmNmdNmNNmddNNNmmNmmhsysdsydhddhyssmmNshNmmmNNNNmmdymdmmmNNNmsmhdmN\r\n" + 
			"hmmhhdhdddddhhhhhhyhmmddddddmddhhhhhddhyymddhNmdmdhssydmmNMmNMMNMmhhhyhmhohdmdhyhhNMdMmdhhmmmmdddhhdmmmmdNNNmhmshyyhdmyhmdoysmdmhmMMMNmNmmmNmmNhhdNMMNNhhmhmm\r\n" + 
			"hhhdhddhddydysyhyhhhhmdddddmmddhddddhddddmmhmmmmmdmddhyyyhmmmNmdmddyyyhmmdmsydhhdmNMdNdoyysydNdyhmmmNmNdddmmmsNsddydhhyhmmhdhhNMNNNmNdmmNmmmmmmdmhmNMNNNdmdmM\r\n" + 
			"hddddhddhyddddhyyhhdhdmhshdmdhhhdmdddhhddmyyhhydhhdhhyhyhyyyyhhdyhhdhdhdmhdhymmhmNNmdddssyydNNNmdmdhddhhhdmmmymydhydyyhhmmmddmmmmNNmdNMMMNNNmdmhmhhNNMMNmmNMN\r\n" + 
			"mmdhhddhdyyydmdmmNmmhmdyhymmmhdhdyshddhddmhdddddhhhyydhdhhhhdmdmmhhddhddhhdhyhmdhNMNmdhshhsmmMMMNdhsshdhydmmmymdmhyhdhhdNMNdmmNNNNNddNMNNNNNmhdymddNMMNNmdmNN\r\n" + 
			"dmdddmmdhdNdyyydNNNNNNmmNMmdhyyddsshhhhyddhhddhhdhhhyyddhhdhhddmNmddhhmdhdyhyyhhhMMMmdmhdhdmmNmddmmyhyydyyhhdhdddmmhdNmmydmNNmmmmmNdhhmmmmNNmydmdhmMMNdydhmMN\r\n" + 
			"mNNNNNNNNNmhydNNNNmNmmmNmNhddhhhdydyddddhhyhddhdhdhhhmNmmmmmmmmdddmdhhdNhsssyyhhNMMMNmddyhhdhmddddhyyhmhhyhhhNhNNMNmdhhhddysymdddmmNhyhmmmmNmyhdyddmNNmhmmNNN\r\n" + 
			"NNNNddmddddddhmmdNNmmmMmmmddmddmmdhhddymdmmmmmmmddysyhhhhdhdhddmddmmddmddmdddhhdNMNdmhdyssyhhdhyhmdsshhmdddhhNhhmmNddhdmNhhdhdyyyymNyhhdmmmNdyNhmhNmMNNhmNNMN\r\n" + 
			"MNmdhdhhmhddhhdddNMmdmNmdhdhddNMmmddhddhydddhhddmhyyhddddddmmmmdmmmdmhmmdhhdddhmMMyymdhhyyoydmmyhdhyshdmdymdmNdyyyymdhhdNshdmhhhdddhysshdmNNhhddydmmNmNdNMNNm\r\n" + 
			"mdyhhdhshyhdmdmdmmmmdmmNmdmmmmmddhhhydhhdhhydddmmdhmhhhhdmmmmmmmmmdhddNmmmmmdymdMMhhhhmhhyhyhdmhyhhyyhmmmmNdNNdhhdNddmmhNhyyyhmhyyhhyyyhmmdmNhNmhdmmNdNNNmMmN\r\n" + 
			"hhssmmdyhsdydmmddddymdmmddhyhmmmmdhdhdhhhhdhmNmmmmhdydddhhhNmddmdmmmmmdNdMNNhhmmNMymdddhhhhdhhdyyydhhyNNMmmdmmmhhmdyhhhdNhhhhhddddddyyhdhmNNMdmmmmmmhhmNNmhdd\r\n" + 
			"syydddmd+oNdmmhddNNdh-hhhdhydmmmmhyshhdmdhhhmdyyhmyyyhhyyhdmmNddmmdddNmmdmNNhydNMNhhhmdmdhhyNdhhyhhhdNMMMmdhyNdhhddyyhhhmyyhmddmddddyyyyhhNNNhdhdhyyddyNNhhmm\r\n" + 
			"hhhdNdmh:/Ndddddmdhdo-+hhhhhshhhy/:-hmdydhhhms::/ddhydddhhdddddmdmdddmmNdNNMdydMMssyhdhmddmdNNNddhmNmmNNNhyhhNhyhdmyydhdNyyydddmdmmdhyyddhNNmmdyhdyyyyhmdmdmd\r\n" + 
			"yhmmddNo/-hdNmmddddy:`:shyshdsydh//-yhhyddhhh+:::ddhhdddhhhdmdddmmmddddddddmhydMNhddyhmdddmdNmmmmNmmNmmNNdhdNddyhdmdddhhhMmymmmmmmmmmdmmhdMMmdmhhsysddNmmyshN\r\n" + 
			"sydmmNd//+/hNdddddho-:-/dhyhdhhhy::-syhhhddydo-::ddmhhhhdhdNddhddhhmmmmdmmNdmhhMMdyhyhdyhhdmNdmNNNmmdhhddddddhhydhmsyddhhmMhdmNNNmNmmddddhmdmddoooyydNNmmhyyh\r\n" + 
			"shdmNNy+--:odmmhdhy:---:yhydhdyys-.:hdhhhdyhh/--:hhhydhyyhhmdhso-.+ddhmmdddmmhhMMdyhhydyhhyhh+:+syhdhhhdmmmhmhmso+-.-hhyhdNhyymdNmNMNhddhyhdmdho``:ohmddmmddy\r\n" + 
			"ymNNmh:----:hdhddh:....--yhhhhhhd---sdhdhhyos/--:hhhyhdddyo:......-..------.--dMMy...`.......+-....-+sddNNmys:-````..`.``.::``.-dNNNo+-.---:.....``.--/ohhdys\r\n" + 
			"dmmmmyo--::/yddhhh++-::/+hhyhhhyh-..osyhhdyyho:::hyhyyhso-````````.`........-.dMMy``.```.``..-.`````..:ymmh:`..```.```....-:``..dNNMy/+/-:..-..`.```...::omyy\r\n" + 
			"mmmmmNN/-:+ddNdmmhhy---shyyoyyyhy-.-shhdddyyh+-::dyyysyysyyo+:`....::++:/+:++:mMMd::::::::::::-``.:+yhddyddddys:.`...///::+s:::/dNMMmhd++o+////:..`.:ohdddmhh\r\n" + 
			"dmmddmd/-:/hyddmmdhs:-:sdhmddyy+/...::+syyyysoo/-s/syhhhyyhdhhyy+:odhydmmmmmNmmMMNddNmddyyydmo/oyhyyhhyhhhddddmmyyo//dddddmMdhmmNNNMdNNmhNNdmdhs::shNdmmmdmmd\r\n" + 
			"dddmmdh:.-/hmNmmmmhs:-:hmddhysd:---..-shdyhd+::/:::yhyyhdhhmmdhddddmmmmmdhdmmmdMMMhddNdhhdmmmmMNhhhhdhhdhddddhddhyhmmdddhdmmNhdNMMmmddNddhmyhdmddhdhhhhdmNdhy\r\n" + 
			"hmmmmmd:--:yhmmmNmdy/:-shhhhhyho--..-ohyyhhhs/-:::yydosyhyhddddmmmddddmmdNdNNNdNMMmddmddNmNmNMMmhddddhhmdMdhhhmmyyyymdddddmdMhNMMMMdyhmhdhdsymhhm+sdh+ydhhyos\r\n" + 
			"hdNNmmm/--+hddmdmmdh-::yhyyhymhy/---/yhyhdshy//::ohhyyddyyhdhhhhhmmdmNmmmNNNNNmmNMNddmhdddmmNmmmdddddhyhhNNddddmhhdyhdhyhyhdNhydNNNmddddhhhdmMhydoydhmmmdhhys\r\n" + 
			"ydddddd:--/hdddmmddd:-:shyhmdyydy:--yyhyhhyyhh--/hyhyyhhysyddddmmmmdmNmmdmNMNNmdMMNddddddmmmmmmNdmddddhhNNmmddmhyhhhhdyhhhymNmhmhNNmmdmhmmmmNmdyhsddhsmmMdMMm\r\n" + 
			"ddmdhyh:.:+hydddddhy-:-yhhhdmdhdh+-+hhhydhhdmd/:sddhhyydyddmmmddmmdmmmmNdmmmMMmNMMNdddmNmmmmmmmNdmddddhNNMMNdhmdhdydddhdyhhdMMdmNNNdmdmNNNddhyyhhdodNmNdhyydm\r\n" + 
			"ddddhhhsoohhhyhddhhy+oshdhydmmydmh/mmddhmhyddms/ddmmmdhdmddmdmmmdmmmddddmNdNNNNmMMMddmNmNmmmNmmNmdhddddNMmNmmhmhdhyddyyhddddmmNdNMMmdNNNmddddhmNNdyyhmmhhdhNd\r\n" + 
			"dNmddhyhsmNhyysshhdyhhdmNddddNhymhymmdddmdymdddhdddmmmhddhhmmmmmmdhmNmNNNNNNNmNNMMMmhdmmmddmNMMNdhdhdddmNNNNmhmddyyhhddhmdhdddMNdNMMmmmdMMNMNmmNmyshddhyyhydh\r\n" + 
			"hmmmmmhydmdddsyyyhhhddddhdyhmmhdmhyhddmdmNdhhmNmmmmmmmyhdmddddmmNmNNNNNmmNMMNmmNMMMNmdddddhmmmNNmNmdddmNNmNdddhmdhhdddhhddddmdNNmmmmmhmNMmhddhhmysshhddhdhoyd\r\n" + 
			"hmmddhhyddhydhyhddddmmmmdddmmmyydhshyymhmNNdmdmdddmmmNmdddNNNNMNNmmmmNmmmNmmNmNMMNNmmhmmmNdmmdmmmddddhmmmmmmddmNmddNdddhddddmmNNNdddNmdhMmddhyyNhhddNdhyyyyyy\r\n" + 
			"dmmmmddhdmhdmmddmmhdhdmmhsyddNhmmdyhddmdNNNmyhmdNNmNmmmNMNMMMMMmmdmmmNNNNmmdmmNMMmNdNddmmmmdmmdmmmmdmdmNNNNNNmmNmddMmdmddddhdmNNNdhdmddmNmhdhdhNhhyhhyhhhydhd\r\n" + 
			"mNmmmmddNmdmmmdmmmdhhhhyyhydmmhmmmdhhmmmNNmmdsmmdddmNNMMMMMMMMMmmmNmddddmNmNdmdNNmdmhmhdmdmNmdmmmmmdddmNNNNNNmmNmddMmNddmddhyhdmNyymmdmNNyyoydhdhysymdNmmdhdd\r\n" + 
			"NNNNNNdNNmdddmNNNddhmddydddhdddhmmhdyyhdhydNdddmmNNNMNNNNMNmNNmdmmmddddddmmmmmNNMNmNdNhdmdddddmmmdmmdmdNNNNNNmmNmddMmNddhhdhhdhmNhhdmmmMNddshyhdossymdmmdhyyN\r\n" + 
			"NNdmmmmmmmmmNmNmNNNdNNNmmdddhdhmdhyhdyhhdmmdhhdNNNmNddNNMNNNNNmmmmdmNmmdmmmmdddmMNmNhNhydmdddmmhdhhmdmmmmmddNdmNNdodyhysyhhhyhyNNhhdmmmNNhdysohdyooyddddysssh\r\n" + 
			"mNdNMmmMNdNmmNMmNMNmNNNmNddmyyyyyyhhdhhhdmdddddmmNNmmmNMMNNmNmNmdmdmNmmmmmmmmmmmMNmdmNmdmNmdmmmmmmmNdmhmNMMNddmmNdhyyhhhssshyddmmddddddNNdyyyyhmysyydmdmmdyhd\r\n" + 
			"NNmNNmmmdmmmNNdNMMNmmddhdhhhhhdddhddhdmmmddhdhmddmNNNNNmmdmmNdmMmmmNmmmNmmhmdmmNMmNNmNmddhddddmmmmdmdmhmmmmNdhhdNhysyhsdhyhhhydmNdhhmmdNmmNdyyyhssssmNNNMhmhN\r\n" + 
			"NNNMdmmMNdhdddNNmmNdhdhddhhdhshhhhddhyhhdmdmmmddmmmNmmmmmmmmmmmNNNNNNmdmmdmmdmNNMNNMNMNddmmmmdmdmmdmdNdmmmNdhyhyNhyyhyydhyhhyydmNmyhhNdNmhmyysyyysydNymNNyddm\r\n" + 
			"mmmdhmNMNddmmNNmdmNmdddmmmmdhsyyhdyhhydmmdmmdddmmmdNmmmmmmmdmdmNNNmdNNmNmmmmNNmNMdmNMNmdddmmmmddmmmhdNmmydhydyhhdyyhyyyhyyhhyhdmmdyymmdMdsNoosohyodmmNmdmdhNN\r\n" + 
			"mmNmddhdmdmmmNNNmmmNdmdhhhhhyhyhhhhdddmddddhdmNNNNmmNNmNmmNNNNNNMMMNmmmNNNmmmNmmNNmddNdhdhdddhdddmdhddhddyhhyshhmysyhsyhhyyyohhmNdysmNNNmmdyysyyyyymhdmmmyymm\r\n" + 
			"NNNMMMNNmNNddddmmmddhhdhyydmddddmddmNmNmNMNddmmmNmdmdNNmmNNmNNMMNMMMNMMNmmmhhhymdddhydNhhhhhhhddmddhmdhyhsssssyhNyyyssyyysssyyshdddNNNNNNNmdysyhhyyhsddmmshdd\r\n" + 
			"NNNNMMmNmmNhdhhdhNdNhdhmhddmdmNNNmNMNmNdmdNdyddNmNdmmdmmmNNNNNNmmMmNNmNNNdmdydhNmddydNMdhhhhddhdhdhdmdyyhhhhyssyNhyssysyshdmNmmmmNNNNNMMMMNmhhhhhyshyhhdmhddh\r\n" + 
			"mdmmNmmmhmmdddNNmNmmddmmdNNNMNMmmmmmmddhdmmyymmNmmddhhyhhdmmddmmmNmmNhmNmhddyhhmmmmdhNMdhyhyhddmddhhhmdhyddhy+symsyyhhddmNNNNmmmNydNNmNNNNNNmyyyNmyNNmhyoooyd";
}
