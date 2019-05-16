/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

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
	private final String startingText = "You stand at the side of an old farm road, watching the black car that "
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

	//===============================================================
	//Variables used for sizing of UI elements
	//===============================================================
	protected final int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	protected final Font font = new Font("Monospaced", Font.BOLD, screenHeight/135);
	protected final FontMetrics metrics;
	protected final int imageWidth;
	protected final int imageHeight;

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
    	//===============================================================
		//Set parameters for this application window's outer container
    	//===============================================================
		this.frame.setUndecorated(true);  
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setFont(this.font);
		this.frame.getRootPane().setBorder(new LineBorder(new Color(0, 0, 0), 4));
		this.frame.setVisible(true);

    	//===============================================================
		//Initialize our final variables used for the UI sizing
    	//===============================================================
		this.metrics = this.frame.getGraphics().getFontMetrics(this.font);
		this.imageWidth = metrics.stringWidth("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW\r\n");
		this.imageHeight = metrics.getHeight() * 45;

    	//===============================================================
		//Set the size of the frame
    	//===============================================================
		this.frame.setBounds(100, 100, imageWidth + 10, this.imageHeight + this.screenHeight/3 + 60);   

    	//===============================================================
		//Initialize the frame's components
    	//===============================================================
		initialize();
	}

	/**
	 * Initialize the components of the application window.
	 */
	private void initialize() 
	{
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
		this.imageBox = new ImageBox(this);
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
        //Clear the text out of the Command Box and reset promp position.
    	//===============================================================
		this.commandBox.clearText();

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
