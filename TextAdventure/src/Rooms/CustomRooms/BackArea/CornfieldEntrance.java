/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms.BackArea;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;


public class CornfieldEntrance extends Room
{
	private static final long serialVersionUID = 1L;

	public CornfieldEntrance(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "CornfieldEntrance";
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
		this.description = "You stand at the edge of the cornfiled, wind blowing rather aggressively through your hair, tugging at your clothes. "
				+ "The corn itself is dead, yet ready to harvest, and even looks edible. You swear to yourself that you hear whispers coming from the "
				+ "cornfield, but could it just be the wind? To your right is a tall windmill.";

		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Movement to the backyard
		//=================================================================================
		this.addMovementDirection("backyard", "Backyard");
		this.addMovementDirection("leave", "Backyard");
		this.addMovementDirection("exit", "Backyard");

		//=================================================================================
		//Movement to the cornfields center
		//=================================================================================
		this.addMovementDirection("forward", "CornfieldCenter");
		this.addMovementDirection("corn", "CornfieldCenter");
		this.addMovementDirection("cornfield", "CornfieldCenter");
		this.addMovementDirection("center", "CornfieldCenter");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//When creating items, in addition to instantiating the object, you also need to
		//set the synonyms for the item, which are words that it can be recognized by in
		//typed commands.  Once that is done, add the item to the Space hashmap.
		//=================================================================================

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
		this.gameState.addFlag("cornfield scouted", new Flag(false, "", ""));
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
			//The cornfield must be scouted before it can be entered
			//===============================================================

			if (command.unordered("corn|cornfield|forward|center"))
			{
				if(this.gameState.checkFlipped("cornfield scouted"))
					return this.move(command);
				else
					return new DisplayData("", "Charging blindily into the cornfield will get you either lost, killed, or both. "
							+ "A smart idea would be to scout it out from a high vantage point");
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
			
		case "climb":
			//===============================================================
			//User can only climb the windmill if the tether has been taken, which scouts the cornfield
			//===============================================================
			if (command.unordered("up|windmill|mill"))
			{
				if(this.gameState.checkFlipped("tether taken"))
				{
					this.gameState.flipFlag("cornfield scouted");
					return new DisplayData("", "You approach the windmill and fasten the climbing tether tightly to your belt and proceed up the windmill. "
							+ "Every so often you remove the tether from its spot and placing it higher. A handful of times a strong gut of wind nearly threw you off, "
							+ "saved only by your tether. As you reach the top you take in a deep sigh as you see before you a breathtaking eagle's eye view of the "
							+ "entire estate. You see the road from which you have arrived from, the decaying farmhouse and barn, the shed, and forest. You also see "
							+ "close to the center of the cornfield, a blackended circle. It is to far away to make out what the circle is made of, but there does seem "
							+ "to be a figure in the circle. It moves around the cicle, with an almost dancing stride to it. You climb back down, having scouted the cornfield sufficiently enough to "
							+ "walk through it.");
				}
				else
					return new DisplayData("", "That doesn't look very safe. You'd probably need some kind of safety harness or something. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		case "search":  //doing this will cause search to execute the go code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The seemingly endless expanses of the cornfield lay in front of you. The corn seems dead, but bears a bountiful harvest. "
						+ "The corn itself seems very strange. The wind seems to be carrying whispers from somewhere deep in the cornfeild, without a "
						+ "definite direction it seems gaurenteed youd get lost in there. To your right is a tall, large windmill made of metal. "
						+ "Behind you lies the backyard you came from.");

			if (command.unordered("corn|cornfield|stright"))
				return new DisplayData("", "You lean in for a glance at the corn. The stalks are bleached and decaying, with what looks like "
						+ "black veins running up and through them. The corn however looks amazing, larger and more golden than any corn you've ever seen. "
						+ "You refrain from eating it, since a glance at the ground reveals what appear to be black and red veins, pulsating as they all connect "
						+ "to the bases of the corn stalks.");

			if (command.unordered("right|windmill"))
				return new DisplayData("", "The windwill itself looks like it has seen better days. Much of the metal has rusted the large monolithic structure well past its prime. "
						+ "Its size and structure do look suprisingly sturdy, and most likely could support your weight, were you to climb it. Doing so without some kind "
						+ "of tether or way to bind yourself to the structure would be suicide in this wind though.");

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
	private final String FarmhousePorchImage = "s+//o+:------------///.`-s+syhy:.:`-+o-`-smdsyhhhhyhyo-..:+:-:/-......-..........-//.`````.-/+++//:-..```````````.......--::-.`...--`+NMd/.....`......-:-:/::\r\n" + 
			"ysooss:::-------::+oo++omNdddmdhydssyy+:ymhhddmdyyydhs/.yNmmh/++:.........-o/....sso--````.-::-......``````````....--:-.`.:/---..-:./mmd:......--::::::-::///\r\n" + 
			"dhyyyys/:----:sssssos+odNMmmmmmdhddyyhhhdNdmmdddhhmdddyhmmmNmdo+-..--...-osys:`.:+++:s/.s/++/-.`.++-....````..-:oo-....:``../+/::o..-------..-::::::--+.::://\r\n" + 
			"mmmdyysho//+++hhhdhhsydmNNNNNNmddddydddmNmmmNNNdhNddmdhdmddmNmdd//:oy+:..:ddy/.-+ooss+/hNms+//dosmds-......-:/+sdN/..soho.ys+oo+o:.-/+oyyhdmmdmhss+..o.`./:--\r\n" + 
			"NNdhhdddsysshhhmmNmdhdNNNNNNNNNNNNmhddmdmNNNMMMNdmmmmdhhhhdNNmmmdsdhdhy//+hNmds/yhhhyyydmmdddhmdhmmh/ss+///+o+ysomhsy+sdhshNdsooyo``hMNdmNNNmdddds-.o:-`.-/o-\r\n" + 
			"mNmddNmmmdddddhmmNNdmNNNMMMNMmNNNmmdmmmmNNMNNNMNmdmmmmddddmNNNmmmdddhhyyshdmmmNdydddmmmmmmNNmNmmNNmNmNNmhhyom+odNmmNNmdmNmmMMNysm+``-MhMMMMMNNMNd-:hdm-.-.sss\r\n" + 
			"mmmmmNNMNNmmmmmNNNNNNMMMNMMMNNNMNNmdmmmNNNMMMNNmNMNmmmdddmNNNNmNNddmdhhmmmmNNNNNmdmmdNmNMNNNmmmNmNmNNNMMNNmNMNyNMMNNNNNNMMNMMMMNy..``hNdmMMMMNMm-:NNNm...-mhN\r\n" + 
			"NdmddmmNMMNNmmNNNmMNNNNNmNmNNNMMMMMmmmmNMMNMMMMNMNNmmNmdmmNNNNNNNNddhmddhmNNNNMMMmmmmmNMNMNNMMNNNNNNNNMNNMMMMMMMMMNNNNMMNMMMMMMN-./.`:NNhhMMNMm-+NMMMN-.-:NNd\r\n" + 
			"mNNmNNNNMNMNMNNNNNNNMMMNNNNNmmNNNMNNNNmNNNNNNmNmNNNmmNdmmdNNNNNNNMMNdNdhmmNNNNNNmmNmmNMMNNMNNNNNNNNNNNNNNMMNMNMMMMNNNNMMMMMMMMMo`/o-`:hNNhsNMh.sMMMMMd/.-:mym\r\n" + 
			"dmNNNNNNNNNNMNNNNMMMNNNNNmNNmdmNNNNmNNMMMMMMMMNNNNNmNNNmmdmNNNNNNNNNhmdmddNNNNMMNmmmmdNNMMNmNMNNNNNNMNMMMMMMNMMMMNNNNNMMMMMMMMN.-+sh. /mmmsoy.sMMMMMm``-:.+oN\r\n" + 
			"ddNNNNNNNMMNMMNMMMMMNNMMMMMMmdhdNNmmmmMMMMMMNNNmMNMMMNNNmmMMNMNmmNNNdNNNmdmNNNNNNyNmNmNMMMNNNNNNMNNNMMMMNMMMMNMMMMMNMNNNMMMMMMs`/ohMy` Nmmd:.yMMMMMMMh.---shM\r\n" + 
			"mNNNNmMMNNMMMMMMMMNMNMMMMmNmhmdhdddNNNNMMMMMMmNMMMNNNNNMmdmNmmmNNNNNdNNmmNMNMMMNNmMNmNNNMMMNNMNMMMMMMMMMNMMMMMMMMMMMMMMMMMNMMm.-+sNMM. /MN:.+hMMMMMMMM----NMM\r\n" + 
			"mNNMNNMMMNNdmNNNMMNNNMMMNmMNdmddhhmNNMMMMMMMMMNMMNNNNdmmNmNNmdddNNNNdmNmddmNmMMMMNNNMNMMMMMMMMMMMMMMNNMNNNNMMMMMMMMMMMMMMMMMMo./ohMMMs``No.ydosNMMMMmm.--+MMM\r\n" + 
			"dmNNNMNNmmNNNMMMMNNMMMMMmMMNdmmdddNmdNMMMMMMMMNNNNNNNNNmNmdmdNmmmmNNmNNmNNmmmMMMNmmNMMMNMMMMMMMMMMMMMMNMMMMNNMMMMMMNMMMMMMMMm.:osNMMMM-`:-dMMdo+yMd+-`.--oMMM\r\n" + 
			"/+/oosyhmmdhdNMMNMNNMNMNyMMmmmNdmddddmmmNNNNNNNMNNNNNmmmmmdmdmNNmNNNmmmNmNNNNMMMMdMNMMMNNNMMMMNMMMMMNmmmNNMMNNNMMNMNMMMNMMMd..+syMMMMM/.:dMMMMmy+++o` .--oMMM\r\n" + 
			"oso+/s/+//:/+sssssssysosyhhddmmhdhmddyyhyhhyhyyyhsssssssoysoosooosssosysydddmNNMMNNMMNMMMMMMMMMMMmMMNmddmmNmNNNNNNmmdmdhhyy:.:ossdNMMo--sdMMMMMNyoo:``.-.yMMM\r\n" + 
			"+:/:o//o//+++o/o//o/:++s+os+/+:o/+///:+o+/+/+/+o+:++///o////+//+///+/:///+//:/++o+oo+/ooossso++//+/+/++o+//:::::::::::::::-..+s+----:../..NMMNmNmyo:-`...dMMM\r\n" + 
			"+s/++++oo+-oo+s++++++s+o//+yo/:soy++:/++++-++/+//o/+///o++/+s+o+oso+//s++/+/+o/++o++++oo:+/+://://///:://///:://////////::-.:oo/:::...-.` -+syoy+hd+.`.-.mmds\r\n" + 
			"++o-:/o:+/+++h/o+sy:/o/+-:+so:+ooyso++oo//:s+y++:hyo+oo+oos++s/s//o+////:+s+///+++o+/o+++++/s//+o+:///::///+++///++://+++:.-os+++--.-/-:.``+md/-/:::``--.-:-:\r\n" + 
			"+++:+o/s.shooyyo++/-+/sos/+/o+++ssso+o++yy+ossosss+osoo:o:oo/++o/+s+/oyo/y+/s///o/+o+/s+o::-++/o+/-o/o+++//++-//+o/::+://../so++:.--:/+-:``-s+//:/:.``-..//::\r\n" + 
			"so::ooho:ooos+o/:oso/:o++yo+h://+s+++o+//o/oso+.so+osoo+o:oo////:o+h+:/osh+o/+://+oo++s:oo:os+o+o+:+:+///o+//sy+/:--/+o--.-+s+//--/o/:::s-``-/::::/.``.../+/+\r\n" + 
			"+o.-ssoo++hoo+y++s//:oo/oo//o+++y+--//o/oyyoy/s:+:s/s++s+/+o+o+o/oo+:/+ss/+/oo+s:o///+/o+://+s:o+oo/++//++s-:+os/+/ooso+-./so/s--:-:o/ssy/``-/://-:.`.-.:+///\r\n" + 
			"/+:-+o//+/+/yyyo.+os.+ssshyyy+yyys-oo.ssh//:/o+//o///-yd/-:o++/./osss:/sssos/-oo-:+oso-/+/+.+-o+--:/oyoyoo/:soyo/.so+o/-.-ooo:.-+s/:syos-o.`./:s+ss``.--+s///\r\n" + 
			"ho/++o++myhsdssos:syyys/yhsy:yy+yhy:/yydhs++myssosoo+os/-/hoo:+oos+y+/smho++o./+/:yysys/:os-:`+-+o--+o++/-`ooo+s:-ooo/-.-+ss/--ss/sdyoos:/.``:o+so+``.:-+yys+\r\n" + 
			"hysod:/ys+ymmyss+-dmh/shss::dmdhy+s-+//+::oohyyhhd+::oosydsohsodsyohhmddho//:-//-/hysd/-.:y:-//so++++h+o:`/+/+ooo.y:s/.-:oso::hdhhms++so+::.`-++o+-``-:-:oyso\r\n" + 
			"+ohy-/h+o+NmNmos/omhy+hdys/ydyyhs:.s+ohmydoyyhmhos./yhhy+shyyyoh+yyhmdsdyydho-s+osdhdyoo/-shdddyyosohsy-`oyho/s+o:yy/-.-+s+-:sohNmo-+s/oho//.`-++o+``--.+:/ss\r\n" + 
			"ssy/++omyddmddds:yh+yoNdNdyddddhs.s+smNdyhmmdhdhh+-dmNhhyhhhsoyosdNMhNNhodMmh:odyNNmhydyhooNdMhdmhshsoh:+dhhdmsysodys--:o/-/symNmo/ddhdom/:o.`.ydy/``.:-symss\r\n" + 
			"hoosNNMmdohdmdNNydmmdNmhy//hhhmyd/shdmMMdmmmh+dsmdmhNmNNNdsoso+yhhmmdhNhdmhdo.hNmhy+mMs/o/sdmmyhhNmy/:yyhNmhoydmhsy+s--+/-/ydNNdsoNmNdsyodoo:``:ss:..:/-shyso\r\n" + 
			"shNmMMMMNNhdNmddsmNNMMmysdNmddmyyymMmymhddhsshosdmNMMNMNmoossy+yhhs/sdmmNNhdosmmmNMNddmssodhNmNNNmhmhoyssNmyNmy/sysh+.::.omMMNs+yhNMNdhhsdyo+``.yh:`--::shd+.\r\n" + 
			"hmmMNdNMMNdyyymhomNNNdMMMdNNmmdyyyNMshhosddmmhhddmNNNNMmNMNNNy+ymh/ohNNNmNddoommmmdddmddhoymNNdmNNmmdodd+dy/sohmmdss.--.oMMMmds+hNMNNmNNNhy+h/``om-`.-:/sd:..\r\n" + 
			"dhNMNNMmMdhNNNmdhymNmdNymNmmNMNshNhdyNMhhdNMNNdshNNmNNMMdddhsmsdd/:hNNMddMdsoyNmNNmNMNmNhsdMMMmMNNMNdy+ysyhNNNMMmmh-.--odMNdNm/smNmNhdmmNyoyh:``:N.`-::+m:`.-\r\n" + 
			"yNdMMMMmNNMmdNdNhyNmdNdsyNNhmhMmyhNNMNmmdmmyNNNhmyhdMNMMMMNMMmshsdNNMMMNNNdddddNMMMMMMMhyymNMhddNdMNmosmdNmmmNMMNmd.--yyNmdNNhyosNh/mNNNydyyyd:``s``-::/-`.-:\r\n" + 
			"yymMMNdyhMMdNmNmsdhmNmMmmdNdNydmyssmmNyssNNMNMMmmmMNMyyhmmMddModNdmsdmMMMMMmhddMMMMMhNMmhshMMmNMMMMmyhodNMNMMdNMNm+-:hMNmNmNddo+moyNMMNmmss+ds+.````-/-``..:d\r\n" + 
			"shMNNNddmhdNMNomymmMNMMMMMMdNNNyymdNMMMMNNmMMmmsmNydhdNMNmNMmhs+myshMMNMMMMM+hodNhmNNmhNmhhNNmmdmNMNdys+mNMmMNMMy+::sdmmNmhmmhss/oyyssssss++oss.```.--``..:oo\r\n" + 
			"NydNNMNNNMNNMMmhydmMMMMMMMMmmMNdssNmMMMMMMMMMMMsdymMmMMMmNMMMNohdNNNMMNMMMMmymdMMNdNMMMNyohNMNMdMNNNMs/yNNMMMMMNy...`....---:----::/++oosssssss+```..`.../oyy\r\n" + 
			"dohMmMMMMMMmdNNM+hyhhMMmMMMMMMhhymNNdMMNMMMNMMNssyNMMMMMNMNNdMyymMMMMMMMMMMMymMMNMMMMMMMy/hdmyNMMMMmms/yMMMMMMNdh:+soo:--...--......-...............:::--+oo+\r\n" + 
			"oddmhNmMMMMhNNMds/ydhNMMMMMMMMssodNMNMMmNMMMMdmssddmMMmMMMMMMNodsNmmMNMMMMmNomMMMMMMMMmNdsmmNhdmMMNMNs+yomNNMMMMo./hNNNNMNmyss:+so///:............::::-:::/::\r\n" + 
			"h+omMMMNMNMNMMdN/yhhmMNmNMMMMMNh/oNmymmMMMMNmhd+odNNdMMMMMMMMM/omMMNMMMNMMmmoomMMMMMMMMhssyymNMMMNmNdo:h+omMMMMd.-++ymMMNMmmdh/mNdMMMMmdds:osss+-:+:ss/:ooooo\r\n" + 
			"ssymmmMNMMMMNdhyoymNmyNNMMNMNMdso+ddNmNMMMMdmsy/osdNymmNMNmddd/ohdmmNmdmmhs//hyddmmdyyyyo+oyhdhmNmhyo//syyydmdm/./++hMNMMNmmho/hhhymdhdhs+:ssyhm+`..///sshmdd\r\n" + 
			"ysdmmdmdmhmdysyyhyyhhhdmmddddhsysyhyyyyhhhhyyhyyhhyyhyysyyyyysyyyyysyysyysyyyyyyyssyssyyyyyyyyysysyysssyyyyhsyo.-ooossshhsssssssssssysysssysssss/`.:+-/ssssso\r\n" + 
			"yhyyhhhhhhhhhhdhhdhhhhhhdhhhdmhyyddhhhhhhhhhdhhhhhhyhhhhhhhhyhhyhhhhdhhhhhyyyyhhhhyhyyyhhyhyyyhhyyhhyyyysyyyyy-.+osyyyyyyhyhyyyyyyyyyyyyyyyyyyyy:`..:-oysyyyy\r\n" + 
			"hhmdhddhdhdddhdhdhdhdhdhhhhdhyhhhhhhdhhhddhhhdhdhhdhhhhhhhhhhhhhhhhhhhhhhyhhdhyhhhyhhhhhhhhyhyyhhyhyhhyhhhhhho.-ssyyyyyyyyhyyyyhhhyyyyyyhhyhyhyy-`.--.oyysyyy\r\n" + 
			"dhsshdhdhhhhhdhhhhhhhhdhdhhhhhdhdddddhhyddhdhhdhhdhhhhdhhdhhhyhhhhhyyhhhhhhhhhhhhhhyhhhyyhhhhhhhhhhhhhhhhhhhh:.+ssyhhhhyhyyyyyyyyyyyyyyyyyyyysyy.``.-.oyyyyys\r\n" + 
			"yddhhddhhhdddhdhddhhhhdhhhhhmmhyhddhhyddhdddhhhdhdhdhdddhhhddhhhhmdhyhdhhhhhhdhhhdhdhhhhhhhhhhhhhhdhhhhhhhhhs.:ssyhhhhhhhyhhhhhhyhyhhyhhhyhhyhh+....--syyyyyy\r\n" + 
			"dhhyhddddhhhdhhhhhhhhhhhhhhhhyhdhhhddhhhhhdhdddhhdhhhhhhhhhhhhhhdyydhdhhddhhddhhdddddhhhdhhhhddhhhhhhhhhhhhy:.oyyhyhhhyhhyhyyyyyyyyyyhhhyyyhhyho....--yhhyyyy";
}

