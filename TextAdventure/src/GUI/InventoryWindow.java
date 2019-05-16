/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package GUI;

@SuppressWarnings("serial")
public class InventoryWindow extends TextWindow 
{
	//===============================================================
	//Text window containing an inventory list, updates dynamically
	//===============================================================
	/**
	 * Constructor.  Create the frame.
	 */
	public InventoryWindow(Application application) 
	{
		super(application);
	}

	@Override
	protected String setTitle() 
	{
		return "Inventory";
	}

	@Override
	protected String setText() 
	{
		return application.getGameState().getInventory();
	}
}
