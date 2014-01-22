package pcclient.networking;



import java.util.*;

import pcclient.events.EventListener;
import pcclient.events.*;

/**
 * This class implements the Reactor design pattern for this project. The class
 * reads definitions from a configuration file and then instantiates the appropriate
 * EventHandler classes and registers them. When an event is received, the Reactor
 * dispatches it to the appropriate EventHandler automatically 
 *
 */
public class Reactor 
{
	
	private EventListener listener;

	//Stores registered event handlers
	protected Vector<EventHandler> handlers;
	
    protected int _port;
    protected final int _poolSize = 3;
    protected Thread workerThreads[];
		
    /**
     * This constructor allows the Reactor to be used for a Client.
     * @param fileName a String containing the path for the configuration file
     * @param c the Client
     * @param IncomingChannel a MessageChannel holding incoming events
     */
	public Reactor(EventListener e, ArrayList<String> handlerNames)
	{
		listener = e;
		handlers = new Vector<EventHandler>();
		initializeEventHandlers(handlerNames);
	}
	/**
	 * Creates and initializes handlers from a list of handler names through reflection.
	 * @param handlerNames
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void initializeEventHandlers(ArrayList<String> handlerNames) 
	{
		for(int i=0; i<handlerNames.size();i++)
		{
			try {
				EventHandler handler = createEventHandler(handlerNames.get(i));
				registerHandler(handler);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Creates EventHandler of the class handlerName
	 * @param p
	 * @param handlerName
	 * @return EventHandler
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public EventHandler createEventHandler(String handlerName) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Class c = Class.forName(handlerName);

		System.out.println(handlerName + " EventHandler registered.");
		
		return (EventHandler)c.newInstance();
	}
	/**
	 * Registers an event handler with the reactor.
	 * @param handler
	 */
	public void registerHandler(EventHandler handler)
	{
		handlers.add(handler);
	}
	/**
	 * De-registers an event handler with the reactor.
	 * @param handler
	 */
	public void deregisterHandler(EventHandler handler)
	{
		handlers.remove(handler);
	}
	/**
	 * This method is called by the worker threads to handle incoming events.
	 * @param evt an Event that will get dispatched to registered EventHandlers
	 */
	public synchronized void handleEvent(JSONEvent evt)
	{
		for(EventHandler h: handlers){
			if( evt.getEventType().equals(h.getType())){
				h.handleEvent(evt,listener);
			}
		}
	}
	
	public void setEventListener(EventListener listener)
	{
		this.listener = listener;
	}
}
