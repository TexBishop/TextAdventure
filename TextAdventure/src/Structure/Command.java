/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Structure;

/**
 * Object representation of a command, broken into verb, subject and target.
 */
public class Command 
{
	private String verb;
	private String subject;
	private String target;
	
	public Command(String verb, String subject, String target)
	{
		this.verb = verb;
		this.subject = subject;
		this.target = target;
	}
	
	public String toString()
	{
		return verb + " " + subject + " " + target;
	}

	//=================================================================================
	//Getters, no setters
	//=================================================================================
	public String getVerb() 
	{
		return verb;
	}

	public String getSubject() 
	{
		return subject;
	}

	public String getTarget() 
	{
		return target;
	}
}
