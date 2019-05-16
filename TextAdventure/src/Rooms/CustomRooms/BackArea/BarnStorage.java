/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.BackArea;

import Items.BasicItem;
import Items.Item;
import Rooms.CountdownRoom;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class BarnStorage extends CountdownRoom 
{
	private static final long serialVersionUID = 1L;

	public BarnStorage(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "BarnStorage";
	}

	@Override
	protected void initializeCountdown() 
	{
		//=================================================================================
		//Death after third command
		//=================================================================================
		this.setCountdown(3, "You stand in the storage room, the smoke secreting creature standing in the right corner of the room, hunched over and breating heavily. It's mouth open, spittle hanging from its teeth as it stares"
				+ " at you, menacingly. ", 
				"The creature attacks you out of nowhere, swinging its claws wildly at your chest, letting out a long screech as it lunges. In turn you duck and roll out of the way, matching the beasts speed "
						+ "almost instictively as you feel the air sliced by its claws against your head. You stand next to it, shaken as much from the sudden attack"
						+ " as your bodys reaction to it.", 
						"The beast quickly turns around and slashes again with both claws, throwing it's head back screeching once more. You attempt to dodge again as you did before, but are unable"
								+ " to match the speed and precision from before. The creatures claws catch your left arm. A searing pain runs through it before you realize you cannot see your left arm below the elbow anymore."
								+ " A breif moment of shock comes over you before you see the rest of it a few feet away, laying on the ground, blood draining from is stump. Before the horror of what has happneded "
								+ "has registered in your mind, the creature shoves you against the wall, running its right claw through your abdomen. The pain registers for a brief second, before it all begins "
								+ "to drain away from you. Your strength, hearing, feeling, nearly all your senses vanish. Finaly your sight grows weary, as the last thing your eyes register is the hunched over creature throwing its head back "
								+ "in what you would assume is another howl, if you could hear it. Your vision does not last long enough to see what the creature would do to you once it has finished it's "
								+ "victory screech. ");

		//=================================================================================
		//Count down starts and progresses unless the knife is used
		//=================================================================================
		this.setTriggers(null, this.gameState.getFlag("used knife"));
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
		//=================================================================================
		//If the user has used the knife, display a description with the dead creature. If not, the creature is alive
		//=================================================================================	
		if(this.gameState.checkFlipped("used knife"))
		{
			this.description = "The storage shed is somewhat smaller than the toolshed you visited earlier. The still bleeding corpse "
					+ "of the smoke creature you killed moments earlier lays still against the right wall where it died. To the left is a shelf"
					+ " loaded with farming tools. There is a long box against the wall infront of you. ";

			return this.description;
		}
		else
			return this.description = "You stand in the room you saw the creature retreat in, the being standing against the wall to the right. It's piercing eyes looking at you. "
					+ "Your lantern is still lit, revealing the creature yet it's smoke still pours out of its skin. "
					+ "The room is too obscured to get a decent look around as a result the only clear entity is the abomination before you, taking long labourous breaths.";
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to the main barn
		//=================================================================================
		this.addMovementDirection("barn", "Barn");
		this.addMovementDirection("leave", "Barn");
		this.addMovementDirection("exit", "Barn");
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Creates the Silver Longsword
		//=================================================================================
		Item SilverLongsword = new BasicItem(this.gameState, "Silver Longsword", "", "A long, light and perfectly balanced longsword with a worn, yet comfortable "
				+ "grip. The crossguard is designed in a V pattern, going away from the weilders hand. The silver-imbued blade is covered in runes and "
				+ "the following quote 'My gleam penetrates the darkness, my brightness disperses the gloom'.");
		this.gameState.addItemSynonyms(SilverLongsword, "silver", "longsword");
		this.gameState.addSpace(SilverLongsword.getName(), SilverLongsword);

		//=================================================================================
		//Creates the Climbing Tether
		//=================================================================================
		Item ClimbingTether = new BasicItem(this.gameState, "Climbing Tether", "", "A sturdy piece of braided rope connected to a metal clip. Assumably used "
				+ "by rock climbers and other people who don't want their necks broken when climbing to higher places.'.");
		this.gameState.addItemSynonyms(ClimbingTether, "climbing", "tether");
		this.gameState.addSpace(ClimbingTether.getName(), ClimbingTether);
	}

	@Override
	protected void createFlags() 
	{

		//=================================================================================
		//Creates the flags for using the knife, taking the longsword, and taking the tether
		//=================================================================================

		this.gameState.addFlag("longsword taken", new Flag(false, "", "The longsword is strapped firlmy to you waist. "));
		this.gameState.addFlag("tether taken", new Flag(false, "", "The climbing tether is rolled up and secured firmly to your waist. "));
		this.gameState.addFlag("used knife", new Flag(false, "", "The knife is wet once again with dark blood."));
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

		case "attack":
		case "stab":
		case "slash":
		case "use":
			//=================================================================================
			//Checks to see if the knife has already been used before letting the user attack
			//=================================================================================
			if (command.unordered("silvered|knife"))
			{
				if(!this.gameState.checkFlipped("used knife"))
				{
					this.gameState.flipFlag("used knife");
					return new DisplayData("", "Your grip on the knife tightens once more, and you rush the creature. This time however, it is not done out of a desperate attempt to "
							+ "save your own life. This time you lunge at the creature with force, pressing it against the right wall. While holding it against the wall, forearm pressed against"
							+ " the creature's throat, you stab viciously into its ribs. Your head starts to ring with the familiar piercng pain. The pain "
							+ "intensifies with each successive cut, slash and gouge. One to hit the creatures lungs. Another to cut aimed at the femoral artery. "
							+ "Each one searching for something vital. How you know where to strike, how to wield the knife, you don't know. By the end of it, you stand over the butchered corpse "
							+ "of the creature. The pain in your head has left, yet after a check of your nose it is bleeding, your blood very different "
							+ "from the blood of the smoke-maker which drenches your knife weilding hand.  ");
				}
			}
			else
				return new DisplayData("", "Nothing to attack here ");

			//=================================================================================
			//Checks to see if the knife has already been used, if it has, there is noting to attack
			//=================================================================================
			if (command.unordered("shard|glass"))
			{
				if(!this.gameState.checkFlipped("used knife"))
				{
					return new DisplayData("", "You rush at the crature and slash across its arm with the glass. It cuts the skin, yet the creature seems not to notice it. Instead the creature"
							+ "returns the blow, swiping away at your arm, grazing your shoulder and drawing blood through your torn shirt.");
				}
				else
					return new DisplayData("", "Nothing to attack here ");
			}

			//===============================================================
			//Subject not recognized
			//===============================================================
			return new DisplayData("", "Can't do that. ");

		case "grab":
		case "pick":
		case "take":
			//===============================================================
			//Take the Silver Longsword
			//===============================================================
			if (command.unordered("silver|longsword"))
			{
				if (this.gameState.checkFlipped("longsword taken") == false)
				{
					this.gameState.addToInventory("Silver Longsword");
					this.gameState.flipFlag("longsword taken");
					return new DisplayData("", "You pick up the longsword and its accompanied scabbard and strap it to your hip. ");
				}
				else
					return new DisplayData("", "You've already taken that. ");
			}

			//=================================================================================
			//Creates the Climbing tether
			//=================================================================================
			if (command.unordered("climbing|tether"))
			{
				if (this.gameState.checkFlipped("tether taken") == false)
				{
					this.gameState.addToInventory("Climbing Tether");
					this.gameState.flipFlag("tether taken");
					return new DisplayData("", "You grab the climbling tether, roll it up and strap it to your belt. ");
				}
				else
					return new DisplayData("", "You've already taken that. ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData ("", "You don't see that here. ");

		case "search":  //doing this will use the search function
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The room is particularly small for the skirmish that just took place in it. It's a miracle you didn't fall on any farming equipment, which the left wall is full of. "
						+ "Could be something useful there. Against the wall in the center of the room is a long ornate box. Interesting. The right wall harbours the still bleeding corpse of the beast you killed. ");

			if (command.unordered("left"))
				return new DisplayData("", "The left wall is covered with random farming tools. Shovels, hoes, post diggers, wire, ect. Nothing that could "
						+ "help you where you are with what you are facing. There does appear to be a coil of rope with a metal clip at the end, however. "
						+ "You recognize it as a climbing tether, something that almost every self-respecting adventurer in any game you've played has, so why shouldn't you?");

			if (this.gameState.checkFlipped("used knife"))
			{
				if (command.unordered("right|creature"))
					return new DisplayData("", "The creature lays still against the wall. Its chest covered with stabs, legs and arms covered with deep cuts severing arteries and slicing organs. "
							+ "It's dark blood covering itself, the wall, and the fllor around it. The craters which moments ago were seeping a pitch black smoke are now dormant. "
							+ "You know deep down that it will no longer be a threat. The actions that you took to kill it still suprise you. Where did I learn any of that?"
							+ " Do I really want to know.....?");
			}
			else
				return new DisplayData("", "TThe creature stares at you, breathing with visible difficulty. The smoke that was once bellowing from the craters on it's skin "
						+ "has now been reduced to a slow trickle. You assume this is the lanters doing.");


			if (command.unordered("center|chest|box"))
				return new DisplayData("", "The chest in front of you is adorned with silver hinges, locks and artwork. You slowly open it and see a long rolled up piece "
						+ "of cloth. Picking it up you discover it's weight betrays it's apperance, and something is wrapped in it. You slowly unravel the cloth which reveals an "
						+ "ornate yet sturdy and beautifuly crafted longsword, blade suffused with silver and runes. The sight of the blade sends a piercing pain nto your head, "
						+ "and once it has supassed you notice the all to familiar trickling of blood running from your nose. You place the sword back in the box and tend to your nose, "
						+ "unsure if you are worthy enough to take such a beautiful weapon... or what more it might lead you to instincively do....");

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
	private final String forestClearingImage = "dhhyyyyhyhds+//:s+++osy///+o+oyhdhddhhysssssssooosyyyhdmdddmNNNNNNNNNNNNNNNmdhhNMmsoosooosoo+/omMs++//:+///:/-./Nh`..-.```-.``.:ym/y+/+oshhmNMMMMMMMMMNdNMMMM\r\n" + 
			"dhhyyyyhysyo+//:o++//+y//:/+//+oossyydmMNdsssssoooossssssyyhdmmNNMNNMNNNmmmdhdhNMmysshysshysssymMhyyyyyhhhhhhyyhmdyyyhhyyyhhhhhhhyyyyyyyhhhhdmmNMMMMNsyMMMMMM\r\n" + 
			"myyyyhyyysh++++/+++/:/s/////////++ooshdMMmsssoshssyyhyysssssssyhhhdmNNNNNNNNNNNNNmmmmmmmmmmddhhhhhhyyyyysssooooo+++////////+////////+/+++osyyhdmMMMMmMMMMMMMM\r\n" + 
			"Nhhyyhhhysd+++//+s+/:/o+/:///::/+//+oyyNMmsoso+o:-/:/+/yysyhhyyyyyyydNNNmmmmmmdddddddhdddhhhhyyyyyyysyssoooooo++++++++++++++++/+o+++++oosssyhdmNNMMMMMMMMMMNN\r\n" + 
			"Nhhhyyhyyoh+++/+sos/+/yo+////////+/++syNMNssso/+--/::/-o/:oohshddmmhhmNNNNNmmmNNNNmmmmmddmmmNNNNNmddshhhyhhhyyymmdoooo+/:///++somyss+++/sooosymmNMMMMMMMMMMmN\r\n" + 
			"Mhhhyhhhyoyo++//yss//:ooo//y/:://+/+/osNMNssso//--:::/.:::+/o+oshNNhhmmmmmdddmmMMMNNNNmmddmNNNMNm+++/ohhhhdmddhsdd///:```. `--o/myoo:.` :-.-:oydmMMMMMMdhdMmM\r\n" + 
			"Mhhhyyyhyoss+///y+y//:+s++/s//://+///ooNMNssoo/+--::/+:+:-/:+/+osmNhyddhyhdhyhdMMMNMMMNMMdoshhyssyy/-//----smmM+hh:--`   `  `-o:my++-`  :...-oshmMNMNmm+sdMmM\r\n" + 
			"Mdhhyyhhysss////osso+//s/o/o+/+sosoo+++mMMsooo/+:::-:sos/o+:///+odmhyddhshyyshhNMMmNmNmNMd+/o+////o/-:/.`ysyMmM/dh//:  ` `   .+:dy/:`   :..-ossyhmdmmdd/hNMNM\r\n" + 
			"Mdhhhyhyhssy/////y+h+/+s+::oo+ysohhy/:-mMMo+oo/o::---yss:oo////++hmhyhhhsyydyhhNMMmNmmmNMds/hoohhdm++++./NNmd+M/hy/// `` `  `.+:hs:-   `:-..+s+ohmmNNNd+mMMNM\r\n" + 
			"Mdyhyyhyhssy++++/sodh++ssoysmoodhmNy-  ymsssso/y//o--o//::o+o/+o/oyhhhhhyyyyyhhNMMdmdmmNMdyohoshsydoo+o:/yyddsm/hy+/+``.``   .-:s+:-``  +-.-+ooymMMMMMMMMMMMM\r\n" + 
			"Mmhyhyyyhysh+++++soyd++yydMdMs:ydmNy-  :yMyooo+mhNMMNd-::/ohNooh- :hymhhyhyhyhhNMMdmdNNMMd/:oo///`:-. - .-.smmM/hs/::`````   ..:so/.`   o+:+ohmmMMMMMMMMMMMMM\r\n" + 
			"MNhyhhhhddmmhdddhs:::::///o/o:`:+oo/-``/ss++ossmhmNhys--sooss+-+-`/osmddyhhhhddNMMmNmNNMMhsssssoo`//.`:-+/`::+d:s+-.-``  ` ``/:oysso.://+:+yodmmMMMMMMMMMMMMM\r\n" + 
			"MNhyyhdddmmNmmmmdmdhhhhhhyyyhysyyyo+/::yNNyooyhhyhyyssoososysso//ydyymddhdhdhdhNMMNNmNNMMhmNhhmMs /d/`+yd+   -h:o+---.-.-::--o/yhyNM-::+My:osmNMMMMMMMMMMMMMM\r\n" + 
			"MMmhyyhmdyoh+++++mhooo+oo+/+o:-:++/////yMMhossoo++/+++-:+:/+/+/+ohmhymddhhhhhddMMMNMmNmNNyysyysssosso++/++:osyy/s+:::-:.-:-:/y:hhdMN.:-oMh:shNNMMMMMMMMMMMMMM\r\n" + 
			"MMmhyyhmhyoyo++ooNyso++oso/+o/-/+o+++++yMMhsssoo+o/oo+:/+:/+/o+oshmhhmhdyhddhhhmmmddydhdds//+s/:/--..-: `::ydmd+yo///-/--:::+o-ddNMd`.`:so+ydNMMMMMMMMMMMMMMM\r\n" + 
			"MMmhyyddhhsys++oymyso++oso//oo:++oo++++yMMdssooooo++oo//o/+o+oosshmhhhhdNmmdmdddhhhhydhdmyd/hm::ssy` /+/sooyoymsds+//-:::--:s+-mmMMy+ysmMN+hmNMMMMMMMMMMMMMMM\r\n" + 
			"MMmhhyddhhsys/+ohhyso+ooyo++os/+ssdssoosdddysooooooooo//o/+s+o+oosss+syhddhyddhhhdhyhdhhddhyhmdddyhooddmNddhhhdhms+//-:--.-:s/-mNMM/+o+dMNsdNMMMMMMMMMMMMMMMM\r\n" + 
			"MMmhhhddhhyyy//+msyysooohhshmhyymMMNdooymmNMmsoosoohsoos+:ss+sooy::o`oyhhhhyshhhhmyosyssossosyhmNdddddhhdhhhsosNmsoo+-:`--:/s/-NMMm---:odNsmMMMMMMMMMMMMMMMMM\r\n" + 
			"MMNdhddhhhyhy/+oNshdhyhhmmhdmyyhdhysdmss+ssssyooo/+////+++osossss++ooooo++/:--.`/doo/o+//oo+symMNss++////++oo+yNNsooo//.---////MMMM+yyyyyhhNMMMMMMMMMMMMMMMMM\r\n" + 
			"MMNdhddhhhydmmNNmMMMMMMMMMh++o/ss+//sm///+++oyssy+yy+://+o++++++/:--.``    .:../hdyyoyssoysosyNMNsso+///++++/+yMNsoss/-.:--o/+sMMMNsmdmMMNmMMMMMMMMMMMMMMMMMM\r\n" + 
			"NNNddmdhyhhmMMMMmMMMMMMMMMh+:+-::`../d::+ossyysoo+mds+//::-..`       `-/+shmh+ohMMmhsysosyysyhNMNo+/ssso+++++ohMNssso::-/:+yoomMMMmymmNMMNNMMMMMMMMMMMMMMMMMM\r\n" + 
			"o/:ydddhhhdmMMMMmMMMMMMNNNy--:--:/o/:/+//:-...--:-:-``         -:///yNNMMMMMNoodMMmhyhysydyo+/:-``:smmdhs+oo+/hNmo+s+//:o+ohssNMMNdssyhddmNMMMMMMMMMMMMMMMMMM\r\n" + 
			"+oohddhyyhdmNmmmyhhhhhyyyyyyhNmmmmdo+++//:-.``          .-:+oy`hdhysmMMMMMNMMs+dNhhyys+:-`     `:++::-.`../oo++sdyhhso++soyhhhMMMNhhddMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMNmysoydNMMMMMdNMMMMMMNNNNNNMNNmmmy:`           `-:/+osyydhhd-yho/::--mMMMMMhsdm/:-```  ..------..`.....-+yosNMMmhyysoshyddmdMMMMhmNNMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMNNmddmNMMMMMMNMMMMMMMMMMMMMMMMMMMd.     `-/+o:ssshdNNNmmNNNd-mM-``..`hNNMMMhyhmydmMmdhs/-:-------../oyyhmdmmmdhhhsossyydNNMMMMMMdddmmNMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MNMNNNmNMMMMMMMMMMMMMMMMMMMMMMMMMMMd-:/+ydmymdm:yydNMMMMmmNMNm:mN:``..-ymdMMMhhmdNdhyNMMNy/////::++sddNMddydNNmyssyosyhhhMMNMMMMMMdMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMNMNNMMMMMMMMMMMMMMMMMMMMMMMMNmdMM:`hhsdMMdNNN/yhNNNMMMNNmNNm:mM/.`.-+m/:ydMMNydNyhdNNNMmdy+ooosyhddhmMdmh+ossssssssyyhdhddmdhhhddNNNNMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMNMNMMMMMMMMMMMMMMMMmddyyyys+oNMo-hdhNMMmMmN+ydMMNmmddhhhhhoydy/oshmNhhmMMMMhyymmdhmMmhhyo++//:::y::+o/:-..--------:::::/ddhhdddmmmmNmdmNNNMMMMMMMMMMMMMMM\r\n" + 
			"MMMmho:NMMMMMMMMmdmdhs::-+++shhydNMs:smdNmdyood+hNMMMNmmmmmy/+ohddhdmmmmNNNMMMMMMMNmdmmmdoooossoo//:/..-o....`.........----:ddddddddmmmmNNMMMMMMMMMMMMMMMMMMM\r\n" + 
			"yo//::oNMMMNmMMh`.sssshyydhosdNMMMMy:mMhsssosohohNNNMdyymmNNydymddmmdmdddddmmmMNNMMdmdhyo+/////::::/::-:.`.......`......--:+mmmddmmmmmmNNNMMMMMMMMMMMMMMMMMMM\r\n" + 
			"osyhhNMysyNNmMMm:+hdmdhdshyyhmNMMMMh:mMdoo/.+:oyyyyhMNddNNMdyssdhhysssssooooymyosyhs/::o-::-------....-...........-----/oydmNNmmmmhyhNMMNNMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMhMMhydMMMMMNoodhNhhmyhyydNmNMMMd:dMh--:/++sssosdNsssNyhhooodh+h++++////syho+oosyNNmh--------------.--.-.---.-------::::/osyhmMMNdysoosydmMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMmMMNmNMMMMMMs+hdNdyddNdmNNNNNMMm/dMmhhdysoooooommooomsomo+ody+oy++++/oshMdMMMMMMMMNNmy/:------------------------::::///+syyhdNMMNmmmddddmNMMMMMMMMMMMMMMM\r\n" + 
			"MNMMMNMNNNMMMMMMs+ddNdmmNNmddmNMNMMNshmddhhsssssosoNhooomooMsooyms+h///+s+ssMNMMMMMMMMmMNhMmy+----:-:-::--------::::::::///odmNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMNNMMMNMMyomMMNmmmmmNNNNNmmMNsshhhhhhhhyyyyyNyysyNsoNmo+ods+h++ss+y/sMMMMMMMMNdhMMMMMMMNh-------:--:---:::::::::////oNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMNMMMMMMMdshmmNMMMMNmdmddmmMMsyhddmmmhyyyyyhNyssssssNddyydyodoohshoohMMMMMMMmmmhNMMMMMMMM-::::----::::::::::::/:///+sMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MNmMMMMmmMMMMNNMmshddhdddddhdddddmMMyyhhhhhhhhhddddmddhhhyyNdydddNmmddhdmmdMMMMMMMMmMMmNMMMMMMMN::::::::::::///://///////++yMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMd+mMMNmMMNNNmmmmNNNmmmmmmmmmMMyshhyyhyyyhyyyyyhhhhhhyyyhdmmNMNNMNMMMMMMMMMMMmMMMdMMMMMMMMN:::///:::///////+/++++++++ohMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMNdy++yNmMmdmmmmmmmmmmmmmmmmmmmddddyyyyyyyyyyysssssssyhhhhhyyyyhmNMMMMMMMMMMMMMMMmMMNNMMMMMMMMN//+///////+///+//+++osyyyhdNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"hsooshNmmmmmmdmmmmddddddhhhhhhyyyyyyyyyyyyyyyysyysyyyyyyhddhyyysyydNMMMMMMMMMMMMMNMMMmMMMMMMMMMm/////+++++++++++/+++ooyhhdNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"hdmNMMMMMMNNmmdddddddhhhhhhhhhhhdmhyyyyyyyyyyyyysssssyyyyhddhhhhdmmmmNNNNmNNNNNmNmmmNdddhhysoo++++++o++++++++++++++++oosyyhddmmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMNNmmdddddhhhhhhhhhhhhyhdddhhyyyyhhhyyyyssssysssssyyhhhhdddhddddddhdhhhhyyhyyyhsooo+++++++ooooo++++oooooooosossssyyyyhhhhddmmNMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMNMmdddhhddhhhhhhhhhhhhhyyhhhhyhhyyyyhyyyyyyyyyyysysssssyyyyyyyyyyyyyyyhhyyysysssosooo+oooooooooooosooooooo+ossyyyysyyhhhhhhdddmmmNMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMNMhdhdhhhhhdmdhhhhhhhhyhhhhhhhhhhhhhhdhyyyyyyysysyssssyyyyyyyyyyyyyyyyssssossosososoooooooooooosooossssssssyssyyyyyyhhhhhhddddmmNNMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMNNydmNNNNNNMMmhhhhhhhhhhhdmNdhhhhhhhhhhhhyyyyyyysyyyyyyyyysyyyyyyyyssssssssooooosssossssoosssssssssosssssyssssyyyyhhhhhdhdddddmmmNNNMMMMMMMMMMMMMMMMMMMMMM";
}

