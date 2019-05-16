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

public class PuzzleCube extends Item 
{
	private static final long serialVersionUID = 1L;

	public PuzzleCube(GameState gameState) 
	{
		super(gameState);
		this.regex = "puzzle|cube|puzzlecube|puzzle cube";
		this.image = this.cubeImage;
	}

	@Override
	protected void setName() 
	{
		this.name = "Puzzle Cube";
		this.gameState.addItemSynonyms(this, "puzzlecube", "puzzle cube", "puzzle", "cube");
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		//=================================================================================
		//Called when item is viewed.
		//=================================================================================
		return new DisplayData(this.cubeImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "It's a small cube, slightly larger than a typical rubik's cube. It looks valuable. "
				+ "All sides are a glossy black, covered with intricate gold leaf designs and filigree. "
				+ "One side is a diamond pattern, one a star pattern, one a square pattern, one a cog pattern, "
				+ "one a trianglular pattern, and the last a cross pattern. "
				+ "In the center of each pattern is a black circle, all of uniform size, except for the star pattern and the cross pattern. "
				+ "The center of the star pattern is larger than the others, and the center of the cross pattern is smaller. ";

		//===============================================================
		//If the cog is extruded, add to description
		//===============================================================
		if (this.gameState.checkFlipped("star pressed") == true)
			this.description += this.gameState.getFlag("cog pressed").toString();
		
		return this.description;
	}

	@Override
	protected void createItems() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createFlags() 
	{
		this.gameState.addFlag("star pressed", new Flag(false, "", ""));
		this.gameState.addFlag("cog turned", new Flag(false, "", ""));
		this.gameState.addFlag("cog pressed", new Flag(false, "There is now a circular cog wheel sticking out of the cube. ", ""));
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
		case "push":
		case "press":
			//===============================================================
			//If the player presses the center circle on one of the sides.
			//Provide cases for either the shape then circle, or circle
			//then shape, in the command.
			//===============================================================
			if (command.unordered("center|circle", "cross|cog|diamond|square|triangle|triangular|star") ||
				command.unordered("small|large", "center|circle"))
			{
				//===============================================================
				//If the wrong side is used, or the correct side is used, but at
				//the wrong time.
				//===============================================================
				if (command.unordered("cross|cog|diamond|square|triangle|triangular|small") ||
				    (command.unordered("star|large") && this.gameState.checkFlipped("star pressed") == true))
					return this.gameState.death("You press in the center circle. There's a click. "
							+ "You feel a pain in your finger, that lances up your arm, straight into your head. "
							+ "The pain intensifies, spreading throughout your body. "
							+ "As you slump to the ground, you see flame erupting from within your flesh, everywhere. "
							+ "Such excruciating pain... ");

				//===============================================================
				//If the correct side is used, at the correct time
				//===============================================================
				if (command.unordered("star|large") && this.gameState.checkFlipped("star pressed") == false)
				{
					this.gameState.flipFlag("star pressed");
					return new DisplayData("", "The circle depresses inward. On the opposite side, the cog begins to extrude. "
							+ "There is now a circular cog wheel sticking out of the cube. ");
				}
			}

			//===============================================================
			//If the player presses the cog after turning the cog, complete
			//the puzzle
			//===============================================================
			if (command.unordered("cog") && this.gameState.checkFlipped("cog turned") == true)
			{
				if (this.gameState.checkFlipped("cog pressed") == false)
				{
					this.gameState.flipFlag("cog pressed");
					this.gameState.flipFlag("cube solved");
					return new DisplayData("", "You press the cog back down into the cube. "
							+ "You hear a click. You feel a sudden rush of something from the cube into you, a small shock. "
							+ "You've just remembered something. Yes. You remember this place now. ");
				}
			}

			//===============================================================
			//Press command not recognized
			//===============================================================
			return new DisplayData("", "That doesn't do anything. ");
			
		case "crank":
		case "rotate":
		case "turn":
			//===============================================================
			//If the player turns the cog
			//===============================================================
			if (command.unordered("cog"))
			{
				//===============================================================
				//Verify that the cog is extruded
				//===============================================================
				if (this.gameState.checkFlipped("star pressed") == true && this.gameState.checkFlipped("cog pressed") == false)
				{
					//===============================================================
					//If the player turns it the wrong direction
					//===============================================================
					if (command.unordered("left|counterclockwise|counter clockwise"))
						return this.gameState.death("You turn the cog counter-clockwise, until you hear a click. It stops turning. "
								+ "Your hand suddenly goes numb, then your arm, then the rest of your body, all in quick succession. "
								+ "Your flesh begins wither away before your eyes, and you slowly lose conciousness. ");

					//===============================================================
					//If the player turns it the correct direction
					//===============================================================
					if (command.unordered("right|clockwise"))
					{
						this.gameState.flipFlag("cog turned");
						return new DisplayData("", "You turn the cog clockwise, until you hear a click. It stops turning. ");
					}

					//===============================================================
					//If the player doesn't specify which direction to turn it
					//===============================================================
					return this.gameState.death("Not sure which direction to turn it, you turn it counter-clockwise, until you hear a click. "
							+ "It stops turning. "
							+ "Your hand suddenly goes numb, then your arm, then the rest of your body, all in quick succession. "
							+ "Your flesh begins wither away before your eyes, and you slowly lose conciousness. ");
				}
			}
			
			//===============================================================
			//turn command not recognized
			//===============================================================
			return new DisplayData("", "Unable to do that. ");
			
		case "solve":
			//===============================================================
			//If the player tries to solve the puzzle indirectly
			//===============================================================
			return new DisplayData("", "You aren't sure how to do that. ");
			
		case "look":
			if (command.unordered(this.regex))
				return this.displayOnEntry();
			
		default: 
			//===============================================================
			//Pass the current command to the room commands when default is reached.
			//===============================================================
			return this.gameState.getCurrentRoom().executeCommand(command);
		}
	}
	
