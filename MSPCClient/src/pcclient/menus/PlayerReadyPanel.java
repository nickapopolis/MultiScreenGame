package pcclient.menus;
import pcclient.model.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class PlayerReadyPanel extends JPanel implements Observer
{
	
	JLabel[] playerLabels;
	int maxPlayers = 4;
	
	public PlayerReadyPanel()
	{
		
		GridLayout layout = new GridLayout(2,0);
		layout.setHgap(15);
		layout.setVgap(15);
		setLayout(layout);
		
		playerLabels = new JLabel[maxPlayers];
		for(int i=0;i<maxPlayers;i++)
		{
			playerLabels[i] = new JLabel("");
			playerLabels[i].setFont(new Font("Arial", Font.PLAIN, 120));
			playerLabels[i].setBackground(Color.GRAY);
			playerLabels[i].setOpaque(true);
			add(playerLabels[i]);
		}
	}
	
	@Override
	public void update(Observable o, Object arg)
	{
		Lobby lobby = (Lobby)o;
		
		Vector<Player> players = lobby.getPlayersInGameSlot();
		Vector<Boolean> readyState = lobby.getReadyState();
		
		int i=0;
		for(i=0;i<players.size(); i++)
		{
			playerLabels[i].setText("<html><font color=darkGrey>" + players.get(i).getUserName()+ "</font></html>");
			if(readyState.get(i) == true)
				playerLabels[i].setBackground(Color.green);
			else
				playerLabels[i].setBackground(Color.red);
		}
		for(int j=i;j<maxPlayers;j++)
		{
			playerLabels[j].setText("");
			playerLabels[j].setBackground(Color.GRAY);
		}
	}
}
