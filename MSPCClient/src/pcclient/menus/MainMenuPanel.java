package pcclient.menus;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class MainMenuPanel extends JPanel
{
	public JButton createGameButton;
	public JButton joinGameButton;
	public JButton exitGameButton;
	
	public MainMenuPanel()
	{
		super();
		initializeComponents();
	}
	private void initializeComponents()
	{
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		this.setLayout(layout);

		constraints.insets = new Insets(80,400,80,400);
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		
		createGameButton = new JButton("Create");
		constraints.gridx = 0;
		constraints.gridy = 0;
		createGameButton.setFont(new Font("Arial", Font.PLAIN, 100));
		add(createGameButton, constraints);
		
		joinGameButton = new JButton("Join");
		constraints.gridx = 0;
		constraints.gridy = 1;
		joinGameButton.setFont(new Font("Arial", Font.PLAIN, 100));
		add(joinGameButton, constraints);
		
		exitGameButton = new JButton("Exit");
		constraints.gridx = 0;
		constraints.gridy = 2;
		exitGameButton.setFont(new Font("Arial", Font.PLAIN, 100));
		add(exitGameButton, constraints);
	}
	public void setListener(ActionListener listener)
	{
		createGameButton.addActionListener(listener);
		joinGameButton.addActionListener(listener);
		exitGameButton.addActionListener(listener);
	}
}
