/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms.BackArea;

import Rooms.CountdownRoom;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class Barn extends CountdownRoom 
{
	private static final long serialVersionUID = 1L;

	public Barn(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Barn";
	}

	@Override
	protected void initializeCountdown() 
	{
		//=================================================================================
		//Death after third command
		//=================================================================================
		this.setCountdown(3, "The barn's interior is pitch black. You are unable to make out even the closest of objects. Puzzling. SOME light should be able to pierce through the cracks of the wood at the very least....", 
				"You begin to sense the darkness in this barn is wrong..... somehow, unnatural. You could also swear that you heard something in the black abyss surrounding you, but once again you see nothing. ", 
				"The sounds coming from the dark void around you seem to be growing closer, until you hear what sounds like heavy, raspy breathing. Suddenly the darkness seems to move, disturbed similar to how "
				+ "smoke moves when a man waves his hand through it, but fast. Blindingly fast. Too fast to see what has disturbed this eerie blackness. Pain explodes from your abdomen as you look down and see several ghastly, deep gashes "
				+ "runing across your stomach, blood spilling down your legs and onto the ground. The mortal blow causes all of the strength to leave your body as you fall to your knees, hands attempting in vain to staunch the crimson flow. you look "
				+ "up just in time to see the darkness become distored once again, identical to before you recieved your mortal blow. Then, you see nothing at all. You feel nothing either. Nothing");

		//=================================================================================
		//Count down starts and progresses unless the lantern is lit
		//=================================================================================
		this.setTriggers(null, this.gameState.getFlag("lantern lit"));
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
		//If the lantern is lit, display this description of the room
		//=================================================================================
		if(this.gameState.checkFlipped("lantern lit"))
		{
			this.description = "The barn itself is a very large, yet mostly empty room. In the center of the room lays a run down, old pickup truck. The second floor holds multiple "
					+ "bales of hay, and the rest of the first floor of the barn holds stalls that at one point would have held horses. In the back left corner is a door, most likely leading to another "
					+ "smaller area of the barn.";
			
			return this.description;
		}
		else   //If the lantern is not lit, display this text
			this.description = "As you enter the barn, you realize that you cannot see anything. Just darkness.";
		
		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to the backyard
		//=================================================================================
		this.addMovementDirection("outside", "Backyard");
		this.addMovementDirection("leave", "Backyard");
		this.addMovementDirection("exit", "Backyard");
		
		//=================================================================================
		//Create directions for the barn storage
		//=================================================================================
		this.addMovementDirection("door", "BarnStorage");
		this.addMovementDirection("left", "BarnStorage");
		this.addMovementDirection("storage", "BarnStorage");
	}

	@Override
	protected void createItems() 
	{
		//default
	}

	@Override
	protected void createFlags() 
	{
		//=================================================================================
		//Creates the flag to light the lantern
		//=================================================================================
		this.gameState.addFlag("lantern lit", new Flag(false, "", "The lantern hangs in your hand, its solid white flame iluminating the room"));
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
			//If the lantern is lit, can enter the room on the left
			//===============================================================
			if (command.unordered("door|left"))
			{
				if(this.gameState.checkFlipped("lantern lit"))
					return this.move(command);
				else
					return new DisplayData("", "You can see nothing in the enveloping darkness");
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

		case "light":
		case "turn":
		case "use":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			
			//=================================================================================
			//If user tries to use the knife, tells them there is nothing to attack here
			//=================================================================================	
			if (command.unordered("silvered|knife"))
			{
				if (this.gameState.checkInventory("Silvered Knife"))
					return new DisplayData("", "Nothing to attack here ");
				else
					return new DisplayData("", "You don't have that item. ");
			}
		
			//=================================================================================
			//If the user tries to use the knife, tells the user that there is nothing to attack here
			//=================================================================================
			if (command.unordered("shard|glass"))
			{
				if (this.gameState.checkInventory("Shard of Glass"))
					return new DisplayData("", "Nothing to attack here ");
				else
					return new DisplayData("", "You don't have that item. ");
			}
			
			//=================================================================================
			//If the user tries to use the lantern, makes sure the lantern is in the players inventory
			//=================================================================================	
			if (command.unordered("bronze|lantern"))
			{
				if(this.gameState.checkFlipped("lantern taken"))
				{
				   this.gameState.flipFlag("lantern lit");
					   return new DisplayData("", "you raise the lantern in your hand and speak aloud 'almata.' A white flame ignites in the "
							+ "lantern. The flame iluminates a light that pierces through the darkness suffocating the barn like headlights through a "
							+ "fog. You see before you a hunchedback creature, similar to the beast you met in the toolshed. It has long claws on its hands and feet, as well as a toothy maw and blood red eyes staring at you. "
							+ " However, it's arms are wrapped around it's torso, as if in pain. It's skin is covered in craters similar to a wasps' nest which exume a black smoke. "
							+ "You come to a sudden realization that the darkness you had been gazing into before was some sort of a smokescreen deployed by the creature. As the Lantern dispells "
							+ "the darkness, the beast screams like a human in tremendous pain, a scream that sends shudders down your spine, and flees."
							+ "It rushes into the door on the left side as the door slams shut behind it. In around half a minuet the ink black smoke dissapates from the barn,"
							+ "at last revealing its interior. ");
				}
				else
					return new DisplayData("", "You dont have that");
				
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "Can't do that. ");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			
			//=================================================================================
			//Makes sure the user can only see things in the room if the lantern is lit
			//=================================================================================
			if(this.gameState.checkFlipped("lantern lit") || command.getSentence().contentEquals("search"))
			{
				if (command.unordered("around|area|room"))
					return new DisplayData("", "The barn holds little in the way of exciting. There is a old, neglected truck in the center of the barn. The walls are filled with"
							+ "horse stalls. The top of the barn has an area loaded with hay, yet there seems to be no way of reaching that level. the left side of the barn "
							+ "does have a door however. ");

				if (command.unordered("wall|stalls"))
					return new DisplayData("", "The walls of the barn are covered in horse stalls. Upon closer look, the decaying equipment of hammers, horeshoes, saddles and tack sit covered in dust,"
							+ " unused for some time.");

				if (command.unordered("truck"))
					return new DisplayData("", "The truck in the center has tools scattered all over the floor. The vehicle itself is missing two wheels, seemingly mid repair when it was abandoned. The custom "
							+ "work done to it's exterior and what you can see through the windows of the interior speak to the owner's dedication and care for the truck. Seems like such a pity for it to be left here to rust"
							+ " in some abandoned barn.");
			}
			else 
				return new DisplayData("", "you see nothing but darkness.");

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
	private final String forestClearingImage = "hyyssossooo+/++++/+++++osyhhdddmNMMMMMMMNNNmmmddhhyyysssssyos++++yyoso+//++ooo+ooooooosyyyhhhodddhydNNNNNdddmNNmmmmmNmNmmddddddmdhhdddddddddddddmmNmmmdddddmN\r\n" + 
			"o++++osssyhhdmmNMMMMMMNdyyhmmdhddhyssyo+o+oo+ooo+/++///++ooooosssyhhhhddmmNNNNNNNNmmmdddddhddyhhsysssssoso+oossysssyyyhhsssssssssyyyyyyhhhhdddddddmmmmmmmmdNN\r\n" + 
			"mNMMNMMMMNNmmdmdhhhyssoo/++oossooo+/oo++ossyyhyyddmmNNNNNNNmmddddhhyyhyhyyysssoo+++++/////+++o++++++oooosyssyhhhhdhdhhhdddddmmmmNmNNNNNNNNNNNMMNNNNMMMMMMMNmN\r\n" + 
			"mmdhmmdmmsososy+o++oossyyyyhddmdhhhmNNmhhdhhhdhhsssssoo+++++o/+o+///+++/o+o++oosssyyyhhhhhsdhdmmyshsyhdddhysyhhddhhhhhyyyyyyyyyyhhdhhhhhhhdddmmmmmmNNNNMMmhdN\r\n" + 
			"hyyshhydhhddmmmNNNNNNmmmddhyyyssy+/oso+oooo++o+o++ossoossyyyyyyyyyyhhddhhhhyyyssssosyssossssooyysosssyyyyyyyyyhhhyhhdddddmmmmNmNNNNNNNNMNMMNMMMMMMMMMMMNNdmNm\r\n" + 
			"mdyMMMMMNmNmhyysysssssyo+++oossssssyyyyyyyyhhdhyhyhhhysysysssssso++o+oysssssyyyyyhhdhhhhhhdhddddmdmddhddhhhhhhhdddddddddmmdmmddmmNmNNmmNmMNNNNNNMMMMMNmmmmmNM\r\n" + 
			"MMMMMMMMddyshhhhdddddddmmmddhhhhyyysossys+syssssyshyyyhhhyyhhhddddmmmddhhhddhdhdhyhhhmNdhhhhdhdddhddddhddmmmmNNmNNNNNNNNNNNNNNNNNNMMMMMMMMMMNNMMMMMMNNmmmNMMM\r\n" + 
			"MMNNNMMNNMNmNMMMMNmddhhdhhdhdhhddddddddmmmmmdddhhhmNmdddddmmmmmmmmmmmmmmmNmmmNNNNNNNNNmmNNmddmdddmmmmddddmmmdddmNNNmNNNmmNNNNNNNMNNNNNNNNNNNNNmmmmmmddhhdNmmM\r\n" + 
			"dddmNNNNNMNNMNNMMMNmMMMMMMMMMMMMMMNNNNNNNNNNNNNNNMMNmmmNmmddmdddmmdmmdmNNNmddmmmmdmmmmmmmmddddddddddddhyhhyhyhhyhyyyyyysysyssysssyssyyssyyhssyysssysyyshmNmmM\r\n" + 
			"yyhdddmmmmmmmNNNNMMMMMMMMMNMMMMMMMMMMNNNMNNNNNNmNNNmmdddmhhhhhhddhyyyyssossssoooo+++////++//++++/++/+o/+oooo++so+oooooyhooooo+ssyssssoosossssyyoooossssydNmdM\r\n" + 
			"sydNNNddhdddyhyNNMNmmmmmmdddhhhhhhyyyyyssssoooo++++o++++++ooooosoo++o++++++/++/+++++:///////:////://:///+os+////o+++osyo+o+soososos+oooso++ohhyssosssooyhdddM\r\n" + 
			"NNNmdhyyhmNNyhyMMmosoooo+/osooo+oy+o+osossoso+++o++++ooo+ooooos++ooo///////++////:/+/++/+////://::/:///+//+++/++//++o+oo+++os+oos++++++oooossssoossyyooyddddN\r\n" + 
			"NmhhhhdmNNNdyhyNNmys++s+/oosoooooo+++++o/+///////++/+++/+////////:/::+/+++++//::::///:/://///://:/::/+/+::////+////+/o++o/+///++o+oys+o++o++oyssyyyssooyhhdhN\r\n" + 
			"dhdmNMNNmddhydhNNmyo+++/+/////+/////:://+++//:///:////:::://-:::/:::/+:::::/:------::-:.--:---://-://+/-//:///+++++ssssyssooodyyyyhhyyhhhhhdddddddddddyyhhdhm\r\n" + 
			"MMMNmmdmNMMdshhMMdo//+//+o++oo+++ooooo++++ooosssysyyyyyhyyyhsyyyhhhhhdddddddmmmmmyNNoNMMM:NM+MMyMhyMoMM/MM+MMsMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMyodhdyd\r\n" + 
			"NmddmNMMNNNhysyMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMdMMoMdMM/MM+MMyMhhMoMM/MMoMMmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNN/:hyhym\r\n" + 
			"MNNMMMMMNNMyoosMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNNNNNNNMMMMNNNNMMNmdmMMMMMMMMMMMMMMMMMMNyMMMMMMMMMNNmmmmmdhhhhhhhhhhdNNNNNMMMMMMMMMMMMMMMMNdMNN/osyyym\r\n" + 
			"MMMMMMMMMMMy/oyNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNmMMMMMMMMNMNNMMMNNNNMMMNMNNMMNMMNMMMMmMNoMMoMNdMNmd/:-......-----::::::::::::::::::::::://++oyyhdNNdmsdssshm\r\n" + 
			"MMMMMMMMMMMs:yyNmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNNNMMMNNNNMMMNmNNMMNNMMMNNMMNm+NN+Mmhy-  `-::///++++++oooooooooooooooooooooooooooooo++/:/ssohyyydm\r\n" + 
			"s+/+o+odNMs+:yhNmhmNMmddhhhdNNMMMMMMMMMMMMMMMMMMMMMMMMMMNMMNMMMMMNMMMMNMMmdsoyNNMNMmdNNNNyNmyMmo   .://++oossssssssssssssssssssssssssysssysyyysyyysssyysssshd\r\n" + 
			"/..:. .ydM`-:+hNN-:hm` `    -mMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNMMMMMNmMmdyo//+mNMMmhdymmdmyomy.  .://++yysssssssssssssssssydsoydhyyyhhhhhhhhhhhhhyyyyymyoshd\r\n" + 
			"o++s++ohdN`.:/yMN.:yN. .    /NMMMMMMMMMMMMMMMmdydNNMMMMMMMMMMNmNNNNNNmdmmhydhhmdmNNhys/syhyyymy. `-://+oh/////::::::::://///+sods/++++++++++++///+sysysmmsshh\r\n" + 
			"m//osyyymM+:--yMm`/yN:.:`..`hNMMMMMMMMMMMMNNMNyssmNMMMMMhNddd/mN     ohsmyshshysddy+h+:+/:oo+ss  `-://+oh:/:::::-------:-::::o+h+///////////++/++/+ysddmmdssh\r\n" + 
			":/-ossosdN`-::yMm`:sN. .  ..yNMMMmNmMMMNMMdNmh:/yhNMMNMNydNyh+dm     +ysd+::omoodMNNhooohyys+yo  .-://++y+:/:-------------::/o+yo::://///////+++///ssdmmNdssy\r\n" + 
			"`-:+///smd .:+yMN-/yM::/-.-:dNNNNNMNNMMmMNNmh:``.:omMNMNmmNmh+mm     oysh/+oyysomdohh::-yos/`.:``.-/o+ososso++++++++++o++oo+oooosso+++ooooooooso+ooysddNNmsyh\r\n" + 
			":y+:/oshyd+-::yMNhdNMmNNNNmNNNmNMNMdMNddNhsdh+`:/shNNdNmhddsh+hm```  oyyy/:o+oo+ms:oh:/:+/y:`./:`-:/y+hh+++++ooosyyyysooooooooooooooooooossyyyhhhddmhdmNNNydh\r\n" + 
			"MMMMMMMMNMM:``sMMNNNNmmmmmmMNhhMMNmhNNddNhshyo-/ohmMMdMNmmmdNdhNMMMNNhshh/:/-+s+d+-.oo..::+:+/s:`-+odody++ooooosyhhdhhssooooooo++ossssoooosshhddmmmmmhmNNNymh\r\n" + 
			"MMMNNMMMNNN: :mMNNdydhdyddmNmyhNNNmhhNNhmddyhs/sydNMMdMdsdhmddmNMMMNmhhyy:/yyyy+mo+:o+/:-:-.ooy/.:ydmsmdo+oossyyhhdddddddddhhddmmmmNmmmdddddmmmmmmmmmdNNMNhmy\r\n" + 
			"mhddhdhhmmm/``sNdmmmmMNmddmNdoyNNhmdmhsym+shhddmdMMMNmMdyhdmhydNMMMMmysss-/+oosoms//y--::-:-ysyo-+hdmhddossyddddmmmmmmmmmmmmmmmmmmNNNNmmmmmmmmmmNmmmmmNNNNdNh\r\n" + 
			"mNmMMMMMMMM/ `omdmhyysh/symhdshMNmmNNmyhNohhhNmNmMMMMhNmdmmNmysNMMMMNhyoy:/o+y+/m+.:d:++:--+oo:-.:/++oooosssssssssssssssssssssssssssssssssssyyyyyhhhyyhdmmdNd\r\n" + 
			"mdddddhyhmh- `oddmshh+sshdNmdshMNNNmNNNsNsyyhdyyyyyyyyyyyyhyysoNdddddsssy+/syys/mdshhymyo/-sd:``-+shhdmdhdmmmmmmmmmmmmmmmmmddddddddmmmmmmmmmmmmmmmmmmmmmmNmmd\r\n" + 
			"shddsyysmmm: `ohomhdhsdhhddmyoddhmmmdddddyhssyysoooosos++++++o+so+//os/+yoossos/mdsyhhmmys:o/.` .so/mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm\r\n" + 
			"ydhhshdymdm/  /yohmmmNNmmdmmho+osysyyysyhyyssoossooosoooo+oo++o++/+++ooo+ooo+o+:dyoymss//h/:` `.-y++ooyhdmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm\r\n" + 
			"dyhmhdmdmNN+``:sohmNmmddddhyhsyyssssysoooo++o++++/+++/////+++++++++o+oooooooo+oosyyyhhhyyyo+/ `-os/+osyyyyyyyydddddddddddmmmmmmmddddmdddddmmmddmdddmmmmmmmmmm\r\n" + 
			"mmNNNNmmmNNo``-y+ysso+ooooooosooossoooo+/+++++///:///+++o+++++++++o+++o++ooo+o++oo+ooooooo:sy`.+yo//+//++oydh+hdddddddddddddddddddddddddddddddddddddddddddhhh\r\n" + 
			"dmmmmmddhhy/`--sooooosooo++os+ssoooo++++//++////+++oo+++++/o+++++++//+++++oo/++oo++++o+-`  oh:..`   ``.ohhsss/hdhhhdddddddddddddhhhhddddddddddddddddddddddddd\r\n" + 
			"yyyoo++o+oo:`..yso++o++oo++osooo+++/:/+/+++++o+//++/+++//++/++/+//+/:oo/////o+///+//+-s. `.:o.        `.smdhoohdhhdhyhhhhhhhhhhhhhhhdhhyyhyyyyyyyyyyyyyyyyhhh\r\n" + 
			"++oo++o+oss/`.-dy++oo++o+osooo/+/////+////////++//+//+////+////:+//+//++/+///++/++///`/s`---o/       `.-:dmdhoydhdhhhhhhhhhhhhhhyhhhhyyyyhhyhyhyyhhhyhhhhyhhh\r\n" + 
			"oosyoooooooo--.syoossssssso++/:+///+//////////+/+///:+///////:+:+/////+//+++++/////+os.y-:-.oo`     `.-:/ommd/ydhdhhhhhhhhhhhhhhyhhhhyyyyhhhhhhhhhhhdhhhhhhhh\r\n" + 
			"so+o+oo++oo+:-.+soyydhys+:/://///////+///////////:/+/////////////////////++/+++//+++/y.s//.`os-` :. .-:/++mmd+hdhhhhhhhhyyhhhhhhyhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"+ooooo+o+o++--.syymdyo+///:////://::::////:/://///::///:/:/+///://///////+//++/////+/o.oyso::so- - `.::/+ohdhohdhhhhhhhhyhhhhhhhyhhhhhhhhhhhhhhhhhhhhhhhhdddd\r\n" + 
			"o+o++o++o++/:--hyyd+++//:/://::/:////::////////::/:/:/:/::///////////+/////+///////+//.+hhmy--s+ ` .-://+oyo++hdhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdddddddd\r\n" + 
			"+o+///+:::::.-:/:/::/:/+/:///////:::::++/://+://::::///:/:/:///:////:/:++//://:+++++/:-+hdNm--+s. `.-:/+++ohhysdhhhhhhhhhhhhhhhhyhhdhhhhhhhhhhhhhdddddddddddd\r\n" + 
			"++++////::::-://-//:::+//://:///:::/:/::+/://:/+::::/:/+::://://///:/:://///://://////-+ymNN/-:s/ `.-://++oydhsdhhhhhhhhhhhhhhyyshhyhyyyyhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"/o+o++/+////:/+//://::::/-:/://::/::///:/+//:/://::::/:://:-::/:/:-:/:/:::-::::/+://:-.+sdNN:-:os``.-://++oshdsdhhyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys";
}
