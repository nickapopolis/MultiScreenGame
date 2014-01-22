package pcclient.game;

public class Space
{
	
	private Wall topWall = null;
	private Wall leftWall = null;
	private Wall topLeftWall = null;
	public int x;
	public int z;
	private boolean visited = false;
	
	public Space(int x, int z)
	{
		this.x = x;
		this.z = z;
	}
	
	
	public Wall getTopWall()
	{
		return topWall;
	}

	public void setTopWall(Wall topWall)
	{
		this.topWall = topWall;
	}

	public Wall getLeftWall()
	{
		return leftWall;
	}

	public void setLeftWall(Wall leftWall)
	{
		this.leftWall = leftWall;
	}
	
	public Wall getTopLeftWall()
	{
		return topLeftWall;
	}

	public void setTopLeftWall(Wall topLeftWall)
	{
		this.topLeftWall = topLeftWall;
	}

	public void setVisited(boolean isVisited)
	{
		visited = isVisited;
	}
	
}
