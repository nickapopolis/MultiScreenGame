package com.msandroidphoneclient.events;

import org.json.JSONException;
import org.json.JSONObject;

public class LobbyStateEvent extends JSONEvent{

	public static String eventName = "LobbyStateEvent";

	static final String menuKey = "M";
	public int menuID;
	
	/**
	 * Event is sent when the state of the lobby changes, so that we can adapt the view of our client
	 * @param dest
	 * @param sender
	 * @param menuID
	 * @throws JSONException
	 */
	public LobbyStateEvent(String dest, String sender, int menuID) throws JSONException
	{
		super(dest, sender);
		this.menuID = menuID;
		serialize();
	}
	public LobbyStateEvent(JSONObject obj) throws JSONException {
		super(obj);
	}

	@Override
	protected void deserialize(JSONObject obj) 
	{
		JSONObject event;
		try {
			event = obj.getJSONObject(eventKey);
			menuID= event.getInt(menuKey);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected Object serialize() {
		JSONObject event = new JSONObject();
		try {
			event.put(menuKey, menuID);
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
