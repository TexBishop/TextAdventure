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

public class ToolShed extends CountdownRoom 
{
	private static final long serialVersionUID = 1L;

	public ToolShed(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "ToolShed";
	}

	@Override
	protected void initializeCountdown() 
	{
		//=================================================================================
		//Death after third command
		//=================================================================================
		this.setCountdown(3, "The beast pounces on you from the open trap-door, pinning you to the ground and letting out a loud, gutteral howl which shakes the shed itself. ", 
				"The beast suddenly raises its claws and begins wildly swiping them at you attempting to claw your chest to shreds. You instinctively raise your arms, sheltering yourself from the blows as your forearms take the initial swipes."
						+ "You desperately think of a way to repel the creature. ", 
						"The beast's frenzied slashes are quickly too much to endure, and your arms give way. It's red eyes gaze at you briefly before it's head lunges at you neck, maw gaping open."
								+ "There is a brief moment of excruciating pain before you feel a warm liquid cover your chest. Slowly all your strength, hearing, feeling, all senses are gone. your head rolls to your right side as finally your vision begins to blur,"
								+ "until that too has finally left you. ");

		//=================================================================================
		//Count down starts and progresses unless the player uses the knife
		//=================================================================================
		this.setTriggers(this.gameState.getFlag("trapdoor open"), this.gameState.getFlag("attacked with knife"));
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
		// default room description
		this.description = "You stand inside the Toolshed. "
				+ "The shed's interior is lit well enough from the windows, leaving nothing hidden under the veil of darkness. The interior is like any toolshed you've ever seen in your life."
				+ "tools hang from the walls, bags sit in the corners, and a workbench holds numerous other items scattered across it. Behind you is the door leading to the backyard.";
		this.description += this.gameState.getFlag("trapdoor open").toString();// displays description if the trapoddor is open
		this.description += this.gameState.getFlag("attacked with knife").toString();// displays description if the creature has been killed

		return this.description;
	}

	@Override
	protected void createMovementDirections() 
	{
		//=================================================================================
		//Create directions that move to backayrd
		//=================================================================================
		this.addMovementDirection("outside", "Backyard");
		this.addMovementDirection("leave", "Backyard");
		this.addMovementDirection("exit", "Backyard");

		//=================================================================================
		//Create directions that move to the basement
		//=================================================================================
		this.addMovementDirection("down", "Shed Basement");
		this.addMovementDirection("trapdoor", "Shed Basement");
	}

	@Override
	protected void createItems() 
	{
		//default
	}


