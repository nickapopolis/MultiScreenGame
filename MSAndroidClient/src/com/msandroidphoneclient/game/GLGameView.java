package com.msandroidphoneclient.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GLGameView extends GLSurfaceView 
{

	private GestureDetector gestureDetector;
	private Game renderer;
	/**
	 * 
	 * @param context
	 */
	public GLGameView(Context context)
	{
		super(context);
		
		//set OpenGl version
		setEGLContextClientVersion(1);
		System.err.println("got past client version");
		//set OpenGl renderer
		renderer = new Game(context);
		setRenderer(renderer);
		

		gestureDetector = new GestureDetector(renderer);
		//render view only when there is a change in drawing data
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	public boolean onTouchEvent(MotionEvent me)
	{
		if(gestureDetector == null)
			return false;
		
	    return gestureDetector.onTouchEvent(me);
	}
}
