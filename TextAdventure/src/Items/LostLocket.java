package Items;

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

		//=================================================================================
		//Don't forget to set the item name
		//=================================================================================
		this.name = "Lost Locket";
		this.image = locketImage;
		
		createItems();
		createFlags();
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
		//Provide a case for each verb you wish to function in this room.
		//Most verbs will require default handling for cases where the
		//given verb has an invalid command.  
		//===============================================================
		switch (command.getVerb())
		{			
		case "pull":
		case "take":	
		case "remove":
			if (command.getSubject().contentEquals("picture") || 
				command.getSubject().contentEquals("photo") ||
				command.getSubject().contentEquals("photograph") ||
				command.getSubject().contentEquals("image") ||
				command.getSubject().contentEquals("portrait"))
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
			if (command.getSubject().contentEquals("picture") || 
				command.getSubject().contentEquals("photo") ||
				command.getSubject().contentEquals("photograph") ||
				command.getSubject().contentEquals("image") ||
				command.getSubject().contentEquals("portrait"))
			{
				return new DisplayData ("", "It's a faded photo of an older woman.  You don't recognize her.");
			}
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
	private final String locketImage = "sooossssssssssyyyhhhddmdhyyhddhysoooooo++oossoooooosssssssssssssssssssssssoo++++++++++++oooooooooooooooooooooo++++++////++++++++++++++++ooooooossssssyyhdmNMM\r\n" + 
			"sssssssssssssyyyhhhdddhsooossssooooooo++++oossooosssyyysssssssssssssssssssoo++++++++++++oooooooooooooooooooooo+++++++++++++++++++++++++ooooooosssssssssyyhdNN\r\n" + 
			"sssyyssssssyyyyhhhhdhs+++oooosooooooo+++++ooosssssyyyyyyyyyyyyyssssssssssssoo+++++++++oooooosssssssoooooooooooo++++++++++++++++++++oooooooooooossssssssyyyyhm\r\n" + 
			"ssyyyyyyyyyyyhhhhhhso++++ooooossoooo++++++ooosssyyyyyyyyyyyyyysssssssssssssoo+++++++++oooooossssssssoooooooooo++++++++++++++++++++oooooooooooooossssssssyyyyh\r\n" + 
			"syyyyhys/osysshhyso++++++oooossssooo+++++ooosssyyyyyhyyyyyssssssssssssssssssoo+++++ooooooooossssssssooooooooo+++++++++++++++++++++ooooooooooooooossssssssyyyy\r\n" + 
			"yyyyyhhhsyhssoysooo+++++oooosssssooo+++oooossssyyyyyhhyyyysssssooooossssssssoooooooooooooooossssssssooooooooo+++++++++++++++++++ooooooo++oooooooooossssssssyy\r\n" + 
			"yyyyyyhhhyy/+hh++ooooooooooossssssooooooossssssyyyyhhhhyyysssooooooooosssssssooooooooooooooossssssssoooooooo+++++++++++++++++++++++++++++++++ooooooossssssssy\r\n" + 
			"sssyyyhhyyyyhhhh+os++//+oooo+ossyyyysossssssssssyyyhhhhhyysssooooooooooosssssoooooooooooooooosssssssooooooo++++++++++++++++++++++++++++++++ooooooooossssssssy\r\n" + 
			"ssyyyyyyyyyyydmd+syys//--+o///++oyhhysssssssssssyyyhhhhhhyysssoooooooooooosssooooooooooooooossssssssooooooo++++++++++++++++++++++++++++++++oooooooosssssssssy\r\n" + 
			"sssyyyyyyyyyyhddo+hd/::+oysso++--+so+ooossssssssyyyyyyhhhhyysssooooooooooossssssoosssoooooooosssssssooooooooo+++++++++++++++++++++++++++o++++oooooossssssssyy\r\n" + 
			"sssyyyyyhhhhhhdmNmdsyyyoosyyyssss+ooso+++osysssssyyyyyyhhhyyyssoooooooooooosssssssssssooooooossssssooooooooooo++++++++++++++++++++++++++++++ooooooosssssssssy\r\n" + 
			"sssyyyyhhhhhhddmNNmddhy/-sh+/:+syhyysso/:/:+++++ssyyyyhhhhyyyssssooooooooooosssssssssssooooossssssooo+++++/////////+++++++++++++++++++++oo++ooooossssssssssyy\r\n" + 
			"ssyyyyhhhhhhhddmmNNmddddyyssyss+ossyyyyyyysooo+::oyyyyhhhyyyysssssooooooosssssssssssssssssooooooooooo+/:/:--.......---:::-..--:::/++++++oo+ooooossssssssssyyy\r\n" + 
			"ssyyyyyhhhhhdddmmNNmmdddhhhhhy+.+hy::+syyyyssso+:+++ossyhhyyyysssssssssssssssssssssssssssooooooooooo:-/:...:://:-`````.``````..--::+oooooooooossssssssssyyyyy\r\n" + 
			"ssyyyyyhhhhddddmmmNmmddhhhyyhhhysoosoo++osyyyyhhhs+osso/yhhyyyyyssssssssssssssssssssyyyysooooooooo+::-`-odmmmmdhys+-```./oyyyo+:.--:+ysossoosssssssssyyyyyyyy\r\n" + 
			"ssyyyyyhhhdddddmmmNNmddhhyyyyyyyyyyyy+.oyy+syyyyyhyoso+-/ssyyyyyyyyyyyyssssssssssyyyyyyyysssssooo+:/-.+ddddhhhhyssoosoymmNNNddhhy/.-:+hysssssssssyyyyyyyyyyyy\r\n" + 
			"ssyyyyhhhhdddddmmmNNmddhhyyyyyyyyyyyys//oo-//+yyyyyyyys:.-:+sso++oyyyyyyyysssssyyyyyyyyyyyyyssooo:/:-sdddhhyso+//+syo+osyhmmdhyyys+..-/hyyyyyyyyyyyyyyyyyhhhh\r\n" + 
			"ssyyyhhhhdddddmmmmNNmmdhhyyyyyyyyyyyyyyyssyso/+ssyyso::://///o-:/ohhyyyyyyyyyyyhhhhyhyyyyyyyysso/:/.:dddhys+/:--./s/-//+ssydyssssss/.--sdhhhhhyyhhyyyyyyyyyyy\r\n" + 
			"yyyyyhhhhdddddmmmmmNmmddhhyyyyyyyyyyyyyyhyys-.yho/..++hNdhhyy+yhosshhsshyyssooooossssyyhhyyyysss:/:.oddhys+:--.../s--:-/ooohs//++++/.--/ddhhhhhhyyyysssssssss\r\n" + 
			"syyyyhhhdddddmmmmmmNNmmddhhhyyyyyyyyyyyyhhhhs/osy//oo+osdmmdmso/-/yhddhyysosysssssyhhysoshhyyyo+/:-.smdhs+/--.....-.---/+/+s/::::::-..-+dddhhyyyssssooooooooo\r\n" + 
			"ssyyyhhhdddddmmmmmmNNNmmddhhyyyyyyyyyhhhhhhhhhosys/o+oydhhs//+++++yhdmdhysoyddddddhhhhhhs+ohhy:/y+:-odhyo/:-.``.......-///:-------...-:ohyyyyyssssooooooooooo\r\n" + 
			"syyyhhhdddddddmmmmmmNNmmmddhhyyyyyyyhhhhhhhdddho+oyddddddddddhhhdhsshhyysooydddmmdddddhhhyo/shs:/yo:/hhso/-..`.--:--.::/++/:----.....-ohhyyysyssssooooooooooo\r\n" + 
			"yyyhhhdddddddddmmmmmNNNmmddhhhhhhhhhhhyyyyyyyyyyyhhhhhhhddddmmmddo+yyysssyshdddddddddddmdhyo/+ss//s+-+hy+/-...--:/---://///::/-:....-ohhhhhyyysssssoooooooooo\r\n" + 
			"yhhhdddmmmmmmmmmmmmmmNNmmmdddhhhhhhyyyyssyyyysssssssssyyyyhhhhhdy-ohysydhhhhhhhhhhhhhdddddhso//+o:+s+.sho/----://:/:-:///:://..`..-:odddddhhhyyysssoooooooooo\r\n" + 
			"yyyyhhhhhhhdddddmmmmmNNmmmmmdddhhhyyssyysssssssssssssyyyyyyyyyyyo./hsyddhhhhhyyyyyyyyyhhhhhyo:.-+/`:s:-+o/::::/::/:::::::/:/:``..:+hmmdddddhhyyysssoooooooooo\r\n" + 
			"ssssssssssssyyyyyyhddNNNNmmmdddhhhyyyyssoooooooooosssssssssssssso--syyhhhhhhyyyyyysssssyyysso+-`.++-:y/./o+++/::/::::::::::....:+ydmmmddddddhyyssssoooooooooo\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyhmmmdhhhysssoooo+++++++++++++++++ooossssssss:-/syyyhhyyyyyysyssssoosssoo+/- `+o//o/--oos+:-:::://::-...-:ohdmmmdddddddhhyyssoooooooooooo\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyhdddhyyssooooooooo++o+++++++++++++++ooooooss/:/oyyys++syyyyssoossooooo++/:.`.ohs-oo--:///:://++/-...-/sdmmmmmddddddhhyyysssoooooooooooo\r\n" + 
			"yyyyyyyyyyyyyyyyyyyyyyyyhhhyyssooooooo++oooo++++++++++++++++++++ooo+:/oyysssssssssssssssoo++++/::. -oys-os:--/+++o/-...-/ohmmmmmddddddhhhyyysssssoooooooooooo\r\n" + 
			"ssssssssssssssssssssyyyyhhhhyssooooooooooo++++++++++++++++++++++++ooo//+osssssssssssssssoo+/////:-``:+ys+sh/--::-..-:/oymmmmmddddddhhhhyyysssssssooooooooooo+\r\n" + 
			"oooooooooooooooosssssssyyyhhhyysssssooo++++++++++++++++++++++++++++oosso+osssso+++oooossooo+/++//:.``/oohmmds::-::+yhmmmmmmmmddddhhhyyyssssssoooooooooooooooo\r\n" + 
			"ooooooooossssssssssssyyyyyyyhhhhhyso+++//////++/+++++++++++++++++++++oosssssssyysoo++/////+++++////::+syydNNNmhyydmmmmmmmmmmmmdddhhyyyssssooooooooooooooooooo\r\n" + 
			"oooooooossssssssssssyyyyyyyyyhddyoo++++///////////+++++++oooooo+++++++ooossyyyyyyyhhhhyyyssssssssssyhhhhddddddddhddddhhhhhyyyyyyyyyyysssssooooooooooooooooooo\r\n" + 
			"+oooooooooooooooooosssssssssyyyyysoo+++++++////////+++++++oooooooooooooooosssyhhhddddddddddddddddddddmmmddhhyysssssssssssooooooooooooooo+++++++++++++++++++++\r\n" + 
			"+++++++++++++++ooooooooooosssyyyhyssoo+++++++///////++++++ooooooooooooooooossssssyyhddmmddddmmmmmmmmmmmdddhyssoooooooo+++++++++++++++++++++++++++++++++++++++\r\n" + 
			"++++++++++++++++++ooooooossssyyyhhhyssooo+++++++/////++++++++oooooooooooooooooooooossyhhddddddddmmmddddddhhyso+++++++++++++++++++++++++++++++++++++++++++++++\r\n" + 
			"+++++++++++++++++oooooossssyyyyhhdddhyssooo++++++///++++++oooooooooooo++++++++++++++++oossyhhhhhhhhdddhhhyysoo+++++++++++++++++++++++++++++++++++++++++++++++\r\n" + 
			"++++++++++++++++ooooossssyyyhhhhyyyhhhyyssoooo+++++/++++oooooooooooooo+++++++++++++++++++++oossyyyysyyysssooo++++++++++++++++++++++++++++++++++++++++++++++++\r\n" + 
			"//++++++++++++++ooooosssyyysssooooooosyhhyyyso+++++++++++ooooooooo+++++++++++++++++++++++++++++ooooooooooo+++++++++++/++++++++++++++++++++++++++/++++++++++++\r\n" + 
			"///+++++++++++++oooosssoooo++++ooooooossyhhyso+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++////++++++++++++++++++++++/+++++++++++++\r\n" + 
			"//////++++++++++++oooo+++++++++ooooooooooooo++/////////////////////+++++++++++++++++++++++++++++++++++++++++++++++++++++//+/+++++++++++++++++++/+++++++++++++\r\n" + 
			"/////////+++++++++++++++++++++++++oooo++++////////////////////////++++++++++++++++++++++++++++++++++++++++++++++++++++++++///++++++++++++++++++/+++++++++++++\r\n" + 
			"///////////////////+++++++//////++++++///////////////////////////+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++/////++++++++++++++++++++++++++++\r\n" + 
			"////////////////////++////////////////////////////////////////////++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++///++++++++++++++++++++++++++++\r\n" + 
			"/////////////////////////////////////////////////////////////////+++++++++++++++++++++++++++++++++++++++++++o+++ooo++++++++++++++++++++++++++++++++++++++++++";
}
