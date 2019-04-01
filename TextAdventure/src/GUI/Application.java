/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;

//==============================================================================
//Main window.  Create and run our application window as a Jframe.
//==============================================================================
public class Application 
{
	private JFrame frame;
	private FrameDragListener frameDragListener;

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
					Application window = new Application();
					window.frame.setVisible(true);
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
		//Set parameters for this application window's outer container
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 800, 930);
		this.frame.setUndecorated(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
		
		//FrameDragListener object initialization, to make our undecorated frame drag-able
		this.frameDragListener = new FrameDragListener(frame);
		this.frame.addMouseListener(frameDragListener);
		this.frame.addMouseMotionListener(frameDragListener);
        
        //Add the LinkBar, ImageBox and CommandBox JPanels
		this.frame.add(new LinkBar(frame));
		this.frame.add(new ImageBox());
		this.frame.add(new CommandBox());
	}

}
