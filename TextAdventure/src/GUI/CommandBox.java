/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import Structure.DisplayData;

import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

//=====================================================================
//The panel containing the simulated command line interface, where
//game commands are entered, using a JTextArea
//=====================================================================
@SuppressWarnings("serial")
public class CommandBox extends JPanel 
{
	//=====================================================================
	//Our class variables.  
	//   action and input are maps used for custom key bindings.
	//   textArea is declared here to make it available to the private classes
	//   promptPosition stores the position of the first editable character
	//   history tracks the last 10 commands entered
	//   command is the current command being entered
	//   index tracks position for the up and down arrow key bindings
	//=====================================================================
	private static final String PROMPT = ">> ";
	private Application application;
	private ActionMap action;
	private InputMap input;
	private JTextArea textArea;
	private int promptPosition = 3;
	private LinkedList<String> history = new LinkedList<String>();
	private String command;
	private int index = -1;
	
	private class CommandFilter extends DocumentFilter
	{
		//=====================================================================
		//Extend and override DocumentFilter to make any text prior to the
		//command prompt become non-editable.
		//=====================================================================
		@Override
		public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr) throws BadLocationException
		{
			if (promptPosition <= offset)
				super.insertString(fb, offset, string, attr);
		}
		
		@Override
		public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException
		{
			if (promptPosition <= offset)
				super.remove(fb, offset, length);
		}
		
