package pcclient.events;

import java.awt.geom.Point2D;

import pcclient.game.Wall;

public interface EventListener
{
	public void playerLobbyEvent(String playerID, int actionType);
	public void pcLobbyEvent(String lobbyID, int actionType);
	
	public void gameLoadEvent(float spaceSizeRatio, float spacePlayerRatio, int levelWidth, int levelHeight, Wall[] walls);
	public void gameStatusEvent(int actionType);
	public void gamePlayerMovementEvent(String playerID, Point2D pos, Point2D vel, float orient);
	public void gamePlayerActionEvent(String playerID, int actionType);
}
