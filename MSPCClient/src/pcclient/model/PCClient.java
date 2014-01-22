package pcclient.model;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import pcclient.events.EventListener;
import pcclient.events.LobbyStateEvent;
import pcclient.game.*;
import pcclient.networking.*;
import pcclient.menus.*;


public class PCClient implements ActionListener, EventListener
{
	Lobby lobby;
	ConnectionManager connectionManager;
	Game game;
	GameMenuPanel menuPanel;
	JFrame frame;
	CardLayout cards;
	JPanel cardPanel;
	
	public PCClient()
	{
		//init logfile
		LogFile.init("PCClientLog.txt");
		LogFile.write("initialize PCClient");
		//set borders to default OS windows
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {     }
		frame = new JFrame();
		frame.setSize(1800, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		addListeners();
		
		//init lobby 
		lobby = new Lobby();
		
		//init networking
		connectionManager = new ConnectionManager(this);
		
		//init game
		game = new Game(frame);
		game.setLevel(new Level(32,18));
		
		
		//init interface
		menuPanel = new GameMenuPanel(frame);
		menuPanel.requestFocus();
		menuPanel.setObservers(lobby);
		menuPanel.setListener(this);
		

		
		cards = new CardLayout();
		cardPanel = new JPanel(cards);
		cardPanel.add(menuPanel);
		cardPanel.add(game);
		frame.add(cardPanel);
		
		
		runTests();
		
	}
	public void createLobby()
	{
		//create a lobby with host duties
		menuPanel.setView(GameMenuPanel.LOBBY);
		lobby.setState(Lobby.STATE_LOBBY);
		for(int i=0;i<lobby.getPlayersInLobby().size();i++)
		{
			LobbyStateEvent event = new LobbyStateEvent(lobby.getPlayersInLobby().get(i).getUserID(), connectionManager.getID(),lobby.getLobbyState() );
			connectionManager.queueEvent(event);
		}
	}
	public void joinLobby()
	{
		//merge lobby into main lobby
		menuPanel.setView(GameMenuPanel.SERVER_SELECTION);
	}
	public void leaveLobby()
	{
		//unmerge lobby and leave
		//menuPanel
	}
	public void exitGame()
	{
		game.stop();
		connectionManager.stop();
		frame.dispose();
		System.exit(0);
	}
	public void startGame()
	{
		Vector<Player> players = lobby.getPlayersInGameSlot();
		int lobbyState = Lobby.STATE_GAME;
		lobby.setState(lobbyState);
		for(int i=0;i<players.size();i++)
		{
			LobbyStateEvent event = new LobbyStateEvent(players.get(i).getUserID(), connectionManager.getID(), lobbyState);
			connectionManager.queueEvent(event);
		}
		game.setPlayers(players);
		game.initialize();
		cards.next(cardPanel);
		
	}
	public static void main(String args[])
	{
		new PCClient();
	}
	public void actionPerformed(ActionEvent evt)
	{
		Object source = evt.getSource();
		if(source == menuPanel.exitButton || source == menuPanel.mainMenuPanel.exitGameButton)
			exitGame();
		else if(source == menuPanel.backButton)
		{
			menuPanel.setView(GameMenuPanel.MAIN_MENU);
		}
		else if(source == menuPanel.mainMenuPanel.createGameButton)
		{
			createLobby();
		}
		else if(source == menuPanel.mainMenuPanel.joinGameButton)
			joinLobby();
	}
	public void addListeners()
	{
		frame.addWindowListener(new WindowListener(){

			
			public void windowActivated(WindowEvent arg0) {
			}
			public void windowClosed(WindowEvent arg0) {
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				exitGame();
			}
			public void windowDeactivated(WindowEvent arg0) {
			}
			public void windowDeiconified(WindowEvent arg0) {
			}
			public void windowIconified(WindowEvent arg0) {
			}
			public void windowOpened(WindowEvent e) {
			}
			});
	}
	public void runTests()
	{
		//lobby
		//run tests
		//Player player = new Player(1,"Nick");
		frame.setVisible(true);

		/*
		lobby.playerJoinLobby(player);
		lobby.playerJoinGame(player);
		lobby.playerReady(player);
				
		player = new Player(2,"Jim");
		lobby.playerJoinLobby(player);
		lobby.playerJoinGame(player);
				
		player = new Player(3,"Alan");
		lobby.playerJoinLobby(player);
		*/
		//startGame();
	}
	
	/**
	 * Player has performed some sort of action inside of the games menu
	 */
	@Override
	public void playerLobbyEvent(String playerID, int actionType) 
	{
		System.out.println("player lobby event");
		switch(actionType)
		{
			case Lobby.JOIN_LOBBY:
			
				//player id is made up of name, address, port seperated by -
				String info[] = playerID.split("-");
				
				//add player to the connectionManager
				ConnectionInfo playerInfo = new ConnectionInfo(playerID, info[4], info[5]);
				connectionManager.addPlayerConnection(playerID, playerInfo);
				
				//add player to the lobby
				Player player = new Player(playerID, info[0]);
				lobby.playerJoinLobby(player);
				LobbyStateEvent event = new LobbyStateEvent(player.getUserID(), connectionManager.getID(), lobby.getLobbyState());
				connectionManager.queueEvent(event);
				break;
				
			case Lobby.LEAVE_LOBBY:
				lobby.playerLeaveLobby(playerID);
				connectionManager.removePlayerConnection(playerID);
				break;
			case Lobby.JOIN_GAME:
				lobby.playerJoinGame(playerID);
				break;
			case Lobby.LEAVE_GAME:
				lobby.playerLeaveGame(playerID);
				break;
			case Lobby.READY:
				boolean preReadyState = lobby.allPlayersReady();
				lobby.playerReady(playerID);
				if(preReadyState && lobby.allPlayersReady())
				{
					startGame();
				}
				break;
			case Lobby.UNREADY:
				lobby.playerUnready(playerID);
				break;
		}
	}
	
	
	
	/**
	 * A pc lobby has performed an action on this lobby
	 */
	@Override
	public void pcLobbyEvent(String lobbyID, int actionType) 
	{
		switch(actionType)
		{
		case Lobby.LOBBY_JOIN:
			//lobby id is made up of name, address, port seperated by -
			String info[] = lobbyID.split("-");
			
			//add lobby to the connectionManager
			ConnectionInfo lobbyInfo = new ConnectionInfo(info[0], info[1], info[2]);
			connectionManager.addLobbyConnection(lobbyID, lobbyInfo);
			break;
		case Lobby.LOBBY_LEAVE:
			connectionManager.removeLobbyConnection(lobbyID);
			break;
		}
	}
	
	/**
	 * Contains the data with which the next level will use to be created
	 */
	@Override
	public void gameLoadEvent(float spaceSizeRatio, float spacePlayerRatio,
			int levelWidth, int levelHeight, Wall[] walls) 
	{
		System.out.println("Game load event");
	}
	
	/**
	 * The status of the game has changed, could have been started, paused, finished
	 */
	@Override
	public void gameStatusEvent(int actionType) 
	{
		
	}
	/**
	 * A player has moved, update their heading
	 */
	@Override
	public void gamePlayerMovementEvent(String playerID, Point2D pos,
			Point2D vel, float orient) 
	{
		
	}
	
	/**
	 * 
	 */
	@Override
	public void gamePlayerActionEvent(String playerID, int actionType) 
	{
		
	}
}
