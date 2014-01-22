package pcclient.events;

import org.json.simple.JSONObject;

public class GameStatusEvent extends JSONEvent{

	public static String eventName = "GameStatusEvent";

	static final String statusIDKey = "SID";
	public int statusID;
	
	public GameStatusEvent(String dest, String sender,int statusID) {
		super(dest, sender);
		this.statusID = statusID;
		serialize();
	}

	public GameStatusEvent(JSONObject obj)
	{
		super(obj);
	}
	@Override
	protected void deserialize(JSONObject obj) {
		JSONObject event;
		event = (JSONObject) obj.get(eventKey);
		statusID= (int) event.get(statusIDKey);
		
	}

	@Override
	protected Object serialize() {
		JSONObject event = new JSONObject();
		event.put(statusIDKey, statusID);
		object.put(eventKey, event);
		
		return event;
	}

	@Override
	public String getEventType() {
		return eventName;
	}

}
