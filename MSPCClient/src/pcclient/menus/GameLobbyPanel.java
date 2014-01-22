package pcclient.menus;
import pcclient.game.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class GameLobbyPanel extends JPanel 
{
	private PlayerReadyPanel playerReadyPanel;
	private LevelPreviewPanel levelPreviewPanel;
	
	public GameLobbyPanel()
	{
		super();
		initializeComponents();
	}
	private void initializeComponents()
	{
		//gridbag layout
				GridBagLayout layout = new GridBagLayout();
				setLayout(layout);
				
				//constraints for layout
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.insets = new Insets(5, 5, 5, 5);
				constraints.fill = GridBagConstraints.BOTH;

				levelPreviewPanel = new LevelPreviewPanel(new Level(30,30));
				constraints.gridwidth = 1;
				constraints.gridheight = 1;
				constraints.gridx = 0;
				constraints.gridy = 0;
				constraints.weightx = 1;
				constraints.weighty = 0.5;
				add(levelPreviewPanel, constraints);
				
				playerReadyPanel = new PlayerReadyPanel();
				constraints.gridwidth = 1;
				constraints.gridheight = 1;
				constraints.gridx = 0;
				constraints.gridy = 1;
				constraints.weightx = 1;
				constraints.weighty = 0.5;
				add(playerReadyPanel, constraints);
	}
	public void setObservers(Observable lobby)
	{
		lobby.addObserver(playerReadyPanel);
	}
}
