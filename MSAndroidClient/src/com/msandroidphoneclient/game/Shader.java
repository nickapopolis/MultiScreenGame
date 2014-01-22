package com.msandroidphoneclient.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import android.opengl.GLES10;
import android.opengl.GLES20;

public class Shader
{
	String vertexShaderCode;
	String fragmentShaderCode;
	
	int vertexShaderHandle; 
	int fragmentShaderHandle; 
	int programHandle;
	
	/** This will be used to pass in the transformation matrix. */
	private int mMVPMatrixHandle;
	/** This will be used to pass in model position information. */
	private int mPositionHandle;
	/** This will be used to pass in model color information. */
	private int mColorHandle;
	
	String MVPMatrixAttributeName = "u_MVPMatrix";
	String positionAttributeName = "a_Position";
	String colorAttributeName = "a_Color";
	
	public Shader(String vertexfileName, String fragmentFileName)
	{
		//read shaders from file
		vertexShaderCode = loadShaderCode(vertexfileName);
		fragmentShaderCode = loadShaderCode(fragmentFileName);
		
		//compile shaders
		vertexShaderHandle = compileShaderCode(vertexShaderCode, "vert");
		fragmentShaderHandle = compileShaderCode(vertexShaderCode, "frag");
		
		//link program
		programHandle = linkProgram(vertexShaderHandle, fragmentShaderHandle);
		
		// Set program handles. These will later be used to pass in values to the program.
	    mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, MVPMatrixAttributeName);
	    mPositionHandle = GLES20.glGetAttribLocation(programHandle, positionAttributeName);
	    mColorHandle = GLES20.glGetAttribLocation(programHandle, colorAttributeName);
		
	}
	public String loadShaderCode(String fileName)
	{
		String code = "";
		
		InputStream is;
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			while((line = br.readLine())!= null)
			{
				code += line +'\n';
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return code;
	}
	
	public int compileShaderCode(String shaderCode, String shaderType)
	{
		int shaderHandle = GLES20.glCreateShader(shaderType.equals("vert")?GLES20.GL_VERTEX_SHADER : GLES20.GL_FRAGMENT_SHADER);
		
		if (shaderHandle != 0)
		{
		    // Pass in the shader source.
		    GLES20.glShaderSource(shaderHandle, shaderCode);
		 
		    // Compile the shader.
		    GLES20.glCompileShader(shaderHandle);
		 
		    // Get the compilation status.
		    final int[] compileStatus = new int[1];
		    GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		 
		    // If the compilation failed, delete the shader.
		    if (compileStatus[0] == 0)
		    {
		        GLES20.glDeleteShader(shaderHandle);
		        shaderHandle = 0;
		    }
		}
		if (shaderHandle == 0)
		{
		    throw new RuntimeException("Error creating shader.");
		}
		
		return shaderHandle;
	}
	public int linkProgram(int vertexHandle, int fragmentHandle)
	{
		// Create a program object and store the handle to it.
		int shaderProgramHandle = GLES20.glCreateProgram();
		 
		if (shaderProgramHandle != 0)
		{
		    // Bind the vertex shader to the program.
		    GLES20.glAttachShader(shaderProgramHandle, vertexHandle);
		 
		    // Bind the fragment shader to the program.
		    GLES20.glAttachShader(shaderProgramHandle, fragmentHandle);
		 
		    // Bind attributes
		    GLES20.glBindAttribLocation(shaderProgramHandle, 0, "a_Position");
		    GLES20.glBindAttribLocation(shaderProgramHandle, 1, "a_Color");
		 
		    // Link the two shaders together into a program.
		    GLES20.glLinkProgram(shaderProgramHandle);
		 
		    // Get the link status.
		    final int[] linkStatus = new int[1];
		    GLES20.glGetProgramiv(shaderProgramHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
		 
		    // If the link failed, delete the program.
		    if (linkStatus[0] == 0)
		    {
		        GLES20.glDeleteProgram(shaderProgramHandle);
		        shaderProgramHandle = 0;
		    }
		}
		 
		if (shaderProgramHandle == 0)
		{
		    throw new RuntimeException("Error creating program.");
		}
		
		return shaderProgramHandle;
	}
	
	public int getMVPMatrixHandle()
	{
		return mMVPMatrixHandle;
	}
	public int getPositionHandle()
	{
		return mPositionHandle;
	}
	public int getColorHandle()
	{
		return mColorHandle;
	}
	
	public void activate()
	{
		GLES20.glUseProgram(programHandle);
	}
}
