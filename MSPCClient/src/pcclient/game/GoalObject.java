package pcclient.game;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class GoalObject extends GameObject
{
	static BufferedImage goalImg = null;
	
	public GoalObject(Point2D pos, Dimension dim)
	{
		super(pos, dim, 0, goalImg);
	}
	public static void loadContent()
	{
		goalImg = loadContent("Images/star.png");
	}
	@Override
	protected BufferedImage getImage()
	{
		return goalImg;
	}
	
	public void update(long elapsedTime)
	{
		rot += elapsedTime/1000.0f * 0.5f;
	}
	

}
