package com.msandroidphoneclient.game;

import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.msandroidclient.R;



import android.content.Context;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public class Game extends GLGameViewRenderer implements OnGestureListener
{
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	

	private Level level;
	private Player player;
	private Vector<Player> players;
	private Sprite sprite;
	
	private Texture playerTexture;
	
	
	public Game(Context context)
	{
		super(context);
	}
	public void initialize(GL10 gl)
	{
		sprite = new Sprite(context, R.drawable.corner);
		sprite.loadGLTexture(gl, this.context);
		
		
		players = new Vector<Player>();
		
		playerTexture = new Texture(gl, context, R.drawable.collisionwall);
		player = new Player(new Vector3d(5.0f, 0.0f, 5.0f), 0, 1, 5, 1);
		player.setTexture(playerTexture);
		level = new Level(gl, context, 10, 10, 0.5f, 0.2f, 0.5f);
		Random random = new Random();
		for(int i=0; i<10; i++)
		{
			for(int j=0;j<10; j++)
			{
					if(random.nextInt(2) ==1)
						level.addWall(i, j, true, true);
					if(random.nextInt(2) ==1)
						level.addWall(i, j, false, true);
			}
		}
	}
	/**
	 * Updates position of all the moving objects inside of the level
	 * @param millis
	 */
	public void updateGameState(float elapsedTime)
	{
		player.getPosition().x+= 1;
		if(player != null)
			player.update(elapsedTime);
		for(Player otherPlayer : players)
			otherPlayer.update(elapsedTime);
		

		//detect collisions
		detectCollision(elapsedTime);
	}
	/**
	 * Draws the game level and all of the objects inside of it
	 * @param gl
	 * @param camera
	 */
	public void drawGame(GL10 gl, Camera camera)
	{
		level.draw(gl, camera);
		if(player!= null)
			player.draw(gl, camera);
		for(Player player : players)
			player.draw(gl, camera);

	}
	
	/**
	 * Detects collision between the player and the level
	 * @param millis
	 */
	public void detectCollision(float millis)
	{
		//determine how far the player has to be moved back so that it is not colliding with any walls
		Vector<Collision> collisions =  level.getCollisions(player);
		double intersectionX = 0;
		double intersectionY = 0;
		
		//find the maximum distance we can move player in both dimensions
		for(Collision c: collisions)
		{
			double distX = player.getPosition().x - c.collisionPoint.x;
			double distY = player.getPosition().y - c.collisionPoint.y;
			
			if(Math.abs(distX)>Math.abs(intersectionX))
				intersectionX = distX;
			
			if(Math.abs(distY)>Math.abs(intersectionY))
				intersectionY = distY;
		}
		
		//adjust player position
		player.position.x -= intersectionX;
		player.position.y -= intersectionY;
	}
	
	public boolean onDown(MotionEvent arg0)
	{
		System.err.println("down");
		return true;
	}
	 public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	   {
	       try {
	           if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	               return false;
	           // right to left swipe
	           if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	  
	             
	           }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	   System.err.println("Right Swipe");
	        	
	           }
	           else if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	   System.err.println("Swipe up");
	           }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	   System.err.println("Swipe down");
	           }
	       } catch (Exception e) {
	           // nothing
	       }

	               return true;
	   }
	 
	public void onLongPress(MotionEvent arg0)
	{
		System.err.println("Long Press");
		
	}
	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3)
	{
		camera.translatePosition((e2.getX()- e1.getX())/viewWidth, 0, (e2.getY()- e1.getY())/viewHeight);
		System.err.println("scroll");
		return true;
	}
	
	public void onShowPress(MotionEvent arg0)
	{
		System.err.println("show press");
	}
	
	public boolean onSingleTapUp(MotionEvent arg0)
	{
		
		System.err.println("single tap up");
		
		return true;
	}
	
}