	// Creates flags to indicate when the trapdoor has been opened and when the user has killed the demon
	@Override
	protected void createFlags() 
	{
		this.gameState.addFlag("trapdoor open", new Flag(false, "", "In the center of the shed is an opened trapdoor, leading downwards. "));
		this.gameState.addFlag("attacked with knife", new Flag(false, "", "The beast's slain corpse is still on the floor, in a pool of its own dark blood."));
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
			//If enter shed basement, only enter if door is open.
			//===============================================================
			if (command.unordered("down|trapdoor"))
			{
				if(this.gameState.checkFlipped("trapdoor open"))
					return this.move(command);
				else
					return new DisplayData("", "The trapdoor is closed");
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

		case "attack":
		case "stab":
		case "slash":
		case "use":
			//===============================================================
			// command which checks if the player has already used the knife. If they haven't, checks if the trapdoor is open,
			// thus releasing the demon. If they haven't opened the door, nothing to attack. If they have already killed the demon,
			// still nothing to attack.
			//===============================================================
			if (this.gameState.checkInventory("Silvered Knife"))
			{
				if (command.unordered("silvered|knife"))
				{
					if(!this.gameState.checkFlipped("attacked with knife"))
					{
						if(this.gameState.checkFlipped("trapdoor open"))
						{
							this.gameState.flipFlag("attacked with knife");
							return new DisplayData("", "You grip the silvered knife and plunge it into the beast's chest, twisting it as you drive it deeper"
									+ "into it's twisted flesh. It's skin sizzles and burns, the beast begins screeching and wailing in pain before throwing it's head back, it's body going limp and still."
									+ "The beast slumps over on the ground, unmoving as a dark pool of blood pools around the slain creature. ");
						}
					}
					else
						return new DisplayData("", "Nothing to attack here. ");
				}
			}
			else
				return new DisplayData("", "You don't have that item. ");

			//===============================================================
			// If the shard of glass is used, it is ineffective
			//===============================================================
			if (this.gameState.checkInventory("Shard of Glass"))
			{
				if (command.unordered("shard|glass"))
				{
					if(!this.gameState.checkFlipped("attacked with knife"))
					{
						if(this.gameState.checkFlipped("trapdoor open"))
							return new DisplayData("", "You quickly grasp the shard of glass in your hand and plunge it into the beast's chest, dark"
									+ "blood begining to coat your hand. The creature amazingly seems to barely notice your sudden act of aggression, and is"
									+ "unphased.");
					}
					else
						return new DisplayData("", "Nothing to attack here ");
				}
			}
			else
				return new DisplayData("", "You don't have that item. ");

			//===============================================================
			//Subject is unrecognized, return a failure message.
			//===============================================================
			return new DisplayData("", "You don't see that here.");

		case "lift":
		case "open":
			//===============================================================
			//opens the trapdoor after checking if it has already been opened
			//===============================================================
			if (command.unordered("trapdoor|trap|door|hatch"))
			{
				if (!this.gameState.checkFlipped("trapdoor open"))
				{
					this.gameState.flipFlag("trapdoor open");
					return new DisplayData("", "You grip the metal rung tightly and pull up with as much force and might as you can muster. After some effort the door heaves open. "
							+ "You gaze into the path newly revealed path, the trapdoor's descending stairway leading down into a dimly lit passageway. "
							+ "As you gaze a shape slowly comes forth from the stairway. The shape is humanoid, hunched over "
							+ "slowly ascending the steps, it looks like a person, bipedal, two legs and arms, yet naked, and almost nothing but skin and bones. You quickly also notice the long claws portruding from it's hands and feet, it's glowing red "
							+ " eyes and it's jagged set of teeth.  ");
				}
				else
					return new DisplayData("", "The door is already open");
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
				return new DisplayData("", "Nothing sticks out too much in here. The walls have very few things hanging on them however. A note"
						+ " appears to be on the workbench as well. The a section of the wooden floor seems to have some sort of a metal ring on it. You belive it's a trapdoor. ");

			if (command.unordered("wall|walls"))
				return new DisplayData("", "The walls are suprisingly light on hardware. All that is present is a pair of gloves, some small mettal pipes, and an empty pouch."
						+ "Odd. You would expect more hardware in a place like this");

			if (command.unordered("bench|workbench"))
				return new DisplayData("", "The workbench holds many small containers fitted with screws, nuts, botls, nails, the works really. Some pipes, a wrench, and some assorted "
						+ " other materials. A note is stuck to the bench. it reads:"
						+ "'To do list "
						+ " - make more ''suitable'' weapons. Damn things are resilient to almost everything."
						+ " - stash more weapons, never know when your gonna need one in a pinch. At least if one shows up"
						+ "while Im grilling Ill be ready... ");

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
	private final String forestClearingImage = "\r\n" + 
			"    `.`    ``   ```  `````````.:..d:/:--..-...`....---..--..-....`                                            .+ys+:`     `     .                    ``-+++h+/\r\n" + 
			"`    `    ````      ``````````.-.h//-..........--..-...-........`                                           +NNNNNms.     `    .`                  :sh+h-:h`-\r\n" + 
			"``   `.++:` ` ``  ` ``````````..-dso:-..........-....-..........`                      `    `              -NNNNNNNm+     .    .`        `````..-../sh+h-+h-/\r\n" + 
			"`.`` `-MMh:  ` ``   ```````````..dhho/-...........-.s+-.........`                       `   `            ` omNNNMNNNs     ..  `+:::/o:://o+:---o/---.d/h+yh+o\r\n" + 
			"...```:MMNs.   ````````.````..`.-dhy/...............hy/:-.......``                      `   `            ` +mmmNMNNM/      .-::     o`   /:   `+   ``y-hyhdyd\r\n" + 
			".`````+ds/+-`-::-..``` .`````..-.yh:+-...`......-..-yd+:--.......``  ` `  `             `` `.  -/++o+o     .//-hs-.`.-::`   `.-   ++-o`  /:  `+`   ``s/mydyyh\r\n" + 
			"-////:-``    :++:-.-..`````````-.+hh:....../hhy+:-.-+mo:..........`. .  .  -             `+` +o+.   `o  +ooo   mo   o+:/-`  /N.   oy -/. /o `+.     -sohsyhyh\r\n" + 
			"+.````          .::.---..```....-:yd:-.....s+ymmo-.-:m+/-........- - -  .--`````.``       h` ./y`   -/ `y/+y  `ms   /hyh/   :m-   sy  `-:os--`       /hhsdyyh\r\n" + 
			"`...```       ` ```-:..--`..`..`.-sd/-.....--yNm+-.--d+/:-...----.  +`   -+ ``./osy-      m-  -s    +: .+hh/  `mo    :h+    :d.   yy     o+           -sydhhh\r\n" + 
			" `.````         ` `../`.-....`...+++/.......-ss/---+oh+/:----/so:. `d`   -h`/sy+-:yo     `d-  :o    h:`  os   `d+    .h/    /d.   ho     o+        ````.+yyhh\r\n" + 
			"`   ``            ````/`.-..``-+ohhyo.......-o/----yhhy+//hm++hy:. `m`  `+mh+.    sy`     h:  /+   `m.   os   .h/    `h+    /d`  `d+     o+             -/ooh\r\n" + 
			"                `  ```/`..-oo+ho.`.+ho:..`..-::---:mMmh//+yms+ho:. `d`.odsy`      /y.     y-  +:   -m`   os   .h:    `ho    /h`  -h/     o/             `....\r\n" + 
			"                    ``/``..-hdh+oss+yy+:..:ossssssymNmm/++oNy+ho:- `dsm+``s`      -y-     s:  o-   +d    os   .h:    `ys    +h   /y:     s/             `...`\r\n" + 
			"-`                  `-:.`..`.ymy+/y+ss+/-.:oNNNNNMNNNNm++oomh+hy:-``dy`   y.      -y-     s-  y.   ss    yy   -h-     dy    +y   os-    `s:             ````.\r\n" + 
			"/+.`                -:.``````.smy+-.os/:----dhymmmmNNNms+oomd+sd+:``h-    o-      +s`     o- `y    s:    yy   -h.     dy    +s   so.    .s-              ````\r\n" + 
			" --:-``           `.````  `````+d+-.os/:-..:smdmmmNNNNdy+ssNd+sy+/. h-    +-      s/      o- .o    s`    yy   :y`     hh    os   h+.    .s-             `````\r\n" + 
			"-h/.`-````  -`           `````..:do:+y/:.::-:mysmmmmmNddhysmmoo+s/- h-    +:     .o-      +- -+   .s     ys   /y      hh    oo  `h/`    .s-             `````\r\n" + 
			"yhms++:.....so-```` `````````.-..:dssh/:-yo/-yd:ohsdNmNmoysmdoo/y:- y-    +:     /:-      +- :/   -+     oo   /s  `--:sh---.s+  -y/`    .y:           ```````\r\n" + 
			"ddddhhhos+///ho:....`-smmhyo:yosyo/dym+/-sso/+myymdmmhMNoyshmoo+s/- y-    ::     +`.      +- /-   /: `://+s///oo  /.:+-:o./+h:  :o/     .y:         ```````..\r\n" + 
			"Nmdddyyyhyso/sh+/--..:ddo:hds/dsoo:/ymhy/s+s+:hNohhhNmmNoosym+oy+/. y:    -/    `+.`      +- o.   o` -.---:--:y+ .`-`--`:.-:h-  ++/     .y:        `````.....\r\n" + 
			"Nmddhhmhdmmds+ds//:-..oyo-y+/:ss+s/::oNdsh/s/:omhshdmmdNo+shdood//. y:``  -/ `--/+-       /- s:- `o  ........-o: . `. .``.-.h`  s//     .y:       `````.....-\r\n" + 
			"sshhmNNmmdyyyymm+++::..-::----:yss::--hhoh+o+::ddyNdhNhN++ssmsyy//- y:``  .+    :/.       /-`s:: -/ .`........s-` ` `` ```..y   y::     -y-      `````...-/++\r\n" + 
			"NNdyhmmmhyyyhhsyysoo++///::::::hhy/:-.//-o:/o::smyymyddN++shdysy//-`y/``` -o    :/-       /-.+   /- .``````.`.s.           -s   h-.     :s.   ```````.-/+//+o\r\n" + 
			"hyydyyydmdhhyysooyhhyso+///////sNho/:--o:+:-s/:/doomyhmNhyyosy/:::-`s/`.``-y `` .+-       /-::   o`           s`           -o  `h-.     /o/-```````./+soo+/--\r\n" + 
			"dhdmmdhsmNdhddmNmNhhhhyso+++///+ds+//::/++++mdo/yhydmdmmssmyyy//:/:`o/```````-:` +-       /:o-``.s            s            //  .h:`     +sd-````.+ys+/-..----\r\n" + 
			"hyyhhhyhhmNNNNNmmNmddhyyso++//++soho++oymmNNMNmmmmddmmmmddddhds++::.//````     ``+o+oossshdNNs+-:+           `y            o:  -y:.`````sdm-.../+:.......----\r\n" + 
			"dddNNMMMMMMMMMNMNmMdddysooooo::mNmmdhdhssymdddmmdddhmmddNNNNNNMyo+/:----..```  -:+/  `-dmooyy..`/-           .y           `y:..:h:..:s---sy:--//-....sN:-----\r\n" + 
			"dhmNmmNNNMMMMNMMMmNNmdhhhdddhhNNmmhssdyy+:+yhhhddyhhNmdmmNddmyooooso+//::--..``  -:    ym                    -y :h+o.ohs-+++---+ho+sss:o/hs:/dd/----.hm:-----\r\n" + 
			"NhyymydmddddhhhdmNNNNNNmmdyyyhhhyyssyodhydddhssdMMNhNmmmMhssyooooosoo+//:--..``  .:    yd               ``````.-sdydmNMMMMdmmmdhdmdddm/homhodm/-----.dy:-----\r\n" + 
			"hyhhhddhy+yo/yhNmMNmhyysssssyhhhyyhhd-/MdNdNNmymmmNmdMdhNmyossooooo++///::--.``` `.    sd   ``.`  . `` ``.......:+dhdMMMMNNNNNmdhdmdNmomhMhyMy::-----m+:-----\r\n" + 
			"mmmdmyyh+omy:/shdhyyyyysshhhddmymNy+yhdNmymmyhshohomydmsyNyysyooooo+////::--..```     `oh  `....```so```.......-ddyhhmmmmNNmmmddyddmNNNmMmshmso++/:-/h/:-----\r\n" + 
			"ddddyo+:/ho::+oydhdNmdmdmyhNmNydNNmmddNhyhyyshyooh+mdshyymhhsysoooo+/+++//:::----:::ssmyy++++oooosddmyys.......:Ndhdy.-:syhhhhhdsdNNMMNNmdhhyyyyyyhyds/:---::\r\n" + 
			"hhmNyss++d+/o:-:s+dNMNNMM-/hNdyMNMmmNmmhshmshoh+yoohMhdNmhmyyyyssshhyhdddddyyhhyoosoo+soooososshhddhyso+:::::--/Nhhhs/:osyyyyyhmdddddmmNNdhddmmdhhosM//:--:::\r\n" + 
			"yyhyyysss//oosyysymNmmNMMMNhmhNNNNNdmMddsdyyyssohoodmddmddhhhydmdhddmmhmmmNNmddhsosoo+oooooooooyhhdyh/+ossoo+//sNoyysyoooosssyhdhhhhyyyyyyhNmddmNm:dN:/::::::\r\n" + 
			"---:hdMMMh+hs/hNNdmNMNdmmNddhyhhsyddhNmhodsyyyssyosyyNdhdmhmNMMNdo-ymdhNmmNMMNNNNmhysoooooooooosNNdyy+/ooooo+//yd+ssssoo/oss+shhdmmhhyossyhmmhhmmm+Nh:+/::///\r\n" + 
			"mhddNMMMNdoyy/oMsomNNNhNhmmNhyyhssddyhdhsyshhdyyysyssNhhdmmhNNNdhmmmNddNNNNMNNNmdhyysssoooysooodMNNmyo+osooo+++ho/ssosyhsyhy+dmNMMmhyoooyyhhhhhddhdmmdy+////+\r\n" + 
			"Ndhhy+ysooosy++NdmNmmmdNhmmNhhmhyshdyhhhmNmmh++hhhhhhmNhyssosoossshhhNNNNNNMNNmdhhssosoooooooooomNmMhmNNNNNmmmddssyssssNmNmhdmdMNdhhs++syyhhyyyhdNMMMMMdo//+o\r\n" + 
			"/:::::/-::ooy++NNddddMmNdNmNdshyohyyyhhhdNNNdsoNMNMMNMMhshyshyyoo+dmmmmmNNMMmhhhhhssysyhsooooooyyNMNdNNNmmddmmddhhdhhdmNMNhmMMNMMmmdsooyyyyyhyshMMMMMMMMMNmNM\r\n" + 
			"::::::::::+sso/mNyydmMmNdMNNmysdhyyssddhosmNMMMMMMMMNNMdooo+//+oo/dmNNNNmNmdyyyhyhhssoyy+/++++oossmddmmmmdmmmdhdhhdddNMMMddMMMMMMMMMdhydhhhyyysMMMMMMMMMMMMMM\r\n" + 
			"::::-:::.:/oy+/dmsymmNmmmMNNNyhhmhoooddhdNMMMNNMMMMNmmNmhhyys/-sy+mNmmmmdhyyyyyyyysyyssso+soossyhhhdmNNMMMNNNdmmdhddhMMMmhNMMMMMMMmmNMNNNmdhyshMMMMMMMMMMNNMM\r\n" + 
			":::::::+-.+odo:dm:hmmNmmmNddddmhdmsooyhhdMMMMMMMMNmmmmNNdmy+`.`.mhddmdhssssssssssssssys+/++ooohmddNNNNMMMNNNmdNMMMNNdmmNdmNMMMMMMNmMMmmNMMMNdyyMMMMMMMMNMMMMM\r\n" + 
			":::::::/++hohy/ymosdmNmmNNddhhhy+so+osyhhMMMMMMNmmhhyydMNmy-.-.+Mhdhsssssssssssssssssyyooo+oydNMmdNmdmMMMmmmNhhhmMMNmmmhmNMMmNMMMNMMMMMMMMMMMNNMMMMMMMMMMMMMM\r\n" + 
			":::/-o:oyhhoss/omyoymmddmhhhyhhy+++o+oyhhmMMMMNdysyhhmmdyso//:/dhsssoooooossssssoossyysso/ymNmhymNNNddNMNNMMmyhdNNNmmNNNNNNNMMMMMMMMMMMMMMMMNMMMMMMMMMMMMMMMM\r\n" + 
			":////ssyhhhs/::smNhhhyyhdyyhyhys+:/o+oyhhhMMmysyhmNdhysssssossssssssoooosssoossssoosyyo/odmmNysyhdmmyhdNMMMNmmNNNNNNNNNNNNMMMMMMMMMMMMMMMMMNMMMMMNMMMMMMMMMMM\r\n" + 
			":///oshhhhyo+//sdmyyyyyyhyyyydmNmhs+++yyyyMNshmNmysssssooo+osooooossoo+ooooooosooooss/+hmmhsymmyyhddhdNMMhmNMMNNNNNmNNNNMMMMMMMMMMMMMMMMMMMMMMNMMMMMMMMMMMMMM";
}
