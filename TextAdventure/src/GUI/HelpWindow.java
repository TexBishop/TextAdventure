/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package GUI;

@SuppressWarnings("serial")
public class HelpWindow extends TextWindow 
{
	//===============================================================
	//Text window containing instructions for playing the game
	//===============================================================
	/**
	 * Constructor.  Create the frame.
	 */
	public HelpWindow(Application application) 
	{
		super(application);
	}

	@Override
	protected String setTitle() 
	{
		return "Instructions";
	}

	@Override
	protected String setText() 
	{
		return "Commands:\n"
				+ "First word is the action verb.\n"
				+ "Can read up to two nouns.\n\n"
				+ "Examples:\n"
				+ "\"return\"\n"
				+ "\"go north\"\n"
				+ "\"look around\"\n"
				+ "\"put the knife on the table\"\n"
				+ "\"use the key on the door\"\n\n"
				+ "To focus on an item in inventory:\n"
				+ "\"look in inventory at XXXXXX\"\n"
				+ "or\n"
				+ "\"look at XXXXXX in inventory\"\n\n"
				+ "Once focus is on the item, you can perform actions on the item as normal. \n\n"
				+ "\"return\" or \"go back\" will return focus to the current room.";
	}
}
