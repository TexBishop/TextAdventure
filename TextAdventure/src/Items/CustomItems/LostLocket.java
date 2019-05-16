/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Items.CustomItems;

import Items.Item;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class LostLocket extends Item 
{
	private static final long serialVersionUID = 1L;

	public LostLocket(GameState gameState) 
	{
		super(gameState);
		this.image = locketImage;
	}
	
	@Override
	protected void setName() 
	{
		this.name = "Lost Locket";
		this.gameState.addItemSynonyms(this, "locket", "lost");
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//Called when item is viewed.
		//=================================================================================
		return new DisplayData(this.image, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		//=================================================================================
		//This is used to return the main item description.  If any flags alter the main
		//item description, that will be handled here.
		//=================================================================================
		this.description = "An old locket, of the sort you'd put a picture in. ";
		this.description += this.gameState.getFlag("picture removed").toString();
		
		return this.description;
	}
	
	@Override
	protected void createItems()
	{
		//=================================================================================
		//If your custom item happens to contain other items, you will create them here.
		//=================================================================================
	}

	@Override
	protected void createFlags() 
	{
		//=================================================================================
		//Create Flags and add them to the Flag hashmap.  The first string field in addFlag()
		//is the key name for the Flag in the hashmap.  In the Flag constructor, the first
		//field is the Flag's starting boolean value.  The second field is the string the
		//Flag will return on toString() if it has not been flipped (false).  The third field
		//is the string the Flag will return on toString() if it has been flipped (true).
		//=================================================================================
		this.gameState.addFlag("picture removed", new Flag(false, "It looks cheap and worn, but it does in fact have a picture in it. "
				+ "The picture seems to be a bit loose in its setting.", "There are some numbers etched into the back of the locket "
						+ "where the picture used to be, \"469\"."));
	}

	@Override
	public DisplayData executeCommand(Command command) 
	{
		//===============================================================
		//Do not provide default cases for command failure in items.
		//===============================================================
		switch (command.getVerb())
		{			
		case "pull":
		case "take":	
		case "remove":
			if (command.unordered("picture|photo|photograph|image|portrait"))
			{
				if (this.gameState.checkFlipped("picture removed") == false)
				{
					this.gameState.flipFlag("picture removed");
					return new DisplayData ("", "You removed the old photo from the locket.  Behind it, etched into the back of "
							+ "the locket, is the number \"469\".");
				}
				else
					return new DisplayData ("", "That has already been done");
			}
			
		case "look":
			if (command.unordered("picture|photo|photograph|image|portrait"))
			{
				return new DisplayData ("", "It's a faded photo of an older woman.  You don't recognize her.");
			}
			
			if (command.unordered(this.regex))
				return this.displayOnEntry();
			
		default: 
			//===============================================================
			//Pass the current command to the room commands when default is reached.
			//===============================================================
			return this.gameState.getCurrentRoom().executeCommand(command);
		}
	}

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String locketImage = "yyyyyyyyyyhhddmmNNNNMMMMNmmNMMNmysssooooooyhhyssosyyhhhhhhhhhhhhhhyyyyyhhyso++///////+++ooooosssssssssssssoooo+////::::://////+++++++++oosssyyyyyhhhhdNNMMMMM\r\n" + 
			"dddhhhhhhhhddmmNNNNNMMmysssyhdhsoooooo+++oosyyyssyhddddddddddddhhhhhhhhhhhyso++/////++++oosssyyyysssssssssssooo+///::////////+++++++oo+oosssyyyyyhhhhddmNMMMM\r\n" + 
			"dddmdddddddmmNNNNNNNNyo++oosyyyssosooo+++oossyhhddmmmmmmmmmmmmdddddddhhhhhhyo+++++++++ooossyyhhhhhyssssssssssso+////////////+++++oooooooosssyyyyyhhhdddmmmNMM\r\n" + 
			"ddmmmmmmmmmmNNNNNNNho++++ossyyyyyysoo+++++osshdddmmNNNNmmmmddddddddddddddhhyo++++++++ooosssyyhhhhhhyyssooosssso////////////++++++oooooooosssssyyyhhhhhddmmmNM\r\n" + 
			"dmmmNNNy/ydmhyNNmho++++++ossyyhhhyso+++++ossyhdmmmNNNNNNmdddddhhhhhhhhhhhdhhsoo++++oooosssyyyhhhhhyyysoooossso++//////////++++++oooooooooosssssyyyyyyhhddmmmN\r\n" + 
			"mmmNNNNNhdNyyydhsoo+o+++oosyyhhhhysoo++oosyyhddmmNNNNNNNmddhhhyyyyyyyhhhddhhysoooooosssssssyyhhhhhyyyysoooooo++//////////+++++++ooooooooooooooossyyyyhhhhddmm\r\n" + 
			"mmmmNNNNNNm/+mm+oyssooooossyyhhyhhyssosssyhhddddmNNNNNNNmmdhhyyssssyyyyyhhhhhysoooosssssssssyyhhhhhyysysoooso+////////////+++++++oooo++o++++ooosssyyyyhhhdddm\r\n" + 
			"hhdmNNNNNNNNNNNN+syo+.-+ssssooyhmmNdhyyyhhhhhdddmNNNNNNNNmdhyysssssssssyyyhyyyysssssssssosssyyhhhhhyssssssoo+++++//////+//++++++oo++++++++ooooosssyyyyhhhhddm\r\n" + 
			"hdmmmNNNNNNNNMMM+ymdh:-`.oo/:-/+smNNmdhhdddhhhddmmNNNNNNNNmhhyyssssssssyyyyyyyyyyyyssssoosssyhhddhhyssssysoo+++++++/++///+++++++++++++++o+oooossssyyyhhhhdddm\r\n" + 
			"hddmmmNNNNNNNNMMooNN/-.oodyhs++``+ys+oosddddddddmmNNNNNNNNNmhhyyssssssssyyyyyyyyyyyyyyssssssyhhddhhysooosssso+++++///////+++++++//++++++o+++ooosssyyhhhhhhddd\r\n" + 
			"hdddmmNNNNNNNMMMMMNhmmmysdddmdhdy+ooyso++shddhhddmmmNNNNNNNmdhyyysssssssyyyyyyyyhhhhhyssssyyyyhhhhhssoooosssso++++/////////////////+++++o+++oosssyyyhhhhhdddm\r\n" + 
			"hddmmNNNNNNNMMMMMMMMMNm:-yN/::/hmNNdhys:--.:++/+ydmNNNNNNNNNmdhyyysssyyyyyyyyyyyyhhhhhyssyyyyyhhyyysoo+++//:/:-:--::////////////////++++oo+oossyyyyyhhhdddddm\r\n" + 
			"hdmmmNNNNNNMMMMMMMMMMMMMmdyymdy+shdNNNmmNmhoyoo--sdNNNNNNNNNmddhhyyyyyyyyyyyyhhyhhhhhdhyyyyysssssssso+----``         `---`    ```-/+++o+os+ossyyyyhhhhdddddmm\r\n" + 
			"hdmmmNNNNNNMMMMMMMMMMMMMMMMNNm+`oNh.:+hmmmmddys+-+/+ohhNNNNNmmddhhhhhyyyyhhhhhhhhhhdddddyyssooosssso-`-.  `:://:.                 ./soossyossyyhhhhdddddmmmmm\r\n" + 
			"hdmmNNNNNNMMMMMMMMMMMMMMMMNNNNNmyssdyo++yddmmNNNNh/yhhs/mNNNNmmdddddddhhhhhhhhhhdddddmmmhyysssssss+.`  .omMMMMMMmh+`   `/ohhhs+-   `+dyyyhyyyhhhhddddmmmmmmNN\r\n" + 
			"hdmmNNNNNNMMMMMMMMMMMMMMNNNmmNNNNNmmd+`omhohdNNNNNdyhs+.:yhNNNNmmmmdddddddddddddddmmmmmmmdhhyyyyso..  oNMMMMMMMNdysyhsdMMMMMMMMMd/  `:NddddhhddddmmmmmmmNNNNN\r\n" + 
			"hdmmNNNNNMMMMMMMMMMMMMMMNNmmmmmmmmmNmy/:so---/mmmmmNNmh.  .+yyyo+smNmmmmmmdddddddddmmmmmmmmddhyso.:` yMMMMMmyo/:-sNNyohNMMMMMMNNmd+   -NNNmmmmmmmmmmNNNNNNNNN\r\n" + 
			"ddmmNNNNMMMMMMMMMMMMMMMMMNNmmmmmmmmmNNNmydmhy:+ydmmdo-::--:-:s`::yNMNmmNNNNNNmmNNMNNNNmmmNmmmhyy:.- -MMMMNh+-`   :M+ /-oNMMMNhddddd:   sMNNNNNNNNNNNNNNNmmmmm\r\n" + 
			"dmmmNNNNMMMMMMMMMMMMMMMMMMNNNmmmmmmmNNNNNmNd.`mNs/. //NMMNmdd+dmoddMNdhMmmhyyooosyyydmNNNmmmmdhh.-` sMMMNh+.     +m` -`:hddMd/::///:   :MMMMNNNNNmdddddhhhhhh\r\n" + 
			"ddmmNNNNMMMMMMMMMMMMMMMMMMMMNNNmmmNNNNNNNNNNh:oyd//os/ohMMMMMyo:`-mMMMmNNhshmhhyydNMMmhoyNNNmms+-`  yMMMd+.      `. `` +s+om:    ``    +MMMMNNmmdhhyyysssssss\r\n" + 
			"hdmmNNNMMMMMMMMMMMMMMMMMMMMMNNNNNNNNNNNNNNNNNNoymy-s+omMNNy::/+//omMMMMMmhsNMMMMMMMMMMMMh/smNd-:No` oMMmy:             -++-           `sMNNNmmddhyyysssssssos\r\n" + 
			"dmmNNNNMMMMMMMMMMMMMMMMMMMMMMNNNNNNNNNNNNNMMMMNs+ohMMMMMMMMNNNNNMNydNMNmhsomMMMMMMMMMMMMNms:yMd-:do.-NMdo-     ``..` ../oo+.` `       oNNNNmdmhhhyyysssssssss\r\n" + 
			"mmNNNMMMMMMMMMMMMMMMMMMMMMMMMMNNNNNNNNNNmNNmNNNNNNNNNNNMMMMMMMMMMo+mmmhhdddNMMMMMMMMMMMMMMNy:+dh::h/ +Mdo-     `-:```.:+o+/--:``     oNMMNNNNmddhhyysssssssss\r\n" + 
			"NNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNNNmmmdddmmmdhhhhddddmmNNNNNMMMd sMmdNMMMMMMMMMNNNNMMMMMMNho:-/s-+do yMh-    .::-:-..::/.-::      .sMMMMMMNNNmddhyyyssssssss\r\n" + 
			"dmmNNNNNNNMMMMMMMMMMMMMMMMMMMMMMMNNmddmmdhyyyyhhhhddddmmmmmmNNNNs /MddMMMMMNNmmddddhhdNMMNNh+` `+/ -h:`sd/--`.:.-/-.`---`::+-    `/NMMMMMMMMNNNmdhyyyysssssss\r\n" + 
			"hhhhhhhddddddmmmNNNMMMMMMMMMMMMMNNNmmmdysssssoosssyhddddddddddddo  ymmNMMMNNmmddhhhyssshmmyso:   /+..d/ /hhso+-./-`..--.:--`   .+hMMMMMMMMMMNNmdhhyyssssssooo\r\n" + 
			"dmmmmmmmmmmmmmmmmmNNNMMMMMMMNmdhyysoo+++++++++++++++++oosyhhhhhhy. -dmmNMNNmddhhyhhsos+ooyyo+/-   /s/-y/`.hmNy. `...---.-`   .smMMMMMMMMMMMNNmmdyyysssssssooo\r\n" + 
			"mmmmNNNNNNNNNNNNNNNNNNNMMMMNmdhyssooooooooo++++//+////++++ooossyhy-`-yNNNd++yhdddyyo+sso///o/:-`   omy.ss. -+/---/+so+.    :yNMMMMMMMMMMMMNNNmdhyyyssssssoooo\r\n" + 
			"mmmmmmmmmmmmmmmmmmmmmNNNNMMNmhyyssoosoo+ooooo+++++/++++o+++++++oosy/`-sddyyhyhhyhyysssys+//:/-.``  `sNy.yd- `/yyhd+:    -omMMMMMMMMMMMMNNNmdddhhyyyyssssooooo\r\n" + 
			"hhhhhhhhhhhhhhhhhhddmmNNNNMNmdhysssssoosso++++++++++++++/++++++++ooss:-/syyyhhhyyyysossso+/----..   .+dh/dN:  `:.   .-ohMMMMMMMMMMMMMNNNmmdhhhyyyyyysssoooooo\r\n" + 
			"ssssssssssyyysyyyhhhdddmmNNNNmmddddhyso+/////////////////++++++++++osyyo+oyhhhs+/++ooosso+/:-::--    :syNNMMy-```-/dNMMMMMMMMMMMMMMNNmmdhhyyyyyyyssssssoooooo\r\n" + 
			"sssssyyyyyyyyhhhddddddmmmmmNNNNNNmho+//::::::::://///+++++++++++++++ossyhhyyhdmmdyo+::--:::/://::----+hNNMMMMMmddNMMMMMMMMMMMMMMMMNNmmdhhyysssssssosssssoooso\r\n" + 
			"ossssyyyyhhhhddddddddmmmmmmmNNMMmyo++//::::::::::::///++oooooooo++++++osyhdmmmmNNNNMNNmmdhyyyyhhhddmNMMMMMMMMMMMMMMMMMMNNNNmmmmmmmmmddhhyyyssssssssoooooooooo\r\n" + 
			"ooooosssssssssssyyyyyhhhhhddmmNNmhyso+////::::::::::///++oooooooooooooosyyhhdmNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMNmdddddddddhyyyssssssssoooooo+++++++++//+++++++++\r\n" + 
			"/////////+++++ooooooosssyyhhdmmNNmdysoo+++//:::::::::///++ooosssssssssssssyyhhhddmNMMMMMMMMMMMMMMMMMMMMMMMMNdyssssyyso++o+++++++++++++++++++++//+////////////\r\n" + 
			"//////////+++++++ooooossyyhhdmmmNNNmdysso++/////:::::///++++ooooosssssssooooooosssyyhdNMMMMMMMMMMMMMMMMMMMNmys+++++++++++///////+++/++////+++++/+////////////\r\n" + 
			"//////////++++++ooosssyyhhdmmmmNNNMNNmhyyso++++//::::///++ooooooossooo++++++++++////+oosyhmNMMMMMMMMMMMMNNmhyo++///////++/////////////////////+++////////////\r\n" + 
			"://///////+++++ooossyyhddmmmmmNmmmmmNNmmdhysso+///::///+oossyyyyyyyssoo++///////////////+++oshdmmmmddddddhysoo++/////////////////////:////////++/////////////\r\n" + 
			":::////////+++++oossyyhdddddhyyssoooyhmmmmmdhs+///////++oooooooooo+++//////////////////://///++osssoooooooo+++//////::://///////////:::::///////:////////////\r\n" + 
			":::///////////++oossyyyysooo+++ooooossyhmmNmho+///////////////:::::::://////////+++++///////////++++o++///+++////////:::://///////////::///:://::////////////\r\n" + 
			":::::::///////+++oooooo++++++++ossssssyyyyys+/::::::::::::::::::::::::///////+++++++++++++++//////+++++++/////////++////::::://////////////:///://///////////\r\n" + 
			"::::::::::////////////////////+++oooooo+//:::----::::::::::::::::::///////////++++++++++++++++++++++++++++++/////////+++//:::://///////////////://///////++++\r\n" + 
			":::::::::::::::::::://////::::::://///:::---------:::::::::::::::://///////////++++++++++++++++++++++++++++++++///////++++//:::::://///////////////////++++++\r\n" + 
			"::::::::::::::::::::::::::-------:::::-----------::::::::::::::::://////////////++++++++++++++++++++++++++++++++++++/////+++/:::::///////////////////////++++\r\n" + 
			"::::::::::::::::::::::::----------::::-----------::::::::::::::::://////////////+++++++++++++++++++++++++ooooooooooo+++++++++++/::///////////////////////++++\r\n" + 
			"";
}
