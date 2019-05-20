/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-20-2019
 */

package Structure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Counting object that can link a count between rooms.  Contains a hashmap of the rooms that are a part of the area, 
 * with each room having a number that indicates how much its value is offset from the count value.
 */
public class AreaCount implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Map<String, Integer> areaMap = new HashMap<>();
	private int count = 0;
	private boolean active;
	
	/**
	 * Add a room to the area count.
	 * @param room  String The name of the room to add.
	 * @param offset Integer The number to offset this room from the count value.
	 */
	public void addRoom (String room, int offset)
	{
		try
		{
			if (this.areaMap.containsKey(room) == false)
				this.areaMap.put(room, Integer.valueOf(offset));
			else
				throw new InvalidMapKeyException();
		}
		catch (InvalidMapKeyException e) 
		{
			System.out.println("Duplicate.  Tried to add a room to an AreaCount, that is already in the AreaCount.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the count for a particular room.
	 * @param room  String  The name of the room.
	 * @return  The count value of the requested room.
	 */
	public int getRoomCount(String room)
	{
		int roomCount = this.count - this.areaMap.get(room).intValue();
		if (roomCount < 0)
			roomCount = 0;
		
		return roomCount;
	}
	
	/**
	 * Increment this area's count.
	 */
	public void increment()
	{
		this.count++;
	}
	
	/**
	 * Decrement this area's count.  Will not decrement below zero.
	 */
	public void decrement()
	{
		this.count--;
		if (this.count < 0)
			this.count = 0;
	}
	
	/**
	 * Activate this count.
	 */
	public void activate()
	{
		this.active = true;
	}
	
	/**
	 * Deactivate this count.
	 */
	public void deactivate()
	{
		this.active = false;
	}
	
	/**
	 * Reset this count to zero and inactive.
	 */
	public void reset()
	{
		this.count = 0;
		this.active = false;
	}
	
	/**
	 * Check if this count is active.
	 * @return boolean Active value.
	 */
	public boolean getActive()
	{
		return this.active;
	}
}
