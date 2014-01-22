package com.msandroidphoneclient.game;

public class Camera
{
	protected Vector3d position; 
	protected Vector3d lookAt; 
	protected Vector3d up;
	
	protected Vector3d velocity;
	
	/**
	 * 
	 * @param position
	 * @param lookAt
	 * @param up
	 */
	public Camera(Vector3d position, Vector3d lookAt, Vector3d up)
	{
		this.position = position;
		this.lookAt = lookAt;
		this.up = up;
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translatePosition(float x, float y, float z)
	{
		position.translate(new Vector3d(x, y, z));
	}
	public Vector3d getPosition()
	{
		return position;
	}

	public void setPosition(Vector3d position)
	{
		this.position = position;
	}

	public Vector3d getLookAt()
	{
		return lookAt;
	}

	public void setLookAt(Vector3d lookAt)
	{
		this.lookAt = lookAt;
	}

	public Vector3d getUp()
	{
		return up;
	}

	public void setUp(Vector3d up)
	{
		this.up = up;
	}
	

}
