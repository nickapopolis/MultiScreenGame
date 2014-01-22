package com.msandroidphoneclient.events;


/**
 * This abstract class defines an EventHandler. Each EventHandler must have a
 * handleEvent method. The EventHandler stores what type it is, which the Reactor
 * needs in order to dispatch the right Events to the right EventHandlers
 * @author the9a3eedi
 *
 */
public abstract class EventHandler {

	protected String eventType;
	
	public abstract void handleEvent(JSONEvent evt, EventListener e);
	
	/**
	 * @return the name of the event that this EventHandler handles.
	 */
	public String getType(){
		return eventType;
	}
}
