package pcclient.events;

import org.json.simple.JSONObject;


public class PlayerLobbyEvent extends JSONEvent{

	public static final String eventName = "PlayerLobbyEvent";
	static String playerIDKey = "PID";
	static String actionTypeKey = "AT";
	
	public String playerID;
	public int actionType;
	
	public PlayerLobbyEvent(JSONObject obj) 
	{
		super(obj);
	}
	public PlayerLobbyEvent(String dest, String sender, String id, int type) 
	{
		super(dest, sender);
		playerID = id;
		actionType = type;
	}
	@Override
	public void deserialize(JSONObject obj) 
	{
		JSONObject event = null;
		event = (JSONObject) obj.get(JSONEvent.eventKey);
		
		playerID = (String) ((JSONObject) event).get(playerIDKey);
		actionType = ((Long)((JSONObject) event).get(actionTypeKey)).intValue();
		
	}

	@Override
	public Object serialize() 
	{
		JSONObject obj = new JSONObject();
		obj.put(playerIDKey, playerID);
		obj.put(actionTypeKey, actionType);
		
		object.put(JSONEvent.eventKey, obj);
		
		return obj;
	}
	@Override
	public String getEventType() {
		return eventName;
	}
}
