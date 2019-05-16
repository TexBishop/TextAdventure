/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
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
				+ "First word is the action verb. Always start a command with the verb.\n\n"
				+ "Examples:\n"
				+ "\"return\"\n"
				+ "\"go north\"\n"
				+ "\"look around\"\n"
				+ "\"put the knife on the table\"\n"
				+ "\"use the key on the door\"\n\n"
				+ "If you're having trouble getting an item in inventory to do something, try using "
				+ "keywords 'my' or 'inventory':\n"
				+ "\"drink my water\"\n"
				+ "or\n"
				+ "\"drink the water in inventory\"\n\n"
				+ "\"return\" or \"go back\" will return to the description of the current room.";
	}
}
