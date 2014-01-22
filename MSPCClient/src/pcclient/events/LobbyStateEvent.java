package pcclient.events;

import org.json.simple.JSONObject;


public class LobbyStateEvent extends JSONEvent{

	public static String eventName = "LobbyStateEvent";

	static final String menuKey = "M";
	public int menuID;
	
	public LobbyStateEvent(String dest, String sender, int menuID)
	{
		super(dest, sender);
		this.menuID = menuID;
		serialize();
	}
	public LobbyStateEvent(JSONObject obj) {
		super(obj);
	}

	@Override
	protected void deserialize(JSONObject obj) 
	{
		JSONObject event;
		event = (JSONObject) obj.get(eventKey);
		menuID= (int) event.get(menuKey);
		
		
	}

	@Override
	protected Object serialize() {
		JSONObject event = new JSONObject();
		event.put(menuKey, menuID);
		object.put(eventKey, event);
		
		return event;
	}

	@Override
	public String getEventType() {
		return eventName;
	}

}
