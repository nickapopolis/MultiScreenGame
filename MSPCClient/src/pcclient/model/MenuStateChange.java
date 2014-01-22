package pcclient.model;
public class MenuStateChange
{
	private Player player;
	private int action;
	
	public MenuStateChange(Player player, int action) 
	{
		this.player = player;
		this.action = action;
	}
	public Player getPlayer()
	{
		return player;
	}
	public int getAction()
	{
		return action;
	}
}
