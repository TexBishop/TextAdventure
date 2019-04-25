/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package GUI;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.text.AbstractDocument;

import Items.Item;
import Items.CustomItems.BottleOfWater;
import Rooms.Room;
import Rooms.CustomRooms.OldFarmhouse;
import Structure.DisplayData;
import Structure.GameState;

//==============================================================================
//Main window.  Create and run our application window as a Jframe.
//==============================================================================
public class Application
{
	private final String startingText = "You stand at the side of an old farm road, watching the taxi that "
			+ "just dropped you off become small in the distance.  The last few days are a haze in your mind, and you "
			+ "find yourself wondering how recent events managed to lead you to this place.  You try to piece things "
			+ "together in your head, but the memories scatter, leaving nothing but the certainty that "
			+ "you're here to do SOMETHING.  Several minutes pass before you shake yourself back to awareness. "
			+ "Your new surroundings come into focus, unfamiliar.\n\n";
	
	protected JFrame frame = new JFrame();
	private GameState gameState;
	private FrameDragListener frameDragListener;
	private LinkBar linkBar;
	private ImageBox imageBox;
	private CommandBox commandBox;

	/**
	 * Main.  Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					@SuppressWarnings("unused")
					Application window = new Application();	
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Default Constructor.  Create the application window.
	 */
	public Application() 
	{
		initialize();
	}

	/**
	 * Initialize the components of the application window.
	 */
	private void initialize() 
	{
    	//===============================================================
		//Set parameters for this application window's outer container
    	//===============================================================
		this.frame.setBounds(100, 100, 800, 930);   
		this.frame.setUndecorated(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);

    	//===============================================================
		//FrameDragListener object initialization, to make our undecorated 
		//frame drag-able
    	//===============================================================
		this.frameDragListener = new FrameDragListener(this.frame, new Point(0, 0));
		this.frame.addMouseListener(frameDragListener);
		this.frame.addMouseMotionListener(frameDragListener);

    	//===============================================================
        //Add the LinkBar, ImageBox and CommandBox JPanels
    	//===============================================================
		this.imageBox = new ImageBox(this.frame);
		this.commandBox = new CommandBox(this);
		this.linkBar = new LinkBar(this);
		this.frame.add(linkBar);
		this.frame.add(imageBox);
		this.frame.add(commandBox);

    	//===============================================================
        //Initialize our game state, and then display the starting room.
    	//===============================================================
		this.gameState  = new GameState();
		Room startingRoom = new OldFarmhouse(this.gameState);
		DisplayData display = this.gameState.setCurrentRoom(startingRoom.getName());

		if (display.getImage().isEmpty() == false)
			this.imageBox.setImage(display.getImage());
		if (display.getDescription().isEmpty() == false)
			this.commandBox.appendText(this.startingText + display.getDescription());

    	//===============================================================
        //Initialize starting inventory
    	//===============================================================
		Item startingBottle = new BottleOfWater(this.gameState);
		this.gameState.addSpace(startingBottle.getName(), startingBottle);
		this.gameState.addToInventory(startingBottle.getName());
	}
	
	/**
	 * Reset the game to a new game state.
	 */
	public void resetGame()
	{
    	//===============================================================
        //Remove, re-initialize, and re-add the CommandBox, to clear it.
		//We can't simply set it's text to empty, because we've made it
		//non-editable.  This was the simplest solution I found.
    	//===============================================================
		this.frame.remove(commandBox);
		this.commandBox = new CommandBox(this);
		this.frame.add(commandBox);

    	//===============================================================
        //Re-initialize our game state, and then display the starting room.
    	//===============================================================
		this.gameState = new GameState();
		Room startingRoom = new OldFarmhouse(this.gameState);
		DisplayData display = this.gameState.setCurrentRoom(startingRoom.getName());

		if (display.getImage().isEmpty() == false)
			this.imageBox.setImage(display.getImage());
		if (display.getDescription().isEmpty() == false)
			this.commandBox.appendText(this.startingText + display.getDescription());

    	//===============================================================
        //Initialize starting inventory
    	//===============================================================
		Item startingBottle = new BottleOfWater(this.gameState);
		this.gameState.addSpace(startingBottle.getName(), startingBottle);
		this.gameState.addToInventory(startingBottle.getName());

    	//===============================================================
		//Update the contents of the Inventory window.
    	//===============================================================
		this.linkBar.getInventoryWindow().updateContents();
	}
	
	/**
	 * Provides access to the LinkBar object.
	 * @return the LinkBar.
	 */
	public LinkBar getLinkBar()
	{
		return this.linkBar;
	}
	
	/**
	 * Provides access to the ImageBox object.
	 * @return the ImageBox.
	 */
	public ImageBox getImageBox()
	{
		return this.imageBox;
	}
	
	/**
	 * Provides access to the CommandBox object.
	 * @return the CommandBox.
	 */
	public CommandBox getCommandBox()
	{
		return this.commandBox;
	}
	
	/**
	 * Provides access to the GameState object.
	 * @return the GameState.
	 */
	public GameState getGameState()
	{
		return this.gameState;
	}
	
	/**
	 * Load a saved game.
	 * @param gameState GameState The loaded GameState object.
	 */
	public void loadGame(GameState gameState)
	{
    	//===============================================================
        //Clear the text out of the Command Box and reset promp position.
    	//===============================================================
		this.commandBox.clearText();

    	//===============================================================
        //Load the saved game state, and then display the current room.
    	//===============================================================
		this.gameState = gameState;
		DisplayData display = this.gameState.getCurrentRoom().displayOnEntry();

		if (display.getImage().isEmpty() == false)
			this.imageBox.setImage(display.getImage());
		if (display.getDescription().isEmpty() == false)
			this.commandBox.appendText(display.getDescription());

    	//===============================================================
		//Update the contents of the Inventory window.
    	//===============================================================
		this.linkBar.getInventoryWindow().updateContents();
	}
}
