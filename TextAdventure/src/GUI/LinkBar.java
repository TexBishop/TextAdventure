/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.Cursor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import Structure.GameState;

//=====================================================================
//The top link bar of our application
//=====================================================================
@SuppressWarnings("serial")
public class LinkBar extends JPanel 
{
	private TextWindow inventoryWindow;
	private TextWindow helpWindow;
	
	public LinkBar(Application application) 
	{		
    	//===============================================================
		//Set the parameters for this panel
    	//===============================================================
		setBorder(new MatteBorder(0, 0, 4, 0, new Color(0, 0, 0)));
		setBackground(new Color(255, 255, 250));
		this.setBounds(0, 0, application.frame.getWidth(), 32);
		setLayout(null);

    	//===============================================================
		//Restart button link
    	//===============================================================
		JLabel restartLink = new JLabel("Restart");
		restartLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		restartLink.setBounds(20, 0, 50, 30);
		restartLink.setForeground(Color.BLACK);
		restartLink.setFont(new Font("Arial Black", Font.PLAIN, 12));
		this.add(restartLink);
		restartLink.addMouseListener(new MouseAdapter()
		{
			//==========================================================================
            //Reset current game
            //==========================================================================
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				application.resetGame();
			}
		});

    	//===============================================================
		//Save button link
    	//===============================================================
		JLabel saveLink = new JLabel("Save");
		saveLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveLink.setBounds(95, 0, 40, 30);
		saveLink.setForeground(Color.BLACK);
		saveLink.setFont(new Font("Arial Black", Font.PLAIN, 12));
		this.add(saveLink);
		saveLink.addMouseListener(new MouseAdapter()
		{
			//==========================================================================
            //Save current game.
			//Use a JFileChooser to set the extension and get the file name.
			//Then use ObjectOutputStream to write our GameState object to the file.
            //==========================================================================
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Adventure Save Game (.adventure)", "adventure"));
				fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[0]);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) 
				{
					try 
					{
						File file;
				    	//===============================================================
						//If the name of the file already ends with .adventure, save it
						//without appending the file type to the end.  If it doesn't have
						//.adventure already, then append .adventure to the end of the filename.
				    	//===============================================================
						if (fileChooser.getSelectedFile().getCanonicalPath().endsWith("." + ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0]))
							file = new File(fileChooser.getSelectedFile().getCanonicalPath());
						else
							file = new File(fileChooser.getSelectedFile().getCanonicalPath() + "." + ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0]);
						
						FileOutputStream fileStream = new FileOutputStream(file);
						ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
						objectStream.writeObject(application.getGameState());
						objectStream.close();
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		});

    	//===============================================================
		//Load button link
    	//===============================================================
		JLabel loadLink = new JLabel("Load");
		loadLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loadLink.setBounds(155, 0, 40, 30);
		loadLink.setFont(new Font("Arial Black", Font.PLAIN, 12));
		loadLink.setToolTipText("Load a saved game");
		this.add(loadLink);
		loadLink.addMouseListener(new MouseAdapter()
		{
			//==========================================================================
            //Load a saved game
			//Use a JFileChooser set the extension and get the file name.
			//Then use ObjectInputStream to overwrite our GameState object with
			//the GameState object from the file, using loadGame().
            //==========================================================================
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Adventure Save Game (.adventure)", "adventure"));
				fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[0]);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
				{
					try 
					{
						File file = new File(fileChooser.getSelectedFile().getCanonicalPath());
						FileInputStream fileStream = new FileInputStream(file);
						ObjectInputStream objectStream = new ObjectInputStream(fileStream);
						
						application.loadGame((GameState) objectStream.readObject());
						objectStream.close();
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					} 
					catch (ClassNotFoundException e1) 
					{
						System.out.println("GameState object not found in file.");
						e1.printStackTrace();
					}
				}
			}
		});

    	//===============================================================
		//Inventory button link
    	//===============================================================
		inventoryWindow = new InventoryWindow(application);
		inventoryWindow.setVisible(false);
		JLabel inventoryLink = new JLabel("Inventory");
		inventoryLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		inventoryLink.setBounds(215, 0, 72, 30);
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
				if (inventoryWindow.isVisible() == false)
				{
					inventoryWindow.updateContents();
					inventoryWindow.setVisible(true);
				}
				else
					inventoryWindow.setVisible(false);
			}
		});

    	//===============================================================
		//Help button link
    	//===============================================================
		helpWindow = new HelpWindow(application);
		helpWindow.setVisible(false);
		JLabel helpLink = new JLabel("Help");
		helpLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		helpLink.setBounds(305, 0, 40, 30);
		helpLink.setHorizontalAlignment(SwingConstants.LEFT);
		helpLink.setFont(new Font("Arial Black", Font.PLAIN, 12));
		this.add(helpLink);
		helpLink.addMouseListener(new MouseAdapter()
		{
			//==========================================================================
            //Display help window
            //==========================================================================
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if (helpWindow.isVisible() == false)
				{
					helpWindow.updateContents();
					helpWindow.setVisible(true);
				}
				else
					helpWindow.setVisible(false);
			}
		});

    	//===============================================================
		//Close application link
    	//===============================================================
		JButton closeButton = new JButton("X");
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setFocusPainted(false);
		closeButton.setBounds(application.frame.getWidth() - 35, 5, 20, 20);
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
				application.frame.dispatchEvent(new WindowEvent(application.frame, WindowEvent.WINDOW_CLOSING));
			}
		});
	}
	
	public TextWindow getInventoryWindow()
	{
		return this.inventoryWindow;
	}
}
