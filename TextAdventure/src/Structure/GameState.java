/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Structure;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Items.Item;
import Rooms.CountdownRoom;
import Rooms.Room;

/**
 * The GameState class is in effect the memory of the game.  It keeps track of all variables
 * important to progression, and is the object that is saved and loaded.
 */
public class GameState implements Serializable
{
	//===============================================================
	//Instance variables.  serialVersionUID needed for saving objects
	//to file, using serialization.
	//===============================================================
	private static final long serialVersionUID = 1L;
	protected Map<String, Flag> flagMap = new HashMap<>();
	protected Map<String, Space> spaceMap = new HashMap<>();
	private Map<String, Item> inventory = new HashMap<>();
	private Map<String, String> itemSearchMap = new HashMap<>();
	private CommandParser commandParser = new CommandParser();
	private Space currentRoom;
	
	public GameState()
	{
		//default constructor
	}

	//===============================================================
	//Current room methods
	//===============================================================
	
	/**
	 * Current room getter
	 * @return Room The current room.
	 */
	public Space getCurrentRoom()
	{
		return this.currentRoom;
	}
	
	/**
	 * Sets the current room, then displays.
	 * @param key  String The key for the room you wish to move to.
	 * @return     DisplayData An object representing the data to display.
	 */
	public DisplayData setCurrentRoom(String key)
	{
		try 
		{
			if (this.spaceMap.containsKey(key) == true)
			{
		    	//===============================================================
				//If the room we are leaving is of type CountdownRoom, reset 
				//the timer before leaving.  This prevents death from occurring
				//on an action to leave the room.
		    	//===============================================================
				if (this.currentRoom instanceof CountdownRoom)
					((CountdownRoom)this.currentRoom).resetTimer();
				
				//===============================================================
				//Set the current room.  If the new room is a CountdownRoom,
				//reset the timer to 0.  This ensures the room will start with
				//the timer at 0.
				//===============================================================
				this.currentRoom = this.spaceMap.get(key);
				
				if (this.currentRoom instanceof CountdownRoom)
					((CountdownRoom)this.currentRoom).resetTimer();
				
				return this.currentRoom.displayOnEntry();
			}
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("The Room you are attempting to move to doesn't exist.");
			e.printStackTrace();
		}
		return new DisplayData();
	}

	//===============================================================
	//Methods which are used to transmit image and description 
	//data to the GUI.
	//===============================================================
	
	/**
	 * Parse the typed command string, execute it, and return the results.
	 * @param commandString  String The command the user typed.
	 * @return               DisplayData An object representing what the UI should display.
	 */
	public DisplayData parseCommand(String commandString)
	{
		//===============================================================
		//Parse the typed command, and turn it into a Command object.
		//===============================================================
		Command command = this.commandParser.parse(commandString);

		//===============================================================
		//Determine and return display data
		//===============================================================
		return this.currentRoom.handleDisplayData(command);
	}
	
	/**
	 * Enacts the death game state.
	 * @param message String The death message to display.
	 * @return Display An object representing the data to display.
	 */
	public DisplayData death(String message)
	{
		return new DisplayData(deathImage, message);
	}

	//===============================================================
	//Flag access and control methods
	//===============================================================
	
