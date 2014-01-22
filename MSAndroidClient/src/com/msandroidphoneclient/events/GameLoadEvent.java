package com.msandroidphoneclient.events;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.msandroidphoneclient.game.Wall;


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
	
	/**
	 * THe game load event is sent to each client in order to load the data of the next level
	 * @param event
	 * @throws JSONException
	 */
	public GameLoadEvent(JSONObject event) throws JSONException
	{
		super(event);
	}
	public GameLoadEvent(String dest, String sender, double spaceSizeRatio, double playerSizeRatio, int mazeWidth, int mazeHeight, Vector<Wall> walls) throws JSONException 
	{
		super(dest, sender);
		this.spaceSizeRatio = spaceSizeRatio;
		this.playerSizeRatio = playerSizeRatio;
		this.mazeWidth = mazeWidth;
		this.mazeHeight = mazeHeight;
		this.walls = walls;
		serialize();
	}
	@Override
	public void deserialize(JSONObject object) 
	{
		JSONArray event = null;
		try {
			event = object.getJSONArray(eventKey);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		JSONArray arr = (JSONArray)event;
		try {
			spaceSizeRatio = (Double) arr.get(spaceSizeRatioKey);
			playerSizeRatio = (Double) arr.get(playerSizeRatioKey);
			mazeWidth = (Integer) arr.get(mazeWidthKey);
			mazeHeight = (Integer) arr.get(mazeHeightKey);
			
			walls = new Vector<Wall>();
			for(Integer i=mazeWallStart;i<arr.length();i+=4)
			{
				Integer xPos = (Integer) arr.get(i);
				Integer yPos = (Integer) arr.get(i+1);
				Integer wallType = (Integer) arr.get(i+2);
				Boolean collision = (Boolean) arr.get(i+3);
				
				//Wall wall = new Wall(wallType, new PoInteger2D.Float(xPos, yPos), null, collision);
				//walls.add(wall);
				/*TODO */
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public Object serialize() {
		return null;
	}
	@Override
	public String getEventType() {
		return eventName;
	}

}
