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

package Items;

import java.io.Serializable;

import Structure.Command;
import Structure.DisplayData;
import Structure.GameState;
import Structure.Space;

public abstract class Item implements Space, Serializable
{	
	private static final long serialVersionUID = 1L;
	
	protected GameState gameState;
	protected String name;
	protected String image;
	protected String description;
	protected String state;
	protected String regex = "";
	
	public Item(GameState gameState) 
	{
		this.gameState = gameState;
		this.setName();
		this.createItems();
		this.createFlags();
	}

	/**
	 * Set room name.
	 */
	protected abstract void setName();
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
		return this.executeCommand(command);
	}

	//===============================================================
	//Setters and getters
	//===============================================================
	
	public String getName()
	{
		return this.name;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public String getState()
	{
		return this.state;
	}
	
	public void setRegex(String regex)
	{
		this.regex = regex;
	}
	
	public String getRegex()
	{
		return this.regex;
	}
}