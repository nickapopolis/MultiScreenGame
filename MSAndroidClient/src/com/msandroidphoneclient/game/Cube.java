package com.msandroidphoneclient.game;

public class Cube extends GameObject
{
	
	
	public Cube(Vector3d position, float rotation, float size)
	{
		super(position, rotation, size, size, size);
	}
	
	protected void initializeGeometry(float width, float height, float depth)
	{
		float halfWidth = width/2;
		float halfHeight = height/2;
		float halfDepth = depth/2;
		
		float vertices[] = {
				-halfWidth, -halfHeight, -halfDepth,
	            halfWidth, -halfHeight, -halfDepth,
	            halfWidth,  halfHeight, -halfDepth,
	            -halfWidth, halfHeight, -halfDepth,
	            -halfWidth, -halfHeight,  halfDepth,
	            halfWidth, -halfHeight,  halfDepth,
	            halfWidth,  halfHeight,  halfDepth,
	            -halfWidth,  halfHeight,  halfDepth
	            };
		float colors[] = {
	           0.0f,  1.0f,  0.0f,  1.0f,
	           0.0f,  1.0f,  0.0f,  1.0f,
	           1.0f,  0.5f,  0.0f,  1.0f,
	           1.0f,  0.5f,  0.0f,  1.0f,
	           1.0f,  0.0f,  0.0f,  1.0f,
	           1.0f,  0.0f,  0.0f,  1.0f,
	           0.0f,  0.0f,  1.0f,  1.0f,
	           1.0f,  0.0f,  1.0f,  1.0f
	        };

		byte indices[] = {
	          0, 4, 5, 0, 5, 1,
	          1, 5, 6, 1, 6, 2,
	          2, 6, 7, 2, 7, 3,
	          3, 7, 4, 3, 4, 0,
	          4, 7, 6, 4, 6, 5,
	          3, 0, 1, 3, 1, 2
	          };
		
		this.vertices = vertices;
		this.colors = colors;
		this.indices = indices;
	}

	
	
	
	
}
