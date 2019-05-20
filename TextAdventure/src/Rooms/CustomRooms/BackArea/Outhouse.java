/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Rooms.CustomRooms.BackArea;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;

public class Outhouse extends Room 
{
	private static final long serialVersionUID = 1L;

	public Outhouse(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fullDescription() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createMovementDirections() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createItems() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void createFlags() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public DisplayData executeCommand(Command command) 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
