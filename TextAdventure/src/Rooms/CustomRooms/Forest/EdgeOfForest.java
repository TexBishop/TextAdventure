/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms.Forest;

import Items.Item;
import Items.CustomItems.BottleOfWater;
import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

public class EdgeOfForest extends Room 
{
	private static final long serialVersionUID = 1L;
	private int waterLevel = 0;

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
	public String fullDescription() 
	{
		this.description = "The path leading to the edge of the forest is surprisingly well worn, a wide swath of clear, packed dirt. "
				+ "It obviously saw regular traffic at some point in the past. "
				+ "It enters the forest just a bit further on, continuing east through the trees, disappearing from sight around a bend. "
				+ "Off to the side of the path is an antique water pump with a large pump handle, and a water basin placed in front of it. ";
		this.description += this.gameState.getFlag("muddy ground").toString();
		
		if (this.waterLevel == 0)
			this.description += "The basin is empty. ";
		else
			if (this.waterLevel == 1)
				this.description += "The basin is half full. ";
				else
					if (1 < this.waterLevel)
						this.description += "The basin is full. ";
				
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
		
		//=================================================================================
		//Create directions that move to Forest Path
		//=================================================================================
		this.addMovementDirection("east", "Forest Path");
		this.addMovementDirection("forest", "Forest Path");
		this.addMovementDirection("path", "Forest Path");

		//=================================================================================
		//Create directions that move to Backyard
		//=================================================================================
		this.addMovementDirection("north", "Backyard");
		this.addMovementDirection("barn", "Backyard");
		this.addMovementDirection("toolshed", "Backyard");
		this.addMovementDirection("tool", "Backyard");
		this.addMovementDirection("shed", "Backyard");
		this.addMovementDirection("corn", "Backyard");
		this.addMovementDirection("cornfield", "Backyard");
		this.addMovementDirection("backyard", "Backyard");
	}

