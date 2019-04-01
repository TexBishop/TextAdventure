package GUI;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class CommandBox extends JPanel 
{

	/**
	 * Create the panel.
	 */
	public CommandBox() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0), 4));
		setBackground(new Color(255, 255, 250));
		this.setBounds(0, 530, 800, 400);
	}
}
