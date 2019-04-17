/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Structure;

import java.io.Serializable;

/**
 * An object representation of a display update.
 * Contains the image and the text description needed for an update to the interface.
 * The GUI normally gets this object as a method return through GameState.
 */
public class DisplayData implements Serializable
{
	//===============================================================
	//Instance variables.  serialVersionUID needed for saving objects
	//to file, using serialization.
	//===============================================================
	private static final long serialVersionUID = 1L;
	private String image = "";
	private String description = "";
	
	public DisplayData()
	{
		//default constructor, used to return a Display object with null values
	}
	
	public DisplayData(String image, String description)
	{
		this.image = image;
		this.description = description;
	}
	
	/**
	 * Add more text to description of this DisplayData object.
	 * @param message
	 */
	public void appendToDescription(String message)
	{
		this.description += message;
	}

	//===============================================================
	//Getters.  No setters, initialization through constructor only.
	//===============================================================
	public String getImage() 
	{
		return image;
	}
	
	public String getDescription() 
	{
		return description;
	}
}
