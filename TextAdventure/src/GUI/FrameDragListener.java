/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package GUI;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

/**
 * Class that is used to make a JFrame drag-able from any point within a chosen component using mouse listeners
 */
public class FrameDragListener extends MouseAdapter
{
	//==============================================================================
	//Our primary JFrame is set to undecorated, which removes the title bar and
	//default border.  Without the title bar, default drag-ability of the window
	//is lost, so we have to implement new code to make our undecorated JFrame
	//drag-able, otherwise it will be fixed in place on the screen.
	//==============================================================================
	private final JFrame frame;
	private Point mouseDownCompCoords = null;
	private Point offset = null;

	/**
	 * Default constructor for our FrameDragListener class.
	 * @param frame  JFrame The primary JFrame object representing our application window.
	 * @param offset Point The amount of x, y offset needed to correct the window positioning when dragging.
	 */
	public FrameDragListener(JFrame frame, Point offset) 
	{
		this.frame = frame;
		this.offset = offset;
	}

	/**
	 * Reset stored X Y coordinates to null when mouse button is released
	 */
	public void mouseReleased(MouseEvent e) 
	{
		mouseDownCompCoords = null;
	}

	/**
	 * Capture X Y coordinates of mouse press within application window
	 */
	public void mousePressed(MouseEvent e) 
	{
		mouseDownCompCoords = e.getPoint();
	}

	/**
	 * Move window as it is dragged around the screen
	 */
	public void mouseDragged(MouseEvent e) 
	{
		//==============================================================================
		//This calculates location by using the difference between screen coordinates
		//and the coordinates of the first mouse click.  The offset coordinate is here
		//to adjust for an unintended shift caused when the Listener is focused on a
		//component.  The first redraw on movement would shift the window across the
		//screen an X, Y amount equal to the offset of the component within the JFrame.
		//==============================================================================
		Point currCoords = e.getLocationOnScreen();
		frame.setLocation(currCoords.x - mouseDownCompCoords.x - this.offset.x, currCoords.y - mouseDownCompCoords.y - this.offset.y);
	}
}
