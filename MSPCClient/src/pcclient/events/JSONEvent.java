package pcclient.events;

import org.json.simple.JSONObject;



public abstract class JSONEvent {
	JSONObject object;
	
	public JSONEvent(JSONObject obj)
	{
		object = obj;
		deserialize(obj);
	}
	public JSONEvent(String dest, String sender) 
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
	public String getDest()
	{
		return (String) object.get(destKey);
	}
	public String getSender()
	{
		return (String) object.get(senderKey);
	}
}
