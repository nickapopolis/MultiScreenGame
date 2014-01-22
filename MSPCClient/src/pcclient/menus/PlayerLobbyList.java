package pcclient.menus;
import pcclient.model.*;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JList;


public class PlayerLobbyList extends JList implements Observer
{

	public PlayerLobbyList()
	{
		super();
		setFont(new Font("Arial", Font.PLAIN, 25));
	}

	@Override
	public void update(Observable o, Object obj)
	{
		Lobby lobby = (Lobby)o;
		Vector<Player> players  = lobby.getPlayersInLobby();
		Vector<String> playerNames = new Vector<String>();
		
		for(int i=0;i<players.size();i++)
		{
			playerNames.add(players.get(i).getUserName());
		}
		this.setListData(playerNames);
	}
}
