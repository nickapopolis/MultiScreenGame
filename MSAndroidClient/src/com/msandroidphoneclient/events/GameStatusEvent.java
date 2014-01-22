package com.msandroidphoneclient.events;

import org.json.JSONException;
import org.json.JSONObject;


public class GameStatusEvent extends JSONEvent{

	public static String eventName = "GameStatusEvent";

	static final String statusIDKey = "SID";
	public int statusID;
	
	public GameStatusEvent(String dest, String sender,int statusID) throws JSONException {
		super(dest, sender);
		this.statusID = statusID;
		serialize();
	}

	public GameStatusEvent(JSONObject obj) throws JSONException
	{
		super(obj);
	}
	
	protected void deserialize(JSONObject obj) {
		JSONObject event;
		try {
			event = obj.getJSONObject(eventKey);
			statusID= event.getInt(statusIDKey);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected Object serialize() {
		JSONObject event = new JSONObject();
		try {
			event.put(statusIDKey, statusID);
			object.put(eventKey, event);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return event;
	}

	@Override
	public String getEventType() {
		return eventName;
	}

	

}
