package pcclient.game;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class Wall extends GameObject
{

	Point2D coordinates;
	int wallType;
	
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	public static final int CORNER = 2;
	boolean collision;
	protected static BufferedImage wallImg;
	
	public Wall(int wallType, Point2D coords, Dimension dim, boolean collisionWall)
	{
		super(null, dim, 0.0f, wallImg);
		setWallType(wallType);
		coordinates = coords;
		collision= collisionWall;
		pos = convertCoordsToPos(coordinates);
	}
	public static void loadContent()
	{
		wallImg = loadContent("Images/wall.png");
	}
	public int getWallType()
	{
		return wallType;
	}

	public void setWallType(int wallType)
	{
		this.wallType = wallType;
	}
	
	public Point2D convertCoordsToPos(Point2D coords)
	{
		Point2D position = new Point2D.Float();
		float spaceSize = (float) (dim.getWidth() + dim.getHeight());
		
		if(wallType == HORIZONTAL)
		{
			position.setLocation(spaceSize*0.5f +spaceSize*coords.getX() , spaceSize *coords.getY());
		}
		else if(wallType == VERTICAL)
		{
			position.setLocation(spaceSize * coords.getX(),spaceSize*0.5f +spaceSize*coords.getY());
		}
		else if(wallType == CORNER)
		{
			position.setLocation(spaceSize * coords.getX(),spaceSize*coords.getY());
		}
		
		return position;
	}
	@Override
	protected BufferedImage getImage()
	{
		return wallImg;
	}
	
	public Point2D getCoords()
	{
		return coordinates;
	}
	public boolean isCollisionWall()
	{
		return collision;
	}
}
