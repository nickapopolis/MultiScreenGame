package pcclient.model;

import java.util.Observer;
import java.util.Vector;
import java.util.Observable;

public class Lobby extends Observable
{
	Vector<Player> playersInLobby;
	Vector<Player> playersInGameSlot;
	Vector<Boolean> readyState;
	int numPlayersInGameSlot;
	Vector<MenuStateChange> stateChanges;
	int lobbyState = 0;
	
	final int maxPlayersInGame = 4;
	
	public static final int JOIN_LOBBY = 0;
	public static final int LEAVE_LOBBY = 1;
	public static final int READY = 2;
	public static final int UNREADY= 3;
	public static final int JOIN_GAME = 4;
	public static final int LEAVE_GAME = 5;
	
	public static final int LOBBY_JOIN = 0;
	public static final int LOBBY_LEAVE  = 1;
	
	public static final int STATE_MAIN = 0;
	public static final int STATE_SERVER_SELECT = 1;
	public static final int STATE_LOBBY = 2;
	public static final int STATE_GAME = 3;
	
	
	
	public Lobby()
	{
		playersInGameSlot = new Vector<Player>();
		playersInLobby = new Vector<Player>();
		readyState = new Vector<Boolean>();
		stateChanges = new Vector<MenuStateChange>();
		numPlayersInGameSlot = 0;
	}
	public void playerJoinGame(String playerID)
	{
		synchronized(playersInGameSlot)
		{
			for(int i=0;i<playersInLobby.size();i++)
			{
				Player player = playersInLobby.get(i);

				//game not full and player isnt already in the lobby
				if(playerID.equals(player.getUserID()) && playersInGameSlot.size()<maxPlayersInGame && !playersInGameSlot.contains(player))
				{
					//add player to the game
					playersInGameSlot.add(player);
					//add a new value of ready state corresponding with the new player
					readyState.add(false);
					
					//record the change in lobby state
					makeChange(new MenuStateChange(player, JOIN_GAME));
				}
			}
		}
	}
	public void playerLeaveGame(String playerID)
	{
		synchronized(playersInGameSlot)
		{
			for(int i=0; i<playersInGameSlot.size(); i++)
			{
				Player player = playersInGameSlot.get(i);
				if (player.getUserID().equals(playerID))
				{
					//remove player to the game
					playersInGameSlot.remove(i);
					//remove the ready state corresponding to the player
					readyState.remove(i);
					
					//record the change of lobby state
					makeChange(new MenuStateChange(player, LEAVE_GAME));
					break;
				}
			}
		}
	}
	public void playerJoinLobby(Player player)
	{
		synchronized(playersInLobby)
		{
			// check if the lobby contains the player
			if(!playersInLobby.contains(player))
			{
				//add the player to the lobby
				playersInLobby.add(player);
				
				//record the change of lobby state
				makeChange(new MenuStateChange(player, JOIN_LOBBY));
			}
		}
	}
	public void playerLeaveLobby(String playerID)
	{
		synchronized(playersInLobby)
		{
			for(int i=0; i<playersInLobby.size(); i++)
			{
				Player player = playersInLobby.get(i);
				if (player.getUserID().equals(playerID))
				{
					//remove player from game
					playerLeaveGame(playerID);
					
					//remove player from lobby
					playersInLobby.remove(i);
					
					//record the change of lobby state
					makeChange(new MenuStateChange(player, LEAVE_LOBBY));
					break;
				}
			}
		}
	}
	public void playerReady(String playerID)
	{
		synchronized(playersInGameSlot)
		{
			for(int i=0; i<playersInGameSlot.size(); i++)
			{
				Player player = playersInGameSlot.get(i);
				if (player.getUserID().equals(playerID))
				{
					readyState.set(i, true);
					
					//record the change of lobby state
					makeChange(new MenuStateChange(player, READY));
					break;
				}
			}
		}
	}
	public void playerUnready(String playerID)
	{
		synchronized(playersInGameSlot)
		{
			for(int i=0; i<playersInGameSlot.size(); i++)
			{
				Player player = playersInGameSlot.get(i);
				if (player.getUserID().equals(playerID))
				{
					readyState.set(i, false);
					
					//record the change of lobby state
					makeChange(new MenuStateChange(player, UNREADY));
					break;
				}
			}
		}
	}
	/**
	 * Returns the changes made and clears the list
	 * @return
	 */
	public Vector<MenuStateChange> fetchStateChanges()
	{
		Vector<MenuStateChange> changes = stateChanges;
		stateChanges = new Vector<MenuStateChange>();
		return changes;
	}
	
	public Vector<Player> getPlayersInGameSlot() 
	{
		return playersInGameSlot;
	}
	public Vector<Player> getPlayersInLobby()
	{
		return playersInLobby;
	}
	public Vector<Boolean> getReadyState()
	{
		return readyState;
	}
	public void makeChange(MenuStateChange change)
	{
		System.out.println("Made change to lobby");
		stateChanges.add(change);
		setChanged();
		notifyObservers();
	}
	public boolean allPlayersReady()
	{
		if(readyState.size()==0)
			return false;
		for(int i=0;i<readyState.size();i++)
		{
			if(!readyState.get(i))
				return false;
		}
		return true;
		
	}
	public void setState(int i)
	{
		lobbyState = i;
	}
	public int getLobbyState()
	{
		return lobbyState;
	}
}
