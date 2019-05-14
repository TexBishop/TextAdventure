/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms.BackArea;

import Items.BasicItem;
import Items.Item;
import Rooms.CountdownRoom;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class CornfieldCenter extends CountdownRoom 
{
	private static final long serialVersionUID = 1L;

	public CornfieldCenter(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Cornfield Center";
	}

	@Override
	protected void initializeCountdown() 
	{
		//=================================================================================
		//Death after third command
		//=================================================================================
		this.setCountdown(5, "You slowly step forward, trying to get a better look at the nightmareish sight, trying deeperately to understand what your eyes are taking in. "
				+ "As you take a few steps closer the sound of your approach draws the attention of the dancing woman. Her head snaps towards you so fast "
				+ "it startles you. ", 
				"The being looks at you with eyes like molten metal, and slowly walks towards you, at first a smile, a joyous one. It sends another wave of piercing pain through your head and "
						+ "a stream og blood from your nose. Her smile quickly dissaperas as she yells something indiscernable as throws her hands bacl, long bony claw-like protrusions extending from them", 
						"The being rushes you swinging swiflty and aggressively at your chest, screaming loudly and cleary 'YOU PROMISED! YOU PROMISED ME AND BROKE IT AGAIN!' Adrenaline bruns through your veins as you "
								+ "roll switfly out of the way, jumping to your feet once more, sword now drawn, instinctively.", 
								"She rushes you again, as unrelenting as the rapids as she swipes over and over screaming 'HOW COULD YOU? WE WERE MEANT TO BE INSEPERABLE' The blows come so fast and hard that you are unable "
										+ "to parry them all and one comes through, slicing your arm and sending pain rippling through it. You quickly regain your composure and fix your stabce panting heavily.",
										"A smile suddenly comes across her lips, a sick, twisted smile that sends another piercing, alsmot unberable shot of pain through your head as she lunges at you once again, putting all her strength and speed against "
												+ "two blows, one which knocks your sword away, and the second which pierces clean through your chest. All five of her terrible claws slowly coat themselves in your blood as she slowly leans her head next to your ear and whispers "
												+ "'Now my love, we can truely be together' as she rips her claw out just as quickly as she impailed you. There is no strenght left in your body. You fall over, landing on your back, unable too look around. All the sensations leave your body, as the "
												+ "last thing your eyes see are the overcast sky above, as your killer slowly walks over yo you, claws and fangs retracted, the only things still monstrous about her are her blood red eyes and black streams "
												+ "coming from them. She looks at you, and smiles.");

		//=================================================================================
		//Count down starts and progresses unless the brooch is in inventory
		//=================================================================================
		this.setTriggers(null, this.gameState.getFlag("used longsword"));
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
		if(this.gameState.checkFlipped("used longsword"))
		{
			this.description = "You stand in the bloodied hellscape of guts, gore, limbs and god knows what else scattered aroun the field. Posts of wood with parts of bodies "
					+ "strapped to them form effigies displaying odd symbols. The ground in the circle is a menagire of bodies and their various bloodied butchered parts. "
					+ " The corpse of the woman lays at your feet, still and unmoving. "
					+ "You see a path you didn't notice before, heading towards the forest behind the cornfield. ";

			return this.description;
		}
		else
			return this.description = "You trudge through the virtual labyrinth of corrupted cornstalks before you finaly come to a clearing that immediatly makes you sick and nearly vomit. The circle "
					+ "you now stand in is charred black, covered in gore and viscera of what is clearly a dozen or so people. The floor itslef if a mess of flesh, bone, limbs, clothing, and other small "
					+ "personal effects such as watches and glasses. Amoung the circle stand poles with limbs and other body parts stuck togther, forming weird totems portraying odd symbols you "
					+ "dont even strive to understand what they mean. The veins and tendrils you saw infesting and corrupting the crops stems from the sides of this murder pit. The Figure you saw earlier "
					+ "is visible in all its grisly glory. She dances and skips around the field of viscera, singing a tune that chills your blood and echos in the field. She finally stops and looks at you, "
					+ "her eyes crimson red, streaks of black ink like liquids pouring from them. Her canines are elongated to virtual fangs, and her hand are soaked in blood, along with the rest of her white dress. "
					+ "Your head hurts once more, that same piercing pain, bloodied nose. ";
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to Cornfield Entrance
		//=================================================================================
		this.addMovementDirection("edge", "Cornfield Entrance");
		this.addMovementDirection("entrance", "Cornfield Entrance");
		this.addMovementDirection("corn", "Cornfield Entrance");
		this.addMovementDirection("cornfield", "Cornfield Entrance");

		//=================================================================================
		//Create directions that move to Back of Cornfield
		//=================================================================================
		this.addMovementDirection("path", "Back of Cornfield");
		this.addMovementDirection("forest", "Back of Cornfield");
		this.addMovementDirection("north", "Back of Cornfield");
	}

	@Override
	protected void createItems() 
	{
		Item EngravedRing = new BasicItem(this.gameState, "Engraved Ring", "", "A ring of platinum, set in with five stones: A diamond, amnythest, emerald, ruby, and saphire. "
				+ "The ring also bears countless other engraved symbols. On the inisde is a engraved saying that reads: 'Here I stand, and here I will stand, by your side for all of time, "
				+ "no matter what stands against us, be it Heaven or Hell, The Brightest Day or Darkest night, I will be your sheild, my love'.");
		this.gameState.addItemSynonyms(EngravedRing, "engraved", "ring");
		this.gameState.addSpace(EngravedRing.getName(), EngravedRing);
	}

	@Override
	protected void createFlags() 
	{	
		this.gameState.addFlag("used longsword", new Flag(false, "", "The longsword drips with fresh, yet tainted blood. "));	
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

		case "attack":
		case "stab":
		case "slash":
		case "use":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("silvered|knife"))
			{
				if(!this.gameState.checkFlipped("used longsword"))
				{
					return new DisplayData("", "You lunge at the woman, gripping the silver knife tightly in your hand. You swing it with all the precision and power you can muster, "
							+ "but she effortlessly parries the blow, and uses the oppertunity to couter-attack, slashing at your leg and drawing blood. Its a miracle she didnt hit an artery, but one thing "
							+ "stikes you immediatly: the knife gets you too close to her..  ");
				}
				else
					return new DisplayData("", "Nothing to attack here ");
			}

			if (command.unordered("shard|glass"))
			{
				if(!this.gameState.checkFlipped("used longsword"))
				{
					return new DisplayData("", "You grip the shard of glass tightly in your hand and lunge at her. You slash deep and powerfully, but she does not so much as blink. She laughs and kicks you "
							+ "hard, sending you tumbling away frm her. You jump back to your feet and ready yourself once more.");
				}
				else
					return new DisplayData("", "Nothing to attack here ");
			}

			if (command.unordered("silver|longsword"))
			{
				if(!this.gameState.checkFlipped("used longsword"))
				{
					this.gameState.flipFlag("used longsword");
					return new DisplayData("", "You grip the sword tightly and lunge at her. You swing the sword across her chest cutting deeply. She screams in pain, turing arund and chraging at you once again. "
							+ "And again you dodge under her blows, delivering a ghastly slide across her left side. She begins to stagger, her face conterted in fear and pain as she lunges at you again, screaming in desperation. "
							+ "You see much of her strength has left her, The balance, percision and cunning she had is failing her. You duck down, and with a brutal upwards thrust you shove the longsword through her "
							+ "torse, picking her up off her feet as you drive the blade through her torso up to the crossguard. You look up at her, and her fangs receede, as do her claws. She reaches a wobbling, bloody hand toward your face, "
							+ "strokes your cheeck and whispers your name before sliding off the blad, collapsing on the floor. The black ink that was streaming down her cheeks have ceased, along "
							+ "with all her movement. You take a deep breath and notice a ring on her finger. ");
				}
				else
					return new DisplayData("", "Nothing to attack here ");
			}

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		case "grab":
		case "pick":
		case "take":
			//===============================================================
			//Take the engraved ring
			//===============================================================
			if (command.unordered("engraved|ring"))
			{
				if (this.gameState.checkFlipped("ring obtained") == false)
				{
					if (this.gameState.checkFlipped("used longsword"))
					{
						this.gameState.addToInventory("Engraved Ring");
						this.gameState.flipFlag("ring obtained");
						return new DisplayData("", "You oick up the ring, gently taking it off her bloodied finger. You clean it on your shirt and look at it. A beautifuly engraved ring, bearing five "
								+ "stones: amnythest, diamond, ruby, saphire, and an emerald. As you look at the ring your head explodes in pain. So bad it forces you to your knees. You grip your head tighly, "
								+ "seeing blood dripping from your nose, and suddenly the pain is gone. You remember. You remember the ring. You remember the sword. You remember all of it. You look at her, close her eyes and struggle to your feet, "
								+ "choking back tears, and compose yourself.");
					}
					else
						return new DisplayData("", "You dont see that.");
				}
				else
					return new DisplayData("", "You've already taken that.");
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
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The circle you stand in is a bloodied field of limbs, bodies, heads, bones, gore and blood. The people look to have been torn apart by something and used in effigies, "
						+ "which also litter the circle. You would assume the people were used in some kind of a ritual. To what end, you dont know. ");

			if (command.unordered("effigies"))
				return new DisplayData("", "Some limbs from the victims were taken and used on poles of wood to create effigies, which are all evenly spaced out around the circle.");

			if (command.unordered("floor"))
				return new DisplayData("", "The floor is a carpet of death. Everywhere you look bodies, limbs, organs and blood litter the floor like a landfill. "
						+ "The edges of the circle spout out the tendrils and veins you see corrupting the surrounding crops.");

			if (command.unordered("body|girl|woman"))
				return new DisplayData("", "The woman lays on the ground among the rest of the gore and death. Her white dress is stained with fresh blood on top of dried blood. "
						+ " Her hands and feet are so red with blood, it's as if they were dipped in it. She lies there, motionless.");

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
	private final String forestClearingImage = "::::o:-----------:-------:-...........................``````````````````````````````                                  /hsshh`..:-+ss+..:::oy   `y-odmmddmddmd\r\n" + 
			"//-//-------------+:---:///-.........................````````````````````````````                                -/::/hh.ydh` +hdy/+ss/+:::/`-+syhddyohdmdshm\r\n" + 
			"/+++/+/::----------+o:--:/+-.......................``````````````````````````````                              :::`  ++.oddd``yosys+oyyso-+/::/osyy-`/dddso:s\r\n" + 
			"o+oo//+//:----------:s+--+:.....................````````````````````````````                                 ---`    .  /ddo:+o+/syo::osy/s/yhyhhs.-:hdhyy+-`\r\n" + 
			"+//+/::--::--------..-+o.+.....................```````````````````````````                                 `.-.:..`.    -y:-:/hyosysyysoydo+so`/ss+hydmhss+/.\r\n" + 
			"yo/o:::::::-------:---.:+:....................````````````````````````````                                 `-.:/:-:/+--..o :+oo:syddhsyhhd+ss+osoymd+yhyy/- `\r\n" + 
			"+yoo+////-::----::::/:-.-/...:+:+:........`````````````````````````````                                       `/-oss+- :+s:osyyo+hhdhdhhddhy+sshho-ymdso://:-\r\n" + 
			"--o++/----------::/ydmy/.:.:s+...:+-..````````````````````````````````                                        ./osso+:ooosysy+s++dhdmmmdyhys:sdm/` `.//ohmdmh\r\n" + 
			"--+//osso/-----+o/++dmys:://-......::.````````````````````````````````                                       `./sos:oso+shy+yddhyyys+yhhddo/:yyd-  ` sodmmhNm\r\n" + 
			":-+/://yo/+::::+yo+oyhoss:-..-..::----.``````````````````````````````                                     .-:/:os:osy+::omhyyhmmdhyyhoodds+ydy/do///smddhdmmm\r\n" + 
			":/o+o++/so:++/ys/++:+yy:+s:-.-.+:.:--..`...`````````````````````````                                    `:.. .-++//+/``yhNoddsymdd+-oyyyhosyy/.dyymmNmhhomNdd\r\n" + 
			"::++so//o+o+:o++s:o:-/oyo++/:o+----.-.```.-:```````````````````````                                   `-:     :ss//o/--yhmyhdssydhy/syyddo:ss/+ddmmddyydhNmhd\r\n" + 
			"+:+ooh+--:+s+++s/-+o:..+hsoo++//:-/.-`..../```````````````````````                                   `:``.--..oh+.++/+syhhssyyhsyddddyyyh+/ohshdmmyNdymhmNmdd\r\n" + 
			"shho+yo+/+sss+ooo///:--/yds-++/--/o-:.`---``````````````````````````                                `.`:/-.-++s:ss//ooshmdyhddmmdmh/ydhdyoyooyymM+hmmhhymmmdN\r\n" + 
			"yymyysoooyyo+oyo+os++:++ssy-/+./o-+-:o/-./+:````````````````````````                               ` `:+::/:`oh/`++so-oydmdmmmmdd:+hydhymdy+ymdNM+mmdmmdmmmmN\r\n" + 
			"yydhsoyss+ysysy+osyo:---sso:/o/y-./o/o+/`+-:-``````````````````````````                             `-.oo-`.++yo/syy:/shddmmddhhdyyhdddsmhysmmmMMoNdhmmdmmmNh\r\n" + 
			"hyyyyoysoyosyhyyosso::::-+s:oss-:/os//-/++--/.`````````````````````````                             ``:-    -shhsoyo:sddmmmmyhdmmNNmdmhhddNNhdhmNsyhdmddddNdy\r\n" + 
			"yydmyhdhyosssddy/oooo+osso+/+s/://+o./:.-+++-.`````````````````````````                `` ``         `  -:.:yydysyo`:hmmmmmmhdmmNNNhNhdhhNmhsyhmNNmmdmdhdmmhy\r\n" + 
			"dysmdsyhhoyddyyysso+so+oy/++/soh+o:s+y/:/so:-``..-.```````````````.```````````````-```.::..````-````.`:::.:/ysm/so+:/oymmmmNddmmmmNdmdhmMNhhhmmNNmmmmhmmmmddy\r\n" + 
			"dddmhdyyhysyyyyhyyyoso+osysy+sys+s/+yys+syo/:++//:/-::::-:.-:-..-----..-::::-//:--s+//+o+++///:+:/::-:/-::oyhdms:y//dydmmmdNmmdNNNmmmmmNMhmhdhmNNmNmNNdMNdmNh\r\n" + 
			"odhddhyhyhyyyhdddshshys++shy+hsysss/sodsos++oysosoooooo+oo+++//+++++++/o+oo+sdoso++//osoyooooosooosoooooooysydhsoysydsmmNNdmmmdNMNNNNmNNNmmdyymNNNmmNmNMdmdmm\r\n" + 
			"ohhhhhhsyssyyhhdyy+yhyyssoso/ydsosshoshoysosyhyyyyysssyyyyyssssoossysssyyyhymmsyyyoooysshhyyyhyyyhhhhyyssyyyhmhhyhshhhmmNNmddhhNMNmmmdNMNmmhyhmNNmdyhdNMNNmdd\r\n" + 
			"yymddshyysyhhssdyy:hsdyhysy+odddhyshyyyhhhohhddhyhhhhyyhdhhyshyyshhhhyhhhddhdNNmhhNNhhhdddddhddhdddddhhhdhydhdhdhddhhyddNNmmddNNNNNmmmdMMddsdmmNNNdhhmMNNNNyd\r\n" + 
			"homdhhoyymdmyyhdyd/yshhhsyyhodyssyohhsohdhoshhhhyhdhyhhdddhhhyhdhhdddddhddddmdddmmNmddmdddmddmdddmdddddhdyydhdhdddmmyhsdmNmmddNNmNmdmmmNMdddmdmNNNmNmNMdNNmhd\r\n" + 
			"dsodhhsyhhdddhmddhsoyysoyssysmhs+yysysohss+ohyyysyyyyyssssssssssssssyyyyyhhhhyyyyhhhyyyyhhhhhhhhddddddddyhhyddhdmdmdhhsmNmmmmdNNNmmNNmmmMdmmmdmNdhmmdNMmmNNNd\r\n" + 
			"dd/hhyssdydddddmhdsdhyhshssoyyyoosyysoyoooo/++++++++++++++++++++/+///////////////////////+++++++++++++oosyyhddyyddyyNdhNNmNdmmNmmmmNmmmmMydmmmNNNNNNdNNdmNmNh\r\n" + 
			"yyho/shoyhhmdyyyyhyyyshyoooosooyoosddyyss+yo+++++++++++++++++++++++///////////////+////++/+/+++++++++ooooyhhhydmdhshmhmMNmNyhNNmdmdmNNmdMhdmdmNNNNNmhNNmmNmNm\r\n" + 
			"ddydy+ooyyhmyhhhhm+ysshh++sso+oy/o+dddh+osooo++++++++++++++/////+/////////////////////////++++++++++++++ohhdmhmdmhsyhdmNdmNddmmmdNmmNNmNNhdhmmMmmmmmdNNmNNNNm\r\n" + 
			"ddhhhyyydsdhdhsdmhshy+hs+sosys+yo+shdhyy+s/oo+++/++++++//+++/////////+++///+//+++++++/++++++++++++++++++ssyhmdddmdydddmNmmNhymmmmNmddNmmdhddmNMNNNNdmNmmmNNNm\r\n" + 
			"dmhmdsssssyhhdhhyo+ysodyosshyyhossoddyosoo++/+++++++++++++++///////+//++/++++++////++++++++++++++o++o+++shydmddmmdhhhhmdmmNdhmmmmdmdNmdmhhddmNNdmmMNmNmmNNNNd\r\n" + 
			"hdmdmydydmhmdhddhdsyyydysshyshyhsdyhmyosss+o/++o++/+++/++//+++//+++++++//+/+++++++++++++++++++++++o+o++yohydmmdmmmmdddmmmNNdhmmmNmNmNNdhmdmNNmNNmNMmNNNmNNmmm\r\n" + 
			"dmmdmhdymhhdhsddhhhsohdyshdyssohdyyhdy+soy+oo+oo++++o+++oo+++++++oo+++++o+++oo+++oo++++++++++++++ooooooyshhdmmmmmmmmmdmmNNmmNNmNNNNNNdhdmmNNNmNdmmmmNddNNNmmm\r\n" + 
			"dddddymhhhyhhyhhddhosyhyyydyyhhydmhddhoso+oyoooooooooooooo+ooso++ooooooooooooooooo+o++o++++oooooooosshhddmmmmmmmmNNNNmmmNNNNdNNNmNNNNddmNNNNmmmhmNNNNmhmNmNmm\r\n" + 
			"dmoddmmhodsmsdhddhy+syyyyhdyyyyooyyhhhosso+sssso++++o+oooo+ooo+oooo+ooooo+ooooooosoooooooooosossssyydmmmmdmdmmmNNNNNmmmNNmmmmNNNNNNmmmmmNNmmmNmdNNNNNNhdNNNmN\r\n" + 
			"dmydyddys/sssdhhdhsyoyydhhdysysyydmhhhsss+yoossooooo+oo+oo++++ooooooo++oo++++++oooooooooooyhssyhhhhddmNmmdmNmmmNNmNNmmmmNNmdmNNmmNNNmmdNNNNmmmmmNmmNNdmmNmNmN\r\n" + 
			"ddydyhhyhooyshddshhyysyhhhyhdhhsyhdyydyyhssoo+ooosoosoooooooooosooooo++oo++++++++ooososssyyhhhdddddmmmmmmmNNNNNNmNNmNNNmNNmNNNmmNNNNmmmNMMNmNmmNmNNMNNNNNmdmN\r\n" + 
			"dmmmmhssss+ohmdhhhysdsosdhdddmhdyddddhyyysoossooooo+oooooosooooo++osysoosysooosssyyyyyyhhhhhddddddddmmmmmNNNNNNNNmmNNNNNNNNmNNNmNNNNmmNNNNNmNmNmNmmNNmNNNMNNN\r\n" + 
			"ydddddyoh+s/+shdhhmhyhyosyddmdysyhddhysssssooooooooooooossoooooooooooyyyyyhhhyhyhhhhhhhhdddddddddmmmmmmmNNNNNNNNNNmmmmNNNNNmmNNNNNNNmNNNNNmNNmNNNMmNNNmmNMNmN\r\n" + 
			"hmsddydyy+h//o+yyhmhshyshysosyyhhyyhyyoosoosooooososoossssyyyyyyyssysyhhhhyyhhhddhhyhyhdddddhhhdmmdmmmmmmNNNNNNNmNNNmmmmNNNmNNMNmNNNmNNMmNNNNmNMMMNmNNNNNNmmN\r\n" + 
			"mhdmddyhyohsoosssdhhshyssyhys+syssooooooooooooooosoyyyyyyyysssyhhyhyyhhhhhhhyhdhdddhhyhhhhhddddddmmmdmmmmmmmNNNNNNNNNNNNNNNNNMMNNMMMNMMNNmmMNNmMMNNmMMNNNNmNN\r\n" + 
			"NmdydhyyyshhoyoooshmyhddhshssosyooooooooososssssyyyyyysoosyyyyyyhhdhhyyyhhhhdddddddddhddddmdddmmmmddmmmmmmmmmmNNNNNNMNNNNMMMNNMMNMMMMNNNNmmNNNmMNNmNNNNNmNNMN\r\n" + 
			"hohmydmdsohmh++syyyhyhyyysyyhddysyyooossssssssssyysyhyoosssssssshyshhhyhddhdhdddddhdhddhddddmddmmdmmmmmmmmNNNNNNNNNNNNNNNNNMNNMMMMMMMMMNmmNNNNNNmmNNNNNNNNNmm\r\n" + 
			"hmdmddNNdsmhho+o+/yyyyhhddmmdddyssoooosyyyysyyyyssoshhysoooosssyyyyhdhhdddddddddhdhdhdddddddddmmmmmmmmmmmmmmNNNNNNNNNNNNNMNNMNMMMMMMNNNmNmmdddNNNNNNmNNNNNNNN\r\n" + 
			"omdNhymNdymdhy+do+/yhhhddddhddhssooosssyysyhhyssssoosyhhhyyyhhhhhhhhhhhhhdhhddddddddddddddddddddmddhddmmmmmmNNNNNNNNNNNNNNNMMMNMMMMNmhmNNmNNmNNNNmNNNNNNNNNmN\r\n" + 
			"omNdyddNdhsm+yoyhysoysyhhmhyhysoossyyssssyhhhyoooosooosssyyyyhhhhyhhdddhhyhyyhddhdddddddddddddhhddddhdmmmmmmmNNNNNNNNNNNNNNNNNNMMMMMMNNmNmNNNmmNNmNNmNmdmNNNN";
}

