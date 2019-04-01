/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package GUI;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

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

	/**
	 * Default constructor for our FrameDragListener class.
	 * @param frame JFrame The primary JFrame object representing our application window.
	 */
	public FrameDragListener(JFrame frame) 
	{
		this.frame = frame;
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
		Point currCoords = e.getLocationOnScreen();
		frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
	}
}
