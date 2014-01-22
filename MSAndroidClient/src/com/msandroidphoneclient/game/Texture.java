package com.msandroidphoneclient.game;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Texture
{
	private int[] textures = new int[1];
	
	public Texture(GL10 gl, Context context, int resourceID)
	{
		loadGLTexture(gl, context, resourceID);
	}
	public void loadGLTexture(GL10 gl, Context context, int resourceID) {
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resourceID);

		// generate one texture pointer
		gl.glGenTextures(1, textures, 0);
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		// Clean up
		bitmap.recycle();
	}
	
	public int[] getTextures()
	{
		return textures;
	}
}
