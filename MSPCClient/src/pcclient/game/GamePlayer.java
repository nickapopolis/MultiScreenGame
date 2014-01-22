package pcclient.game;
import pcclient.model.*;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class GamePlayer extends GameObject
{
	Player player;
	int numGoalObjects;
	public GamePlayer(Player player, Dimension dim)
	{
		super(new Point2D.Float(), dim, 0, null);
		this.player = player;
		pos = convertCoordsToPos(pos);
		numGoalObjects = 5;
	}
	public int getNumGoalObjects()
	{
		return numGoalObjects;
	}
	public void addNumGoalObjects(int num)
	{
		numGoalObjects = Math.max(0, numGoalObjects + num);
	}
	public Point2D convertCoordsToPos(Point2D coords)
	{
		Point2D position = new Point2D.Float();
		/*
		position.setLocation(x, y)
		if(horizontal)
		{
			position.setLocation(coords.getX() * dim.getWidth() +  dim.getWidth()* 0.5f, 
							coords.getY() * dim.getHeight());
		}
		else
		{
			position.setLocation(coords.getX() * dim.getWidth(),
							coords.getY() * dim.getHeight() +  dim.getHeight()* 0.5f);
		}
		*/
		return position;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	public void setPositionFromCoords(Point2D newPos)
	{
		pos = convertCoordsToPos(newPos);
	}
	@Override
	protected BufferedImage getImage()
	{
		return null;
	}
}
