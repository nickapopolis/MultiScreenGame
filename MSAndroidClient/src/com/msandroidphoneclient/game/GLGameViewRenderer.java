package com.msandroidphoneclient.game;


import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public abstract class GLGameViewRenderer implements GLSurfaceView.Renderer, OnGestureListener
{

	protected int viewWidth;
	protected int viewHeight;
	
	
	protected Camera camera;
	protected float lastDrawTime;
	protected Context context;
	public GLGameViewRenderer(Context context)
	{
		this.context = context;
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//shader = new Shader("shader.vert", "shader.frag");
	    //shader.activate();
		
		Vector3d position = new Vector3d(0.0f, 30.0f, 0.0f);
		Vector3d lookAt = new Vector3d(10.0f, 0.0f, 10.0f);
		Vector3d up = new Vector3d(0,1,0);
		camera = new Camera(position, lookAt, up);
		
		initialize(gl);
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 
        
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                  GL10.GL_NICEST);
        
        
	}
	
	public void onDrawFrame(GL10 gl)
	{
		//get current time for position update
		float currentTime = System.currentTimeMillis();
		float elapsedTime = currentTime - lastDrawTime;
		
		//update positions of game objects
		updateGameState(elapsedTime);
		
		lastDrawTime = currentTime;
		
		//clear the screen for drawing
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);  
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		//adjust camera position
	
		//draw level and objects inside of it
		drawGame(gl, camera);
		
	}

	
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();   

        viewWidth = width;
        viewHeight = height;
	}
	
	public abstract void initialize(GL10 gl);
	/**
	 * Updates position of all the moving objects inside of the level
	 * @param millis
	 */
	public abstract void updateGameState(float millis);
	
	/**
	 * Draws the game level and all of the objects inside of it
	 * @param gl
	 * @param camera
	 */
	public abstract void drawGame(GL10 gl, Camera camera);

	
	

}
