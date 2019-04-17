/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Rooms.CountdownRoom;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;

public class EdgeOfForest extends CountdownRoom 
{
	private static final long serialVersionUID = 1L;

	public EdgeOfForest(GameState gameState) 
	{
		super(gameState);
	}
	
	@Override
	protected void setName() 
	{
		this.name = "Edge of Forest";	
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData(edgeOfForestImage, this.fullDescription());
	}
	
	@Override
	protected void initializeCountdown() 
	{
		this.setCountdown(3, "One tick", "Two tick", "Blown up.");
		this.setTriggers(null, null);
	}

	@Override
	public String fullDescription() 
	{
		this.description = "The path leading to the edge of the forest is surprisingly well worn, a wide swath of clear, packed dirt.  It was "
				+ "obviously heavily used in the past.";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Farmhouse Porch
		//=================================================================================
		this.addMovementDirection("west", "Farmhouse Porch");
		this.addMovementDirection("porch", "Farmhouse Porch");
		this.addMovementDirection("house", "Farmhouse Porch");
		this.addMovementDirection("farmhouse", "Farmhouse Porch");
		
		if (this.gameState.checkSpace("Farmhouse Porch") == false)
			new FarmhousePorch(this.gameState);	
	}

	@Override
	protected void createItems() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createFlags() 
	{
		// TODO Auto-generated method stub
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
			//Else, move room and return new room DisplayData.
			//If the subject of the command is unrecognized, return
			//a failure message.
			//===============================================================
			if (command.getSubject().contentEquals("back"))
				return this.displayOnEntry();

			if (this.checkMovementDirection(command.getSubject()) == true)
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));

			return new DisplayData("", "Can't go that direction.");

		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();

		case "search":  //doing this will cause search to execute the go code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "");

			if (command.getSubject().contentEquals("forest"))
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
	private final String edgeOfForestImage = "NNNNMMMMMMMMMMMNyhdsoo++yNNNm--+yNMMM-.:.-` - `/`     .sm+dyy..:yhy++o++sy/:///-:+++:++:+++/:/+/+/oys++mMyo+sosdyyhdo:dMNs/sNMdNMMMMMMMhodyhhhddhNNmmmysMmmmm\r\n" + 
			"NNNMMMMMMMMMMMMy:y/.-..`oNNNy ./dNMMN-.-`::.-`.:..`   `om/hys. :dmy:://+yyoo/oo+s+yoooo+/+/:+yhhhyyhso+dNysyhyyhyhddh+mMNs/sNMdNMMMMMMMhhmhyhdddyNNdddssNmmmm\r\n" + 
			"NNNMMMMMMMMMMMMy+o`- ` `smNmo.-+mNMMN``- .. -..:``.`  `+d+oo/--.hNy:-//+dN/:+oosso::-+++:oso:hyhsshmmmdmmmdddhdmmdNmdoNMNhssNMdmMMMMMMMhhmhhhhhhyNNdddysNmmmm\r\n" + 
			"NNNMMMMMMMMMMMMh/+```.-:hNmms`-/mNMMN/:/`.. . `/````  `odssy+:..hNd///+ohdsss++-:///o+ss+ssooyso+yddhymNNmmmyhmddhddhoNMds:sNMhmMMMMMMMssNsyhdhhsNmdmdhoNmmmm\r\n" + 
			"NNNNMMMMMMMMMMMh++---:-/ymmmy..:mNMMN.`.``.`.  :`..``  /hoos/:`+dNm..-/sdms+so+//+:+osyyyhdmhddmmmmmddmNNmmmdmdddmmmhyNMdsooNMhmMMMMMMMy+mhyhhysyNmdddh+NNddd\r\n" + 
			"NNNNMMMMMMMMMMMhss:/-`` /mmdd::+mNNMN. -  . .``-`  `` `/hsoh/+`+hmm-`..:yh//o+/:///+osyyoydhyddhdhhdh+hNNhdhhhhmdhmdyhNNdyo+NNhmNMMMMMNhsmdhhhdhhNmdddd+Nmmmm\r\n" + 
			"MNNNMMMMMMMMMMMo++:..`.-/dmhNd.-mNMMN:.:.`-``` ..````  :hy+d/+-/symo-../mN+/--:-.//-:/d-:smh+oosy/-s/:sNMs+sos+mddhd/yMMyso+NNhNNMMMMMNy+ddysyhsdNmdmmd+Nmdhy\r\n" + 
			"MNNNMMMMMMMMMMMo:/`...`./dmNNy--mmNMN- `. . ..`-. ```  -yh/d/+-:smmo:--/mNy/:.//`````:y.-ymd.+o++--d-.+NNh::sy+mmdNdsyMMyoo+NNhmNMMMMMNs+dssyyy/hNdddddoNmhdh\r\n" + 
			"MNNNMMMMMMMMMMMs-+````-:+dmNNo-.dmNNN/`.-`.  ` `-``````-yh:d++.:ymms+-.:mNy.----````.:m..ymd`+s++..h:.+NNh..-+/mNmNdshMMyos/mNymNMMMMMNs/Nsss+o-hMdhddh/mmhhy\r\n" + 
			"MNNMMMMMMMMMMMNo+/:--::++hNNN: `dmNMN:  . .``. `-  ````.sd:h/:`:ydms.-./dmo`......`.`-m.-hmy-::/o:.h/.:NNd:/::/mNmNhshMMy+s:mNymNMMMMMNoodys+os:dNhdhhh/mmssy\r\n" + 
			"MmNMMMMMMMMMMMN/:o/:-::-omNNN:  dmNMNs` . `` ``.:`  ````sd/h+/.-yhmo`.`+mm-..`...`--.:m-:dNy+oooso/yo--mNm.-::/hmmmyodMMo/+-mNyNNMMMMMMsyms+osy-dNdhhdd/mmoss\r\n" + 
			"MNNNMMMMMMMMMMNs/y``...`+NNNm.``hmNNNN:`.  ` ` `.```` ``od/y+:.`smmy...smd......-.:-:-d:/dNs:++/so+hyoomNN..--:ymmmyodMMs/o-mNsmNMMMMMMoyd/+ssy-mNhhdmy+mNysh\r\n" + 
			"MNNNMMMMMMMMMMNyo+```.-:+mNNm-.`hmmNNN+ .  ` ```.```````od+yo+:`smms...smy..--.:o:-:--d//dms-+o/+-:yy+/dNN:----shhyoomMMs/o-mmsmNMMMMMMshd/++ss:mNhyyso+dmhdh\r\n" + 
			"MNNMMMMMMMMMMMdssh+/-:../hmmNs--hmNNNNh .  ` ` `:````.``+h+ys/:.+dmy...ydo-:-.:-::--`-d/+dm/-:y-/:-+s+/dNN-.---oNmmhsdmN///-hdymNMMMMMMohd/::oo-mNssyyy+dNhyd\r\n" + 
			"MmNNMMMMMMMMMMh//o/++oooosmNNh-.hmNMMm` .  `````.``    `/hoys::-+dmh//+yhso///+::-:-`-s/+os/-/o:+/:oso/hNN/+/+/+dmhs+odd+++/hhhmNMMMMMM+ym/o/++:NNddddh+dNhyd\r\n" + 
			"MNNNMMMMMMMMMMs+++/++:ooydmNNh..hmNMMy``.  ` `  :``...../hssy---:hmd::/dds:--::-/::::::++//o++oos++oso+smNyooosos+oo/oosho+:hdymNMMMMMMhhmo:/s+/Nmysyhd+hNdhd\r\n" + 
			"MNNNMMMMMMMMMM+oysyshyss/hddmm-.ydNNMy `-` ``.``..``...`/hysh:++:ymm/-oddo.....-/oooo+oss++oshyyyyhyhyysyysooo+sosyyosyyhss:hhymNMMMMMMdhmhhhhoyNmyhyyyohNhsy\r\n" + 
			"MNNNMMMMMMMMMMoshyshssyssdmmmmo`smNNMd:-:......`....-.../hhsdoo//hmh::smhy..-:-:/:/:/:+sosys:hdhhmddhhdmdyo+o/+yhdhsoyyossh+hmymNMMMMMMdymysoyyhhhhyyhhohmdyh\r\n" + 
			"MNNNMMMMMMMMMN-+yosy+///+mNNNNs/mNMMMh` -  ```.`......../hdsdoos-ymh:/dmmy-.-:/--/:/+:soshhsoshydds+mdyddh+yhysohdys/yhyyyy/mNhmNMMMMMMNmmmdmmmmmdmddhhyhmdhh\r\n" + 
			"MNNNMMMMMMMMMd.:+//sooyosdmmmms:mNNMMm/::..-....--/++++:/hmydy+/-ymmsomNNo/://+oo//++ohsomdyyyhsos::ho+dmdsso/:/syssyNNys+s:dmdmNMMMMMMmdmdNmNmNNNNmddhydNmhd\r\n" + 
			"MNNNMMMMMMMMMd/soy+/syssodNNmmhyhmNNMmo++:..-:::----:-:./hmydyss-hmdddmmm+:::////:+++odyodm///hsss..:/-dMNo:++::ymsohmhooos/yddNMMMMMMMNdddmNNNNNNNmmmNmNNNhm\r\n" + 
			"MNNNMMMMMMMMNho+hyyyhsyhsdmmmmhhhmNNMNs/+++ss/:::+--:+//ohdydsh+:dmNhdNNNsoo::::+ooo:omhodmss+yshyyyso:dNNo::::/hhyosohhsssohymNMMMMMMMMNNNNmmmNNNMNNNNMNNNNN\r\n" + 
			"MNNNMMMMMMMMmhydddhhyhddhdmmmdhhymNNMNy++o++:/+:-.-.:++sohddddd++mmmdmNNmsssyy++ssosoydhsdmh::yo/hyss/-hNms:--//+oo/yddoyossyhmNMMMMMMMMMMNNNNNMMMNNMMMMNMNNN\r\n" + 
			"MNNNMMMMMMMMMhsyo:+-:smyymmmddhhhNMMMMs+++/o::://::+oo+:+hddddd+ommmdmmmmyhyyhhddddhdhddhmmo- oy:+./s:`ymdh:-:::++osyyhyhyhyddmNNMMMMMMMMMMMMNMMMMMMMMMMMMMNN\r\n" + 
			"MNNNMMMMMMMMMhhdmdhhdhhydmmmmmmddNMMMMs//:../.-`.+///+-`+hmddso//dmdddddmdddddmddhhyyhmddmmo: +y.../y:.smNh-:+o//o+ydmdhddmdNmdmNMMMMMMMMMMMMNMMMMMMMMMMMMMMM\r\n" + 
			"MNNMMMMMMMMMNhyydddddddmmmmNmNmddNMMMMdssy+--`- `+.-:.--ohdmmdy:.hmmmddhddddddyhyydhyhmddmm:. +y---++/-sdmy:::/+ooshddmmhddmmmdmNNNddNmMMMNNNNMNNMMNMMMMMMMNN\r\n" + 
			"NNNMMMMMMMMMMNNmmmmmNNNmNmNNmNNmmNNNMMmhyhh//`-`.--.://+ydmmmmh/smmNNmyymmhddmyhhddhshmddmh.- /dys+oho:+sy/:/+sydmmmmmNNNmNNNNdNmymdMMMMMNNNNMMMMMMMMMMMMMMNN\r\n" + 
			"NNNMMMMMMMMMMMNNNNNNNNNNNNNNNNNmNNNMMMmmmNmmdy+//+/oo///ydmmmd//ymmNNNmddydhhmhhddddhdmmmmh:/`:dyo//y+:/hs/-/:ydddhyddmmmmmNNNdNMMMNMMNMMMNNMMNNMMNNmMMNmMmdm\r\n" + 
			"NNMMMMMMMMMMMMNMNNNNNNNNNNNNNNNmNNMMMMNNNmyydh++:-o/////sdmmmhsohmNNNNNmhhdddhdmmdmddhmmmmhyy`:ddh/-hh::sys:-oo+ssyhddddhyhNMNdNMMMMMMMMmNNNNMMMMMMMMMMMNMNmd\r\n" + 
			"NNMMMMMMMMMMMMNNNMNmNMNMMMMNNNMmNNNNMMNhydhyssyoo/+/-::+sdmmmd/ohmmmdhdmddddmmmdhdmddhmmmNdyy`:yddo.dh::oyo::://ohhdmdhdddNMMNmMMMMMMMMMNMMmmMMMMMMMMMMMMMMMN\r\n" + 
			"NNMMMMMMMMMMMMNNNNMMMMMMMMMNMMMmNNNMMMNmydNmsdsso++:--//smmmNm+sdmmdhdmmmmmdyyhdsyyhssmNmNy+s.-oss:-:-::+--:/+ssohmdyddhmNmymNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN\r\n" + 
			"NMMMMMMMMMMMMMMMMNMMMMMMMMMNNMNmNNMMMMNNNNNmdymdsoo/::..+mmNNhsymNdyshmNmmh++o/...-//omNNNs/o`.so:....::///oo+oooshyossddsyyyNMMMMMMMMMMMNNNmmNMMMMMMMMMMMMNN\r\n" + 
			"NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmNNNMMMMMNNNNNmddsho+-...-omNNmsshmNmdmmmmdysyy+:-.....omNNNy+/``-:.----//:/sosy+syysyss+ossyhmMMMMMMMMNNNmmdmmmmmmmmmmmmmmNNmd\r\n" + 
			"NMNNNNMMMMMMMMMNNMMMMMMNNMMMMMmNNMMMMMNMNhhhhhdhhso//+++shyhososdNmdydmyoo+oyyo+os//ohmmmdmys:--:.:-:-//+oyhhhmmhho++::sosymNMMMMNmmNmmmmmddddhhhdhhdddhdmhhN\r\n" + 
			"MMNNNMMMMMMMMMMNMMMMMMMMNMMMMNNMMMMMMMNMNNNNmmdds++o++/hyhddmyoyddddhhdyssyhyyhhhddmmmNNNmmmho+++--:::/-/oshhdhy+/::+s/+yymmmhdhhyhmNNNNdhhdhdddhdddyhhhmdmNN\r\n" + 
			"MMMNNMMMMMMMMMMMNNMMMMMNMMMMMNMMMMMMMMNNNMMNNmhysoyshsyhhdmmmmhhmmNNNmddddmddmNmmdmmdddmmmNmds+///+/o+:-::+yhs+:+/:/+/+hhmNhysyysymdddhhyhhyhhhhhmmmNmmmNdmmm\r\n" + 
			"MNNNNNNNNMMMMMMMMMMMNNNNNNNmNmMMMMMMMMNNmNNNNNNmmdhhosyhhNmmmmhdmNNNNNmdhhdmmmmmmmmddhhhhmddms:-+o++/:/+:/oo+:/o+:+ososshhddmdhsyyyyyhhhhhhyyyyhhdddhhhhdmmmm\r\n" + 
			"MMMMNNNNNNNNNNNNNNNMNmNNNNNNNNNNNNNNNNNNNNmdmddmmmmmmdyyyddhyssoyyyyhhyyhhyyyyyysyyysoooshssso/-.::-.--///:-+s///+/++oohdmddhsoyhhhhhhddddhyhyhyyyyhddmdddhhd\r\n" + 
			"NMMMMMMNNmNNMMMMMMMNMNNNNNNNNNNNNNNmmNNNNNNNhyydddddmdddmyyso++oo/::::::::-::::/::::-----::--/:-o:---:/:/--:/:///+ohyysyyhs/++yhdyhhysyyhhdmmmmmddhhyhddddddh\r\n" + 
			"NNNNNMMMMMMMMMMMMNMMMMMNNNMNNmNNNNNNNNmNNNNNmdyo++++ooooossoo+/:---....``..`.``````````..``....-:-::::---:///::://++o+os+//+syyyysyhysssyhNNNNNNNmhhhhhddmddm\r\n" + 
			"MMMMMMMMMMMMMMMMMMMNNNNNMMMMMNNNNNNNNNNNMMNNNNdo///:---.````````````````````````````....`...---.-:/::/:--:/+//+y+/://+o+++ysshdyyyyyssyhdNNNmmmdmddddddmmddmN\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMNNNMMMMMMMNNMMMMMMMNNNNMNmo+/::-.:...````````..``````````````.`...-..-.-.....-::--:-///+///+++ooosossssyyyyysyhddhmmdmdmmmNNNdmmdddmNNN\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMNNMMMMMMMMMMMMMMNMMMMNhdysoo+////:---...`..-...........------------------:///+/++++++o+oooosssyssssyyyhhdhhdhdddmmdmmmmmmmddhddmmmm\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNMNNNMMMMMMMMNNNMMMNmddhyyyyyso+s//:::::-:/:::-::--:--:::::::::::::::/::///++oooossosooososysyhhhhhhdddddddddddmmdmmmmddmNNmmmm\r\n" + 
			"NNNMMMMMNMMNNNNNMMMMMMMMMMMMMMMMMMNMNNNNNMMMMNNNNMMNNNmmmmmmdddhysssooo+oo+/+////+////////+/////+++++/+++o+++o+ooosssyyyyhyhhhhdhddddddddhddddddmdmmmmmNmmmNN";
}
