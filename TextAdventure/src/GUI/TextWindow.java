/**
 * A Text Adventure
 * @author Tex Bishop
 * @version 1.2
 * @date 05-16-2019
 */

package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Abstract class, creates a basic window for displaying a String of text.
 */
@SuppressWarnings("serial")
public abstract class TextWindow extends JFrame 
{
	protected Application application;
	protected JFrame frame;
	protected JPanel contentPane;
	private FrameDragListener frameDragListener;
	private FrameDragListener textDragListener;
	private Point screenPosition = new Point();	
	private JLabel nameLabel;
	private JTextArea textArea;

	//===============================================================
	//The abstract methods to be inherited by child classes.
	//===============================================================
	/**
	 * Sets the title label at the top of the window
	 * @return The String containing the title.
	 */
	protected abstract String setTitle();
	/**
	 * Sets the text to be displayed in the window
	 * @return The String containing the text to display.
	 */
	protected abstract String setText();
	
	public void updateContents()
	{
		textArea.setText(this.setText());
		textArea.setCaretPosition(0);
	}

	/**
	 * Constructor.  Create the frame.
	 */
	public TextWindow(Application application) 
	{
		this.application = application;
		
    	//===============================================================
		//Set parameters for this window's outer container
    	//===============================================================
		this.frame = this;
		this.setBounds(100, 100, 300, (int) (application.frame.getHeight()/2.7));
		this.setUndecorated(true);
		this.setBackground(new Color(255, 255, 250));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

    	//===============================================================
		//Set parameters for this window's content panel
    	//===============================================================
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		contentPane.setBackground(new Color(255, 255, 250));
		contentPane.setLayout(null);
		this.setContentPane(contentPane);

    	//===============================================================
		//Title label at the top of the window
    	//===============================================================
		nameLabel = new JLabel(this.setTitle());
		nameLabel.setBounds(10, 10, 100, 20);
		nameLabel.setFont(new Font("Arial Black", Font.PLAIN, 12));
		contentPane.add(nameLabel);

    	//===============================================================
		//Close window link
    	//===============================================================
		JButton closeButton = new JButton("X");
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setFocusPainted(false);
		closeButton.setBounds(270, 10, 20, 20);
		closeButton.setBorder(new LineBorder(new Color(0, 0, 0)));
		closeButton.setBackground(new Color(255, 255, 250));
		closeButton.setFont(new Font("Arial Black", Font.PLAIN, 14));
		contentPane.add(closeButton);
		closeButton.addActionListener(new ActionListener() 
		{
			//==========================================================================
            //Close the window
            //==========================================================================
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.setVisible(false);
			}
		});
		
    	//===============================================================
		//The text area that will be used as the text display
    	//===============================================================
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLocation(0, 0);
		textArea.setBackground(new Color(255, 255, 250));
		textArea.setFont(new Font("Aria", Font.PLAIN, 14));
		textArea.setBorder(null);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

    	//===============================================================
		//A scroll pane, to make our text area scroll-able
    	//===============================================================
		JScrollPane scrollBar = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBar.setBounds(15, 40, 270, (int) (application.frame.getHeight()/2.7 - 50));
		scrollBar.getVerticalScrollBar().setPreferredSize(new Dimension(10, 350));
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
		getContentPane().add(scrollBar);

    	//===============================================================
		//A line separator, set between the text area and the top bar
    	//===============================================================
		JSeparator separator = new JSeparator();
		separator.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		separator.setForeground(new Color(0, 0, 0));
		scrollBar.setColumnHeaderView(separator);
		
		//===============================================================
		//Add a mouse listener to the text area, to catch the offset for
		//redrawing on drag, when the text area has been scrolled down
		//===============================================================
		textArea.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				screenPosition.x = 15;
				if (scrollBar.getVerticalScrollBar().getModel().getValue() == 0)
					screenPosition.y = 40;
				else
					screenPosition.y = 40 - scrollBar.getVerticalScrollBar().getModel().getValue();
			}
		});

    	//===============================================================
		//FrameDragListener object initialization, to make our undecorated 
		//frame drag-able
    	//===============================================================
		this.frameDragListener = new FrameDragListener(frame, new Point(0, 0));
		this.frame.addMouseListener(frameDragListener);
		this.frame.addMouseMotionListener(frameDragListener);

    	//===============================================================
		//FrameDragListener object initialization, to make our frame 
		//drag-able from the text area
    	//===============================================================
		this.textDragListener = new FrameDragListener(frame, screenPosition);
		textArea.addMouseListener(textDragListener);
		textArea.addMouseMotionListener(textDragListener);
	}
}
