package pcclient.menus;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pcclient.networking.*;
public class ServerSelectionPanel extends JPanel
{

	JList serverList;
	JButton addServerButton;
	
	public ServerSelectionPanel(ArrayList<ConnectionInfo> serverConnectionInfo)
	{
		super();
		initializeComponents();
		
	}
	
	@SuppressWarnings("unchecked")
	private void initializeComponents()
	{
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		this.setLayout(layout);
		
		//constraints.insets= new Insets(20,200,20,200);
		serverList = new JList(new String[]{"<HTML>Nick <BR>Address: 192.168.1.64<BR>Port: 5448</HTML>", 
											"<HTML>Jim <BR>Address: 192.168.1.64<BR>Port: 5448</HTML>", 
											"<HTML>Alan <BR>Address: 192.168.1.64<BR>Port: 5448</HTML>"});
		serverList.setFont(new Font("Arial", Font.PLAIN, 30));
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.ipadx = 400;
		JScrollPane serverListScrollBar = new JScrollPane(serverList);
		constraints.anchor = GridBagConstraints.CENTER;
		add(serverListScrollBar, constraints);
		
		addServerButton = new JButton("Search");
		addServerButton.setFont(new Font("Arial", Font.PLAIN, 25));
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.ipady = 50;
		constraints.anchor = GridBagConstraints.SOUTH;
		add(addServerButton, constraints);
	}
	public void shiftMenuSelection(int amount)
	{
		int selected = serverList.getMinSelectionIndex();
		int size = serverList.getModel().getSize();
		
		//nothing selected
		if(selected == -1)
		{
			if(amount <0)
				selected = size-amount;
			else
				selected = -1 + amount;
		}
		else
		{
			selected = selected + amount;
		}
		
		//bound check on index
		if(selected>= size)
			selected = size-1;
		else if(selected <0)
			selected = 0;
		
		serverList.setSelectedIndex(selected);
		
	}
}
