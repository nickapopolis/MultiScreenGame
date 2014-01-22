package pcclient.events;

import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GetLobbyEvent extends JSONEvent{

	public String requester;
	public String ip;
	public int port;
	public static String eventName = "GetLobbyEvent";
	public static String requesterKey = "REQ";
	public static String ipKey = "IP";
	public static String portKey = "PORT";
	
	public GetLobbyEvent(JSONObject obj)
	{
		super(obj);
	}
	public GetLobbyEvent(String dest, String sender, String requester, String ip, int port)
	{
		super(dest, sender);
		this.requester = requester;
		this.ip = ip;
		this.port = port;
		serialize();
	}
	@Override
	protected void deserialize(JSONObject obj) 
	{
		JSONObject event = (JSONObject) obj.get(eventKey);
		requester = (String) event.get(requesterKey);
		ip = (String) event.get(ipKey);
		port = (Integer) event.get(portKey);
	}

	@Override
	protected Object serialize() 
	{
		JSONObject event = new JSONObject();
		event.put(requesterKey, requester);
		event.put(ipKey, ip);
		event.put(portKey, port);
		object.put(eventKey, event);
		return event;
	}

	@Override
	public String getEventType() {
		return eventName;
	}

}
