package com.msandroidphoneclient.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.msandroidclient.R;



import android.content.Context;

public class Level
{
	protected enum WallPosition{LEFT, RIGHT, TOP, BOTTOM, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
	protected Vector<Wall> collisionWalls;
	protected Vector<Wall> nonCollisionWalls;
	protected Space spaces[][];
	
	protected float playerSpaceRatio = 0;
	protected float wallSpaceRatio = 0;
	protected float itemSpaceRatio = 0;
	
	protected float spaceSize = 0;
	protected float wallLength = 0;
	protected float wallThickness = 0;
	protected float playerSize = 0;
	protected float itemSize = 0;
	
	protected int levelWidth = 0;
	protected int levelDepth = 0;
	
	protected Texture wallTexture;
	protected Texture wallCollisionTexture;
	protected Texture cornerTexture;
	
	protected static Comparator<GameObject> wallComparator = new Comparator<GameObject>() {
	      public int compare(GameObject o1, GameObject o2) {
	    	  if(o1.getPosition().x < o2.getPosition().x)
	    		  return -1;
	    	  else if(o1.getPosition().x> o2.getPosition().x)
	    		  return 1;
	    	  else
	    	  {
	    		  if(o1.getPosition().z < o2.getPosition().z)
	    			  return -1;
	    		  else if(o1.getPosition().z > o2.getPosition().z)
	    			  return 1;
	    		  else
	    			  return 0;
	    		  
	    	  }
	      }
	    };
	
	/**
	 * Creates a level object with the specified parameters
	 * @param width Width in spaces of the level
	 * @param depth Depth in spaces of the level
	 * @param playerRatio the ratio of the player size to the level tile size
	 * @param wallRatio the ratio of the wall size to the level tile size
	 * @param itemRatio the ratio of the item size to the level tile size
	 */
	public Level(GL10 gl, Context context,  int width, int depth, float playerRatio, float wallRatio, float itemRatio)
	{
		loadTextures(gl, context);
		//initialize spaces
		spaces = new Space[width][depth];
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<depth;j++)
			{
				spaces[i][j] = new Space(i, j);
			}
		}
		
