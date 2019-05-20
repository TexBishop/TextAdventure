/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-20-2019
 */

package Structure;

import Rooms.Room;

/**
 * Rooms implementing this interface will be included in the area of the gas leak.
 */
public interface GasLeak 
{
	/**
	 * Use this method to call GasLeak.addRoomToLeakArea(), to add this room to the AreaCount object.
	 */
	public abstract void initializeGasLeak();
	
	/**
	 * Adds a room to the gas leak area.
	 * @param gameState
	 * @param room       The room to add
	 * @param roomsAway  The number of rooms away from the actual gas leak.
	 */
	public default void addRoomToLeakArea(GameState gameState, String room, int roomsAway)
	{
		gameState.gasLeak.addRoom(room, roomsAway);
	}
	
	/**
	 * Activates the gas leak
	 * @param gameState
	 */
	public default void initializeLeak(GameState gameState)
	{
		gameState.gasLeak.activate();
	}
	
	/**
	 * Deactivates the gas leak
	 * @param gameState
	 */
	public default void terminateLeak(GameState gameState)
	{
		gameState.gasLeak.deactivate();
	}
	
	/**
	 * Checks to see if the gas leak level is high enough to trigger an explosion in the passed room.
	 * @param gameState
	 * @param room  The room you wish to check the level for.
	 * @return  The boolean value of whether the level is lethal or not.
	 */
	public default boolean isLevelLethal(GameState gameState, String room)
	{
		if (2 < gameState.gasLeak.getRoomCount(room))
			return true;
		else
			return false;
	}
	
	/**
	 * Get the string about the gas levels to append to the display data.
	 * @param gameState
	 * @return  The message to display.
	 */
	public default String getGasMessage(GameState gameState)
	{
		switch (gameState.gasLeak.getRoomCount(((Room) gameState.getCurrentRoom()).getName()))
		{
		case 0:   return "";
		case 1:   return "You think you smell some gas in the air. ";
		case 2:   return "You're pretty sure you're smelling gas in the air. ";
		case 3:   return "You definitely smell gas in the air. ";
		default:  return "The smell of gas has become overwhelming. This is dangerous. ";
		}
	}
	
	/**
	 * Handle cases where a gas leak explosion is triggered by an inventory item.  Return null if death is not triggered.
	 * @param gameState
	 * @param command  The current command.
	 * @return
	 */
	public default String handleInventorySparks(GameState gameState, Command command)
	{
		//=================================================================================
		//If carrying a lit torch, trigger death on lethal levels
		//=================================================================================
		if (gameState.checkInventory("Torch"))
		{
			if (((Items.CustomItems.Torch) gameState.getSpace("Torch")).getState().contains("lit"))
			{
				if (((GasLeak) this).isLevelLethal(gameState, ((Room) gameState.getCurrentRoom()).getName()) == true)
					return "You have only a second to realize your mistake, before your lit torch ignites the gas you've been smelling in the air. "
							+ "The room is enveloped in flame, blasting you with hellish fury. "
							+ "Thankfully, the pain only lasts a moment... ";
			}
		}

		//=================================================================================
		//If using the holy zippo, trigger death on lethal levels
		//=================================================================================
		if (command.unordered("use|light|ignite|spark", "zippo|lighter"))
		{
			if (((GasLeak) this).isLevelLethal(gameState, ((Room) gameState.getCurrentRoom()).getName()) == true)
				return "You have only a second to realize your mistake, before the flame from your zippo ignites the gas you've been smelling in the air. "
						+ "The room is enveloped in flame, blasting you with hellish fury. "
						+ "Thankfully, the pain only lasts a moment... ";
		}

		//=================================================================================
		//If no death was triggered, return null
		//=================================================================================
		return null;
	}
}
