/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Items.BasicItem;
import Items.Item;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
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
		//=================================================================================
		//When entering a room, reset inner space to null.  This is to prevent previously
		//used inner spaces from lingering.
		//=================================================================================
		this.resetInnerSpace();
		return new DisplayData(this.forestPathImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		//=================================================================================
		//Description is different the first time this room is entered
		//=================================================================================
		this.description = this.gameState.getFlag("ForestPath first visit").toString()
				+ "A huge, ancient tree stands before you, an oak by the looks of it. It splits the path in two, with a fork to either side. "
				+ "There is no discernable difference between the left path and the right path, other than direction. "
				+ "The path leading back to the farmhouse trails off into the forest around a bend. ";

		this.gameState.flipFlag("ForestPath first visit");
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
		this.addMovementDirection("farm", "Edge of Forest");
		this.addMovementDirection("house", "Edge of Forest");
		this.addMovementDirection("farmhouse", "Edge of Forest");
		this.addMovementDirection("path", "Edge of Forest");
		
		if (this.gameState.checkSpace("Edge of Forest") == false)
			new EdgeOfForest(this.gameState);	
		
		//=================================================================================
		//Create directions that move to Forest Clearing
		//=================================================================================
		this.addMovementDirection("right", "Forest Clearing");
		this.addMovementDirection("clearing", "Forest Clearing");
		
		if (this.gameState.checkSpace("Forest Clearing") == false)
			new ForestClearing(this.gameState);	
		
		//=================================================================================
		//Create directions that move to Forest Cliff
		//=================================================================================
		this.addMovementDirection("left", "Forest Cliff");
		this.addMovementDirection("cliff", "Forest Cliff");
		
		if (this.gameState.checkSpace("Forest Cliff") == false)
			new ForestCliff(this.gameState);	
	}

	@Override
	protected void createItems() 
	{
		//=================================================================================
		//Create brooch
		//=================================================================================
		Item illuminatiBrooch = new BasicItem(this.gameState, "Religious Brooch", this.broochImage, "A brooch. It looks like some sort of religious symbol, "
				+ "a pyramid with an eye inside of it.");
		this.gameState.addItemSearch(illuminatiBrooch.getName(), "religious", "brooch");
		this.gameState.addSpace(illuminatiBrooch.getName(), illuminatiBrooch);
	}

	@Override
	protected void createFlags() 
	{
		//===============================================================
		//Flag to determine if this is the player's first time visiting
		//this room.
		//===============================================================
		this.gameState.addFlag("ForestPath first visit", new Flag(true, "", "As you follow the path into the forest, passing beneath the enclosing "
				+ "canopy into wood's shadowed depths, you feel a tingling on your skin.  You dismiss it as simple nervousness, brought on by "
				+ "the creepy atmosphere of your current surroundings. Ignoring the sensation, you continue following the path, "
				+ "until a massive shape rises in the gloom before you. "));

		//===============================================================
		//Flag to control whether the left path is active.
		//===============================================================
		this.gameState.addFlag("left path switch", new Flag(false, "", ""));

		//===============================================================
		//Flag to control whether the right path is active.
		//===============================================================
		this.gameState.addFlag("right path switch", new Flag(false, "", ""));

		//===============================================================
		//Flag to control whether the right path is active.
		//===============================================================
		this.gameState.addFlag("brooch in inventory", new Flag(false, "", ""));

		//===============================================================
		//Flag to determine if the brooch has been taken
		//===============================================================
		this.gameState.addFlag("brooch taken", new Flag(false, "The brooch is still sitting inside. ", "It is empty. "));

		//===============================================================
		//Flag to determine if combination is set.
		//===============================================================
		this.gameState.addFlag("combination correct", new Flag(false, "You step closer to it to put a hand on it, and notice something curious. "
				+ "Inset on the surface of the tree, is a combination wheel, with three number choices, such as you'd find on a combination lock. "
				+ "What the... ", "The combination is as you left it, set to 469. The small compartment is still open. "));
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
			{
				//===============================================================
				//If right path hasn't been unlocked, loop back to this room
				//===============================================================
				if (this.getMovementDirectionRoom(command.getSubject()).contentEquals("Forest Clearing"))
				{
					if (this.gameState.checkFlipped("right path switch") ==  true)
						return this.gameState.setCurrentRoom("Forest Clearing");
					else
						return new DisplayData(this.forestPathImage, "You come upon a fork in the path, that looks strikingly similar to the "
								+ "fork you just left. " + this.fullDescription());
				}
				
				//===============================================================
				//If left path hasn't been unlocked, loop back to this room
				//===============================================================
				if (this.getMovementDirectionRoom(command.getSubject()).contentEquals("Forest Cliff"))
				{
					if (this.gameState.checkFlipped("left path switch") ==  true)
						return this.gameState.setCurrentRoom("Forest Cliff");
					else
						return new DisplayData(this.forestPathImage, "You come upon a fork in the path, that looks strikingly similar to the "
								+ "fork you just left. " + this.fullDescription());
				}
				
				//===============================================================
				//If choice wasn't to go left or right, execute move normally
				//===============================================================
				return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getSubject()));
			}

			//===============================================================
			//Go / Move command not recognized
			//===============================================================
			return new DisplayData("", "Can't go that direction.");

		case "return":
			//===============================================================
			//Return base room DisplayData.
			//===============================================================
			return this.displayOnEntry();
			
		case "469":
		case "turn":
		case "set":
			//===============================================================
			//If combinations successfully set
			//===============================================================
			if (command.getVerb().contentEquals("469") || command.getSubject().contentEquals("469") ||
			   (command.getSubject().contentEquals("combination") && command.getTarget().contentEquals("469")))
			{
				this.gameState.flipFlag("combination correct");
				return new DisplayData("", "You set the combination to 469. There's a faint clicking sound. "
						+ "A small compartment in the tree opens up beneath the combination input.  Inside, you see a brooch");
			}

			//===============================================================
			//If set command not recognized
			//===============================================================
			return new DisplayData("", "That doesn't seem to work. ");
			
		case "pour":
		case "fill":
		case "put":
			//===============================================================
			//If command is to pour water into the basin
			//===============================================================
			if ((command.getSubject().matches("water|dasani|bottle") && command.getTarget().contentEquals("basin")) ||
				(command.getSubject().contentEquals("basin") && command.getTarget().matches("water|dasani|bottle")))
			{
				//===============================================================
				//Verify that the player has water in inventory
				//===============================================================
				 if (this.gameState.checkInventory("Bottle of Water"))
				 {
						//=================================================================================
						//Flip the switch for the right path, and set the display data
						//=================================================================================
						this.gameState.flipFlag("right path switch");
						DisplayData displayData = new DisplayData ("", "You pour some water into the basin, filling it. You're not sure if that really accomplished anything, "
								+ "but it feels right. ");

						//=================================================================================
						//Use the Bottle of Water, which will cause it to break, and create an empty bottle
						//=================================================================================
						DisplayData bottleData = this.gameState.getSpace("Bottle of Water").executeCommand(new Command("execute usage", "", ""));
						
						return new DisplayData("", displayData.getDescription() + bottleData.getDescription());
				 }
				 else
					 return new DisplayData ("", "You don't have any water. ");
			}

			//===============================================================
			//If put command not recognized
			//===============================================================
			return new DisplayData("", "That doesn't seem to do anything. ");
			
		case "ignite":
		case "light":
		case "use":
			//===============================================================
			//If the command is to light the torch using the holy zippo
			//===============================================================
			if ((command.getSubject().contentEquals("torch") && command.getSubject().matches("holy|zippo")) ||
				(command.getSubject().matches("holy|zippo") && command.getTarget().contentEquals("torch")) ||
				command.getSubject().contentEquals("holy") && command.getTarget().contentEquals("zippo"))
			{
				//===============================================================
				//Verify that the player has the holy zippo lighter
				//===============================================================
				if (this.gameState.checkSpace("Holy Zippo") == true)
				{
					this.gameState.flipFlag("left path switch");
					return new DisplayData("", "You light the troll figure's torch. It sputters to life, illuminating the area among the ancient tree's "
							+ "roots. You feel as if you've just completed some pagan ritual. "
							+ "Maybe next, you'll meet a genie and get three wishes, you chuckle to yourself. ");
				}
				else
					return new DisplayData("", "That doesn't work. ");
			}

			//===============================================================
			//If use command not recognized
			//===============================================================
			return new DisplayData("", "That doesn't seem to do anything. ");
			
		case "get":
		case "remove":
		case "take":
			//===============================================================
			//If the command is to take the brooch
			//===============================================================
			if (command.getSubject().matches("brooch|religious"))
			{
				this.gameState.flipFlag("brooch taken");
				this.gameState.flipFlag("brooch in inventory");
				this.gameState.addToInventory("Religious Brooch");
				return new DisplayData("", "You take the brooch from the small compartment. ");
			}

			//===============================================================
			//If take command not recognized
			//===============================================================
			return new DisplayData("", "You can't take that. ");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.getSubject().contentEquals("around"))
				return new DisplayData("", "This stretch of forest is much like the rest, aside from the massive tree at the fork of the path. "
						+ "The huge oak dominates the area, like a gnarled monarch. "
						+ "It's so captivating, you almost don't notice the two small figurines seated at its foot, "
						+ "among the roots and shadows on a little pedestal. ");

			//===============================================================
			//When looking at the tree, the messages will differ based on 
			//whether or not the combination has been correctly entered, and
			//whether the brooch has been taken.
			//===============================================================
			if (command.getSubject().matches("huge|tree|oak"))
			{
				if (this.gameState.getFlag("combination correct").isFlipped() == true)
					return new DisplayData(this.treeCombinationImage, "It's immense, obviously ancient, much older than the other trees around you. "
							+ this.gameState.getFlag("combination correct").toString() + this.gameState.getFlag("brooch taken").toString());
				else
					return new DisplayData(this.treeCombinationImage, "It's immense, obviously ancient, much older than the other trees around you. "
							+ this.gameState.getFlag("combination correct").toString());
			}

			if (command.getSubject().matches("figures|figurines|statues|idols"))
				return new DisplayData(this.figurineImage, "There are two figures.  The one on the left is some sort of grotesque troll, holding "
						+ "a torch in one hand, and a triangular symbol with an eye on it, in the other hand. "
						+ "The one on the right is a kneeling woman, holding a large basin up in front of her. "
						+ "Some sort of religious idols, possibly? They're unfamiliar to you. ");

			if (command.getSubject().contentEquals("torch"))
				return new DisplayData("", "The figurine is made of some sort of stone, but the torch is made of wood, inserted into a slot "
						+ "in the troll's hand. It's tip is wrapped with oily cloth. You think it might be an actual, miniature torch. ");

			if (command.getSubject().contentEquals("basin"))
				return new DisplayData("", "The figurine is made of stone, as is the basin. If this is indeed a religious idol, it is likely "
						+ "meant to hold some sort of offering. ");

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		default: 
			//===============================================================
			//If default is reached, return a failure message.
			//===============================================================
			return new DisplayData("", "That doesn't seem to do anything.");
		}
	}

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String forestPathImage = "MMMMMMMMmyddsoo++dNNNh-:+dNMMm.:-.:  . ./`     :ym+dss``+hmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMd++/o//o/+:/+/+++sso+sMNo+os+hdyydy++NMds:mNNdNMMMMMMNodhyhhdd\r\n" + 
			"MMMMMMMMo:y:...``hNNN+`-omNMMd...`+-.-`-:..    -ym:dss `+dmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmsoooo+/+:+ohyhyyhssooNNoshhyhhydddysNMho:mMNdNMMMMMMNymhhyhdd\r\n" + 
			"MMMMMMMM+o/`.   `dmNm:.-sNNMMh``- -``-..-`..`  .sm:s+/.-+hmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMd-::o+//sy:+dyyssmmdddmmddddhmNdmNdhyNMhh/mNmdNMMMMMMNymdhhhhh\r\n" + 
			"MMMMMMMMo//``.--/mmmm/`-smNMMm:::`.. . ./````  .sm+hoo.sdhdNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMd/++ossosoosho+syddhhNNNdmhhdmdddddyhMM+s:mNmdNMMMMMMmoddsyhdh\r\n" + 
			"MMMMMMMMso/---:-+dmdm+.`omNMMd``.`..`.  :`.``  `od/so+-hhhdmNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMs/oosyyhhddhddmmmmmhdNNNmmddddmdmmdymMMoh:dNmhNMMMMMMd+hmyhhyy\r\n" + 
			"MMMMMMMMyyo//.```smmdy::smNMMm  - `. -``:  ``` `+d+yyo:yhhmNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM+++ssysohdhhddhdsddy+NNNydhhyhmydmhsmNNsh-dNmdNMMMMMMmshmdhhhd\r\n" + 
			"MMMMMMMM/++:.``--odmdNy`+mNMMm.`:``- .``-``` ` `+h+sh//:yhmNNNNMMMMMMMMMMMMMNMMMMMMMMMMMMMMMMMMo/.-ho::dmo+yoyy.+s::NMN/ooo+yNhddy/mMM+h:hNmdNNMMMMMd+ydhyshh\r\n" + 
			"MMMMMMMM/::`.-``.odmNN+-/mNNMN  -  ``-``:````` `/hooh:+.+hmNNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMs```oo./mNo.h:o:.+s.`dNM/:+yoyNdmNyomMN+y:hNddNNMMMMMd/yhssyss\r\n" + 
			"MMMMMMMN/:/```.--smmNN:::mmNMN.`-.`. `  -.`` ```/hs+d//.+hmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMh..`ys`:mN+.h:y../y.-dNN/.-::sNmNmyoNMN+y/yNddNMMMMMMd/hdssoo/\r\n" + 
			"MMMMMMMm++/:--::++dNNN` -mmNMN` .` -`.` -`  ````-yy+h-:.ohmNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMh..`ss`+mN/--/o/-+h..hNN+//-/sNNNmhoNMN+o/sNddNMMMMMMd+yhy++oo\r\n" + 
			"MMMMMMMm-/+:::::-ymNNN` -mmNNN/ `` ` ```--  ````-yh/h::-ohmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMh--`sy`omN++ooss/sy:.sNN+.:-/+dmdhhoNMd++:oNhdNMMMMMMdodho+oy+\r\n" + 
			"MMMMMMMm+s+`..-`.yNNNh `-mmNNNm``` `  ` `.```` `.yd:h::s+hmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMd::.oh.ymN/:++os+odossNNo...:+dmmdyoNMm/+/oNhdNMMMMMMdods/+ss+\r\n" + 
			"MMMMMMMdyo:```.-:sNNNd..-dmmNNm-`` `` ```.``````.sd/h++s+hmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMm-:.od.ymN::o///-+hs/oNNy--.::yhyssoNMm++++NydNMMMMMMmomo+++s+\r\n" + 
			"MMMMMMMhosy/:---.+hmmN/-:dmNNNN+`. `` ` `-```.```sd/ho//ohmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN:.`od.ymd-.s+-/::s++oNNy.---:dmNhd+mNy+//odydNMMMMMMdoms/-+o/\r\n" + 
			"MMMMMMMo/++/o+oooosmNNs.-dmNMMy `` ``````.`     `od+h+/+shmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN:.`/s:sos--o+/+//so+omNh/+/+/ydmyo/yms++++dydNMMMMMMm/ms++/+:\r\n" + 
			"MMMMMMM/o++/+//osydNNNo.-dmNMM/``.  ` ` `:``...-`odoho-:ohmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMy/::/++/++o/ooo++soo/dmdooos+ss+oo++yyy+/odydNMMMMMMNsmh/:oo:\r\n" + 
			"MMMMMMM:ssyhshyss+dddmd`-hmNNN/ `.` ``.``-``....`odohs:oohdNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMho++oso+/yshyyhhyhhysyysoso++osyyyoyyyyo+ohydNMMMMMMNymdhdhy+\r\n" + 
			"MMMMMMN/hhsyhssssymmmmm:-hmNMNs:--.`...`.........odyhho+oydmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMh:/:soosyo+hdhdmdhdhdmhyo++:oyddy+sysosyysddhNMMMMMMMydhyssyy\r\n" + 
			"MMMMMMd.ssoss++:/sNmNNN++NMMMMo` -  ` .``-`......odhhhossmmmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMd+/+y+yhys+hyydh/hmhhhdy+hhyosdhy+oyhsysooNdmNMMMMMMMmmmdmmmm\r\n" + 
			"MMMMMMs./+/+soss+ymmmmm+/NNNMMh/::..-.-..:-+/+++-oddyds/ommNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN++yh+ymhyyhy+y+.ssosmmdos+:/+ysssdNNoo++omdmNMMMMMMMhdmmmmmN\r\n" + 
			"MMMMMMy+sos/+sssssdNNmmhsdNNNMho++:..-:::----:--.odmydyyymmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMh+hdosmh+:shoy/`-///NNN-+o/:+dd+sddy+sooohhmNMMMMMMMmddmmNNN\r\n" + 
			"MMMMMmy+shsyyysyhsdmmmdhydNNNMms/++oso/-://--/+//shmydsh/yNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNdhmoymhsoohshyyys/oNNm::-:/sydsosshsssssyhNNMMMMMMMNNNNNmmN\r\n" + 
			"MMMMMmhhdddhyyhdmhdmmmdhydmNNMNs++oo////:--.-/oooyhmhmhd:yNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNdshdmo.+d-sysso:/NNd/---+:os++hdyssssyydNNMMMMMMMMMNNNNNN\r\n" + 
			"MMMMMNysy+:/-:ydyhmmmddhydNMMMNo++//+::///-:+so+:shmdmhd/dNNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNhdmm-`.d+/:-oo.-mdd+:-:://oosyhhyyyhdmmmNMMMMMMMMMMMMNNM\r\n" + 
			"MMMMMNhhdmhhhhdyhdmmmmmmhmMMMMN///:.-:.-`-+///+..shmdm+sshmmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMhmmh+..h/-.:+y.:dNm+-:+o/+oohmmdhdmdmNmdNMMMMMMMMMMMMMNM\r\n" + 
			"MMMMMmhyhmddddddmmmmNmNmhmNMMMNhoyy/--`- .+.:-.--ydmmmhyyhmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmmms-`.y+--/+o-:hdm+-::/++oyhdmmdhdmNmmdNNNmhmNNMMNNNNNN\r\n" + 
			"MMMMMMNmmmmmmNNmNmNNNmNNdNNNMMNmyhyy:+ -`.-:-://ohdmmmdy+hmNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmm+...yhys/yy/-sso::+ohhmmdmmNNNmNmNNmdNhmhNMMMMMNNmNMM\r\n" + 
			"MMMMMMNNNNNNNNNNNNNNNNNNmNNNMMNmmmNmmdy+/++/oo//+hdmmmy/odmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNmm+/-`sds+-oy/:sh+:/:+ddddshmdmmmmmNNmmMMMNMMNNMMNNNMMN\r\n" + 
			"MMMMMMMNNNNNNNNNNNNNNNNNmNNMMMMNNNdyydy++-:o/////hdmmmyosdmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmmyy+`oddy.odo-+yy/-++oooyyhdddhyhmMNmmMMMMMMMMNmNNNNMM\r\n" + 
			"MMMMMMNNNNNNmNMNMMMMNNNMmNNNMMMmhhddyssy+o/+:::/+hmmmmyymNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNyy+`ohdm.+mo:/ss+-::++shdmddhddmNMMmNMMMMMMMMNMMMdNMM\r\n" + 
			"MMMMMMNNNNNMMMMMMMMMMMMMmNNNMMMNdydNdyhyso+/:-::/ymmNmmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN+++`/sso.::--+/---+ssoyddhhmddNNhdNNMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMNMNNMMMMMMMMNNMMNmNNMMMMNNNNmmhhmhos+/:-..smmddNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNMNNNNmmm:+/`/s+-...-:///+o+ooooydoosyddshsdMMMMMMMMMMMNNNmmNM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMmNNMMMMMMMNNNNmdhoyo/-...:sddmNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNmmNmmmmmmdN++-`-------:/::osohooyysysss+sssydNMMMMMMMMNNmmddmmmm\r\n" + 
			"MMMMMMMMNMMMMMMMNNMMMMNmNNMMMMMNMmydhhhdhyoo/+/+ydmNNNNNMMMNNNmmmmmmmNmmmdmmmmddmmNmmmmmmNmmNNNmmmmmmmmmhy+:-:.-:-:://osydhhNdhso+/:+sosdNMMMMNNmNmmmmmmddddy\r\n" + 
			"MMMMMMMMNMMMMMMMMNMMMMmNMMMMMMMNNmmddddhh+/oyhhdmNmdmddyyhhyhysshdmmNNNddmmmsydmmmNNNhdmmNNNNNNNmmddmNNmmds+//.`..:/::+syhddho//:/oo/ohhmmdhdhyydmNNNmhhhhhdd\r\n" + 
			"MMMMMMMMMNNMMMMNNMMMMMNMMMMMMMNNm+///:--..+hhdmNNNhyhysyyyyyssshddmmdmNmdmmdsdddmdmNNddmmdmNmmdmmdddmmddys/-...-/+o::-:/shyo//+/:/+/shhNNhsysysdmdddhyyhhhhhh\r\n" + 
			"NNMMMMMMMMMMMNNNNNNNmmNMMMMMMMNNNdo:.`````..//osyhhmmmddhhyoshhhhhhdmmmdhhdmhhhhdmmmmhhdmmmmmmmmmdhysso/:-```-/+o//:++:+s+://+//os+oyshhhmmdhsyyyyhhhhhhyysyy\r\n" + 
			"NNNNNNNNNNNMMNNNNNNNNNNNNNNNNNNNNNNmd:.```.```````.-::/++++syhhyyhhyyyyyhhyyyyhhyyyyyyhhyyyyyyyso/:--..````.:.-::..-:///-/oo//+//+ooyhmmddysohhhyhhddddhhhyyy\r\n" + 
			"NNmNNMMMMMMMNMMNNNNNNNNNNNNmmmNNNNNNms....---.`..`````.--:-::-::::::::/:::-:::::-:::/:::::://:::--...`....-../+----:/::.-//://o+yyyysyhy+/+ohdhyyhsyyyhhdmmmm\r\n" + 
			"MMMMMMMMMMNMMMMMNNNMNNmNNNNNNNmmNNNNNmy+////:::..-.......---...```.``.```..`.```.```.``.``.`.........-.---.--::-::/-:-::/+:::://++o+so+/+osysysyyhyssyymNNNNN\r\n" + 
			"MMMMMMMMMMMMNNNNNMMMMMNNNNNNNNNNNMMNNNNho+++/:::..----........```````````````````````````````````.....`...--.-::/:::--::++//os+/:/++o++ossydhsyyyssyhhmNNNmmm\r\n" + 
			"MMMMMMMMMMMMMMNNNMMMMMMMMNNMMMMMMMNNNMMNd++/::--:...````````..``````````````````````````````````.``..-....--.`..---:--::///+//++++soosssssyyyyyssyddhdmdmmdmm\r\n" + 
			"MMMMMMMMMMMMMMMMMMMNNMMMMMMMMMMMMMMNMMMMmhhysoo+/////---......-....`.............`......`......-----------------:://+/+++++/+++oooosyysssyyyyyhhhhddhdddmddmm\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMNNNMNNNMMMMMMMMNNMMMNNdddyyyyyysooo//::::--:::::-::/:::-:/::::-::::::-:---::-:::::::::::::::::////+ooooosossosoossyshhhhhhddddddddddddmm\r\n" + 
			"MNMMNNNNMMMMMMMMMMMMMMMMMMMNNMNNNMMMMMNNNNMMNNNmmmmmmddhhysssooo+so+++/+oo+/oooso/+//oso/+//+/+////////++///+//+++/+++o+++++ooosssssyyhyhhhdhdhddddddddhddddd";
	
	private final String treeCombinationImage = "-/:..oyhmsso/:dmNMMMNNNNsoo//:.:++/::+NNMNdhsdNNNMMNNNMdsossy:o++++dh+://+odmyshNNMNys+::y+symyhymmNMNhyys/shhsooomMd/osoossmysmNNmdmhddyohhyyyhyNNssssyyo/oy\r\n" + 
			"::.-/+sNh+ssdmNmmyyyhddddso+sh+-:/:--ymNNNho++sdyhNNNNMyo//os:/ooydmo+sssshdmhsdNNMMdo+-+++o+hoomNmMMmyyyohyhs+yhhNMdsyoo+o:yssNNNmhmhdyyydhysohyNdosossyo/:s\r\n" + 
			"://ssydNhs+/ohmNsyhyhdmhsdmmmy:..-/+dmNNNNysyhmmNNNNNNNyo/os+//ooddNo++oshdhds+hmNNNm+//s/::/+hmNNmNNhodhhshsso++dyNmhyooyhydhdNNNMmNhhhshydyo/ssNy+/+/:oooyd\r\n" + 
			"/h+oo:/mo+ssodNMddyoosyosyyNMdsmNdmNNNmNNmyssyyyNNNNNNNy+hho+/+y+hhNmo:+s+o+y+ydNNNNm+s+os/+hdNmNNMMmssshyss+++ssmmdNNhhhhhydhdNNNMMNdhhyhhhs+-++hy:+o/++++yy\r\n" + 
			"dmhddhhNyos++dNMyss++s/syydmMMmyoyddysdmddhyoohhNMNNNdmyyy+:/+s+++hNmoo/++/ossdNNNNMms++++/+sshmNMMMhsyyyhs+ohyhmNdhmMdyohhsdddmmNNMmhhhhysyoo:ooso///:o/s:o/\r\n" + 
			"hmMmoosh//o::yNNdsssso+yssdNMNs/hyhyo+hmmNysosmdNNNNNmdy++o+os+:+/dNNyoss+:+oshmNNNNyso+o+dmdodNNNMMyoohs+osyhyydhmdNNhooysyhddmNNNNdhhsyyssso/+oo///+-++h:o/\r\n" + 
			"hdmdysyysy+:ydNmdyss/++o/hNMNNss+syyyoyNNNo/+yhNNNNNmhhhohh/o+o::ymNMhsso++yoohdNNmNm+sosmmddhmmNNMms+sys/soyhyydsyNMmohyyyssyhmNNNmMdyhyysoo+:++/o/:/.:+s:+/\r\n" + 
			"+smd+:/mdds/symNmso/://:shmNNyoyosodNmdNNh++yodNNNNmNmhsos+:ys+:+dNMNhsosoysy+ysNNNNd+soshy+osNmNmMmsoysoyy+sdhhhydMMm++ossosoymNNNNNmmdsyso+yohho+::/./yso++\r\n" + 
			"::sN///mhss++ymNmyy/:--/hhdNm+ossysmNNNmyssss+hNmNmmMdyyysssyy+:sdmNMdo++syoyoshNNNmyo/++s/+ohdmNMNmsys-/so+oysysodNMmooo+hs/:ymNmNNNNNhysy+o/hooo+++/:+o:s//\r\n" + 
			"-:/ds-sddo+o+hNmd+//+:::ydmNsoosyysdmNNdyyhysohmmNmdNNyso/sosos:+hdmMhs//+y+ho+hNMNhyy/+://sohmNNNMdss+-sy++os+oy+ddNmds+ys/+/ydNNNNNmddhyyyooy//o+o+ooso/o/s\r\n" + 
			":-/hhsdmd+o:+mNNm/:+/++sdhmNhssymdhhmNNNdyys++hmNNNNMMyhsshyysooydNNMsyhddmmdosdNMNhyy/oo/sysdmNNMMNyyyddmmNsooohyshNmy+oo++sohmmNmNNmhyyhys++o//+/s++:+:::/y\r\n" + 
			"+s+sNmmhyo+.+mNNd/-++--+hmNNmyydhyoyNNNmho::/oNMMMMMMMMMMMMMMm+oymNmddsoMMMMMMMMMMMMMMdos/o/-ymNMMMMNNNdhhmMMMMshhsmNmsyhso+oshmNmmNMmoyyos+oshsys/s//:+/+/+/\r\n" + 
			"s+/hNNyyho//ymNNmoo+:::y/dNNmdhdyh+hdhmmmyosohNMMm++++////sdMm/sshmh+mdoMM/--......oMMmyo/o-/hhNMN-```   ` yMMN+dhohNmyoooho+hdmNNNNNdyhhyyyhmddmdss/+/o://ss\r\n" + 
			"//+hNMhs+++sydNNdsy+/://+hNNNmohdyohoyNmNys+sNdMMs   `     /Mms+hhhydMhsMN`   ``    dMNo+++:/hNNMm` `-:-.` `NMNyhyyhNmho+sshdNhdNNNNNdhsysshhssydsss///o:++oo\r\n" + 
			"+:sydMy///:-+hmNm+oo/+o/smNNmm/+++yo/yNNNsyshdhMM/ -//++:` /Mm/+yhmyNm/sMN  +s::++` yMm+o+o/omNNMd `//----  mMMmmMNNNNdyhhhmmdymNNmNNdhhssoshssoo+/s/+//:+//o\r\n" + 
			"o:+sNMho+/:-+smNm/++++-:shmmNNo+:+o+odmNNsyhhhoMM/ ..::-.` /Mm/+ymmhmm-yMN` o/::-/` yMms/++:/mNNMN` +/``.:` yMMyhNMNshhssy+sysshMNNmNydhsyoyysoso++o++/o+sdhd\r\n" + 
			"//sdNNs///+-somdy--:--/+ssmNNMh::+s//mNyooy/sssMM: `:///-` /Mm:syyyhmh+hMd- -+ooo+. yMNhy:+-/NNNMM-  .-:..` oMNs+NMMyooshhsyosymNNNmdNNdyyssoys+oosooo/+osoyd\r\n" + 
			":ohmNN+-:oo/odNNh+-::/ohommNNNy-/s/-/ddos+yssydMM/`-://:--./MmssymyyhyymMM..:////:/-yMmsyo::smNNMM:`---//::/oMNo+NMMhsooyyoosyhmNNNmmNNhhyoys++/o/+yoooo+++yh\r\n" + 
			":hdhmNo:+o/:ydmNhsyooshydydNNMy/ss//+dmo/+s+ydNMMo-+so+//+/+MN++shsyhoydMM.`------..yMmshs+/odNMMM:         oMNhhmMMdyo+oossyyhmNNNNdNNhdsoooo+/+/+yyhhoo//hN\r\n" + 
			"/hdNNm+://::oydN+//:::/o/odNNMy+o+/:smy//++sddNMM+``.-...` /MNooNyyyyyydMM- ./:::+. yMm+++shhmNMMM/ :oosss+ /MNysNNdoo+/+/+osohdNNNmdNNhy+s/ssoo//oosoo/+sNMM\r\n" + 
			":dNNNo/o+o+:/hNmoo::-::::yNNNNyo+s+oymy/++hmyyNMM+:osyyyy/-/MMMdNdss+/:yMM-`++ooyy: yMd/+hsydNNNMM:.-:-/o/-`/MNyhmmdhoo//++++ossmNNmdMNhhoo/ooo+++ooo+yydNMMN\r\n" + 
			"ydNmNhoosho/+oNNym+/::::/hdmNMh+ydhdoos+hmmh:yNMM+-/--oo..++MMMmNyso::shMM---.:dy..`yMdo/ooo+dNMMM/:+//yo-///MMdhNmmm/o+yo/+++hmNNNmmNdhdss//so/+/o/:o+yhNMNN\r\n" + 
			"dodhms+yy+//++Nd+o///:-::ddNNdyoo++ssy+ydys/yhNMM++o:dy-yy-/MMMNdoo/:/hhMM:++/mh:+o-yMmo/o//:hhsMM//soho/ss+:MNdhNhymyddy+-++oodNNNmmmdhssoooys/oos//shdmNMNN\r\n" + 
			"hyddmo/so:/:+sNdo/++o/::/+oyNsdmMmyooho/+/+omNMMMyyyhd/hdd//MMNNdyss+sssMM:sood/sos/hMm+/:://ooyMMooh/osydhy:MNddNshhNMo+/+s+osdNNNmMmhhss+yoyso+oo+s+s/smNNm\r\n" + 
			"hddhmdsy+:--+sNh/+s+/:+//-+dmdmNMN+o+o/:::+yyNNMMydyo+hddho/MMNddhhs+s+sMM/syoossosohMm+::.+++shMMy/shdyssss+MNhymsdNMMysoyo+oyhmNmmMdyyso:yss++o++sh/oohmNdm\r\n" + 
			"+shhmyoy+/--+dNd//+:/-/:+ohNmNNMMNoy+h+s+do+ymdMMs+oyyooos+oMMNyhymhhysyMN/::/:/::::hMm::::++sshMNs--:sso:--oMNyoyymNMmyyyso+ooshmmmNmhsos/yss/+oosy+:::ymNdN\r\n" + 
			"shhhNd/+/:.-:yNh++o:/:+oyyoNNmNMNdsy+dshdd+ydNNMM/:++o:++:-+MMhyyyshmmoyMM.::-:o/::-dMm:+/+o+oosMMs+ssshhyoosMNo+somNMmooso++/sodmNmhdMysoohysooso+----:dNmdN\r\n" + 
			"yyhNNm:::.---dNs/+/+//:+ysdmmmNNmmyyohyhho:dhmNMM+yddmdddhooMMyoosyodmoyMM.yhmhhyss+dMd//-+s++//MMyoymdo+osssMN+o+oNNMNyssoooss+hmmdhNMhh+/sys/+s/++/--+hNmdM\r\n" + 
			"++sNmh.:---.-mNso//::::+ddmmNNNNmNymyoss/+:ddNNMM/omNhysshosMMs/+++yyyodMM:yNNyhddh+mMm:+/ossyssMMh+ddoooNmsyMN+//sdNMNhssyo+osoymddmNNhdshoyo:++/yo/://dNdmM\r\n" + 
			"/:hmms.:s+-.:mNo//+-:/:odhdmNmNNNh+soo/://:hmNMMMssNh//sMdohMMs++//s/yoyMM+yMh+/hMs/NMm-soooyo+yMMmoymmddNd+dMNo+/hNMMhsoys++/s+odmmmNMysysos+:o+hss/+osdNNNN\r\n" + 
			":odmNy./hyo-:mdoo:++/+/shoymNNNNNh/+ossyhmdNMMMMNy+ydmdmdhhMMMs:--:/ss+sMMyssyyysyydmMm+o/so/oshMMMmhyos+oddMMNsodMMMNsyyhs-+/o:oyNmNMNyyssyh+yys/::+/:ydmNNd\r\n" + 
			"o+sdmyoyhd:/hm+:/o/::o:+yoomNmmmmy+o:+oohNMMMMNMMMNmmdddmNMMMMs--.--sdhyMMMMMNNNNMMMMMNos-+/+sooMMMMMMMMMMMMMMN+omNNMMsyyyy-//o/shNmNMNyysoyshys:/+/soydNmNmN\r\n" + 
			"ysdmdhhhhhsyhh/:oys::+-/ysymmhhNNhdo:/+sdNNNMMmoyysyyyhsoyomNMs..:o+shhhmNmdysmNNNMdhhhs+://ysoyNNMMMmhhdyhyoss+ydhdMNdyyys+//+/yNmmmNdyo+oyyhy++s+/odhmNmNNM\r\n" + 
			"o///so/shdo///-:os+--+:++o+ysmdNmNh////shNNNNNhss++++odo+/+mmms/ohs//+o/oy/os+dmmNMysossossyyoodhmNMNh+syoss-+//dyyNNmdhyyooso++dNMNNMNyosssoss++s++sNNNNNNMm\r\n" + 
			"/+o+o/+hms+.://+//+--o:++/s/ydmNNNd+s//odNNNmmoyo+s++sms//dmmmmyos////o:/++s++mmmNMysss/s+ssso/dhdNMN+o+so//-+++hyhNNMhyh+ossodmmNNNMMmoyy/ysyoo/+:-yNNNNmNNh\r\n" + 
			"/++/+:/hyho.-/oohoo::+:/++y+ohmNmdsos:/+hNNmNmyyhshsyymh/yymmmNso++//+so/:+//oymmNNmy++osoo+++sdhhNMmos/ys+o//o++ymMMMyss++osshsdNNNNNdysoohss+:::/sdmNdddNds\r\n" + 
			":::-++sdNmyo-:/oho+:::///o++odNNNmsyo//+hNNmmNoyyydyhyNyossddmN+-:/--+++:o+///smmNmmdosyoso+//sh+hNMdyooys/s/+++oyNNMmh//+/+soohymNNmNdhs+/y+/+/::+omNdhmmNN+\r\n" + 
			"//.:+s+omhod++oys+oo::/+hs++hmNNNNdso/+/hNNdmNo+/shhydNyosoyymN/+yy+:/s++s+ssohNNmNmNysoooo/:+os:dNMmyyoss:+:::/somNMmys++o++ooohmNmdmdos/+so+o/::shmNmNhddyy\r\n" + 
			"/o-/++:oNhs/o//+s++o++ooo/ysdNMNNNyoo/+ssmNmMNosyyshdNho+/++hmNysss+o/o+soohyomNmNNddooo//o+//+shdNMdsssoo+o/:::d:mNNmy/ohhoo++odNNMMmh++os+::+/:+sdmNNMNmhys\r\n" + 
			"--::/--yNdy://+:o:++/s+oo+++hNMNNNyos/+/omNNNh+osydmMhs/++:+hNMhsoo++/s+++/hyommNmdhdss+/ydss/:yshNMmhoyso/s+oosysNNMds//y++os+ohmNMNhh+so:+-::-/syhmNNMNmhys\r\n" + 
			"+//::--hmdyo:///s+///s+os+ooymMNNNds+//+sNNMms+++oyhMN+////shmMmyso+++//soso/smmmyhNNNyyhmmyoo/ssymMMdhdyyo++sosyNNMMh++:o--+o/ohmNNNss++//+////os//hmNMNdyyy\r\n" + 
			"o//::-/dhhh+++::/:/:+o::oo:/hNNNNNho+:/:+mNNN++o+o+hNN+/-:/sddNs+s+//++ossooymNmmyyNMMdhmmy+-osdmMMMNyss+oysos+ohNMNmhyo:+sdhs//smNmNmyso/::-++////sdmMMmyyss\r\n" + 
			"o+:::-:hyNm:-:+//:/:/o-/so+yydNMNNyos++:+NNNm/oosyomMNs/:/oyddNs+oo/:+oooshsdmmNNddMMNyhymso-/+mNNMMdohosoosoo+sdNMNmyo::ohhhysyhmmNMdhs//./:--/++sydNMMNhhoo\r\n" + 
			"hs/.--o+dNy+//oy+o+:+s/+o/hysdNNNNyoho//omNNdysosssdNNd/:sdNmdm+/o///s/+:+hsdMMMNhdNMmydNmoso+hNMMMmdohysssososydNMdhyso/ss+oos+omNNNhss//+sysdydhdmNNNNmhsym\r\n" + 
			"ss/o/+hdmysd+.+ss+:+ss+ohdNhodmNNNys+-:/omNmhyooosymdmNhmNhyyhhs+s//oo+ooshmNMMNmdmmMdyhdmmdymMMMNMdhss+o+sosooodNNddys+os+oshsoyNNmdyys//:/soh+yhdhmNNmdy++h\r\n" + 
			"";
	
	private final String figurineImage = "MNNNNNNNmNmmdhdhddyyNmyyyhdMmysssyyhmhs+/+ooyyyyhhhhsdyyy+o+ossoyyosssyNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MNNmNNNNNNmNmmmdmdyyNNNdmNmddysyyyyyNh++/+osssyyyyyyymNdhdyssyyhyssosyyshmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"NNNNmmNNNNNNNNNNmmddmhNNNNmNhyyyhyyhNyo//ossssyyyhyhmNmmmmNNdhmhhyoshss+++ydmNNNMMMMMMMMMMNNmmmmNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"NNNNmdmmmmNNNNNNNNNNNddmhhhhdhdyhyydNs++ooososhyhhhhNMMMMNNNmdmNdyhhyoo+++/+oydddmmmmdddddddmdhhhhdNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"NNNNNmNmdhmNNNNNNNNNNNNNhyssyhmmmdhddo++o+sssyysyyyhNMMMNmNMMMNmmdhdyyyys+++//oshdNNNNNNNmmNNNNmdhhyhhdmNNNmddmmmmmmNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"NNNmmmNNmdmmNNmmNNNNNNNNmmhsydmNNNmho++ooosyydhyyhdmNMMMMMMMMMMmmmmdhdhhysso++/+/+oshmmNNNmmNNNNNmddhhhhhddmhyyysyssysooshyhhhddmmmNNNMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"NNmmmmdmmmdNmNNmmmNNNNNNNdmdhyhmNNdooooos+shymNddhhmNMMMMMMMMMMMNNdhddhhhysyyoo+++/////+oydNNMMMNNmmmmNNmNNNmmmmdhyhhyyyyyhyosssssoo+syysyyhhdmMMMMMMMMMMMMMM\r\n" + 
			"NNNmmmmmmmdmNNNNNmNNNNNNNNmmhsosydo++oooyyohhhdmmddNNdMMNMNMNMMMNNmdddhddhhdysssss+++//++oosydNMMMNNNNNNNMNMMMNNdhddmmmmdddmddmmddyhhhhhyhyyyyysyhdmNNNNMMMMM\r\n" + 
			"MNNNNNmmmmddddNNNNNNNNNNNNNNmhsyy//+ossssooyyhhmmddNNNNNNMMMMMNNNNNNNNNmmmdmhhyyhyysooo+o+++++smMMMMMMMMMMMMMMMMNNNmNMMMMNNNNNNNNNmmNNNNmmmmmdhyyyyosssyyhddm\r\n" + 
			"NNNNNNNNmmddmdmNmNNNNNNNNNNNNMNh+oo+osshsssoyhdmmdmmMMNmmNNNNdyyyhhNNNNmNmmmddhhhhhyyysso/-::+ssyhdmNMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNNMMMMMMMMMmmmmmdhdyyhhyyyy\r\n" + 
			"NNMNNNNNNNNmmmmmNNNNNNNNNNMmysoossssosssoyyoohmmNNNNMNNNNmh+:-----:ydmNNNNNNNmddddmhhhyo/:::---/sssymdhhhddmNNNNNMNNmNMMMMMMMMMMMMMMMMMMMMMMMMMMNNmddmmdhyyhy\r\n" + 
			"NNNNNNNNNNmmmmmNNmNNmdddhssoooossyosyosyyssyshdNNMMNMMMMho:::::::::-/dMMNMNNNMNmmmNddyho+oo/++:-+ooosyhhyyysys/ossyyhhhmmmmmNNMMMMMMMMMMMMMMMMNNmmmNmNNmdhhdh\r\n" + 
			"NNNNNNNNNNNNmdhddysoooossossossssssssyysssshyyhMMMMNhhyds+yy//oyyyys+:NMMMNMNMMMNNMNNdhsyssyyssoo++/////+++oshdhyo++oyddddhddhdhyyyddddmNNMMNNNmdhdmmdNNNmmmd\r\n" + 
			"MNNNNNNmdhyo+osysssoosssyssyyssssssyysyssyyyhdmMNMMysyo/yyy+-:+soo+ss+hMMMMNNNMMMNMNNyoyssoyysosyo+o+++++++oohds+o++/+++oydmNmmmmmdhdddyysyyssysyyyhhhdmmmdmN\r\n" + 
			"MNMMmhs++osssossyyssyosyssosyyyysssssyyyyyhydMMMMMMsyo::++:-:/syyyyhy+:ssssymNMMMMMNNdyhyysyyoyhyysssmhsydmmhoooo+++/++++oooyyddmmNNNNNmddmmhshyyhyyhddddysos\r\n" + 
			"MNho/+oooosssossysoooyysssysosyysyyyhyhhhdddNMMMMMNyooos/---:-:/++++/::osyysodNMMNMMMMs+syssymdmmhhhmMMNNMdso+ssy+ossoosso+++//+/+++shNMNNMNNmNNNNNNNmNNNmddh\r\n" + 
			"s///+ossooosyoosohyyosyhyshhyshhyhdhhydhmMNmMMMMMNNMdo++:--:::/+s/:::::/soossmMMMNNNdsoooyssNNNNmNmNMMMMNmsshhhhysssssyyssssssso+++++//+oyhdmNNMMMMMMMMMMMNNN\r\n" + 
			"/++ooossoosssyssssyysyhdhhdddhdhdmddhdddmo:sMMMMMNMMMd/---:+syyhhyyysoo++osomMMMMd+----://o+osmMMNNNMMMMMMNmNddhmNmmmhhdhhddhhsysoo+++o+//////+oyhdmNNMMMMMMM\r\n" + 
			"++++ooosysosoyssyhddhdmMNdmmMNNmMMmmddmmm--/NMMMMNMMN::::/+ososssysyyso+oshNMMMMMs::/+/::::-/+hMMMMNMNMNNNNNNmmdNMNNMNNmNNNMNmddhyyshyyyo/+o++++////+oyhmNMMN\r\n" + 
			"//+oooosssyyssshydNNNmMMMNMNMMMMMMNNmNMMN/-oMMMMNMMMMh++o++oo/+oyyyyyso+hNNNMMMMMs/:/o+o/+//://+sooooo++//:/+++//++osydNMMMMMNNNNmNmmmddhhhhhhyo+o+////++ydmd\r\n" + 
			"///+++osssosyyyhddNMMMMMMNMMMMMMMMMMMMMMNh-+NmNNmmmmy//oy+o+ssssosyyyso++sdddddmdo+:-shysoo++::://++++++++++++++++/:--:oMMMMMMNMMMMMMMNMMmNmNmhhhsyo+///+/+sd\r\n" + 
			"//++oooosyyssohdNNMMMMMMMNNMMMMMMMMMMMMNmh:+mmmmmdyo+oss+o++++++/+oyyys+/+odddddds//-:+oo+++os++////::::::---:::::::/+ohMMMMMMMMMMMMMMMMMMMMMMmhmdyyo::+/++ss\r\n" + 
			"//++/o/+oosssssdNMMMMMMMMNNMMMMMMMMMMMMNdy//ooyyo:://os//:/+//+///+++/::::/ddddddh/::--------::://+ooooooo+++oooooosyyyNMMMMMMMMMMMMMMMMMMMMMMMNNNhy+/://+++o\r\n" + 
			"://++osy+oossosyNMMMMMMMNMMMMMMMMMMMNmo//o+/yso+/++osyy+//:/++::/:-:--//++odddddddy+:::--:://ssso++ossssssyssyhhhyhhdmNMMMMMMMMMMMMMMMMMMMMMMMMMMMds+//++/sss\r\n" + 
			"oo+oosossssysosdMMMMMMMMMMMMMMMMMMMNMsosss/-ssooshhhhs++o++oo/:/::/-/:-+hhdhdhdddhyso++o+///hmdddddhhhhhyyyhhdmmmNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNy++++++++oo\r\n" + 
			"+oosssyssysysyyNMMMMMMMMMMMMMMMMMMMNNhy+++/-osyhhhhy//osyssy+//:+/://::/yhhhhhhhhhhsssso+://:ohhddhddddmMNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMm++/+/+:/++o\r\n" + 
			"+ooosyhsoossyodMMMMMMMNMMMMMMMMMMMNNMMMdhys/shhhhhho+/syyyhhhy+oo:+o+ooohhhhhhhhhyo/////++:------://oyhhdMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMm+++/+++//+/+\r\n" + 
			"ssooyyysssssosmNMMMMMMMMMMMMMMMNMNMMMMMMMhhhhhhhhhhysssyhhhhhhhooossssoshhhhhhhs:---------------------:hhdNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN+++++o+++o+++\r\n" + 
			"ssshssosshhysdNNMMMMMMMMMMMMMMMMMMMMMMMMMhyyyyyyyyyyysyyhhhhhhhooosssyoshyyyhhyo/:----------:::-----::/yhhhNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNs///:/ooss+//o\r\n" + 
			"s+o+oooosyyysdNNMMMMMMMMMMMMMMMMMMMMMMMMMdyyyhhhyyo+/+syyyyhy/----::::/yyyyyyhhyyyyo/:::-:://////++++oyhhhyymMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMm++////+/oo//sso\r\n" + 
			"yssosoososssyhNMMMMMMMMMMMMMMMMMMMMMMMNNMNyyhhh+---:///+syys------:----+yyyyyhhhhysssooooooooooooossyhhhyyyyymMMMMMMMMMMMMMMMNmMMMMMMMMMMNmMm+/://+++syyoo+++\r\n" + 
			"hsso++++ssssshNMMMMMMMMMMMMMMMMMMMMNNdmmMNyyyhhs+++oosyhyy+---:---:--:-/yyyyyhhhhdhyo++++ooosyhhdhhhhhyyyyssyshMMMMMMMMMMMMMNNNNNMMMMMNMMNMMy++/+/+/:////+/+o\r\n" + 
			"dhyooooo+oosydMMMMMMMMMMMMMMMMMMMMMMMNMMMMyyyyyyyhhyhhyyyys+/+o+/+///::oyyyyyyhhhhhhhhhhhhhhhhhhhyyyyyssssssssshMMMMMMMMMMMMNNNNMMMMMNMMNNMNhds//+++sddo/++oo\r\n" + 
			"dmdoyhyyssoooymNMNMMMMMMMMMMMMMMMMMMMMMMMMdsssyyyyyyyyyyyyyyyyyyyyyyyssyyssssyyyyyyyyyyyyyyyyyyyyyssssssssssssosyNMNNNNNNMMMNMMMMMMNMMNNNNMNhs/+ooosyyshosyhh\r\n" + 
			"NMmdh++ooooososmNNMMMMMMMMMMMMMMMMMMMMMMMMmsoosssssssssssssssyyyysssssssssssssssssssssssssssssssssooooooooooooooosNMMMMMMNNMMMMMNNNmNmNMMNMNds++oosshdmNdysys\r\n" + 
			"Ndhdssoo++++oosyNMMMMMMMMMMMMMMMMMMMMMMMMMMsoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooossssyyyyhhdMMMMMMMMMNMMNMMmNMMMMNMMMmh+////+shmmhshmh\r\n" + 
			"hdyoss+o+oo++o++sNMMMMMMMMMMMMMMMMMMMMMMMMMyoooooooooooooooooooooooooo+ooooooooooooososssssyyyyyhhhhhhhdhhdddddhdhhNMMMMMMMMMNMMNNNMMMMMNMNMmso+s/-:////oydmN\r\n" + 
			"Ndssoshyo+++///+ssydMMMMMMMMMMMMMMMMMMMMMMMmooosssyssssssssyyyyyyyyhyyhhhhhhhhhhhhhhhhhdhhhhhhhhhdhhdhhhdddddhddddhNNNMNNmNmNNNMNNNNMNMMMNNNyohhmhosso+/::::+\r\n" + 
			"NdmhdmysysooooohhyyhNMMMMMMNNMMMMMMMMMMMMMMNddddddddddddhddddddddddhhhhhhhhhhhhhhhhhhhhhhdhhhhdhhhdddhhhhdhdddddhddmNNNmNMNNNNNNNNNNNmNNNNNmhymmNNmmmdhys+:::\r\n" + 
			"NmNNNmymyyyys/sy++++yNNNNNMNNMMMMMMMMMMMMMMNdhhddhdddddddhdhhdddhddhhhhhhhhhhhhhhhhhhhhhhhhhdhhhdddhhdhdhhhddhhhhhhdNNmNNMMMNNdmmNmmmNMMNNNydhhmNMMMMNNNmhsoo\r\n" + 
			"NmNMNmmddhhsoohs+:::/ohdhdNmNMMMMMMMMMMMMMMNdddddddddhdhddhddhhdddhdhdhhhhhhhhhhhhhhhhhhhhhhddhhddddhhhhhdhhdddhddddmmmNmmNNNNNNNNMNNmMNmmyddNNmNMMMMMMMMNmhs\r\n" + 
			"mmNMNmNdyyssshhyso////::/+oshdNNMMNNMMMMMMMNhhhhhhdddhhhhdhhhdhhdhhhhhddhhhhdhhhhhhhhhhhhhhhhhhhddmmddmmmmNNNNNNNNNNNNmdmmNNNNNNNNMNmmdmmsohdhmNmNNMMMMMNNMNN\r\n" + 
			"NNNNmNmdhyyyhysoooo+/+++/:::///oydNNMMMMMMMNdhddhddddddddhhhdhhddhhhddhhhhhhdhhdddddddmmmNNNmNNNNMNMMMMMMMMMMMMMMMNNNNMMMMMMNNMNNdhmdmNNNmNNNmmmNNNNMMNMMNNMM\r\n" + 
			"dNMMNNNNdshhyys+yyso+oo+os+++/:/::/+ymNMMMMmddddddddddddddmmmmmmmmmNNNNNNNNNNMMMMMMMMMMMMMMNNNNNNNNmNNMMMMMMMMMMMMMNMNmNMMMMMMNNNmmmNNMMMMMMMNMMNmmNNNmNMMNNN\r\n" + 
			"mMMMMNNmymmhhoohdyso/+++s++oso++::/:/+ohNNNNNNNNNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNMNNNmmmNmydNNNmmmNMMNMMNNMMMMmmdNNNNMNNMNNNNNNNNMMNNNNNNNMMmNN\r\n" + 
			"NNMMNNNmNNNmmdodhsys+syymysysooo+://::+++ohNNNmNNNNMMMMMMMMNNNMMMNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNMNNNmmNNmmmhmNNNNNMNMMMMNNdmNNNNNMMMMNNMNNNNMMNNNNMMMMmNN\r\n" + 
			"NNNMNMMmmNNNMNmdyyhyhyhddyhdysyso+++/::///+odNNNdmNNNNNMMNNNMMNNMNMNMNMMMNMMMMMMMMMMMMMMMMMMMMMMMNNNMNNNNMNNNmNdmNNNMNmNmNMMNNNNNMMmNMNNNNMNNNNmNNNNMmmNMNNmN";
	
	private final String broochImage = "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys+//::----...--:/+syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy+-.`                     `-/oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy+:.     `-/+syhddm-hmddhyo/-`     -/syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys:     .///NMMMMMMMMM-mMMMMMMMMMy-+-    .+yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy+`    -smMMM:yMMMMMMMMN.yMMMMMMMMm-mMMNy:    :syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:`   .omMMMMMMN.hMMMMMMN:s-yMMMMMMN-hMMMMMMNs.   .syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy+`    +NMMMMMMMMMd.dMMMMd-hMN:oMMMMM:sMMMMMMMMMN+    :yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy-   .yNo:hMMMMMMMMMy.NMMh-dMMMN//NMM+/MMMMMMMMMh:+my.  .syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyo`   +NMMMNo:yMMMMMMMMo:My-mMMMMMMo-Ns-NMMMMMMMh:+mMMMN+  `oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys`  `hMMMMMMMNo-yMMMMMMM/-:NMMMMMMMMy..NMMMMMMh:+mMMMMMMMy`  oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy`  .mMMMMMMMMMMNo-yMMMMM/+MMMMMMMMMMMh.dMMMMh:+mMMMMMMMMMMh`  oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy`   sdNMMMMMMMMMMMNo:yMN:sMhMMMM-mMMMdmd.yMh:+mMMMMMMMMMMMmy-  `syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy/  `ddy+/+sdNMMMMMMMMNs--hMM++MMM`mMMy-NMm--+mMMMMMMMMNy+//ohms  -yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys   sMMMMMNds//+sdNMMMMd-dMMMMsdMMdMMNoNMMMN:+MMMMNds//+ymMMMMMN-  oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy.  `NMMMMMMMMMMNds+/+ss-NMMNyo:-++o+/::ohNMMM+/s+/+ydNMMMMMMMMMMy  -yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy   +MMMMMMMMMMMMMMMMN::NMy/oo-hMMdhdmMh-/o/yNMo-NMMMMMMMMMMMMMMMN`  syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy/   yMMMMMMMMMMMMMMMN/+Mh:yMd-NMo+ydho/NM:yMy:yMy-mMMMMMMMMMMMMMMN-  +yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:   /oo++++++++++++/.sMy.NMM+oMd-MMMMM/yMy/MMN.yMh.:++++++++////oo.  /yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:   hMMMMMMMMMMMMMm.yMMMo:dMy-MN-sNNNh:NM/sMd:oMMMm-yMMMMMMMMMMMMN-  /yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy-   sMMMMMMMMMMMMh.hMMMMMm+:s/:dMhsoydMm//y/+mMMMMMm-sMMMMMMMMMMMm`  oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy/   :MMMMMMMMMMMs.mMMMMMMMMMho:.-/osoo:`:+hNMMMMMMMMN:+MMMMMMMMMMs   yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyo    mMMMMMMMNd/:NMMMMMMMMMMMMMhmNmdmNdyMMMMMMMMMMMMMM+-oymNNNMMN.  :yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy.   :MMMmy+++`/MMMMMMMMMMMMMM+-NMm`NMN-+MMMMMMMMMMMMMMo.so++ohmo   syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy`   /oosdNN:oMMMMMMMMMMMMMMdoNMMm`NMMNomMMMMMMMMMMMMMMs-mMNmy:   /yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy+    sMMMm.-oo++++++++++++/++++++/++/+///++++++:-/++//+:+NMMy`  -yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy-    +MMMNNNNNNh:+mNNNNNN:+NMMMd`NMMMM/:MMMMMMNs:sNNMMMMMNs   -yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:    :mMMMMMd/+mMMMMMMM+:MMMMMh`NMMMMN-oMMMMMMMNy:sNNNMm/   :yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyo.   `oNMd/+NMMMMMMMMs-NMMMMMh`NMMMMMm.hMMMMMMMMNs:smo`  `oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:    .:+NMMMMMMMMMh.mMMMMMMh`MMMMMMMd.mMMMMMMMMNm-    -yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys:    .omMMMMMMMm.dMMMMMMMd.MMMMMMMMy-NMMMMMNmo.   -syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys:     :smMMMN-hMMMMMMMMd-MMMMMMMMMo/NNNms-    -syyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys+-     -+y/sMMMMMMMMMd:MMMMMMMMMN:++.    .+yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyys/.      `:+syhdmNNd:Nmmddys+:`     -/yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy+-.                        `-/oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyso+/:-.````  ``..-:/+oyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy";
}