	@Override
	protected void createItems() 
	{
		// TODO Auto-generated method stub
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
		this.gameState.addFlag("muddy ground", new Flag(false, "", "The ground around the basin is wet and muddy. "));
		this.gameState.addFlag("water in basin", new Flag(false, "", ""));
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
			
		case "drink":
			//===============================================================
			//Drink some of the water from the basin.
			//===============================================================
			if (command.unordered("water"))
			{
				if (this.gameState.checkFlipped("water in basin") == true)
					return new DisplayData("", this.decrementWaterLevel() + "Refreshing. ");
				else
					return new DisplayData("", "There isn't any water in the basin.");
			}

			//===============================================================
			//Drink command not recognized
			//===============================================================
			return new DisplayData("", "You don't see that here.");
			
		case "take":
			//===============================================================
			//Reject attempt to take mud
			//===============================================================
			if (command.unordered("mud"))
				return new DisplayData("", "Why would you want that?");

			//===============================================================
			//Take command not recognized
			//===============================================================
			return new DisplayData("", "Can't take that.");
			
		case "pump":
		case "use":
			//===============================================================
			//Use the pump to fill the basin with water.  When it overflows,
			//it creates mud around the basin.
			//===============================================================
			if (command.unordered("pump|water|handle"))
			{
				//===============================================================
				//Flip the flag indicating that the basin has water in it
				//===============================================================
				this.gameState.flipFlag("water in basin");

				//===============================================================
				//Increment the water, up to a value of 3
				//===============================================================
				if (this.waterLevel < 3)
					this.waterLevel++;

				//===============================================================
				//Set the messages for each level of water, and flip the flag
				//for muddy ground at water level 3
				//===============================================================
				if (this.waterLevel == 1)
					return new DisplayData("", "You fill the basin halfway. ");
				else
					if (this.waterLevel == 2)
						return new DisplayData("", "You fill the basin to the top. ");
						else
							if (this.waterLevel == 3)
							{
								this.gameState.flipFlag("muddy ground");
								return new DisplayData("", "The basin is overflowing.  Water is splashing over the edges, and turning the ground "
										+ "around the pump to fresh mud. ");
							}
			}
			
			//===============================================================
			//Intentionally allow case "use" to pass into fill/refill, if it
			//hasn't yet been caught.  This is to allow "use" to also be
			//caught by the code in fill/refill.
			//===============================================================
			
		case "put":
		case "fill":
		case "refill":
			//===============================================================
			//Use the water in the basin to turn the empty dasani bottle
			//into a full dasani bottle.
			//===============================================================
			if (command.unordered("bottle|water|dasani|empty"))
			{
				if (this.gameState.checkFlipped("water in basin") == true)
				{
					if (this.gameState.checkInventory("Dasani Bottle (Empty)") == true)
					{
						//===============================================================
						//Remove empty bottle from inventory
						//===============================================================
						this.gameState.removeFromInventory("Dasani Bottle (Empty)");

						//===============================================================
						//Create and add a new bottle of water to inventory
						//===============================================================
						Item refilledBottle = new BottleOfWater(this.gameState);
						this.gameState.addSpace(refilledBottle.getName(), refilledBottle);
						this.gameState.addToInventory(refilledBottle.getName());
						
						return new DisplayData("", this.decrementWaterLevel());
					}
					else
						return new DisplayData("", "Your bottle of water is already full.");
				}
				else
					return new DisplayData("", "There isn't any water in the basin");
			}

			//===============================================================
			//Use / Fill / Refill command not recognized
			//===============================================================
			return new DisplayData("", "Not sure what you're trying to do.");

		case "search":  //doing this will cause search to execute the look code
		case "look":
			//===============================================================
			//If look around, return descriptive.
			//If look at 'subject', return descriptive for that subject.
			//===============================================================
			if (command.unordered("around|area|room") || command.getSentence().contentEquals("search"))
				return new DisplayData("", "The forest looms large before you here, an inscrutable wall. "
						+ "You feel a bit of apprehension at the thought of entering the ancient wood, but you aren't sure why. "
						+ "From this angle, you can see a barn and what looks to be a tool shed behind the house, with a corn field stretching beyond. ");

			if (command.unordered("forest"))
				return new DisplayData("", "The edge of the forest is abrupt, immediately dense. It doesn't look like much light is getting "
						+ "through beneath the canopy.  The path continues on, cutting between the trees bravely. ");

			if (command.unordered("water|pump"))
				return new DisplayData("", "The old water pump looks to be in good repair.  It probably still works. ");

			if (command.unordered("basin"))
				return new DisplayData("", "A small tin trough.  Surprisingly, it looks clean, and in good repair. ");

			if (command.unordered("mud"))
			{
				//===============================================================
				//Change return message based on whether the ground is muddy
				//===============================================================
				if (this.gameState.checkFlipped("muddy ground") ==  true)
					return new DisplayData("", "The water has soaked into the soil around the basin, causing some mud. ");
				else
					return new DisplayData("", "You don't see any mud here. ");
			}

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
	
	/**
	 * Decrement the basin's water level, and return a String message to display.
	 */
	private String decrementWaterLevel()
	{
		//===============================================================
		//Decrement water level.  Either 3 or 2, both of which indicate
		//a full basin, reduce to 1, which is a half-full basin.
		//===============================================================
		if (0 < this.waterLevel)
			this.waterLevel = this.waterLevel/2;

		//===============================================================
		//Return message, based on water level.
		//===============================================================
		if (this.waterLevel == 0)
			return "You use some of the water. The basin is now empty. ";
		else
			if (this.waterLevel == 1)
				return "You use some of the water. The basin is now half full. ";

		//===============================================================
		//Blank return, to provide an always reachable return.
		//===============================================================
		return "";
	}

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String edgeOfForestImage = "++++//////////////////////////:::--::-----....``````````````````````         ``````..............`..``````````````````````````...`````.``````````````````````\r\n" + 
			"///////////////////////////////////////::--....````````````````````````````````   ``````````` ```      ````````````..........................................\r\n" + 
			"/////////////////////////////::://///////::::--...`````````````````````````````````````````    ``````````.............--------.............--........----....\r\n" + 
			"////////////////////////////:-.-:///////////::-.--................``````````````` `````````````````..........----------------------.......-----..........-:-.\r\n" + 
			"////////////////::---:::::/::----:////////////::::::::::::::------.......```````````.....``````.......-------------------------.........-----------...:.ohsso\r\n" + 
			"///////////////::--........-::/://////////////////////////////////:....``````````````...............----------------------------.........-------------//ssyys\r\n" + 
			"/::::::-...--...............----://////////////////////////////////:::--..``````````````````........---------:::::--------------.......-----------./+yyhsddhh\r\n" + 
			"/::-....``````````````.......``.....---:////////////////////////////////:-............``````````......------------------------------.....----.--::+yyyhshhhhd\r\n" + 
			"::::::-----.............````..``.......-:::/:::///////////////////////////::::::::-::---.....``````````...----------:------------:+:---:::-://+yyyhdhdhhddddm\r\n" + 
			"::::::::::::::----::-----.....``..........--::::://////////////////////////////////////::::------........---:::::::::::-::::---/oyhhysyhhhhyhyyyhhhdhhhhhhdhh\r\n" + 
			"::::::::::::::------:::--...`..............-....--:::::::///////////////////////////////////::///::::::::::::::/::::::+oyhshssyhhddhhdhdddddhhhhdddhhddhdhdmh\r\n" + 
			"::::::::::::::::::::::--..`````````````.`` ```````......-::://////////////////////////////////////////:...-/+--:/oooyyhddhdddhddmmhddddddddddhhddhhdmmmmmmdmm\r\n" + 
			"...--..--:-:::::::::-........````.....`````````````````...--:::::::/:-------:://///////////////////::::--+yyhhhhhddhhhhdhddmmmmdmmmhmdhyhhdddhhhhhdmmmdmmdddd\r\n" + 
			"`............```..------------.............................--:::::::-.``.`.....-://:://::::::///ooso//o+hhhhhddddddhhdmmmmdmmmdhhdddmdmmdhdddddhdmdmmdmdhdmdd\r\n" + 
			"...........```````````..---::::------------------------:::::::::::::::--.......--::-/:/soo+osyhohdddhhhddddmdhhdddhdddddddhhdddddmmdhmdmhhhddhdmmmmdmNmdmmddm\r\n" + 
			"......------.............---::-----:::::::::::::::::::::::::::::::::::::::-::/:/++:yhyhdhyhddddmmddddddddhdmmmdddhhdmmmmddmdddddmdhdyyddmdmmmmddddmmmmmdddhdd\r\n" + 
			"------------------------..------....--:::::--::::::::::::::::::::::::+/+/+yhhddyyhhhdddddhdddddddmmdmmmdhddmmmmmmdddmmmNNmmmddddhhhmmddddmNmmddmmdmmmNmmdmNmm\r\n" + 
			"---------------------------........-----:::::::::::::::::::/o/:osooshdhdddddddddhdddmdddddmmmmdddmmmmmdmmhmmdNNdhdhdhddmNmdddhhdhhdddmmdhhmddmmmmmmmmmmmmmmNm\r\n" + 
			"--------------------------------.......-----::-/o+++::/oysdhhdhhdhhhddmdmdmmmmmmmmmmmdmmmdmmmmdmmmddhdmNmmmmmdhhdhhhhdmmdmdddddhhhhddmmmmmhhhmmmmmmmmdddmdmhh\r\n" + 
			"-------------------------:-:::::///:/:////:/yyyhddhddddddddhhhddddddddddmmmNNmmmNmmmddmmmmmmmmmmmmmddhhdmmmdhhmmmdddhhmmdhdmdmdhdhdddmmmmmmmmmdddhddddmNmdyhm\r\n" + 
			"///////sso++/oyhhyyyyyhyyhydhddhdhhhhhhhdhhhyysydmmmmmdysyyyyyyyhddmmmmmmmmNNmdddmNmmmmmmmmmmNmdddmdmdmdddddmmmNmdddmmmmdhhhmmdddddddmmmNNNNNmNNmhmmmmddhyydd\r\n" + 
			"dmmmmmmmmmmdddddhdddddmmmmmdmmdddhyhydhhhhsyyssoossyhyssosyyyyhyshddmmmmmNmNNmmmddmdmmmmmNNNmmdmddmmNmNmmmdmmmmdmdmmmhmmmhdyhhhddhdmmNmmNNmNNmmmddmmdhyssyyhd\r\n" + 
			"mmmmmmmmmmmmdmmdmmmdmmmmmmddddhyyhyssyyyhssyhhssoddhd+osyyyhddyyyysyyyhmmmNmmmmmdmdmmmmmmddmddhhysyhhdmNNmdmNNNmmmmddmmmmdmmmddddmmdmNdmmNmmmNddhhyydddmyydmm\r\n" + 
			"NmNNmmNmmdhddmddddhysooshhdmmdssyyhhhyyyysyyhdhodms+hoshmdddmyosyyhhhyyydmmmmmddyososysyhhmmsoyyhhyyyyymmmdmmNNNNNmmmhdmNmmNNNmmmdhyyyyyhhddNmhddmddhysyyhmmN\r\n" + 
			"mmNNNNhhysssyhyyysyysyyyydhyyosssssyyyysshdhhyssdNNyy+hdmdhdsoyssssyyyysssyyyyysyhsyysyhyysssssssyyyyssdNddmmmmmNNNNNNmddmmmNmNmdddmdhhdhyyyyyyhyhddhyddddhdh\r\n" + 
			"dmmmmdyyyyyyyyhdossydhhhhhyyssshdddddhyyyyyyyhho+mNdhsyhysyyssydddddhyhyssyyydsssydyyhyhyysssyhhdddhddymmhhmmmmmNNNNNNNNmmddmNNmyyhdmmmmmhhyyyssssyyyhhdmhddd\r\n" + 
			"hyyyyyysssyhyyhyyhdhddyyyhyyyydNmdddsysssyhhyhy+ymNmdhssyyyyyhmNdddhysyyyhyydyshhhhdhyhhyyyyhmmddmmmdmmhddhdmdmmmmdmmmmmmmmmmmhys//ydmmdmmmdmhyhhyhyhhydmmmmm\r\n" + 
			"ysssssssysysssssyyysssssssssossssssooooo++ooo++/+dddNms+oooo+sosooosooo+ooooooosssssososssosyyysyhdhyyyyhyyyhhhysyyhddddhyo/-...-/sydhyhhhhyyhdddhdyhhyhdmmmm\r\n" + 
			"ooooooo+++++++++/////////////////////////////////doyNh//////////////////////////////////////////////////////////////+s+-.......-/oo+oo+ooyhyyyhhdddhdddhhyhdm\r\n" + 
			"////////////////////////////////////////////////sh/yNh/////////////////////////////////////////////////////////////:--.......-:////+::/++syyhddmmmmmmmmddhhhd\r\n" + 
			"///////////////////////////////////////////////od//dmd+//////////////////////////////////////////////////////////:-..........//////+o+:syyhddmmmdmmmmmmmmmmmm\r\n" + 
			"//////////////////////////////////////////////+d+//dNddy//////////////////////////////////////////////////////:-............-///////+yssyhdhddddhdmmmmNmmmNNN\r\n" + 
			"//////////////////////////////////////////////sh///yNs/yo////////////////////////////////////////////////////:..............://///////syhhhyhddhhhdmmmNNNmmNN\r\n" + 
			"//////////////////////////////////////////////sh///yNs/////////////////////////////////////////////////////:-...............//////////+yhhhhhdhdhhhddddmmmNNN\r\n" + 
			"//////////////////////////////////////////////+m///hNs+osssyyyyhhhyyyyso+////////////////////////////////:-................./////////////oyhyyhhhhyyhddddmmmm\r\n" + 
			"///////////////////////////////////////////////+//smmmmddddddddddddddddddmmds//////////////////////////:-...................//////////////+osyhhdmddhhsyysyhd\r\n" + 
			"/////////////////////////////////////////////////ommmNmmmdddddddddmmmmmmmmmmm+////////////////////////-.....................//////////////////++osssoosss+oos\r\n" + 
			"/////////////////////////////////////////////////+mmNmmmddddddddddhddddmmmmmm///////////////////////:-....................../////////////////////////+//////+\r\n" + 
			"//////////////////////////////////////////////++++mmmmmmmmmmmmmmmmNNNNNNNmmmh/++///////////////////:......................../////////////////////////////////\r\n" + 
			"////////////////////////////////////////////+++ooomNNNmmmNNNNNNNNNNNNNNNNNNNhooo+++//////////////:-........................./////////////////////////////////\r\n" + 
			"//////////////////////////////////////////+++oosssdmNNNNNNNNNNNNNNNNNNNNNNmmdssooo++++//////////:...........................:////////////////////////////////\r\n" + 
			"////////////////////////////////////////+++oooossyyddddmmmmmmmmmNNNNNmmddhhhyyssooooo++////////-............................:////////////////////////////////\r\n" + 
			"////////////////////////////////////////++ooooossyyhhhhhhhhdddddddddhhdhhhhhyyyssoooo++//////:-.............................-////////////////////////////////\r\n" + 
			"/////////////////////////////////////////++oooossyyyyhhhhhhhhhhhhhhhhhhhhhyyyyyssooo+++/////:...............................-////////////////////////////////\r\n" + 
			"//////////////////////////////////////////+++ooooooooosssssyyyyyyyyyysssssooooooo++++/////:-................................-////////////////////////////////";
}
