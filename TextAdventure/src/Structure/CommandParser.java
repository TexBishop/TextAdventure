/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package Structure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class contains tools to parse a command and return it as a Command object.
 */
public class CommandParser implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Map<String, String> commandSynonyms = new HashMap<>();

	public CommandParser()
	{
		//===============================================================
		//Set verb synonyms.  Avoid setting words that could be used for
		//multiple types of actions as synonyms.  For example "move"
		//could be used to move north, or to move a rug.  Such cases
		//need to be handled by the switch case command execution.
		//===============================================================
		this.setCommandSynonyms("go", "walk", "head", "travel", "approach", "enter", "follow");
		this.setCommandSynonyms("look", "examine", "check", "inspect", "scan", "scrutinize", "study");
	}
	
	/**
	 * Parse a command and return the results as a Command object.
	 * @param command String the command to parse
	 * @return        Command the resulting Command object
	 */
	public Command parse(String command)
	{
		//===============================================================
		//If command is empty, return an empty Command.
		//===============================================================
		if (command.isEmpty())
			return new Command("", "");

		//===============================================================
		//Set command to all lowercase, then split it into separate
		//words, and remove the unneeded words to get the basic command.
		//===============================================================
		command.toLowerCase();
		String[] words = command.split(" ");
		words[0] = this.checkSynonyms(words[0]);

		//===============================================================
		//Return the new Command object.
		//===============================================================
		return new Command(words[0], command);
	}
	
	/**
	 * Create synonyms for a command, placing them in the synonym hashmap.
	 * @param command  String The command that is having synonyms set for it.
	 * @param words    String The words that are being set as synonyms.
	 */
	private void setCommandSynonyms(String command, String ...words)
	{
		for (String word : words)
		{
			try 
			{
				if (this.commandSynonyms.containsKey(word) == false)
					this.commandSynonyms.put(word, command);
				else
					throw new InvalidMapKeyException();
			} 
			catch (InvalidMapKeyException e) 
			{
				System.out.println("Command synonym " + word + " already exists in the hashmap for command " + this.commandSynonyms.get(word));
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Check verb for synonym values.
	 * @param verb  String The verb to check.
	 * @return      String The default verb value for code use.
	 */
	private String checkSynonyms(String verb)
	{
		//===============================================================
		//Loop through the synonym hashmap, checking to see if the given
		//verb is listed as a key.  If it is, return the verb it is a
		//synonym of.  If the loop is left without finding it as a key,
		//return the verb unchanged.
		//===============================================================
		for (Entry<String, String> entry : commandSynonyms.entrySet())
		{
			if (entry.getKey().contentEquals(verb))
				return entry.getValue();
		}
		return verb;
	}
}