		collisionWalls = new Vector<Wall>();
		nonCollisionWalls = new Vector<Wall>();
		levelWidth = width;
		levelDepth = depth;
		setLevelDimensions(width, depth);
		setObjectRatio(playerRatio, wallRatio, itemRatio);
		setSpaceSize(4);
		
		
	}
	
	public void loadTextures(GL10 gl, Context context)
	{
		wallTexture = new Texture(gl, context, R.drawable.wall);
		wallCollisionTexture = new Texture(gl, context, R.drawable.collisionwall);
		cornerTexture = new Texture(gl, context, R.drawable.corner);
	}
	/**
	 * Takes the coordinate position of a wall to add and it's orientation. Initializes a wall object and adds it to the level.
	 * @param x
	 * @param y
	 * @param horizontal
	 */
	public void addWall(int x, int z, boolean horizontal, boolean collision)
	{
		
		float width;
		float depth;
		float height;
		Vector3d position;
		float rotation = 0;
		
		//set the height depending on whether the wall is a collision wall or not
		if(collision)
			height = spaceSize;
		else
			height = spaceSize/3;
		
		//set the position in game world
		if(horizontal)
		{
			width = wallLength;
			depth = wallThickness;
			position = new Vector3d(spaceSize*0.5f +spaceSize*x , height*0.5f, spaceSize *z);
		}
		else
		{
			width = wallThickness;
			depth = wallLength;
			position = new Vector3d(spaceSize *x, height*0.5f,spaceSize*0.5f +spaceSize*z );
		}
		
		
		Wall wall = new Wall(position, rotation, width, height, depth);
		wall.setTexture(wallTexture);
		if(collision)
			collisionWalls.add(wall);
		else
			nonCollisionWalls.add(wall);
		
		Space space = getSpace(x,z);
		if(space != null)
		{
			if(horizontal)
				space.setTopWall(wall);
			else
				space.setLeftWall(wall);
		}
		
		//add a corner to each side of wall if it does not exist already
		width = wallThickness;
		depth = wallThickness;
		height = spaceSize;
		
		if(getWall(space, WallPosition.TOP_LEFT) == null)
		{
			position = new Vector3d(spaceSize * x, height*0.5f, spaceSize * z);
			wall = new Wall(position, rotation, width, height, depth);
			wall.setTexture(cornerTexture);
			collisionWalls.add(wall);
			space.setTopLeftWall(wall);
		}
		Space adjacentSpace;
		if(horizontal)
			adjacentSpace = getSpace(x+1, z);
		else
			adjacentSpace = getSpace(x, z+1);
		
		//if there is no corner at the adjacent space add a corner
		if(adjacentSpace != null && getWall(space, WallPosition.TOP_LEFT) == null)
		{
			position = new Vector3d(spaceSize * adjacentSpace.x, height*0.5f, spaceSize * adjacentSpace.z);
			wall = new Wall(position, rotation, width, height, depth);
			wall.setTexture(cornerTexture);
			collisionWalls.add(wall);
			adjacentSpace.setTopLeftWall(wall);
		}
	}
	
	/**
	 * Draws all the walls in the level
	 * @param gl
	 * @param camera
	 */
	public void draw(GL10 gl, Camera camera)
	{
		for(Wall wall:collisionWalls)
			wall.draw(gl, camera);
		for(Wall wall:nonCollisionWalls)
			wall.draw(gl, camera);
	}
	
	/**
	 * Checks for collisions between player and the surrounding walls
	 * @param player
	 * @return
	 */
	public Vector<Collision> getCollisions(Player player)
	{
		Vector<Collision> collisions = new Vector<Collision>();
		int x = (int) (player.getPosition().x/spaceSize);
		int z = (int) (player.getPosition().z/spaceSize);
		
		//bound check
		if (x >= levelWidth)
			x = levelWidth-1;
		
		if(z >= levelDepth)
			z = levelDepth-1;

		ArrayList<Wall> nearbyWalls = new ArrayList<Wall>();
		
		Space space = getSpace(x,z);
		nearbyWalls.add(getWall(space, WallPosition.LEFT));
		nearbyWalls.add(getWall(space, WallPosition.RIGHT));
		nearbyWalls.add(getWall(space, WallPosition.BOTTOM));
		nearbyWalls.add(getWall(space, WallPosition.TOP));
		nearbyWalls.add(getWall(space, WallPosition.BOTTOM_LEFT));
		nearbyWalls.add(getWall(space, WallPosition.TOP_LEFT));
		nearbyWalls.add(getWall(space, WallPosition.BOTTOM_RIGHT));
		nearbyWalls.add(getWall(space, WallPosition.TOP_RIGHT));
		
		// The following code is optimized for finding which walls the player could possibly collide with, 
		// so that we do not have to check all the walls
		Space topSpace = getSpace(x, z-1);
		if(topSpace!= null)
		{
			nearbyWalls.add(getWall(space, WallPosition.LEFT));
			nearbyWalls.add(getWall(space, WallPosition.RIGHT));
		}

		Space bottomSpace = getSpace(x, z+1);
		if(bottomSpace != null)
		{
			nearbyWalls.add(getWall(space, WallPosition.LEFT));
			nearbyWalls.add(getWall(space, WallPosition.RIGHT));
		}
		
		Space leftSpace = getSpace(x-1, z);
		if(leftSpace != null)
		{
			nearbyWalls.add(getWall(space, WallPosition.LEFT));
			nearbyWalls.add(getWall(space, WallPosition.RIGHT));
		}

		Space rightSpace = getSpace(x+1, z);
		if(rightSpace != null)
		{
			nearbyWalls.add(getWall(space, WallPosition.LEFT));
			nearbyWalls.add(getWall(space, WallPosition.RIGHT));
		}
		
		//calculate collisions
		for(Wall wall:nearbyWalls)
		{
			Vector3d collisionVector = checkCollision(player, wall);
			if(collisionVector!= null)
			{
				double distance = Math.sqrt(Math.pow(player.getPosition().x - collisionVector.x, 2) + Math.pow(player.getPosition().z - collisionVector.z, 2));
				if(distance< player.halfWidth *2)
				{
					System.err.println("collision");
					collisions.add(new Collision(player, wall, collisionVector));
					wall.setTexture(wallCollisionTexture);
				}
			}
			
		}
		return collisions;
		
	}
	/**
	 * Gets a space in the space array
	 * @param x
	 * @param z
	 * @return
	 */
	public Space getSpace(int x, int z)
	{
		Space space = null;
		
		if(x>=0 && z>=0 && x<levelWidth && z<levelDepth)
			space = spaces[x][z];
		
		return space;
	}
	
	public void setLevelDimensions(int width, int depth)
	{
		if(width>=0 && depth >=0)
		{
			levelWidth = width;
			levelDepth = depth;
		}
	}
	public void setSpaceSize(float size)
	{
		if (size <=0)
			return;
		
		spaceSize = size;
		
		if(playerSpaceRatio>0 || wallSpaceRatio>0 || itemSpaceRatio>0)
			calculateObjectSizes();
	}
	
	/**
	 * Since the game is displayed in multiple resolutions, we just keep track of the size ratios of the objects.
	 * @param player
	 * @param wall
	 * @param item
	 */
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
	
	/**
	 * Calculate the size of objects in our 3D world from the size ratios
	 */
	public void calculateObjectSizes()
	{
		wallThickness = spaceSize * wallSpaceRatio;
		wallLength = spaceSize  - wallThickness;
		playerSize = spaceSize * playerSpaceRatio;
		itemSize = spaceSize * itemSpaceRatio;
	}
	/**
	 * Get the wall relative to a certain space in the level
	 * @param space
	 * @param wallPos
	 * @return
	 */
	public Wall getWall(Space space, WallPosition wallPos)
	{
		Wall wall = null;
		Space adjacentSpace = null;
		
		switch(wallPos)
		{
			case LEFT: 
				wall = space.getLeftWall();
				break;
			case RIGHT:
				adjacentSpace = getSpace(space.x +1, space.z);
				if(adjacentSpace != null)
					wall = adjacentSpace.getLeftWall();
				break;
			case TOP:
				wall = space.getTopWall();
				break;
			case BOTTOM:
				adjacentSpace= getSpace(space.x, space.z+1);
				if(adjacentSpace != null)
					wall = adjacentSpace.getTopWall();
				break;
			case TOP_LEFT:
				wall = space.getTopLeftWall();
				break;
			case TOP_RIGHT:
				adjacentSpace = getSpace(space.x +1, space.z);
				if(adjacentSpace != null)
					wall = adjacentSpace.getTopLeftWall();
				break;
			case BOTTOM_LEFT:
				adjacentSpace = getSpace(space.x, space.z+1);
				if(adjacentSpace != null)
					wall = adjacentSpace.getTopLeftWall();
				break;
			case BOTTOM_RIGHT:
				adjacentSpace = getSpace(space.x +1, space.z+1);
				if(adjacentSpace != null)
					wall = adjacentSpace.getTopLeftWall();
				break;
		}
		
		return wall;
	}
	
	
	/**
	 * Checks for a collision between player and a wall. Returns the closest point from the wall to the players position.
	 * @param player
	 * @param wall
	 * @return
	 */
	public Vector3d checkCollision(Player player, Wall wall)
	{
		if(wall == null)
			return null;
		
		  // relative position of p from the point 'p'
	    Vector3d diff = (player.getPosition().minus(wall.getPosition()));
	   
	    // rectangle half-size
	    float depth = wall.getHalfDepth();
	    float width = wall.getHalfWidth();

	    // special case when the sphere centre is inside the rectangle
	    if(Math.abs(diff.x) < width && Math.abs(diff.z) < depth) 
	    {
	        // use left or right side of the rectangle boundary
	        // as it is the closest
	        if((width - Math.abs(diff.x)) < (depth - Math.abs(diff.z)))
	        {
	             diff.z = 0.0f;
	             diff.x = width * diff.x /Math.abs(diff.x);
	        }
	        // use top or bottom side of the rectangle boundary
	        // as it is the closest
	        else
	        {
	             diff.x = 0.0f;
	             diff.z = depth * diff.z/Math.abs(diff.z);
	        }
	    }
	    else
	    {
	        // clamp to rectangle boundary
	        if(Math.abs(diff.x) > width) diff.x = width * Math.abs(diff.x);
	        if(Math.abs(diff.z) > depth) diff.z = depth * Math.abs(diff.z);
	    }

	    // the closest point on rectangle from p
	    Vector3d c = wall.getPosition().plus(diff);
	    
	    return c;
	}
	
}
