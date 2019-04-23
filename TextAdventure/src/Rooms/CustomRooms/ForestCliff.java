/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms.CustomRooms;

import Rooms.Room;
import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;

public class ForestCliff extends Room 
{
	private static final long serialVersionUID = 1L;

	public ForestCliff(GameState gameState) 
	{
		super(gameState);
	}

	@Override
	protected void setName() 
	{
		this.name = "Forest Cliff";
	}

	@Override
	public DisplayData displayOnEntry() 
	{
		return new DisplayData (forestCliffImage, this.fullDescription());
	}

	@Override
	public String fullDescription() 
	{
		this.description = "XXXXX";
		
		return this.description;
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

	//===============================================================
	//ASCII image String constants beyond this point
	//===============================================================
	private final String forestCliffImage = "";
}
