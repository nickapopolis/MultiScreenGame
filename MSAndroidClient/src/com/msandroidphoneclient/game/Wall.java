package com.msandroidphoneclient.game;

public class Wall extends GameObject
{
	protected float halfWidth;
	protected float halfHeight;
	protected float halfDepth;
	public Wall(Vector3d position, float rotation, float width, float height, float depth)
	{
		super(position, rotation, width, height, depth);
	}

	protected void initializeGeometry(float width, float height, float depth)
	{
		halfWidth = width/2;
		halfHeight = height/2;
		halfDepth = depth/2;
		
		float vertices[] = {
                //Vertices according to faces
                -halfWidth, -halfHeight, halfDepth, //Vertex 0
                halfWidth, -halfHeight, halfDepth,  //v1
                -halfWidth, halfHeight, halfDepth,  //v2
                halfWidth, halfHeight, halfDepth,   //v3

                halfWidth, -halfHeight, halfDepth,  //...
                halfWidth, -halfHeight, -halfDepth,         
                halfWidth, halfHeight, halfDepth,
                halfWidth, halfHeight, -halfDepth,

                halfWidth, -halfHeight, -halfDepth,
                -halfWidth, -halfHeight, -halfDepth,            
                halfWidth, halfHeight, -halfDepth,
                -halfWidth, halfHeight, -halfDepth,

                -halfWidth, -halfHeight, -halfDepth,
                -halfWidth, -halfHeight, halfDepth,         
                -halfWidth, halfHeight, -halfDepth,
                -halfWidth, halfHeight, halfDepth,

                -halfWidth, -halfHeight, -halfDepth,
                halfWidth, -halfHeight, -halfDepth,         
                -halfWidth, -halfHeight, halfDepth,
                halfWidth, -halfHeight, halfDepth,

                -halfWidth, halfHeight, halfDepth,
                halfWidth, halfHeight, halfDepth,           
                -halfWidth, halfHeight, -halfDepth,
                halfWidth, halfHeight, -halfDepth,
                                    };

/** The initial texture coordinates (u, v) */   
float texture[] = {         
                //Mapping coordinates for the vertices
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f, 

                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,

                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,

                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,

                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,

                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,

                                    };

/** The initial indices definition */   
byte indices[] = {
                //Faces definition
                0,1,3, 0,3,2,           //Face front
                4,5,7, 4,7,6,           //Face right
                8,9,11, 8,11,10,        //... 
                12,13,15, 12,15,14,     
                16,17,19, 16,19,18,     
                20,21,23, 20,23,22,     
                                    };
		
		this.vertices = vertices;
		//this.colors = colors;
		this.indices = indices;
		this.texture = texture;
	}
	
	public float getHalfWidth()
	{
		return halfWidth;
	}
	public float getHalfHeight()
	{
		return halfHeight;
	}
	public float getHalfDepth()
	{
		return halfDepth;
	}
	
}
