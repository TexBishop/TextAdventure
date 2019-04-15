/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
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
		this.setCommandSynonyms("go", "walk", "head", "travel", "approach");
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
			return new Command("", "", "");

		//===============================================================
		//Set command to all lowercase, then split it into separate
		//words, and remove the unneeded words to get the basic command.
		//===============================================================
		command.toLowerCase();
		String[] words = command.split(" ");
		words = this.removeWords(words);
		words[0] = this.checkSynonyms(words[0]);

		//===============================================================
		//Return a Command object.  Replace any null string values with
		//empty strings.
		//===============================================================
		if (words.length == 1)
		{
			if (words[0] == null)
				return new Command("", "", "");
			else
				return new Command(words[0], "", "");
		}
		else
		if (words.length == 2)
		{
			if (words[1] == null)
			{
				if (words[0] == null)
					return new Command("", "", "");
				else
					return new Command(words[0], "", "");
			}
			else
				return new Command(words[0], words[1], "");
		}
		else
		if (words[2] == null)
		{
			if (words[1] == null)
			{
				if (words[0] == null)
					return new Command("", "", "");
				else
					return new Command(words[0], "", "");
			}
			else
				return new Command(words[0], words[1], "");
		}	
		return new Command(words[0], words[1], words[2]);
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
	
	/**
	 * Remove any determiners or prepositions from the command
	 * @param words String[] The array containing the words in the current command
	 * @return      String[] The array with the conjunctions and determiners removed
	 */
	private String[] removeWords(String[] words)
	{
		//===============================================================
		//Loop through the String array once, and assign any verb or noun
		//value to our temporary array, then return the temporary array.
		//===============================================================
		String[] parsed = new String[words.length];
		int index = 0;
		for (int i = 0; i < words.length; i++)
		{
			if (words[i].equals("at") == false &&
				words[i].equals("the") == false &&
				words[i].equals("to") == false &&
				words[i].equals("a") == false &&
				words[i].equals("an") == false &&
				words[i].equals("on") == false &&
				words[i].equals("this") == false &&
				words[i].equals("that") == false &&
				words[i].equals("these") == false &&
				words[i].equals("those") == false &&
				words[i].equals("my") == false &&
				words[i].equals("other") == false &&
				words[i].equals("by") == false &&
				words[i].equals("against") == false &&
				words[i].equals("for") == false &&
				words[i].equals("from") == false &&
				words[i].equals("in") == false &&
				words[i].equals("inside") == false &&
				words[i].equals("into") == false &&
				words[i].equals("near") == false &&
				words[i].equals("of") == false &&
				words[i].equals("out") == false &&
				words[i].equals("over") == false &&
				words[i].equals("through") == false &&
				words[i].equals("with") == false &&
				words[i].equals("under") == false &&
				words[i].equals("about") == false &&
				words[i].equals("above") == false &&
				words[i].equals("across") == false &&
				words[i].equals("after") == false &&
				words[i].equals("among") == false &&
				words[i].equals("before") == false &&
				words[i].equals("behind") == false &&
				words[i].equals("below") == false &&
				words[i].equals("beside") == false &&
				words[i].equals("between") == false &&
				words[i].equals("down") == false &&
				words[i].equals("during") == false &&
				words[i].equals("except") == false &&
				words[i].equals("off") == false &&
				words[i].equals("out") == false &&
				words[i].equals("toward") == false &&
				words[i].equals("towards") == false &&
				words[i].equals("up") == false &&
				words[i].equals("and") == false &&
				words[i].equals("or") == false &&
				words[i].equals("nor") == false &&
				words[i].equals("but") == false &&
				words[i].equals("yet") == false &&
				words[i].equals("so") == false &&
				words[i].equals("all") == false &&
				words[i].equals("one") == false &&
				words[i].equals("each") == false &&
				words[i].equals("another") == false &&
				words[i].equals("many") == false &&
				words[i].equals("its") == false &&
				words[i].equals("their") == false &&
				words[i].equals("such") == false)
			{
				parsed[index] = words[i];
				index++;
			}
		}	
		return parsed;
	}
}
