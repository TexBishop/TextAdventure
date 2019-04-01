/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

//=====================================================================
//The top link bar of our application
//=====================================================================
@SuppressWarnings("serial")
public class LinkBar extends JPanel 
{
	/**
	 * Default constructor.  Create this LinkBar and add components.
	 */
	public LinkBar(JFrame frame) 
	{		
		//Set the parameters for this panel
		setBorder(new LineBorder(new Color(0, 0, 0), 4));
		setBackground(new Color(255, 255, 250));
		this.setBounds(0, 0, 800, 30);
		setLayout(null);
		
		//Save button link
		JLabel saveLink = new JLabel("Save");
		saveLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveLink.setBounds(20, 0, 50, 30);
		saveLink.setForeground(Color.BLACK);
		saveLink.setFont(new Font("Arial Black", Font.PLAIN, 12));
		this.add(saveLink);
		saveLink.addMouseListener(new MouseAdapter()
		{
			//==========================================================================
            //Save current game
            //==========================================================================
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				//code to save game
			}
		});
		
		//Load button link
		JLabel loadLink = new JLabel("Load");
		loadLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loadLink.setBounds(80, 0, 50, 30);
		loadLink.setFont(new Font("Arial Black", Font.PLAIN, 12));
		loadLink.setToolTipText("Load a saved game");
		this.add(loadLink);
		loadLink.addMouseListener(new MouseAdapter()
		{
			//==========================================================================
            //Load a saved game
            //==========================================================================
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				//code to load a saved game
			}
		});
		
		//Inventory button link
		JLabel inventoryLink = new JLabel("Inventory");
		inventoryLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		inventoryLink.setBounds(140, 0, 72, 30);
		inventoryLink.setHorizontalAlignment(SwingConstants.LEFT);
		inventoryLink.setFont(new Font("Arial Black", Font.PLAIN, 12));
		this.add(inventoryLink);
		inventoryLink.addMouseListener(new MouseAdapter()
		{
			//==========================================================================
            //Display inventory
            //==========================================================================
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				//code to display inventory
			}
		});
		
		//Close application link
		JButton closeButton = new JButton("X");
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setFocusPainted(false);
		closeButton.setBounds(765, 5, 20, 20);
		closeButton.setBorder(new LineBorder(new Color(0, 0, 0)));
		closeButton.setBackground(new Color(255, 255, 250));
		closeButton.setFont(new Font("Arial Black", Font.PLAIN, 12));
		this.add(closeButton);
		closeButton.addActionListener(new ActionListener() 
		{
			//==========================================================================
            //Close the application
            //==========================================================================
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
	}
}
