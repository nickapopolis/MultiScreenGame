package pcclient.game;
import pcclient.model.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;
import javax.swing.JFrame;


public class Game extends GameEngine 
{
	
	Level currentLevel;
	Vector<GamePlayer> players;
	GameInterface gui;
	/**
	 * Comparator to sort the game players by their score
	 */
	public static final Comparator<GamePlayer> playerScoreComparator = new Comparator<GamePlayer>() {
	
		@Override
		public int compare(GamePlayer p1, GamePlayer p2)
		{
			if(p1.getNumGoalObjects()>p2.getNumGoalObjects())
				return 1;
			else if(p1.getNumGoalObjects()<p2.getNumGoalObjects())
				return -1;
			else 
				return 0;
		}
	};
	
	
	public Game(JFrame parentFrame) 
	{
		super(parentFrame);
		players = new Vector<GamePlayer>();
		gui = new GameInterface();
		loadContent();
	}
	public void initialize()
	{
		super.initialize();
		
		//initialize player positions as random
		for(int i=0; i <players.size();i++)
			players.get(i).setPosition(currentLevel.getRandomLevelPosition());
	}
	public void loadContent()
	{
		Wall.loadContent();
		GoalObject.loadContent();
	}
	public void updateGame(long elapsedTime)
	{
		if(!running)
			return;
		
		//update player state
		for(int i=0; i <players.size();i++)
			players.get(i).update(elapsedTime);
		
		currentLevel.update(elapsedTime);
		
		checkCollisions();
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		if(!running)
			return;
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//draw level
		if (currentLevel!= null)
			currentLevel.paint(g2d, this);
		
		//draw players
		for(int i=0;i<players.size();i++)
			players.get(i).draw(g2d);
		
		//draw interface
		gui.draw(g2d, players, currentLevel, getWidth(), getHeight());
	}
	public void setLevel(Level lvl)
	{
		currentLevel = lvl;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			running = false;
			frame.setVisible(false);
			frame.setEnabled(false);
			frame.dispose();
			
		}
	}
	/**
	 * Detected that a player has changed their movement pattern, update their state to reflect this
	 * @param player
	 * @param newPos
	 * @param newVel
	 */
	public void updatePlayerState(Player player, Point2D newPos, Point2D newVel)
	{
		GamePlayer gamePlayer = null;
		
		//find the player in the list of players
		for(int i=0;i<players.size();i++)
		{
			GamePlayer curr = players.get(i);
			if(curr.getPlayer().equals(player))
			{
				//found the right player, update its position
				gamePlayer = curr;
				gamePlayer.setPositionFromCoords(newPos);
				break;
			}
		}
	}
	/**
	 * Detect collisions in the level
	 */
	public void checkCollisions()
	{
		
	}
	public Vector<GamePlayer> getPlayers()
	{
		return players;
	}
	
	public void sortPlayersByScore()
	{
		Collections.sort(players, playerScoreComparator);
	}
	public void setPlayers(Vector<Player> newPlayers)
	{
		players.clear();
		Dimension playerDim = new Dimension();
		playerDim.setSize(Level.spaceSize * Level.playerSpaceRatio,  Level.spaceSize * Level.playerSpaceRatio);
		for(int i=0;i<newPlayers.size();i++)
			players.add(new GamePlayer(newPlayers.get(i), playerDim));
	}
	public void handleCollision(Collision collision)
	{
		
	}

}

