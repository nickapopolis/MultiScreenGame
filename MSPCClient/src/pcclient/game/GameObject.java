package pcclient.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public abstract class GameObject
{
	protected BufferedImage img;
	protected Dimension dim;
	protected Point2D pos;
	protected Point2D vel;
	protected Point2D acc;
	protected float rot;
	
	public GameObject(Point2D pos, Dimension dim, float rot, BufferedImage img)
	{
		this.pos = pos;
		this.dim = dim;
		this.rot = rot;
		this.img = img;
		this.vel = new Point2D.Float(0.0f, 0.0f);
		this.acc = new Point2D.Float(0.0f, 0.0f);
	}
	protected static BufferedImage loadContent(String filePath)
	{
		BufferedImage img = null;
		
		try{
			img = ImageIO.read(new File(filePath));
			}
			catch(Exception e)
			{
				try {
					img = ImageIO.read(Game.class.getResource(filePath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		return img;
	}
	public void setPosition(Point2D newPos)
	{
		pos.setLocation(newPos);
	}
	public Point2D getPos()
	{
		return pos;
	}
	public Dimension getDim()
	{
		return dim;
	}
	/**
	 * sets velocity in squares per second
	 */
	public void setVelocity(Point2D newVel)
	{
		vel.setLocation(newVel);
	}
	
	/**
	 * Update the game object as a function of time
	 * @param elapsedTime
	 */
	public void update(long elapsedTime)
	{
		float timeInSeconds = elapsedTime/1000.0f;
		vel.setLocation(acc.getX() * timeInSeconds , acc.getY() * timeInSeconds);
	}
	
	/**
	 * Draw the object
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		synchronized(pos)
		{
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
			AffineTransform transform = new AffineTransform();
			
			g.setColor(new Color(0,0,0,0.67f));
			transform.translate(pos.getX(), pos.getY() );
			transform.rotate(rot);
			transform.translate(-pos.getX()- dim.getWidth()/2.0f,-pos.getY()- dim.getHeight()/2.0f);
		     
		     // take a copy of the transformation that g2d is using
		    AffineTransform prevTransform = g.getTransform();
		    
		     // apply the transformation for this object and draw
		    g.setTransform(transform);
	
			int posX = (int)(pos.getX()-dim.getWidth()/2.0f);
			int posY = (int)(pos.getY()-dim.getHeight()/2.0f);
			
		    if(img!= null)
		    {
		    	g.drawImage(img, (int) pos.getX(), (int) pos.getY(), (int)dim.getWidth(), (int)dim.getHeight(), null);
		    }
		    else
		    {
		    	g.fillOval((int) pos.getX(), (int) pos.getY(), (int)dim.getWidth(), (int)dim.getHeight());
		    }
		}
	}
	
	protected abstract BufferedImage getImage();
	
}