	private final String cubeImage = ".............................................................................................................................................................\r\n" + 
			"..........................................................................----...............................................................................\r\n" + 
			"...................................................................-----.--/::::.-----.......................................................................\r\n" + 
			"..............................................................---::++/oyos+o/syyo+++:/:-:.----...............................................................\r\n" + 
			".........................................................----::o-++osyssssoooosyyyy++/-//::-:/:---:---.......................................................\r\n" + 
			"....................................................---/:+//+:+o///osssyhhh+o/osoooysoo+///--://+oso/+::::---................................................\r\n" + 
			"...............................................--:///o+ooooo+/ooshhyyhhosso+/+-+/oosyyyyhhhs++sssoyhhdos+:-//://-:---........................................\r\n" + 
			"..........................................--:///+o/ssshyyssyysyo++:+y+so-------------/yo+o+o+oo/o+o+++sy+://+/:/:osyyyo......................................\r\n" + 
			"......................................-/oo+:/+/o:+o++y+//://+yo/++-+ssys+/---------:/oyoyyysyo+sso+sshyyooo//oshhddmmms......................................\r\n" + 
			".....................................-mNNNNmdhhsoo+oossyyshhhhyyyssossyshhddhhy/soddhhhhysss////+///++///syydddmmmhho++......................................\r\n" + 
			".....................................-mNdmNNNNNmmmmddhy+++++//+/+/+o+++shyyyyyhsyhyhdh+syoo/-/o+-/o+o+yhhddmmNdho++++/:......................................\r\n" + 
			"......................................mhdhmmdddmmNNMNNmNNdhyyo+///+os::osohyhy+hso/yyy+shysss+/+/oyyhdddmNmhy/o+sshmo+-......................................\r\n" + 
			"......................................dhdmdmNydNmdhNhmmNNMNNNNNmdysss/:ososyhyyosos++oyyys++s+oshdddmNmhs+o+/oydmmmNo+-......................................\r\n" + 
			"......................................hdhhmmhdmdhmddddNNmmdmNmNMMNNmmmdhyyy+/+o/o++/:+///+ssydmmmmNdho+o+oyo+smmmmmN+:-......................................\r\n" + 
			"......................................hdmhddmhyhddhyNhNhmhdhdymmmmmmNMMMNNNNmdhyso+:+/sohhmmmNNdhh+oooo/ymNsoommmmmN/+-......................................\r\n" + 
			"......................................ydNNddymhhhyyhdNhyhdhdNhdMMMMhhmhmmmNNMMMNNNmmdddddmNmhs+s+/+odm+/+ydh+/smmmmm:/.......................................\r\n" + 
			"......................................sdNdyhdhdmhdddhNmhhmddNhhhMMddNhmdNMNmmdddmNMMMNmNmhooyoosh/oyNo++/+/y++/oyhhs//.......................................\r\n" + 
			"......................................smhhhhhhhdmdhhhhmhmhddmyhhmmdMdyddhdmhhddddmMNMNNh//ooydmmm:/ms//+/+/:/://+///+/.......................................\r\n" + 
			"......................................odhmdhdhyyhmhhhhdNhdddmhhyhhNmhmmddddNhydNNhymMNNs/smmmmmNssos:::+/+::/::+++so/:.......................................\r\n" + 
			"......................................+hmmmmmhyhmddmmdNNNdmNNmdyymmhhdydydhhmMmhdNmmMNNoosmmmmmd+++//:::/o::+://ydms:-.......................................\r\n" + 
			"......................................+dNMNmdmmhdhdMmysssssssymmNNhmdmhhddmmdhdmmNmmMNNo+ymmmmN+////::::s::-:::::+hso-.......................................\r\n" + 
			"....................................../dmmdhhyyhmNmsosssssssssmsymmhddhmNmdyhhhmNhymMNN+oymmmNoo:://:-/:::/+/:-/:::/o-.......................................\r\n" + 
			"--------------------------------------/mhhhyyyyydNyoooossossoshssssmmmmdhdhhymmyMMhmMNNoohmmmoo+o:--::oooo:-:-::::y+:----------------------------------------\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdmhdmmmdyhmmyysoooooooossossssdmhydyyhhhhhNMdmMNNo+hmyo///++-::smmmy-::-+:-sN//hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmddyhyhydNdoosssoooooosoooosssymhydyyhyhhdhdNMNNo++/++s::::--ymmmmd/+-:/-omy:+hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmdmhyyyyyddooooooooooooooooooosdNmmdNNmmmdddNMNN/:+/sh::+o+-/mmmmmo--::/os/+//hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmhmhhyyhyhNo+++++oooooooooooooosNmhddhhyhmmhNMNmo:ymd::/--:+ymmmmy/--/::/+/s:+hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmdhmmmmdyhdm+++++ooooooooooooooshmyhyhhhhhMdNMNm++dd:::---/-odmmo----+--:sdh:shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmyhyyyyyoyshh++++ooooooooossssoohMmdyhyhyhmhNMNm++h://----+:/::+-:o----+hdmy/+hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmyNhhhsyyyhsNdo/++os++ooooooosyhdMydhmmNmddhNNmd:-:-///s:--::-//-:/-++:hddmsoohhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmydNyhdyyhdhdhmo+++y++oooooooooyNmhhyhyydddmMMmd++hs/--/-----:o----/::ydddms/shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmhmdmhyhmmhsdmyhmysh++++oooooosNNmmyyhhhdmNyMMmh//dmds:-/://::--:-:y/oddddy:/hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmhNmdmNdyhhhyyhsNhmmhso+oooshmNhhdddmdmNMMMyNMmho/yo///-:-:-::/o-:d+-ydyo/+/shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmhdNmdhmddhddhyNyssyhNmhmNNdhhdddmhyydmmmNMyNMmho/o/-++:/-/--/---hm-/o:/:+yhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdNmhdhdhmdhhhhNdhhyyymsyyymyysyymysysdshmNhyMMmy::oyyys://sy+:--yho:/:sshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdddmmmNNNNdhmmhNNsyyMsyyysmsssyydyyyyhyyhdNMMNho/mmmmmd++:ddh:/://oyhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdddmmmmyNMNmyyMsyyooddshsyohhoyhsydmyMMNh++mmmmmms-+hy+.-oyhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdddmmmmhymssmsssms+ossoodssydMNyMNmy/+mmmmmdy/:-:/shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdddhNyydmNdsm+syo++ohsy+hmsMNNy++mmmdy/:-+yhdhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdddhyhoyshdhdhyoyh+oysNNms/+my+//+sydhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhdddmshhy+sNyod+ssNNmo::o//sydhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhddhyyyo/ssoNNmo-:+sdhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhsmdNNmhydhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhddddhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\r\n" + 
			"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
}
