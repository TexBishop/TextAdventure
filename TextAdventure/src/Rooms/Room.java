/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

//=================================================================================
//Parameters for the ASCII art:
//   Base image needs to be 800 x 460 pixels in size
//   Use converter at https://www.text-image.com/convert/ascii.html
//   Convert at image width 157, black text, white background, do not
//   invert image or add extra contrast.
//=================================================================================

package Rooms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;
import Structure.GasLeak;
import Structure.InvalidMapKeyException;
import Structure.Space;

/**
 * Abstract Room class.  Contains movement controls between rooms.  Extend this to create a new room.
 * Room implements the Space class, which contains controls to update the interface with images and descriptions.
 */
public abstract class Room implements Space, Serializable
{
	private static final long serialVersionUID = 1L;

	//=================================================================================
	//String to display if the game won flag is flipped
	//=================================================================================
	protected final String gameWonMessage = "\n\nYou pull out your cellphone and dial a number. "
			+ "\"I believe I'm done here Anderson. I'll meet you out front.\" "
			+ "You put away the cell phone, feeling a sense of relief. ";

	//=================================================================================
	//The movementDirection hashmap stores a the keys to the connected rooms for this
	//object.  The innerSpace object is used for viewing Items while in this room.
	//=================================================================================
	protected Map<String, String> movementDirection = new HashMap<>();
	protected String movementRegex = "";
	protected Space innerSpace;
	
	private RoomFactory roomFactory;
	protected GameState gameState;
	protected String name;
	protected String image;
	protected String description;
	
	public Room(GameState gameState) 
	{
		this.roomFactory = new RoomFactory(gameState);
		this.gameState = gameState;
		
		this.setName();
		gameState.addSpace(this.name, this);
		
		this.createItems();
		this.createFlags();
		this.createMovementDirections();
	}
	
	/**
	 * Set room name.
	 */
	protected abstract void setName();
	/**
	 * Creates the movement between Rooms for this Space.
	 */
	protected abstract void createMovementDirections();
	/**
	 * Creates the custom Items for this space.
	 */
	protected abstract void createItems();
	/**
	 * Creates the custom flags for this space.
	 */
	protected abstract void createFlags();
	
	@Override
	public DisplayData handleDisplayData(Command command)
	{
		DisplayData displayData;
		//=================================================================================
		//Handle the gas leak.
		//If the current room is part of the gas leak, handle cases where an inventory
		//item ignites the gas.
		//=================================================================================
		this.gameState.handleGasLeak();
		if (this instanceof GasLeak)
		{
			String message = ((GasLeak) this).handleInventorySparks(this.gameState, command);
			if (message != null)
				return this.gameState.death(message);
		}
		
		//=================================================================================
		//If innerSpace is null, check it to see if it needs to be set
		//=================================================================================
		if (this.innerSpace == null)
		{
			displayData = this.checkInnerSpace(command);
			
			if (displayData != null)
			{
				//=================================================================================
				//If the current room is part of the gas leak, add the leak message
				//=================================================================================
				if (this.gameState.getCurrentRoom() instanceof GasLeak)
					displayData.appendToDescription(((GasLeak) this.gameState.getCurrentRoom()).getGasMessage(gameState));
				
				//=================================================================================
				//Check if the game won flag was flipped on this command
				//=================================================================================
				this.checkGameWon(displayData);
				return displayData;
			}
		}
		
		//=================================================================================
		//If the current room's innerSpace is not null, handle the command
		//execution in the Item that innerSpace is pointing to.
		//=================================================================================
		if (this.innerSpace != null)
		{
			displayData = this.innerSpace.handleDisplayData(command);
			
			//=================================================================================
			//If the current room is part of the gas leak, add the leak message
			//=================================================================================
			if (this.gameState.getCurrentRoom() instanceof GasLeak)
				displayData.appendToDescription(((GasLeak) this.gameState.getCurrentRoom()).getGasMessage(gameState));

			//=================================================================================
			//Check if the game won flag was flipped on this command
			//=================================================================================
			this.checkGameWon(displayData);
			
			return displayData;
		}
		else
		{
			displayData = this.executeCommand(command);

			//=================================================================================
			//If the current room is part of the gas leak, add the leak message
			//=================================================================================
			if (this.gameState.getCurrentRoom() instanceof GasLeak)
				displayData.appendToDescription(((GasLeak) this.gameState.getCurrentRoom()).getGasMessage(gameState));

			//=================================================================================
			//Check if the game won flag was flipped on this command
			//=================================================================================
			this.checkGameWon(displayData);
			
			return displayData;
		}
	}
	
