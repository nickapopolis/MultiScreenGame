package pcclient.events;

import java.awt.geom.Point2D;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pcclient.game.Wall;

public class GameLoadEvent extends JSONEvent
{

	static final int eventNameKey = 0;
	static final int spaceSizeRatioKey = 1;
	static final int playerSizeRatioKey = 2;
	static final int mazeWidthKey = 3;
	static final int mazeHeightKey = 4;
	static final int mazeWallStart = 5;
	
	public static final String eventName = "GameLoadEvent";
	protected double spaceSizeRatio;
	protected double playerSizeRatio;
	protected int mazeWidth;
	protected int mazeHeight;
	protected Vector<Wall> walls;
	
	public GameLoadEvent(JSONObject event)
	{
		super(event);
	}
	public GameLoadEvent(String dest, String sender, double spaceSizeRatio, double playerSizeRatio, int mazeWidth, int mazeHeight, Vector<Wall> walls) 
	{
		super(dest, sender);
		this.spaceSizeRatio = spaceSizeRatio;
		this.playerSizeRatio = playerSizeRatio;
		this.mazeWidth = mazeWidth;
		this.mazeHeight = mazeHeight;
		this.walls = walls;
	}
	@Override
	public void deserialize(JSONObject jObj) 
	{
		/*
		JSONArray arr = (JSONArray)jObj;
		spaceSizeRatio = (double) arr.get(spaceSizeRatioKey);
		playerSizeRatio = (double) arr.get(playerSizeRatioKey);
		mazeWidth = (int) arr.get(mazeWidthKey);
		mazeHeight = (int) arr.get(mazeHeightKey);
		
		walls = new Vector<Wall>();
		for(int i=mazeWallStart;i<arr.size();i+=4)
		{
			int xPos = (int) arr.get(i);
			int yPos = (int) arr.get(i+1);
			int wallType = (int) arr.get(i+2);
			boolean collision = (boolean) arr.get(i+3);
			Wall wall = new Wall(wallType, new Point2D.Float(xPos, yPos), null, collision);
			walls.add(wall);
		}*/
	}

	@Override
	public Object serialize() 
	{
		JSONArray arr = new JSONArray();
		arr.add(eventName);
		arr.add(spaceSizeRatio);
		arr.add(playerSizeRatio);
		arr.add(mazeWidth);
		arr.add(mazeHeight);
		for(int i=0; i<walls.size();i++)
		{
			Wall wall = walls.get(i);
			arr.add(wall.getCoords().getX());
			arr.add(wall.getCoords().getY());
			arr.add(wall.getWallType());
			arr.add(wall.isCollisionWall());
		}
		object.put(JSONEvent.eventKey, arr);
		return arr;
	}
	@Override
	public String getEventType() {
		return eventName;
	}
	

}
