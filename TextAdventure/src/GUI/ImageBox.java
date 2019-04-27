/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package GUI;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JTextPane;

import java.awt.Point;

//=====================================================================
//The panel containing the ASCII art image in our application
//=====================================================================
@SuppressWarnings("serial")
public class ImageBox extends JPanel
{
	private FrameDragListener frameDragListener;
	private JTextPane textPane;
	
	/**
	 * Default constructor. Create the ImageBox panel.
	 */
	public ImageBox(Application application) 
	{
    	//===============================================================
		//Set the parameters for this panel
    	//===============================================================
		this.setBorder(new MatteBorder(0, 0, 4, 0, new Color(0, 0, 0)));
		this.setBackground(new Color(255, 255, 250));
		this.setBounds(0, 28, application.frame.getWidth(), application.imageHeight + 10);
		this.setLayout(null);

    	//===============================================================
		//The text area containing the ASCII art being displayed.
    	//===============================================================
		textPane = new JTextPane();
		textPane.setFont(application.frame.getFont());
		textPane.setBackground(new Color(255, 255, 250));
		textPane.setBounds(0, 5, application.frame.getWidth(), application.imageHeight);
		textPane.setEditable(false);
		textPane.setHighlighter(null);
		this.add(textPane);

    	//===============================================================
		//Set the alignment of textPane to centered
    	//===============================================================
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

    	//===============================================================
		//FrameDragListener object initialization, to make the ASCII image 
		//area capable of click-dragging the application window
    	//===============================================================
		this.frameDragListener = new FrameDragListener(application.frame, new Point(5, 26));
		textPane.addMouseListener(frameDragListener);
		textPane.addMouseMotionListener(frameDragListener);
	}
	
	/**
	 * Change the displayed ASCII image.
	 * @param image String The ASCII image to display.
	 */
	public void setImage(String image)
	{
		textPane.setText(image);
	}
}
	