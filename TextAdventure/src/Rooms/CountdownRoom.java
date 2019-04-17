/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package Rooms;

import java.util.LinkedList;

import Structure.Command;
import Structure.DisplayData;
import Structure.Flag;
import Structure.GameState;

/**
 * Room type that has a count down to death component.  Increments by number of commands entered.
 */
public abstract class CountdownRoom extends Room 
{
	private static final long serialVersionUID = 1L;
	private int limit;
	private int timer;
	private Flag startTimer;
	private Flag stopTimer;
	private LinkedList<String> messages = new LinkedList<>();

	public CountdownRoom(GameState gameState) 
	{
		super(gameState);
		initializeCountdown();
	}
	
	/**
	 * Call setCountdown() and setTriggers() to initialize count down parameters..
	 */
	protected abstract void initializeCountdown();

	/**
	 * Initialize count down death number and step messages.  The step messages will be displayed
	 * after a command attempt that failed to disable the timer.  Therefore, there will need to be
	 * a number of messages passed equal to the number of the limit, with the final message being
	 * the death message.  For example, if you set limit to 3, which would mean death after the 
	 * third command, you will need three messages.  One for after the first failed attempt.  One
	 * for after the second failed attempt.  And one to display for death after the third failed
	 * attempt.
	 * @param limit     int    The command number at which death occurs
	 * @param messages  String The messages to display at each increment of the timer.  The
	 *                         final message should be the death message.
	 */
	protected void setCountdown(int limit, String ...messages)
	{
		this.limit = limit;
		
		for (String message : messages)
		{
			this.messages.addLast(message);
		}
	}
	
	/**
	 * Initialize the two flags that control whether the count down is active.  A value
	 * of null for startTimer indicates that the count down is active by default.  A value of null
	 * for stopTimer indicates that the count down cannot be de-activated.
	 * @param startTimer  Flag The flag that signals the count down can be activated.
	 * @param stopTimer   Flag The flag that signals the count down has been de-activated.
	 */
	protected void setTriggers(Flag startTimer, Flag stopTimer)
	{
		this.startTimer = startTimer;
		this.stopTimer = stopTimer;

		//=================================================================================
		//Set a null startTimer to a flipped flag
		//=================================================================================
		if (this.startTimer == null)
		{
			this.startTimer = new Flag(false, "", "");
			this.startTimer.flipToggle();
		}

		//=================================================================================
		//Set a null stopTimer to an unflipped flag
		//=================================================================================
		if (this.stopTimer == null)
			this.stopTimer = new Flag(false, "", "");
	}

	@Override
	public DisplayData handleDisplayData(Command command)
	{	
		DisplayData displayData;
		//=================================================================================
		//If innerSpace is null, check it to see if it needs to be used.  If innerSpace 
		//is not null, execute the innerSpace executeCommand()
		//=================================================================================
		if (this.innerSpace == null)
			displayData = this.checkInnerSpace(command);
		else
			displayData = this.innerSpace.handleDisplayData(command);
		
		//=================================================================================
		//If diplayData is still null, run this room's executeCommand()
		//=================================================================================
		if (displayData == null)
			displayData = this.executeCommand(command);
		
		//=================================================================================
		//If count down is active, enter if statement
		//=================================================================================
		if (startTimer.isFlipped() == true && stopTimer.isFlipped() == false)
		{
			try 
			{
				this.timer++;
				//=================================================================================
				//If timer limit is reached, return death.  Else, append failure message to result.
				//=================================================================================
				if (this.timer == this.limit)
					return this.gameState.death(this.messages.get(this.timer - 1));
				else
					displayData.appendToDescription(" " + this.messages.get(this.timer - 1));

				return displayData;
			} 
			catch (Exception e) 
			{
				System.out.println("Likely the result of timer being greater than the number of supplied messages, in CountdownRoom " + this.getName());
				e.printStackTrace();
			}
		}

		//=================================================================================
		//If count down is inactive, return the DisplayData unchanged.
		//=================================================================================
		return displayData;
	}
	
	/**
	 * Reset timer to 0.
	 */
	public void resetTimer()
	{
		this.timer = 0;
	}
}
