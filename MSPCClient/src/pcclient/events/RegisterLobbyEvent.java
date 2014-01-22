package pcclient.events;

import org.json.simple.JSONObject;

public class RegisterLobbyEvent extends JSONEvent {

	public static final String eventName = "RegisterLobbyEvent";
	public String requester;
	public String ip;
	public int port;
	public boolean register;
	
	public static String requesterKey = "REQ";
	public static String ipKey = "IP";
	public static String portKey = "PORT";
	public static String registerKey = "REG";
	public RegisterLobbyEvent(JSONObject eventMessage) 
	{
		super(eventMessage);
	}
	public RegisterLobbyEvent(String dest, String sender, String requester, String ip, int port, boolean register)
	{
		super(dest, sender);
		this.requester = requester;
		this.ip = ip;
		this.port = port;
		this.register = register;
		serialize();
	}
	
	@Override
	protected void deserialize(JSONObject obj) 
	{
		JSONObject event = (JSONObject) obj.get(eventKey);
		requester = (String) event.get(requesterKey);
		ip = (String) event.get(ipKey);
		port = (Integer) event.get(portKey);
		register = (Boolean) event.get(registerKey);
	}

	@Override
	protected Object serialize() 
	{
		JSONObject event = new JSONObject();
		event.put(requesterKey, requester);
		event.put(ipKey, ip);
		event.put(portKey, port);
		event.put(registerKey, register);
		
		object.put(eventKey, event);
		return event;
	}

	@Override
	public String getEventType() {
		return eventName;
	}

}
