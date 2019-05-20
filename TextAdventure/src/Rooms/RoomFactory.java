/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms;

import Structure.GameState;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Used to instantiate rooms
 * @author Tex Bishop
 */
public class RoomFactory implements Serializable
{
	private static final long serialVersionUID = 1L;
	private GameState gameState;
	
	public RoomFactory(GameState gameState)
	{
		this.gameState = gameState;
	}
	
	/**
	 * Create an instance of a room, which will auto-add it to the game state's space map.
	 * @param room  String  The name of the room to instantiate.
	 */
	public void makeRoom(String room)
	{
		try 
		{
			//=================================================================================
			//Build the package hierarchy for the room being instantiated.
			//Lack of a ClassNotFoundException indicates that the package hierarchy is correct.
			//=================================================================================
			String roomString = "Rooms.CustomRooms." + room;
			try
			{
				Class.forName(roomString);
			}
			catch (ClassNotFoundException e1)
			{
				roomString = "Rooms.CustomRooms.Forest." + room;
				try
				{
					Class.forName(roomString);
				}
				catch (ClassNotFoundException e2)
				{
					roomString = "Rooms.CustomRooms.BackArea." + room;
					try
					{
						Class.forName(roomString);
					}
					catch (ClassNotFoundException e3)
					{
						roomString = "Rooms.CustomRooms.House." + room;
					}
				}
			}

			//=================================================================================
			//Instantiate the room
			//=================================================================================
			Class.forName(roomString).getConstructor(GameState.class).newInstance(this.gameState);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) 
		{
			System.out.println("Something went wrong with room instantiation.");
			e.printStackTrace();
		}
	}
}
