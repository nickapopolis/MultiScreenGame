package com.msandroidphoneclient.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;

public abstract class GameObject
{
	protected FloatBuffer mVertexBuffer;
	protected FloatBuffer mColorBuffer;
	protected FloatBuffer mTextureBuffer;
	protected ByteBuffer  mIndexBuffer;
	
	protected Vector3d position = new Vector3d(0.0f,0.0f,0.0f);
	protected Vector3d velocity = new Vector3d(0.0f,0.0f,0.0f);
	protected float rotation = 0.0f;
	
	protected float vertices[];
	protected float colors[];
	protected float texture[];
	protected byte indices[];
	
	protected Texture objectTexture;
	
	public GameObject(Vector3d position, float rotation, float width, float height, float depth)
	{
		this.position = position;
		this.rotation = rotation;
		initializeGeometry(width,height,depth);
		initializeBuffers();
	}
	protected abstract void initializeGeometry(float width, float height, float depth);
	
	protected void initializeBuffers()
	{
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
            
      
        byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTextureBuffer = byteBuf.asFloatBuffer();
        mTextureBuffer.put(texture);
        mTextureBuffer.position(0);
            
        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
	}
	
	public void setTexture(Texture objTexture)
	{
		objectTexture = objTexture;
	}
	public void draw(GL10 gl, Camera camera)
	{ 
		gl.glBindTexture(GL10.GL_TEXTURE_2D, objectTexture.getTextures()[0]);
		//enable states
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
	   if(camera != null)
		{
			gl.glLoadIdentity();
			GLU.gluLookAt(gl,camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, /* look from camera XYZ */
							 camera.getLookAt().x,camera.getLookAt().y,camera.getLookAt().z, /* look at the origin */
							 camera.getUp().x, camera.getUp().y, camera.getUp().z); /* positive Y up vector */ 
		}
		
	    gl.glRotatef(rotation, 1.0f, 1.0f, 1.0f);
	    gl.glTranslatef(position.x, position.y, position.z);
	    
		gl.glFrontFace(GL10.GL_CCW);
        
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        //gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        
        //draw the object 
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, 
                        mIndexBuffer);
        
        //disable states
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
		//gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void update(float elapsedTime)
	{
		position.x += velocity.x * elapsedTime;
		position.y += velocity.y * elapsedTime;
		position.z += velocity.z * elapsedTime;
	}
	
	public Vector3d getPosition()
	{
		return position;
	}
}
