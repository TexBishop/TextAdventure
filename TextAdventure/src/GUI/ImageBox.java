/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package GUI;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Point;

//=====================================================================
//The panel containing the ASCII art image in our application
//=====================================================================
@SuppressWarnings("serial")
public class ImageBox extends JPanel
{
	private FrameDragListener frameDragListener;
	private JTextArea textArea;
	
	/**
	 * Default constructor. Create the ImageBox panel.
	 */
	public ImageBox(JFrame frame) 
	{
    	//===============================================================
		//Set the parameters for this panel
    	//===============================================================
		this.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		this.setBackground(new Color(255, 255, 250));
		this.setBounds(0, 26, 800, 500);
		this.setLayout(null);

    	//===============================================================
		//The text area containing the ASCII art being displayed.
    	//===============================================================
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Monospaced", Font.BOLD, 8));
		textArea.setBackground(new Color(255, 255, 250));
		textArea.setBounds(5, 5, 790, 490);
		textArea.setEditable(false);
		textArea.setHighlighter(null);
		this.add(textArea);

    	//===============================================================
		//FrameDragListener object initialization, to make the ASCII image 
		//area capable of click-dragging the application window
    	//===============================================================
		this.frameDragListener = new FrameDragListener(frame, new Point(5, 26));
		textArea.addMouseListener(frameDragListener);
		textArea.addMouseMotionListener(frameDragListener);
	}
	
	/**
	 * Change the displayed ASCII image.
	 * @param image String The ASCII image to display.
	 */
	public void setImage(String image)
	{
		textArea.setText(image);
	}
}
	