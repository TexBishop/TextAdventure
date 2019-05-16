/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Structure;

/**
 * Abstract Space class, which provides access to the Space hashmap, along
 * with displayOnEntry() and fullDescription() abstract methods.
 */
public interface Space
{
	/**
	 * Executes command and determines data to display to the UI
	 * @param command  Command the command to process
	 * @return         DisplayData the data to display in the UI
	 */
	public abstract DisplayData handleDisplayData(Command command);
	/**
	 * Implementation of Image and / or Text to display when this space is entered.
	 * @return Display An object representing the data to display.
	 */
	public abstract DisplayData displayOnEntry();
	/**
	 * Builds the full description of the space, based on flag states.
	 * @return String The completed description as a String.
	 */
	public abstract String fullDescription();
	/**
	 * Execute the custom commands for this room
	 */
	public abstract DisplayData executeCommand(Command command);
}
