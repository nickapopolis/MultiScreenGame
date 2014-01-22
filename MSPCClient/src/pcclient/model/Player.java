package pcclient.model;
public class Player
{
	String userID;
	String userName;
	
	public Player(String userID, String userName)
	{
		this.userID = userID;
		this.userName = userName;
	}
	public String getUserID()
	{
		return userID;
	}
	public void setUserID(String userID)
	{
		this.userID = userID;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public boolean equals(Player p2)
	{
		return userID.equals( p2.userID) && userName.equals(p2.userName);
	}
	
	public String tostring()
	{
		return userName;
	}
}