	/**
	 * Add a flag to the flag hashmap.
	 * @param gameState GameState The current GameState object the application is working from.
	 * @param name String The key to assign the flag to in the hashmap.
	 * @param flag Flag The flag to put into the hashmap.
	 */
	public void addFlag(String name, Flag flag)
	{
    	//===============================================================
		//If the given String already exists as a key in the hashmap,
		//throw an InvalidMapKeyException
    	//===============================================================
		try 
		{
			if (this.flagMap.containsKey(name) == false)
				this.flagMap.put(name, flag);
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Invalid addFlag(), key already exists");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a Flag from the Flag hashmap.
	 * @param key  String The key to the wanted Flag object.
	 * @return     boolean The requested Flag object.
	 */
	public Flag getFlag(String key)
	{
		try 
		{
			if (flagMap.containsKey(key) == true)
				return flagMap.get(key);
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Flag doesn't exist in flagMap");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Check to see if the flag has been flipped.
	 * @param key  String The key to the Flag needing checked.
	 * @return     boolean False if the flag has not been flipped.
	 */
	public boolean checkFlipped(String key)
	{
		try 
		{
			if (flagMap.containsKey(key) == true)
				return flagMap.get(key).isFlipped();
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Flag doesn't exist in flagMap");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Toggle the value of the Flag.
	 * @param key  String The key to the Flag to toggle.
	 */
	public void flipFlag(String key)
	{
		try 
		{
			if (flagMap.containsKey(key) == true)
				flagMap.get(key).flipToggle();
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Flag doesn't exist in flagMap");
			e.printStackTrace();
		}
	}

	//===============================================================
	//Space access and control methods
	//===============================================================
	
	/**
	 * Get a Space value from the Space hashmap.
	 * @param key  String The key to the wanted Space object.
	 * @return     boolean The requested Space object.
	 */
	public Space getSpace(String key)
	{
		try 
		{
			if (spaceMap.containsKey(key) == true)
				return spaceMap.get(key);
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Space doesn't exist in spaceMap");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Add the passed Space into the GameState's Space hashmap.
	 * @param name  String The name that is used as the key in the hashmap.
	 * @param space Space The space that is being added to the hashmap.
	 */
	public void addSpace(String key, Space space)
	{
		try 
		{
			if (this.spaceMap.containsKey(key) == false)
				this.spaceMap.put(key, space);
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Space already exists in spaceMap.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Check to see if a Space is already in the hashmap.
	 * @param key  String The key to the Space needing checked.
	 * @return     boolean The result.
	 */
	public boolean checkSpace(String key)
	{
		if (spaceMap.containsKey(key) == true)
			return true;
		
		return false;
	}

	//===============================================================
	//Inventory access and control methods
	//===============================================================
	
	/**
	 * Add an item to the inventory.
	 * @param name String The key in the hashmap of the item to add, which should be the item name.
	 */
	public void addToInventory(String name)
	{
		//===============================================================
		//Attempt to add an item to the inventory.  try/catch to make sure
		//the item exists, that the object the key points to is actually
		//an item, and that the item isn't already in inventory.
		//===============================================================
		try 
		{
			if (this.spaceMap.containsKey(name) == true)
			{
				if (this.inventory.containsKey(name) == false) 
				{
					if (this.spaceMap.get(name) instanceof Item)
						this.inventory.put(name, (Item) this.spaceMap.get(name));
					else
						throw new RuntimeException();
				}
				else
					throw new ArithmeticException();
			}
			else
				throw new NullPointerException();
		} 
		catch (NullPointerException e)
		{
			System.out.println("Error in addToInventory(). Item does not exist.");
			e.printStackTrace();
		}
		catch (ArithmeticException e)
		{
			System.out.println("Error in addToInventory(). Item already exists in inventory.");
			e.printStackTrace();
		}
		catch (RuntimeException e) 
		{
			System.out.println("Error in addToInventory(). Was not of type Item");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns inventory as a printable list.
	 * @return String The text list representation of inventory.
	 */
	public String getInventory()
	{
		//===============================================================
		//Convert the inventory hashmap into a printable String.
		//If the hashmap is empty, avoid parsing it and set the String
		//to display "empty".
		//===============================================================
		String inventoryList = "";
		if (this.inventory.isEmpty() == false) 
		{
			for (Map.Entry<String, Item> entry : this.inventory.entrySet()) 
			{
				//===============================================================
				//If the item has a value stored in state, append it to the 
				//item name.
				//===============================================================
				inventoryList += entry.getValue().getName();
				if (entry.getValue().getState() != null)
					inventoryList += " " + entry.getValue().getState();
				inventoryList += "\n";
			} 
		}
		else
			inventoryList += "Empty";
		
		return inventoryList;
	}
	
	/**
	 * Check to see if an item is in inventory.
	 * @param key  String The key to check.
	 * @return     boolean The result.
	 */
	public boolean checkInventory(String key)
	{
		return this.inventory.containsKey(key);
	}
	
	/**
	 * Remove an item from inventory.
	 * @param key  String The key to check.
	 */
	public void removeFromInventory(String key)
	{
		this.inventory.remove(key);
		this.spaceMap.remove(key);

		//===============================================================
		//Remove all entries in itemSearchMap that contain links to this
		//item.
		//===============================================================
		this.itemSearchMap.values().removeAll(Collections.singleton(key));

		//===============================================================
		//If innerSpace is currently set to this deleted item, reset
		//innerSpace.
		//===============================================================
		if (((Room)this.currentRoom).getInnerSpace() != null)
			((Room)this.currentRoom).resetInnerSpace();
	}
	
	/**
	 * Add alternate keywords as names for an item that can be used in commands.
	 * @param name   String The name / key of the item.
	 * @param words  String The words to use as synonyms.
	 */
	public void addItemSearch(String name, String ...words)
	{
		for (String word : words)
		{
			try 
			{
				if (this.itemSearchMap.containsKey(word) == false)
					this.itemSearchMap.put(word, name);
				else
					throw new InvalidMapKeyException();
			} 
			catch (InvalidMapKeyException e) 
			{
				System.out.println("Inventory Item synonym " + word + " already exists in the hashmap for Item " + this.itemSearchMap.get(word));
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Check to see if an item has a synonym matching the given String.
	 * @param key  String The word to check.
	 * @return     boolean The result.
	 */
	public boolean checkItemSearch(String key)
	{
		return this.itemSearchMap.containsKey(key);
	}
	
	/**
	 * Get the Item key associated with the supplied synonym.
	 * @param key  String The Item name synonym to use.
	 * @return     String The key to access the Item.
	 */
	public String getItemKey(String key)
	{
		try 
		{
			if (this.checkItemSearch(key) == true)
				return this.itemSearchMap.get(key);
			else
				throw new InvalidMapKeyException();
		} 
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Inventory Item synonym " + key + " doesn't exist in the hashmap.");
			e.printStackTrace();
		}
		return "";
	}

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String deathImage = "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
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
			"MMMMMMMMMMMhyyhMMMhyydMMMmyssshNMMMNyyymMMNyyymMMMMMMMMMMMMMyyyhMMMMMhyyyyyydNMMhsyyyyysdMMMMMMMMMMhyyyyyhmMMMMmsyyyyysyMMMMMyyydMMMMNyyyyyyhmMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMM:  -MMM`  :Mh-       :mMN   sMMd   yMMMMMMMMMMMMy    hMMMM.   `    +M:       -MMMMMMMMMM.   ``  `/NMh        dMMMy    dMMMm    `   .oMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMo  `hMy   oy   +dmd/  `dN   sMMm   hMMMMMMMMMMMm` `  `mMMM-  oMMo   m/  .mmdddMMMMMMMMMM-  +MMm/  .Nd   ymmdhMMMm` `  .NMMN   hMMd-  /MMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMy`  -   sM:  /MMMMM-  oN`  yMMm   dMMMMMMMMMMM-  so  -MMM-  :so-  :M+       oMMMMMMMMMM:  +MMMN   yd       .MMN-  yo  :MMM   hMMMh   NMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMm-   .dMM+  .mMMMh`  yN`  yMMm   dMMMMMMMMMM+   ++   +MM-  ./`  /MM/  .hhhhmMMMMMMMMMM-  +MMMy   dd   shhhhMM+   +/   oMN   hMMM+  .MMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMh   dMMMM/   .-`   oMM.  `/+-   mMMMMMMMMMh   ....   yM.  /Mh   yM:   ://::NMMMMMMMMM.  .++-  `yMh   -//::yy   ....   hN   :+/.  -mMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMh:::dMMMMMmo/---/sNMMMm+:----:odMMMMMMMMMM/-:sMMMMo--/M/::oMMy:::m+::::::-/MMMMMMMMMM+::-:::+yNMMh:::::::-d/-:sMMMM+--/N:::-::/ohMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNyydMMhyyMMMMMMMMMhyyNMdyymMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN`  :.  :MMMMMMMMMo  `/  `dMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN/    sMMMMMMMMMMMh`   -mMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM+  `  `sMMMMMMMMMh.  `  :mMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN--/Nd:-/MMMMMMMMMo-:yNo--hMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMms:.     ./yNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM+ `-/+ooo+/- `sMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMy-yMMMMMMMMMMNy-hMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
			"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\r\n" + 
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
