package pcclient.game;

import java.awt.geom.Point2D;


public class Collision
{
	public GameObject obj1; 
	public GameObject obj2;
	public Point2D closestPoint;
	
	public Collision(GameObject obj1, GameObject obj2, Point2D closestPoint)
	{
		this.obj1 = obj1;
		this.obj2 = obj2;
	}
	
	public static Collision check(GamePlayer player, Wall wall)
	{
		Collision collision = null;
		
		if(wall == null)
			return null;
		
		  // relative position of p from the point 'p'
	    Point2D diff = new Point2D.Float((float)(player.getPos().getX() - wall.getPos().getX()), 
	    								 (float)(player.getPos().getX() - wall.getPos().getY()));
	   
	    // rectangle half-size
	    float height = (float) (wall.getDim().getHeight()* 0.5f);
	    float width = (float) (wall.getDim().getWidth()* 0.5f);

	    // special case when the sphere centre is inside the rectangle
	    if(Math.abs(diff.getX()) < width && Math.abs(diff.getY()) < height) 
	    {
	        // use left or right side of the rectangle boundary
	        // as it is the closest
	        if((width - Math.abs(diff.getX())) < (height - Math.abs(diff.getY())))
	        {
	        	diff.setLocation(width * diff.getX() /Math.abs(diff.getX()), 
	        					 0.0f);
	        }
	        // use top or bottom side of the rectangle boundary
	        // as it is the closest
	        else
	        {
	        	diff.setLocation(0.0f, 
	        					 height * diff.getY()/Math.abs(diff.getY()));
	        }
	    }
	    else
	    {
	        // clamp to rectangle boundary
	        if(Math.abs(diff.getX()) > width) 
	        	diff.setLocation(width * Math.abs(diff.getX()), diff.getY());
	        if(Math.abs(diff.getY()) > height) 
	        	diff.setLocation(diff.getX(), height * Math.abs(diff.getY()));
	    }

	    // the closest point on rectangle from p
	    Point2D closest = new Point2D.Float((float)(wall.getPos().getX() + diff.getX()), (float)(wall.getPos().getY() + diff.getY()));
	    
	    if(closest.distance(player.getPos())< player.getDim().getWidth())
	    	collision = new Collision(player, wall, closest);
	    
	    return collision;
	}
	
	public static Collision check(GamePlayer player, GoalObject obj)
	{
		Collision collision = null;
		
		float dist = (float) player.getPos().distance(obj.getPos());
		if(dist< player.getDim().getWidth()* 0.5 + obj.getDim().getWidth()* 0.5f) 
			collision = new Collision(player, obj, null);
		
		return collision;
	}
}
