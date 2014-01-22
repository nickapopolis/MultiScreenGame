package com.msandroidphoneclient.events;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class JSONEvent {
	
	JSONObject object;
	
	/**
	 * All of our events are converted into JSON objects so that it is portable between different types of clients
	 * @param obj
	 * @throws JSONException
	 */
	public JSONEvent(JSONObject obj) throws JSONException
	{
		object = obj;
		deserialize(obj);
	}
	public JSONEvent(String dest, String sender) throws JSONException
	{
		object = new JSONObject();
		object.put(eventTypeKey, getEventType());
		object.put(senderKey, sender);
		object.put(destKey, dest);
		serialize();
	}
	
	public static String eventTypeKey = "ET";
	public static String eventKey ="E";
	public static String senderKey = "SENDER";
	public static String destKey = "DEST";
	protected abstract void deserialize(JSONObject obj);
	protected abstract Object serialize();
	public abstract String getEventType();
	
	public String toString()
	{
		return object.toString();
	}
}