		@Override
		public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs) throws BadLocationException
		{
			if (promptPosition <= offset)
				super.replace(fb, offset, length, text, attrs);
		}
	}

	//=====================================================================
	//This class creates new actions to represent our custom key bindings
	//=====================================================================
	private class KeyAction extends AbstractAction
	{
	    /**
		 * Used to save GameState objects to a file
		 */
		private static final long serialVersionUID = 1L;
		
		private Action originalAction = null;
	    private int key;
	    
	    public KeyAction(int key)
	    {
	    	this.key = key;
	    }

	    /**
	     * Adds an action to be performed to this Action.
	     * Used to add a previous key binding that was overwritten
	     * back into the new action.
	     * @param originalAction Action The action to add.
	     */
	    public void AddAction(Action originalAction)
	    {
	    	this.originalAction = originalAction;
	    }
	    public Action getOriginalAction()
	    {
	    	return originalAction;
	    }
	    
		@Override
		public void actionPerformed(ActionEvent e) 
		{
        	//===============================================================
			//Set cursor to end of text area, to prevent a user from
			//new-lining in the middle of their typed command.
			//Then, if an original action exists, perform it.
        	//===============================================================
			textArea.setCaretPosition(textArea.getText().length());
        	if (originalAction != null)
        	{
        		originalAction.actionPerformed(e);
        	}

        	//===============================================================
        	//Set the code for our custom key bindings
        	//===============================================================
        	if (key == KeyEvent.VK_INSERT)
        	{
            	//===============================================================
        		//Set the new prompt and prompt position.
            	//===============================================================
        		textArea.append("\n" + PROMPT);
				promptPosition = textArea.getText().length();
        	}
        	else
        	if (key == KeyEvent.VK_ENTER)
        	{
            	//===============================================================
            	//Reset index, set value of command, and push the new command
        		//into history.  If history has more than 10 commands, pop the
        		//oldest one.
            	//===============================================================
        		index = -1;
        		command = textArea.getText().substring(promptPosition, textArea.getText().length() - 1);
        		history.push(command);
        		if (10 < history.size())
        			history.removeLast();

            	//===============================================================
        		//Set the new prompt and prompt position, then resolve the
            	//command, and update image and append text as needed.
            	//===============================================================
    			textArea.append("\n" + PROMPT);
    			promptPosition = textArea.getText().length();
        		DisplayData display = application.getGameState().parseCommand(command);
        		
        		if (display.getImage().isEmpty() == false)
        			application.getImageBox().setImage(display.getImage());
        		if (display.getDescription().isEmpty() == false)
        			application.getCommandBox().appendText(display.getDescription());

            	//===============================================================
        		//Update the contents of the Inventory window.
            	//===============================================================
        		application.getLinkBar().getInventoryWindow().updateContents();
        		
        	}
        	else
        	if (key == KeyEvent.VK_UP)
        	{
            	//===============================================================
            	//Up Arrow: move through command history, and paste the listed
        		//command to the current line
            	//===============================================================
        		if (index < history.size())
        		{
        			if (index < history.size() - 1)
        			{
        				index++;
        				textArea.replaceRange(history.get(index), promptPosition, textArea.getText().length());
        			}
        		}
        	}
        	else
        	if (key == KeyEvent.VK_DOWN)
        	{
            	//===============================================================
            	//Down Arrow: move through command history, and paste the listed
        		//command to the current line
            	//===============================================================
        		if (history.isEmpty() == false)
        		{
        			if (history.get(index).isBlank() == false)
        			{
        				if (0 < index)
        					index--;
        				textArea.replaceRange(history.get(index), promptPosition, textArea.getText().length());
        			}
        		}
        	}
		}
	}

	/**
	 * Add a key binding to a key, to tie key presses to Actions.
	 * @param name        String The name of the key.
	 * @param pressedKey  int Value representing a key on the keyboard.
	 */
    private void addKeyBinding(String name, int pressedKey) 
    {	 
    	//===============================================================
    	//For keys that already have key bindings, grab the name of
    	//that key binding.  Will be null if no binding exists.
    	//===============================================================  
    	KeyStroke thisKeyStroke = KeyStroke.getKeyStroke(pressedKey, 0);
    	String oldKeybindingName = (String) input.get(thisKeyStroke);
    	
    	//===============================================================
    	//Add our inputs and actions
    	//=============================================================== 	
    	input.put(KeyStroke.getKeyStroke(pressedKey, 0, false), name + ".pressed");
        action.put(name + ".pressed", new KeyAction(pressedKey));
        
    	//===============================================================
    	//For keys that already had key bindings, they have been overwritten.
        //To repair this, we will now capture the action from the old binding,
        //and add it to our new action, so that both actions execute.
    	//===============================================================
        if (oldKeybindingName != null)
    	{
        	Action originalKeyAction = action.get(oldKeybindingName);
        	((KeyAction) action.get(name + ".pressed")).AddAction(originalKeyAction);
        }
    }
    
	/**
	 * Constructor.  Create the panel.
	 */
	@SuppressWarnings("static-access")
	public CommandBox(Application application) 
	{
		this.application = application;
    	//===============================================================
		//Set the parameters for this panel
    	//===============================================================
		//this.setBorder(new MatteBorder(4, 0, 0, 0, new Color(0, 0, 0)));
		this.setBackground(new Color(255, 255, 250));
		this.setBounds(0, application.getImageBox().getHeight() + 40, application.frame.getWidth(), application.screenHeight/3);
		this.setLayout(null);

    	//===============================================================
		//The text area that will be used as the command line interface
    	//===============================================================
		textArea = new JTextArea();
		textArea.setLocation(0, 0);
		textArea.setBackground(new Color(255, 255, 250));
		textArea.setFont(new Font("Arial Black", Font.PLAIN, 14));
		textArea.setBorder(null);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(PROMPT);
		
		//===============================================================
		//Initialize our inputMap and actionMap to our JTextArea object.
		//Focus is set to WHEN_FOCUSED, to force overwrite of existing
		//key bindings for this text object.  If we don't do this, the
		//existing key bindings will supersede any new ones we create,
		//and our key binding won't work.
		//===============================================================
		this.action = textArea.getActionMap();
		this.input = textArea.getInputMap(textArea.WHEN_FOCUSED);
		
    	//===============================================================
    	//Set our textArea to use our custom DocumentFilter, and set
		//our custom key bindings.
    	//===============================================================
		((AbstractDocument)textArea.getDocument()).setDocumentFilter(new CommandFilter());
		addKeyBinding("Enter", KeyEvent.VK_ENTER);
		addKeyBinding("Up", KeyEvent.VK_UP);
		addKeyBinding("Down", KeyEvent.VK_DOWN);
		
    	//===============================================================
		//Disallow placement of cursor within the non-editable portion
		//of the text area.
    	//===============================================================
		textArea.addCaretListener(new CaretListener() 
		{
			@Override
			public void caretUpdate(CaretEvent e) 
			{
				if (textArea.getCaretPosition() < promptPosition)
					textArea.setCaretPosition(textArea.getText().length());
			}
		});

    	//===============================================================
		//A scroll pane, to make our command line text interface area scroll-able
    	//===============================================================
		JScrollPane scrollBar = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBar.setBounds(5, application.getImageBox().getHeight() + 40, this.getWidth() - 10, application.screenHeight/3);
		scrollBar.getVerticalScrollBar().setUI(new BasicScrollBarUI() 
		{
	    	//===============================================================
			//Overrides to change the color elements of the scroll bar
	    	//===============================================================
			@Override
			protected void configureScrollBarColors()
			{
				this.thumbColor = new Color(220, 220, 180);
				this.trackColor = new Color(255, 255, 250);
			}
			@Override
			protected void installComponents()
			{
				super.installComponents();
				this.incrButton.setBackground(new Color(255, 255, 250));
				this.decrButton.setBackground(new Color(255, 255, 250));
			}
		});
		scrollBar.setBorder(null);
		this.add(scrollBar);
	}
	
	/**
	 * Add a text description / message to the text command area.
	 * @param text String The description / message to append.
	 */
	public void appendText(String text)
	{
    	//===============================================================
		//Clear anything currently typed onto the command line before appending
    	//===============================================================
		this.textArea.setSelectionStart(promptPosition);
		this.textArea.replaceSelection("");
		this.textArea.append(text);

    	//===============================================================
		//Use Robot to simulate an enter key press, to newline to a new
		//command prompt.  It is necessary to set focus to the text area
		//first for the robot key press to be read by the text area.
		//To prevent the appended text from being stored as a command upon
		//the newline, we need to give this input its own key binding, set
		//to use the enter key action as its base.  We are appropriating
		//the insert button, since it is unlikely to be used.
    	//===============================================================
		try 
		{	
			Robot robot = new Robot();
			addKeyBinding("Robot_Enter", KeyEvent.VK_INSERT);
        	((KeyAction) action.get("Robot_Enter" + ".pressed")).AddAction(((KeyAction) action.get("Enter.pressed")).getOriginalAction());

			this.textArea.requestFocusInWindow();
			robot.keyPress(KeyEvent.VK_INSERT);
			robot.keyRelease(KeyEvent.VK_INSERT);
		} 
		catch (Exception e) 
		{
			System.out.println("Robot failed in CommandBox.appendText()");
			e.printStackTrace();
		}
	}
	
	/**
	 * Clear the text in the CommandBox and reset the prompt position.
	 */
	protected void clearText()
	{
		this.promptPosition = 3;
		this.textArea.setText(">> ");
	}
}