	protected DisplayData move(Command command)
	{
		if (this.gameState.checkSpace(this.getMovementDirectionRoom(command.getMatch(this.movementRegex))) == false)
			this.roomFactory.makeRoom(this.getMovementDirectionRoom(command.getMatch(this.movementRegex)));
		
		return this.gameState.setCurrentRoom(this.getMovementDirectionRoom(command.getMatch(this.movementRegex)));
	}
	
	/**
	 * Add a new movement direction and the Room it leads to, to the hashmap.
	 * @param direction String The direction to move.
	 * @param roomKey   String The key to the room the movement direction leads to.
	 */
	protected void addMovementDirection(String direction, String roomKey)
	{
    	//===============================================================
		//If the given String already exists as a key in the hashmap,
		//throw an InvalidMapKeyException
    	//===============================================================
		try 
		{
			if (this.movementDirection.containsKey(direction) == false)
			{
				this.movementDirection.put(direction, roomKey);
				
		    	//===============================================================
				//Build a regex of the movement direction keys for matching purposes
		    	//===============================================================
				if (this.movementRegex.isEmpty())
					this.movementRegex += direction;
				else
					this.movementRegex += "|" + direction;
			}
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Invalid addMovementDirection(), key already exists.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the Room which a given movement direction leads to, from the movementDirection hashmap.
	 * @param direction String The direction to move, used as a key to retrieve room information.
	 * @return          String The key to the room the given direction key leads to.
	 */
	protected String getMovementDirectionRoom(String direction)
	{
    	//===============================================================
		//If the given String does not exist as a key in the hashmap,
		//throw an InvalidMapKeyException
    	//===============================================================
		try 
		{
			if (movementDirection.containsKey(direction) == true)
			{
				return movementDirection.get(direction);
			}
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Invalid getMovementDirectionRoom(), key does not exist.");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Check whether a movement direction exists in the hashmap for this room.
	 * @param direction String The key for the movement direction.
	 * @return          String The boolean result.
	 */
	protected boolean checkMovementDirection(String direction)
	{
    	//===============================================================
		//If the given String does exists as a key in the hashmap, return
		//true, else return false;
    	//===============================================================
		if (movementDirection.containsKey(direction) == true)
			return true;
		return false;
	}
	
	/**
	 * Check to see if innerSpace needs to be set due to a look at an inventory object.
	 * If yes, set it and return the display data.  If no, return null.
	 * @param command  Command      The command to check.
	 * @return         DisplayData  An object representing the data to display.
	 */
	public DisplayData checkInnerSpace(Command command)
	{
		//===============================================================
		//If the command contains the inventory or my keywords, check
		//the command for a match for an inventory item.  If one is found,
		//set it as innerSpace, and run the command through it.
		//===============================================================
		if (command.unordered("inventory|my") == true)
		{
			if (this.gameState.checkItemSearch(command.getMatch(this.gameState.getInventoryRegex())) == true)
			{
				String key = this.gameState.getItemKey(command.getMatch(this.gameState.getInventoryRegex()));
				try 
				{
					if (this.gameState.checkSpace(key) == true)
					{
						this.innerSpace = this.gameState.getSpace(key);
						return this.innerSpace.handleDisplayData(command);
					}
					else
						throw new InvalidMapKeyException();
				} 
				catch (InvalidMapKeyException e) 
				{
					System.out.println("The Space you are attempting to view doesn't exist.");
					e.printStackTrace();
				}
			}
			else
				return new DisplayData("", "Item not found in inventory.");
		}
		
    	//===============================================================
		//If the Space does not exist, or the command is not for looking
		//at an inventory object, return null
    	//===============================================================
		return null;
	}
	
	/**
	 * Sets the inner space, then returns display data.
	 * @param key  String The key for the Space you wish to view.
	 * @return     DisplayData An object representing the data to display.
	 */
	public DisplayData setInnerSpace(String key)
	{
		try 
		{
			if (this.gameState.checkSpace(key) == true)
			{
				this.innerSpace = this.gameState.getSpace(key);
				return this.innerSpace.displayOnEntry();
			}
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("The Space you are attempting to view doesn't exist.");
			e.printStackTrace();
		}
		
    	//===============================================================
		//If the Space does not exist, return a blank DisplayData object.
    	//===============================================================
		return new DisplayData("", "");
	}
	
	/**
	 * Return the value of innerSpace to null.
	 */
	public void resetInnerSpace()
	{
		this.innerSpace = null;
	}
	
	/**
	 * Check to see if an inventory item was referenced in the command.  If yes, set it as innerSpace and run the command through it.
	 * @param command  String       The command to check.
	 * @return         DisplayData  The result of the command.  Null if a reference to an item was not found in the command.
	 */
	protected DisplayData inventoryTest(Command command)
	{
		if (command.unordered(this.gameState.getInventoryRegex()) == true)
		{
			if (this.innerSpace == null)
			{
				//===============================================================
				//If innerSpace is null, set it to the referenced item.
				//===============================================================
				this.innerSpace = this.gameState.getSpace(this.gameState.getItemKey(command.getMatch(gameState.getInventoryRegex())));
				return this.innerSpace.handleDisplayData(command);
			}
			else
			{
				//===============================================================
				//If innerSpace is not null, check to see if the referenced item
				//is already set as innerSpace.  If it is not, change innerSpace's
				//value to the referenced item.
				//===============================================================
				if (Objects.equals(this.innerSpace, this.gameState.getSpace(this.gameState.getItemKey(command.getMatch(gameState.getInventoryRegex())))) == false)
				{
					this.innerSpace = this.gameState.getSpace(this.gameState.getItemKey(command.getMatch(gameState.getInventoryRegex())));
					return this.innerSpace.handleDisplayData(command);
				}
			}
		}
		return null;
	}
	
	/**
	 * Check to see if the game won message needs to be printed.  If it does, append it to the display data.
	 * @param displayData  DisplayData The diplay data object we will edit if needed.
	 * @return             DisplayData The resulting display data object.
	 */
	protected DisplayData checkGameWon(DisplayData displayData)
	{
		if (this.gameState.checkFlipped("game won") == true && this.gameState.checkFlipped("game won message printed") == false)
		{
			this.gameState.flipFlag("game won message printed");
			displayData.appendToDescription(this.gameWonMessage);

	    	//===============================================================
			//If the current room is the Old Farmhouse room, set game as
			//ended, and output the game ended description
	    	//===============================================================
			if (this.gameState.getCurrentRoom().equals(this.gameState.getSpace("Old Farmhouse")))
			{
				this.gameState.flipFlag("game ended");
				displayData.appendToDescription("\n\n" + this.fullDescription());
				
				//=================================================================================
				//Change the display image to the current room's display image
				//=================================================================================
				displayData = new DisplayData(this.gameWon, displayData.getDescription());
			}
		}
		
		return displayData;
	}

	//===============================================================
	//Getters
	//===============================================================
	public Space getInnerSpace()
	{
		return this.innerSpace;
	}
	
	public String getName()
	{
		return this.name;
	}

	//===============================================================
	//Game won ASCII image
	//===============================================================
	protected final String gameWon = "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM+//oMMM///yMMNs/:-:/sNMMm///dMMd///dMMMMM///hMM+//hMM+//ym://yM:-./NMMM/--oMs//sMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-  :MMN`  +Mo   `-`   oMd   hMMh   dMMMMM`  sMM.  oMM.  +m   sM.   .dMM-  +M+  /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMy`  +m:  `ds  `hMMMh`  sm   hMMh   mMMMMN`  sMM`  oMM.  oN   yM-     oM:  oM/  sMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMm.     -mM/  :MMMMM:  /N   dMMd   mMMMMMh  `ms   `mo  :MN   hM:  +:  -.  sMh `NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN:   oMMMh   omNd+   hm   yMMy   mMMMMMMy  `  -  .  -NMm   yM.  oMy`    sMMosMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMs   mMMMMh-       -hMM.   ``   -MMMMMMMMy   .Ns   -NMMd   sM`  +MMm:   sM+  oMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMdyyyNMMMMMMmysosymMMMMMhsooosydMMMMMMMMMMhyymMMyyyNMMMNyyymMyyydMMMMyssmMNssNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMdyssydMMMMMMMMMMMmyssshNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMy-:oooo/-oMMMMMMMm::+ooo/-/NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMdNMMMMMMMdNMMMMMMmmMMMMMMMmmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMm-sNMMMMMMMMMMh:sMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMy` -/+oooo+:` /NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMy+-`    .:omMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM";
}
