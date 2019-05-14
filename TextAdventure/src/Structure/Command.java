/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Structure;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Object representation of a command, broken into verb, subject and target.
 */
public class Command 
{
	private String verb;
	private String sentence;
	
	public Command(String verb, String sentence)
	{
		this.verb = verb;
		this.sentence = sentence;
	}
	
	/**
	 * Check for the presence of each passed regex in the command, in the order in which
	 * they were passed.
	 * @param regexes  String...  Any number of Strings to check
	 * @return         boolean    Result of whether the passed Strings were found in the command, 
	 *                            and in the correct order.
	 */
	public boolean ordered(String ...regexes)
	{
		int index = 0;
		for (String regex : regexes)
		{
			//=================================================================================
			//Compile the regex into a Pattern object, and create a Matcher object that
			//searches for this pattern.  Assign our command sentence as the sequence to
			//search.  
			//=================================================================================
			//Every time we use the Matcher.find() method, it will return a boolean
			//indicating if a pattern match was found.  If true, it will set an index value
			//for matcher at the beginning of the found pattern.  This index number can be
			//accessed using the Matcher.start() method.  In addition, Matcher.group() will
			//return the String value that matched one of the regex values.
			//=================================================================================
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(this.sentence);
			
			//=================================================================================
			//If the regex is matched at a position after index, then the order is maintained.
			//=================================================================================
			if (matcher.find(index) == true)
			{
				//=================================================================================
				//If the end of the matched String is the end of the command, then check to see if 
				//the previous character is a space.  If it is, set index to the 
				//end of the command. If it is not, then check to see if the previous character and 
				//the next character are spaces.  If they are not, then the matched String is not 
				//an exact match.  Return false.
				//=================================================================================
				if (matcher.start() + matcher.group().length() == sentence.length())
				{
					if (this.sentence.charAt(matcher.start() - 1) == ' ')
						index = matcher.start() + matcher.group().length();
					else
						return false;
				}
				else
				{
					if (this.sentence.charAt(matcher.start() - 1) == ' ' &&
						this.sentence.charAt(matcher.start() + matcher.group().length()) == ' ')
						index = matcher.start() + matcher.group().length();
					else
						return false;
				}
	        }
	        else
	        	return false;
		}
		return true;
	}
	
	/**
	 * Check for the presence of each passed regex in the command, in no particular order
	 * @param regexes  String...  Any number of Strings to check
	 * @return         boolean    Result of whether the passed Strings were found in the command
	 */
	public boolean unordered(String ...regexes)
	{
		for (String regex : regexes)
		{
			//=================================================================================
			//Compile the regex into a Pattern object, and create a Matcher object that
			//searches for this pattern.  Assign our command sentence as the sequence to
			//search.  
			//=================================================================================
			//Every time we use the Matcher.find() method, it will return a boolean
			//indicating if a pattern match was found.  If true, it will set an index value
			//for matcher at the beginning of the found pattern.  This index number can be
			//accessed using the Matcher.start() method.  In addition, Matcher.group() will
			//return the String value that matched one of the regex values.
			//=================================================================================
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(this.sentence);
	        if (matcher.find() == true)
	        {
				//=================================================================================
				//If the end of the matched String is not the end of the command, then check to 
				//see if the next character is a space.  If it is not, then the matched String is 
	        	//not an exact match.  Return false.
				//=================================================================================
				if (matcher.start() + matcher.group().length() != sentence.length())
				{
					if (this.sentence.charAt(matcher.start() + matcher.group().length()) != ' ')
						return false;
				}

				//=================================================================================
				//If the matched String is not either the first character, or preceded by a space, 
				//then match is not an exact match.
				//=================================================================================
				if (matcher.start() != 0)
					if (this.sentence.charAt(matcher.start() - 1) != ' ')
						return false;
	        }
	        else
	        	return false;
		}
		return true;
	}

	/**
	 * Checks the command String for an occurrence of the given regex, and returns the matching String
	 * @param regex  String  The regex to check
	 * @return       String  The matches String, if found.  Else, returns null.
	 */
	public String getMatch(String regex)
	{
		//=================================================================================
		//Compile the regex into a Pattern object, and create a Matcher object that
		//searches for this pattern.  Assign our command sentence as the sequence to
		//search.  
		//=================================================================================
		//Every time we use the Matcher.find() method, it will return a boolean
		//indicating if a pattern match was found.  If true, it will set an index value
		//for matcher at the beginning of the found pattern.  This index number can be
		//accessed using the Matcher.start() method.  In addition, Matcher.group() will
		//return the String value that matched one of the regex values.
		//=================================================================================
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(this.sentence);
		if (matcher.find() == true)
		{
			//=================================================================================
			//If the end of the matched String is not the end of the command, then check to 
			//see if the previous character and the next character are spaces.  If they are not, 
        	//then the matched String is not an exact match.  Return null.
			//=================================================================================
			if (matcher.start() + matcher.group().length() != sentence.length())
			{
				if (this.sentence.charAt(matcher.start() + matcher.group().length()) == ' ' &&
					this.sentence.charAt(matcher.start() - 1) == ' ')
					return matcher.group();
			}
			else
			{
				//=================================================================================
				//Verify that the matched String is preceded by a space.  If it is not, then it is
				//not an exact match.
				//=================================================================================
				if (matcher.start() != 0)
				{
					if (this.sentence.charAt(matcher.start() - 1) == ' ')
						return matcher.group();
				}
				else
					return matcher.group();
			}
		}
		return null;
	}
	
	public String toString()
	{
		return this.sentence;
	}

	//=================================================================================
	//Getters, no setters
	//=================================================================================
	public String getVerb() 
	{
		return this.verb;
	}
	
	public String getSentence()
	{
		return this.sentence;
	}
}
