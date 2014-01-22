package com.msandroidphoneclient.game;

public class Vector3d
{
	public float x;
	public float y;
	public float z;
	
	public Vector3d(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void translate(Vector3d translation)
	{
		this.x += translation.x;
		this.y += translation.y;
		this.z += translation.z;
	}
	
	public Vector3d minus(Vector3d v)
	{
		return new Vector3d(this.x - v.x, this.y - v.y, this.z - v.z);
	}
	
	public Vector3d plus(Vector3d v)
	{
		return new Vector3d(this.x + v.x, this.y + v.y, this.z + v.z);
	}
}
