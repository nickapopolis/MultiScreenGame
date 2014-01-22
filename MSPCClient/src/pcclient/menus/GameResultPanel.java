package pcclient.menus;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import pcclient.game.*;
import pcclient.model.*;


public class GameResultPanel extends JPanel
{
	Game game;
	BufferedImage starImg;
	
	public GameResultPanel()
	{
		try{
			starImg = ImageIO.read(new File("Images/star.png"));
			}
			catch(Exception e)
			{
				try {
					starImg = ImageIO.read(Game.class.getResource("Images/star.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void setGame(Game game)
	{
		this.game = game;
	}
	public void paint(Graphics g)
	{
		if(game != null)
		{
			int squareWidth = (int) (getWidth()* 0.25f);
			game.sortPlayersByScore();
			Vector<GamePlayer> players = game.getPlayers();
			for(int i=0; i<players.size();i++)
				g.drawString(players.get(i).getPlayer().getUserName(), squareWidth * i, 0);
				
				
		}
	}
}
