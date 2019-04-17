/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
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

import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;
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
	//The movementDirection hashmap stores a the keys to the connected rooms for this
	//object.  The innerSpace object is used for viewing Items while in this room.
	//=================================================================================
	Map<String, String> movementDirection = new HashMap<>();
	protected Space innerSpace;
	
	protected GameState gameState;
	protected String name;
	protected String image;
	protected String description;
	
	public Room(GameState gameState) 
	{
		this.gameState = gameState;
		
		this.setName();
		gameState.addSpace(this.name, this);
		
		this.createMovementDirections();
		this.createItems();
		this.createFlags();
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
		//=================================================================================
		//If innerSpace is null, check it to see if it needs to be set
		//=================================================================================
		if (this.innerSpace == null)
		{
			DisplayData displayData = this.checkInnerSpace(command);
			if (displayData != null)
				return displayData;
		}
		
		//=================================================================================
		//If the current room's innerSpace is not null, handle the command
		//execution in the Item that innerSpace is pointing to.
		//=================================================================================
		if (this.innerSpace != null)
			return this.innerSpace.handleDisplayData(command);
		else
			return this.executeCommand(command);
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
			if (movementDirection.containsKey(direction) == false)
				movementDirection.put(direction, roomKey);
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
		//If the command is to look at an item in inventory, do so, and
		//set the value of the current rooms innerSpace to the Item's space.
		//===============================================================
		/*if (command.getVerb().contentEquals("look") == true && command.getSubject().contentEquals("inventory") == true)
		{
			if (this.gameState.checkItemSearch(command.getTarget()) == true)
			{
				String key = this.gameState.getItemKey(command.getTarget());
				if (this.gameState.checkInventory(key) == true)
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
				}
				else
					return new DisplayData("", "Item not found in inventory.");
			}
			else
				return new DisplayData("", "Item not found in inventory.");
		}*/
		//===============================================================
		//If the command is to look at an item in inventory, do so, and
		//set the value of the current rooms innerSpace to the Item's space.
		//===============================================================
		if (command.getSubject().contentEquals("inventory") == true || command.getTarget().contentEquals("inventory") == true)
		{
			if (this.gameState.checkItemSearch(command.getTarget()) == true || this.gameState.checkItemSearch(command.getSubject()) == true)
			{
				String key;
				if (this.gameState.checkItemSearch(command.getTarget()) == true)
					key = this.gameState.getItemKey(command.getTarget());
				else
					key = this.gameState.getItemKey(command.getSubject());
					
				if (this.gameState.checkInventory(key) == true)
				{
					try 
					{
						if (this.gameState.checkSpace(key) == true)
						{
							this.innerSpace = this.gameState.getSpace(key);
							if (command.getVerb().contentEquals("look") == true)
								return this.innerSpace.displayOnEntry();
							else
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
}