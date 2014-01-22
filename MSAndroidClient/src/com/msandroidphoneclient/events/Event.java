package com.msandroidphoneclient.events;
import java.io.Serializable;


public abstract class Event {
	
		
	protected String type;
	protected String source;
	protected String destination;
	
	public Event(String t, String s, String d)
	{
		type = t;
		source = s;
		destination = d;
	}
	public String getType(){
		return type;
	}
	public String getSource()
	{
		return source;
	}
	public String getDestination()
	{
		return destination;
	}
	public void setDestination(String d)
	{
		destination = d;
	}
	

}
