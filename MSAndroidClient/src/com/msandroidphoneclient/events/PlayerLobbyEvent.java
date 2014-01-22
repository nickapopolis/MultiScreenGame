package com.msandroidphoneclient.events;

import org.json.JSONException;
import org.json.JSONObject;


public class PlayerLobbyEvent extends JSONEvent{

	public static final String eventName = "PlayerLobbyEvent";
	static String playerIDKey = "PID";
	static String actionTypeKey = "AT";
	
	String playerID;
	int actionType;
	
	/**
	 * This type of lobby event is sent when a player changes their state inside of the lobby
	 * @param obj
	 * @throws JSONException
	 */
	public PlayerLobbyEvent(JSONObject obj) throws JSONException 
	{
		super(obj);
	}
	public PlayerLobbyEvent(String dest, String sender, String id, int type) throws JSONException
	{
		super(dest, sender);
		playerID = id;
		actionType = type;
		serialize();
	}
	@Override
	public void deserialize(JSONObject obj) 
	{
		JSONObject event = null;
		try {
			event = obj.getJSONObject(JSONEvent.eventKey);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			playerID = (String) ((JSONObject) event).get(playerIDKey);
			actionType = (Integer)((JSONObject) event).get(actionTypeKey);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object serialize() 
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put(playerIDKey, playerID);
			obj.put(actionTypeKey, actionType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			object.put(JSONEvent.eventKey, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	@Override
	public String getEventType() {
		return eventName;
	}
}
