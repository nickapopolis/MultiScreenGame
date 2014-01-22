package pcclient.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
public class GameInterface
{
	protected BufferedImage starImg;
	public GameInterface()
	{
		
		try{
			starImg = ImageIO.read(new File("Images/translucentstar.png"));
			}
			catch(Exception e)
			{
				try {
					starImg = ImageIO.read(Game.class.getResource("Images/translucentstar.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void draw(Graphics2D g, Vector<GamePlayer> players, Level level, int width, int height)
	{
		int starSize = 50;
		int fontSize = 50;
		//set font and color 
		g.setColor(new Color(0,0,1, 0.4f));
		g.setFont(new Font("Charcoal", Font.PLAIN, fontSize));
		
		//draw player scores
		if(players.size()>0)
			drawScore(g, players.get(0), 0,fontSize, starSize, fontSize);
		if(players.size()>1)
			drawScore(g, players.get(1), (int) width - 100, fontSize, starSize, fontSize);
		if(players.size()>2)
			drawScore(g, players.get(2), 0, (int) height  - starSize - fontSize, starSize, fontSize);
		if(players.size()>3)
			drawScore(g, players.get(3), (int) width  - 100, (int) height  - starSize - fontSize, starSize, fontSize);
		
	}
	/**
	 * Draw the score of the player at a specified position
	 * @param player
	 * @param x
	 * @param y
	 */
	public void drawScore(Graphics2D g, GamePlayer player, int x, int y, int starSize, int fontSize)
	{
		if(player == null)
			return;

		g.drawString(player.getPlayer().getUserName(), x, y);
		for(int i=0; i<player.getNumGoalObjects();i++)
		{
			g.drawImage(starImg, x + i* starSize, y  ,starSize, starSize, null);
		}
	}
}
