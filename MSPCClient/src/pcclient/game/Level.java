package pcclient.game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Vector;



public class Level 
{
	protected enum WallPosition{LEFT, RIGHT, TOP, BOTTOM, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
	
	int width;
	int height;
	
	public static float playerSpaceRatio = 0;
	public static float wallSpaceRatio = 0;
	public static float itemSpaceRatio = 0;
	
	protected static int spaceSize = 0;
	protected int wallLength = 0;
	protected int wallThickness = 0;
	protected int playerSize = 0;
	protected int itemSize = 0;
	
	
	Vector<Wall> collisionWalls;
	Vector<Wall> nonCollisionWalls;
	GoalObject goal;
	Space[][] spaces;
	
	public Level(int width, int height)
	{
		this.width = width;
		this.height = height;
		collisionWalls = new Vector<Wall>();
		nonCollisionWalls = new Vector<Wall>();
		setSpaceSize(60);
		setObjectRatio(0.5f, 0.2f, 0.5f);
		createLevel();
		
	}
	private void createLevel()
	{
		Random rand = new Random(System.currentTimeMillis());

		//create spaces
		spaces = new Space[width][height];
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				spaces[i][j] = new Space(i, j);
			}
		}
		
		//create walls, 
		//cycle through spaces and create a wall a certain percentage of the time
		//to the top or top left of the space. On corner spaces automatically create wall
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				//vertical walls
				if(i==0)
				{
					//left boundary
					//verticalWalls.add(new Wall(Wall.VERTICAL, new Point(i, j)));
				}
				else
				{
					//right boundary
					if (i == width-1)
						collisionWalls.add(new Wall(Wall.VERTICAL, new Point2D.Float(i+1, j), new Dimension(wallThickness, wallLength), true) );
					
					if(rand.nextDouble()> 0.5)
						collisionWalls.add(new Wall(Wall.VERTICAL, new Point2D.Float(i, j), new Dimension(wallThickness, wallLength), true));
					
				}
				
				//horizontal walls
				if(j==0)
				{
					//top boundary
					//horizontalWalls.add(new Wall(Wall.HORIZONTAL, new Point(i, j)));
				}
				else
				{
					//bottom boundary
					if (j == height-1)
						collisionWalls.add(new Wall(Wall.HORIZONTAL, new Point2D.Float(i, j+1) , new Dimension(wallLength, wallThickness), true));
					
					if(rand.nextDouble()> 0.5)	
						collisionWalls.add(new Wall(Wall.HORIZONTAL, new Point2D.Float(i, j) , new Dimension(wallLength, wallThickness), true));
					
				}
			}
		}
		
		
	}
	public void setSpaceSize(int size)
	{
		if (size <=0)
			return;
		
		spaceSize = size;
		
		if(playerSpaceRatio>0 || wallSpaceRatio>0 || itemSpaceRatio>0)
			calculateObjectSizes();
	}
	
	public void setObjectRatio(float player, float wall, float item)
	{
		if(player<=0 || wall<=0 || item<=0)
			return;
		
		playerSpaceRatio = player;
		wallSpaceRatio = wall;
		itemSpaceRatio = item;
		
		if(spaceSize >0)
			calculateObjectSizes();
	}
	public void calculateObjectSizes()
	{
		wallThickness = (int) (spaceSize * wallSpaceRatio);
		wallLength = spaceSize  - wallThickness;
		playerSize = (int) (spaceSize * playerSpaceRatio);
		itemSize = (int) (spaceSize * itemSpaceRatio);
	}
	public void update(long elapsedTime)
	{
		if(goal == null)
			addNewGoal();
		goal.update(elapsedTime);
	}
	public void addNewGoal()
	{
		
		
		if(goal == null)
		{	
			Dimension endGoalDimension = new Dimension();
			endGoalDimension.setSize(spaceSize * itemSpaceRatio, spaceSize * itemSpaceRatio);
			goal = new GoalObject(getRandomLevelPosition(), endGoalDimension);
		}
	}
	public void paint(Graphics2D g, Container c )
	{
		
		Color temp = g.getColor();
		g.setColor(Color.black);
		
		//draw walls
		for(Wall wall: collisionWalls)
			wall.draw(g);
		
		for(Wall wall: nonCollisionWalls)
			wall.draw(g);
		
		if(goal != null)
			goal.draw(g);
		//revert to previous colour
		g.setColor(temp);
	}
	public Point2D getRandomLevelPosition()
	{
		Random rand = new Random();
		int spaceX = rand.nextInt(width);
		int spaceY = rand.nextInt(height);
		Point2D randomPosition = new Point2D.Float(spaceX * spaceSize + spaceSize * 0.5f, spaceY * spaceSize+ spaceSize * 0.5f);
		return randomPosition;
	}
}
