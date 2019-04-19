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
import Structure.GameState;

public class ForestPath extends Room 
{
	private static final long serialVersionUID = 1L;

	public ForestPath(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Forest Path";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData(this.forestPathImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "XXXXX";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Edge of Forest
		//=================================================================================
		this.addMovementDirection("west", "Edge of Forest");
		this.addMovementDirection("edge", "Edge of Forest");
		
		if (this.gameState.checkSpace("Edge of Forest") == false)
			new EdgeOfForest(this.gameState);	
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
		case "move":  //doing this will cause move to execute the look code
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
	private final String forestPathImage = "NNNNMMMMMMMMMMMNyhdyoo++yNNNm--+yNMMM-.:.-` - `/`     .sm+dyy..:ymNNMMMMMMMMMMMMMM++:++:+++/:/+/+/oys++mMyo+sosdyyhdo:dMNs/sNMdNMMMMMMMhodyhhhddhNNmmmysMmmmm\r\n" + 
			"NNNMMMMMMMMMMMMy:y/.-..`oNNNy ./dNMMN-.-`::.-..:..`   `om/hys. :hmNNMMMMMMMMMMMMMMhoooo+/+/:+yhhhyyhso+dNysyhyyhyhddh+mMNs/sNMdNMMMMMMMhhmhyhdddyNNdddssNmmmm\r\n" + 
			"NNNMMMMMMMMMMMMy+o`- ` `smNmo.-+mNMMN``- .. -..:``.`  `+d+oo/--:hdmNMMMMMMMMMMMMMM/:-+++:oso:hyhsshmmmdmmmdddhdmmdNmdoNMNhssNMdmMMMMMMMhhmhhhhhhyNNdddysNmmmm\r\n" + 
			"NNNMMMMMMMMMMMMh/+```.-:hNmms`-/mNMMN/:/`..`. `/````  `odssy+:/mhdmNMMMMMMMMMMMMMM+/o+ss+ssooyso+yddhymNNmmmyhmddhddhoNMds:sNMhmMMMMMMMssNsyhdhhsNmdmdhoNmmmm\r\n" + 
			"NNNNMMMMMMMMMMMh++---:-/ymmmy..:mNMMN.`.``.`.  :`..``  /hoos/:odydmNMMMMMMMMMMMMMm/+osyyyhdmhddmmmmmddmNNmmmdmdddmmmhyNMdsooNMhmMMMMMMMy+mhyhhysyNmdddh+NNddd\r\n" + 
			"NNNNMMMMMMMMMMMhss:/-.` /mmdd::+mNNMN. -  . .``-`  `` `/hsoh/++dydmNNMMMMMMMMMMMMh/+osyyoydhyddhdhhdh+hNNhdhhhhmdhmdyhNNdyo+NNhmNMMMMMNhsmdhhhdhhNmdddd+Nmmmm\r\n" + 
			"MNNNMMMMMMMMMMMo++:..`.-/dmhNd.-mNMMN:.:.`-``` ..````  :hy+d/+-symNNNNMMMMMMMMMMMm/-:/d-:smh+sssy/-s/:sNMs+sos+mddhd/yMMyso+NNhNNMMMMMNy+ddysyhsdNmdmmd+Nmdhy\r\n" + 
			"MNNNMMMMMMMMMMMo:/`...`./dmNNy--mmNMN- `. . ..`-. ```  -yh/d/+-/ydmNNNMMMMMMMMMMMm-``:y.-ymd.+o++--d-.+NMh::sy+mmdNdsyMMyoo+NNhmNMMMMMNs+dssyyy/hNdddddoNmhdh\r\n" + 
			"NNNNMMMMMMMMMMMs-+````-:+dmNNo-.dmNNN/`.-`.  ` `-``````-yh:d++.:ydmNNMMMMMMMMMMMMm+`.:m..ymd`+s++..h:.+NMh..-+/mNmNdshMMyos/mNymNMMMMMNs/Nsss+o-hMdhddh/mmhhy\r\n" + 
			"MNNMMMMMMMMMMMNo+/:--::++hNNN: `dmNMN:  . .``. `-  ````.sd:h/:`/ydmNNMMMMMMMMMMMMN:.`-m.-hmy-::/o:.h/.:NNd:/::/mNmNhshMMy+s:mNymNMMMMMNoodys+os:dNhdhhh/mmssy\r\n" + 
			"MmNMMMMMMMMMMMN/:o/:-::-omNNN:  dmNMNs` . `` ``.:`  ````sd/h+/-+ymNNNMMMMMMMMMMMMN/-.:m-:dNy+oooso/yo--mNm.-::/hmmmyodMMo/+-mNyNNMMMMMMsyms+osy-dNdhhdd/mmoss\r\n" + 
			"MNNNMMMMMMMMMMNs/y``...`+NNNm.``hmNNNN:`.  ` ` `.```````od/y+:o+ydmNNMMMMMMMMMMMMM+-:-d:/dNs:++/so+hyoomNN..--:ymmmyodMMs/o-mNsmNMMMMMMoyd/+ssy-mNhhdmy+mNysh\r\n" + 
			"MNNNMMMMMMMMMMNyo+```.-:+mNNm-.`hmmNNN+ .  ` ```.```````od+yo+o/ymmNMMMMMMMMMMMMMM+:--d//dms-+o/+-:yy+/dNN:----shhyoomMMs/o-mmsmNMMMMMMshd/++ss:mNhyyso+dmhdh\r\n" + 
			"MNNMMMMMMMMMMMdssh+/-:../hmmNs--hmNNNNh .  ` ` `:````.``+h+ys//+hdNNMMMMMMMMMMMMMMo-`-d/+dm/-:y-/:-+s+/dNN-.---oNmmhsdmN///-hdymNMMMMMMohd/::oo-mNssyyy+dNhyd\r\n" + 
			"MmNNMMMMMMMMMMy//o/++oooosmNNh-.hmNMMm` .  `````.``    `/hoys:/shdmNMMMMMMMMMMMMMMy-`-s/+os/-/o:+/:oso/hNN/+/+/+dmhs+odd+++/hhhmNMMMMMM+ym/o/++:NNddddh+dNhyd\r\n" + 
			"MNNNMMMMMMMMMMs+++/++:ooydmNNh..hmNMMy``.  ` `  :``...../hssy-:+hdNNNMMMMMMMMMMMMMm+:::++//o++oos++oso+smNyooosos+oo/oosho+:hdymNMMMMMMhhmo:/s+/Nmysyhd+hNdhd\r\n" + 
			"MNNNMMMMMMMMMM+oysyshyss/hddmm-.ydNNMy `-` ``.``..``...`/hysh:+ohdmNNMMMMMMMMMMMMMNso+oss++oshyyyyhyhyysyysooo+sosyyosyyhss:hhymNMMMMMMdhmhhhhoyNmyhyyyohNhsy\r\n" + 
			"MNNNMMMMMMMMMMoshyshssyssdmmmmo`smNMMd:-:......`....-.../hhsdoo+ydmNNMMMMMMMMMMMMMN//:+sosys:hdhhmddhhdmdyo+o/+yhdhsoyyossh+hmymNMMMMMMdymysoyyhhhhyyhhohmdyh\r\n" + 
			"MNNNMMMMMMMMMN-+yosy+///+mNNNNs/mMMMMh` -  ```.`......../hdsdoosdmmNNMMMMMMMMMMMMMMo+:soshhsoshydds+mdyddh+yhysohdys/yhyyyy/dNhmNMMMMMMNmmmdmmmmmdmddhhyhmdhh\r\n" + 
			"MNNNMMMMMMMMMd.:+//sooyosdmmmms:mNNMMm/::..-....--/++++:/hmydy++dmNNMMMMMMMMMMMMMMMy+ohsomdyyyhsos::ho+dmdsso/:/syssyNNys+s:dmdNNMMMMMMmdmdNmNmNNNNmddhydNmhd\r\n" + 
			"MNNNMMMMMMMMMd/soy+/syssodNNmmhyhmNNMmo++:..-:::----:-:./hmydysydmNNMMMMMMMMMMMMMMMNoodyodm///hsss..:/-dMNo:++::ymsohmhooos/yddNMMMMMMMNdddmNNNNNNNmmmNmNNNhm\r\n" + 
			"MNNNMMMMMMMMNho+hyyyhsyhsdmmmmhhhmNNMNs/+++ss/:::+--:+//ohdydsh+oNNNMMMMMMMMMMMMMMMMmymhodmss+yshyyyso:dNNo::::/hhyosohhsssohymNMMMMMMMMNNNNmmmNNNMNNNNMNNNNN\r\n" + 
			"MNNNMMMMMMMMmhydddhhyhddhddmmdhhymNNMNy++o++:/+:-.-.:++sohddddd+oNNNMMMMMMMMMMMMMMMMMNmhsdmh::yo/hyss/-hNms:--//+oo/yddoyossyhmNMMMMMMMMMMNNNNNMMMNNNMMMNMNNN\r\n" + 
			"MNNNMMMMMMMMMhsyo:+-:smyymmmddhhhNMMMMs+++/o::://::+oo+:+hddddd+ymNNNMMMMMMMMMMMMMMMMMMdhmmo- oh:+./s:`ymdh:-:::++osyyhyhyhyddmNNMMMMMMMMMMMMNMMMMMMMMMMMMMNN\r\n" + 
			"MNNNMMMMMMMMMhhdmdhhdhhydmmmmmmddNMMMMs//:../.-`.+///+-`+hmddsoohmmNNMMMMMMMMMMMMMMMMMMmdmmo: +y-../y:.smNh-:+o//o+ydmdhddmdNmdmNMMMMMMMMMMMMNMMMMMMMMMMMMMMM\r\n" + 
			"MNNMMMMMMMMMNhyydddddddmmmmNmNmddNMMMMdssy+--`- `+.-:.--ohdmmdyshmNNMMMMMMMMMMMMMMMMMMMNdmm:. +y---o+/-sdmy:::/+ooshddNmhddmmmdmNNNddNmMMMNNNNMNNMMNMMMMMMMNN\r\n" + 
			"NNNMMMMMMMMMMNNmmmmmNNNmmmNNmNNmmNNNMMmhyhh//`-`.--.://+ydmmmmh/hmNMMMMMMMMMMMMMMMMMMMMMNmh.- /dys+oho:+sy/:/+sydmmmmmNNNmNNNNdNmymdMMMMMNNNNMMMMMMMMMMMMMMNN\r\n" + 
			"NNNMMMMMMMMMMNNNNNNNNNNNNNNNNNNmNNNMMMmmmNmmdy+//+/oo///ydmmmd//hmNMMMMMMMMMMMMMMMMMMMMMNmh:/`:dyo//y+:/hs/-/:ydddhyddmmmmmNNNdNMMMNMMNMMMNNMMNNMMNNmMMNmMmdm\r\n" + 
			"NNMMMMMMMMMMMMNMNNNNNNNNNNNNNNNmNNMMMMNNNmyydh++:-o/////sdmmmhsodmNMMMMMMMMMMMMMMMMMMMMMNmhyy`:ddh/-hh::sys:-oo+ssyhddddhyhNMNdNMMMMMMMMmNNNNMMMMMMMMMMMNMNmd\r\n" + 
			"NNMMMMMMMMMMMMNNNMNmNMNMMMMNNNMmNNNNMMNhydhyssyoo/+/-::+sdmmmdsmNNMMMMMMMMMMMMMMMMMMMMMMMNdyy`:yddo.dh::oyo::://ohhdmdhdddNMMNmMMMMMMMMMNMMmmMMMMMMMMMMMMMMMN\r\n" + 
			"NNMMMMMMMMMMMMNNNNMMMMMMMMMNMMMmNNNMMMNmydNmsdsso++:--//smmmmmNNMMMMMMMMMMMMMMMMMMMMMMMMMNy+s.-oss:-:-::+--:/+ssohddyddhmNmymNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN\r\n" + 
			"NMMMMMMMMMMMMMMMNNMMMMMMMMMNNMNmNNMMMMNNNNNmdymdsoo/::..+mmmdNNMMMMMMMMMMMMMMMMMNNMMNNNNmms/o`.so:....::///oo+oooshyossddsyyyNMMMMMMMMMMMNNNmmNMMMMMMMMMMMMNN\r\n" + 
			"NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmNNNMMMMMMNNNNmddsho+-...-oddmNMMMMMMMMMMMMMMMMMNmmNNmmmmmdmy+/``-:.----//:/sosy+syysyss+ossyhmMMMMMMMMNNNmmdmmmmmmmmmmmmmmNNmd\r\n" + 
			"NMNNNNMMMMMMMMMNNMMMMMMNNNMMMMmNNMMMMMNMNhhhhhdhhso//++yhmNNNNNMMMMNNNmmmdmmNmmNNNmmdmmmmdmys:--:.:-:://+oyhhhmmhho++::sooymNMMMMNmmNmmmmmddddhhhdhhdddhdmhhN\r\n" + 
			"MMNNNMMMMMMMMMMNMMMMMMMMNMMMMNNMMMMMMMNMNmddddhho/+shhhmNNdmddyyhhyhyysydmmNNNNNNNNmdddmNmmmho+/:...-:/-/oshhdhy+/::+s/+yymmmhdhhyhmNNNNdhhdhdddhdddyhhhmdmNN\r\n" + 
			"MMMNNMMMMMMMMMMMNNMMMMMNMMMMMNMMMMMMMMNNs///:--..:hhhmNNMdyhyyyyyyysyoyddmmdmNNmmdmddddmddhy+:....//o+:-::+yhs+:+/:/+/+hhmNhysyysymdddhhyhhyhhhhhmmmNmmmNdmmm\r\n" + 
			"MNNNNNNNNMMMMMMMMMMMNNNNNNNmNmMMMMMMMNNNms/.``````.:/+sshhdmmddhhyssyhhhhhdmmmmmmmmddhyso+:-.``.-o++/:/+:/oo+:/o+:+ososshhddmdhsyyyyyhhhhhhyyyyhhdddhhhhdmmmm\r\n" + 
			"MMMMNNNNNNNNNNNNNNNMNmNNNNNNNNNNNNNNNNNNNNmdo.```..``````.-::/++++syhhyyhhyyyyyyyys+/:--.````.--.::-.--///:-+s///+/++oohdmddhsoyhhhhhhddddhyhyhyyyyhddmdddhhd\r\n" + 
			"NMMMMMMNNmNNMMMMMMMNMNNNNNNNNNNNNNNmmNNNNNNNh-...--:-`..`````.--:-::::::::-::::/:::-...`....-..-o:---:/:/--:/:///+ohyysyyhs/++yhdyhhysyyhhdmmmmmddhhyhddddddh\r\n" + 
			"NNNNNMMMMMMMMMMMMNMMMMMNNNMNNmNNNNNNNNmNNNNNmyo////:::-.-........--....``..`.`..........--------:-::::---:///::://++o+os+/++syyyysyhysssyhNNNNNNNmhhhhhddmddm\r\n" + 
			"MMMMMMMMMMMMMMMMMMMNNNNNMMMMMNNNNNNNNNNNMMNNNNds+++/:::-..--.-.......`.`````````````....`...---.-:/::/:--:/+//+y+/://+o+++ysshdyyyyyssyhdNNNmmmdmddddddmmddmN\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMNNNMMMMMMMNNMMMMMMMNNNNMNmo+/::-.:...````````..`````````````..`...-..-.-.....-::--:-///+///+++ooosossssyyyyysyhddhmmdmdmmmNNNdmmdddmNNN\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMNNMMMMMMMMMMMMMMNMMMMNhdysoo+////:---...`..-...........------------------:///+/++++++o+oooosssyssssyyyhhdhhdhdddmmdmmmmmmmddhddmmmm\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNMNNNMMMMMMMMNNNMMMNmddhyyyyyso+s//:::::-:/:::-::--:--:::::::::::::::/::///++oooossosooososysyhhhhhhdddddddddddmmdmmmmddmNNmmmm\r\n" + 
			"NNNMMMMMNMMNNNNNMMMMMMMMMMMMMMMMMMNMNNNNNMMMMNNNNMMNNNmmmmmmdddhysssooo+oo+/+////+////////+/////+++++/+++o+++o+ooosssyyyyhyhhhhdhddddddddhddddddmdmmmmmNmmmNN\r\n" + 
			"";
}
