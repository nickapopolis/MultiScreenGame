package com.msandroidphoneclient.networking;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import com.msandroidclient.Configuration;
import com.msandroidclient.PhoneClientActivity;
import com.msandroidphoneclient.events.GameLoadEvent;
import com.msandroidphoneclient.events.JSONEvent;
import com.msandroidphoneclient.events.LobbyStateEvent;
import com.msandroidphoneclient.events.PlayerLobbyEvent;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.tools.json.JSONUtil;
public class ConnectionManager implements Serializable
{
	private static final long serialVersionUID = -5021544803466589413L;
	private static ConnectionManager mInstance;
	
	ConnectionDiscoverer discoverer;
	RabbitMQConnection lobbyConnection;
	RabbitMQConnection phoneConnection;
	Properties configurationProperties;
	String configurationFileName = "config.txt";
	
	ArrayBlockingQueue<JSONEvent> outgoingEvents;
	ArrayBlockingQueue<JSONEvent> incomingEvents;
	
	public int numThreads = 4;
	public boolean running = true;
	Reactor reactor;
	
	/**
	 * The ConnectionManager will handle both the discovery of new services, and all of the relaying of messages
	 * between client and server.
	 * @param activity
	 * @param context
	 */
	private ConnectionManager(Activity activity, Context context)
	{
		reactor = new Reactor((PhoneClientActivity)activity, Configuration.getEventHandlers(context, configurationFileName));
		
		outgoingEvents = new ArrayBlockingQueue<JSONEvent>(100);
		incomingEvents = new ArrayBlockingQueue<JSONEvent>(100);
		discoverer = new ConnectionDiscoverer(activity, context);
		
		configurationProperties = Configuration.readProperties(context, configurationFileName);
	}
	/**
	 * Creates a singleton instance of the class ConnectionManager
	 * Returns the created singleton
	 * @param activity
	 * @param context
	 */
	private static ConnectionManager createInstance(Activity activity, Context context)
	{
		mInstance = new ConnectionManager(activity, context);
		return mInstance;
	}
	/**
	 * Returns the singleton instance of the class ConnectionManager
	 * @param activity
	 * @param context
	 * @return
	 */
	public static ConnectionManager getInstance(Activity activity, Context context)
	{
		if(mInstance== null)
		{
			createInstance(activity, context);
		}
		return mInstance;
	}
	
	/**
	 * Create a rabbitmq connection with our lobby, so that communication can begin
	 * @param lobbyInfo
	 */
	public void initializeConnection(ConnectionInfo lobbyInfo)
	{
		String phoneName = configurationProperties.getProperty("PhoneName");
		String phoneAddress = getIPAddress(true);
		int port = lobbyInfo.getPort();
		String name = phoneName + "-" + phoneAddress + "-" +  Integer.toString(port)+ "-" + lobbyInfo.getName();
		
		ConnectionInfo phoneInfo = new ConnectionInfo(name, lobbyInfo.getIP(), lobbyInfo.getPort());
		phoneConnection = new RabbitMQConnection(phoneInfo, true);
		lobbyConnection = new RabbitMQConnection(lobbyInfo, true);
		
		startSending();
		startListening();
	}
	
	/**
	 * Start our thread that is waiting for events to be queued up and sent to the lobby.
	 */
	public void startSending()
	{
		//start listening
		Thread sendThread = new Thread()
		{
			public void run()
			{
				while(running)
				{
					//check to see if lobby connection has been initialized
					if(lobbyConnection == null)
					{
						try {
							sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					//get next event and send it
					JSONEvent event;
					try {
						if((event = outgoingEvents.poll(100, TimeUnit.MILLISECONDS)) != null){
							System.out.println(event.toString());
							lobbyConnection.sendMessage(event.toString());
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		sendThread.start();
	}
	/**
	 * Start our thread that listens for new messages from the lobby
	 */
	public void startListening()
	{
		Thread retrieveThread = new Thread()
		{
			 public void run() {
	            	//listen to lobby
	        		QueueingConsumer consumer = phoneConnection.beginListening();

	        	    while (running) {
	        	    	if(lobbyConnection == null)
						{
							try {
								sleep(100);
								continue;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
	        	    	
	        	    	QueueingConsumer.Delivery delivery = null;
	            		try {
	            			delivery = consumer.nextDelivery();
	            			String eventString = new String(delivery.getBody());
	            			System.out.println(eventString);
	            			if(eventString!= null)
	            			{
	            				//Event is a wrapped inside of a JSONObject, with a single argument that holds type
	    						JSONObject eventMessage = new JSONObject(eventString);
	    						String eventType = eventMessage.getString(JSONEvent.eventTypeKey);
	    						String sender = eventMessage.getString(JSONEvent.senderKey);
	    						
	    						//Create an event obect from the JSON event we receive
	    						JSONEvent event = null;
	    						if(eventType.equals(PlayerLobbyEvent.eventName))
	    							event = new PlayerLobbyEvent(eventMessage);
	    						else if(eventType.equals(GameLoadEvent.eventName))
	    							event = new GameLoadEvent(eventMessage);
	    						else if(eventType.equals(LobbyStateEvent.eventName))
	    						{
	    							event = new LobbyStateEvent(eventMessage);
	    						}
	    						//queue our new event up so that the Reactor can handle it
	    						incomingEvents.put(event);
	            			}
	            		}catch(Exception e)
	            		{
	            			e.printStackTrace();
	            		}
	        	    }
	            }
	        };
	    retrieveThread.start();
		
	    /**
	     * Create multiple threads to deal with handling events that are received
	     */
		for(int i=0; i<numThreads;i++)
		{
			Thread listenThread = new Thread()
	        {
	            public void run() {

            		System.out.println("starting retrieval thread");
	            	while(running)
	            	{
	            	try {
						JSONEvent event = incomingEvents.poll(100, TimeUnit.MILLISECONDS);
						
						if(event!= null)
						{
							System.out.println("handling event");
							reactor.handleEvent(event);
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	            }
	            }
	        };
			listenThread.start();
		};
		
	}
	
	
	public Vector<ConnectionInfo> getConnections()
	{
		return discoverer.SERVICES;
	}
	public ConnectionInfo getPhoneInfo()
	{
		return Configuration.getConnectionInfo(configurationProperties, "Phone");
	}
	/**
	 * Queue up an event to be sent to the lobby
	 * @param e
	 */
	public void queueEvent(JSONEvent e)
	{
		try {
			System.err.println("sending message");
			outgoingEvents.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * Gets our IPv$ address
	 * @param useIPv4
	 * @return
	 */
	 public static String getIPAddress(boolean useIPv4) 
	 {
	        try {
	            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
	            for (NetworkInterface intf : interfaces) {
	                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
	                for (InetAddress addr : addrs) {
	                    if (!addr.isLoopbackAddress()) {
	                        String sAddr = addr.getHostAddress().toUpperCase();
	                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
	                        if (useIPv4) {
	                            if (isIPv4) 
	                                return sAddr;
	                        } else {
	                            if (!isIPv4) {
	                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
	                                return delim<0 ? sAddr : sAddr.substring(0, delim);
	                            }
	                        }
	                    }
	                }
	            }
	        } catch (Exception ex) { } // for now eat exceptions
	        return "";
	    }
	 public ConnectionInfo getPhoneConnectionInfo()
	 {
		 return phoneConnection.getLobbyInfo();
	 }
	 public ConnectionInfo getLobbyConnectionInfo()
	 {
		 return lobbyConnection.getLobbyInfo();
	 }
}
