package pcclient.networking;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import pcclient.events.EventListener;
import pcclient.events.GameLoadEvent;
import pcclient.events.JSONEvent;
import pcclient.events.PlayerLobbyEvent;
import pcclient.model.LogFile;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;



public class ConnectionManager
{
	ConcurrentHashMap<String, RabbitMQConnection> phoneConnections;
	ConcurrentHashMap<String, RabbitMQConnection> lobbyConnections;
	RabbitMQConnection serverConnection;
	RabbitMQConnection lobbyConnection;
	RabbitMQConnection hostConnection;
	RabbitMQBroker broker;
	BonjourServiceAnnouncer serviceAnnouncer;

	ArrayBlockingQueue<JSONEvent> incomingEvents;
	ArrayBlockingQueue<JSONEvent> outgoingEvents;
	Reactor reactor;
	
	LinkedList<ConnectionInfo> hosts;
	
	Properties configurationProperties;
	String configFile = "config.txt";
	Thread listenThread;
	boolean listening = false;
	boolean sending = false;
	boolean connectedToLobby = false;
	int numThreads = 4;
	
	public ConnectionManager(EventListener listener)
	{
		hosts = new LinkedList<ConnectionInfo>();
		incomingEvents = new ArrayBlockingQueue<JSONEvent>(100);
		outgoingEvents = new ArrayBlockingQueue<JSONEvent>(100);
		
		LogFile.write("Initialize ConnectionManager");
		phoneConnections = new ConcurrentHashMap<String, RabbitMQConnection>();
		lobbyConnections = new ConcurrentHashMap<String, RabbitMQConnection>();
		configurationProperties = Configuration.readProperties(configFile);
		
		//init reactor and event handlers
		reactor = new Reactor(listener, Configuration.getEventHandlers(configFile));
		initializeConnections(configFile);
		
	}
	public void initializeConnections(String configFile)
	{
		//get initialization settings from config file
		ConnectionInfo serverInfo = Configuration.getConnectionInfo(configurationProperties, "Server");;
		ConnectionInfo brokerInfo = Configuration.getConnectionInfo(configurationProperties, "Lobby");
		String rabbitMQDir = configurationProperties.getProperty("RabbitMQDir");
		
		//initialize the broker
		broker = new RabbitMQBroker(rabbitMQDir, brokerInfo.getName(), brokerInfo.getPort()); 
		
		
		//LogFile.write("Connected to Server");
		
		//create lobby connection
		brokerInfo.setName(brokerInfo.getName()+ "-" + brokerInfo.getIP()+ "-"+ brokerInfo.getPort());
		lobbyConnection = new RabbitMQConnection(brokerInfo, true);
		startListening();
		
		//create bonjour service
		serviceAnnouncer = new BonjourServiceAnnouncer(brokerInfo);
		LogFile.write("Service Announcer Initialized");
		
		//create server connection
		serverConnection = new RabbitMQConnection(serverInfo, false);
		startSending();
		
	}
	/**
	 * Starts listening so that the lobby can receive messages
	 */
	public void startListening()
	{
		listening = true;
		listenThread = new Thread()
        {
            public void run() {
            	//listen to phone clients
        		QueueingConsumer consumer = lobbyConnection.beginListening();
        	    while (listening) {
        	      QueueingConsumer.Delivery delivery = null;
        		try {
        			delivery = consumer.nextDelivery();
        			String eventString = new String(delivery.getBody());
        			if(eventString!= null)
        			{
        				//Event is a wrapped inside of a JSONObject, with a single argument that holds type
						
						JSONObject eventMessage = (JSONObject) new JSONParser().parse(eventString);
						String eventType = (String) eventMessage.get(JSONEvent.eventTypeKey);
						String sender = (String) eventMessage.get(JSONEvent.senderKey);
						

						JSONEvent event = null;
						if(eventType.equals(PlayerLobbyEvent.eventName))
							event = new PlayerLobbyEvent(eventMessage);
						else if(eventType.equals(GameLoadEvent.eventName))
							event = new GameLoadEvent(eventMessage);
	           	      	System.out.println(" [x] Received '" + event + "'");
	           	      	

	           	      	//message came from server
	           	      	if(sender.equals(serverConnection.getLobbyInfo().getName()))
	           	      	{
	           	      		incomingEvents.put(event);
	           	      	}
						//message came from host
	           	      	else if(!isHost() && sender.equals(hostConnection.getLobbyInfo().getName()))
						{
		           	      	incomingEvents.put(event);
						}
						//message came from child but not host
						else if(!isHost() && !sender.equals(hostConnection.getLobbyInfo().getName()))
						{
							outgoingEvents.put(event);
						}
						//message came from child and host
						else 
						{
							incomingEvents.put(event);
						}
						
        			}
           	      	
           	      	
        		} catch (ShutdownSignalException e) {
        			e.printStackTrace();
        		} catch (ConsumerCancelledException e) {
        			e.printStackTrace();
        		} catch (InterruptedException e) {
        			e.printStackTrace();
        		} catch (ParseException e) {
					e.printStackTrace();
				}
        	    }
            }
        };
		listenThread.start();
		
		
		for(int i=0; i<numThreads;i++)
		{
			Thread reactorThread = new Thread()
	        {
	            public void run() {
	            	while(listening)
	            	{
		            	try {
		            		JSONEvent event;
							if((event = incomingEvents.poll(100, TimeUnit.MILLISECONDS))!= null)
							{
								reactor.handleEvent(event);
							}
							
							
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 
	            	}
	            }
	        };
			reactorThread.start();
		};
	}
	public void startSending()
	{
		sending = true;
		Thread sendThread = new Thread()
		{
			public void run()
			{
				System.err.println("sending");
				while(sending)
				{
					JSONEvent evt;
					
					try {
						if((evt = outgoingEvents.poll(100, TimeUnit.MILLISECONDS))!= null)
						{
							String destination = evt.getDest();
							System.err.println(evt.toString());
							System.err.println("destination: " + destination);
							if(phoneConnections.containsKey(destination))
							{
								RabbitMQConnection connection = phoneConnections.get(destination);
								if(connection!= null)
								{
									System.err.println("sent message");
									connection.sendMessage(evt.toString());
								}
								else
								{
									System.err.println("connection null");
								}
							}
							else
							{
								System.err.println("does not contain connection");
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		sendThread.start();
	}
	public void stop()
	{
		//tell all clients we have disconnected
		
		listening = false;
		//unregister service
		serviceAnnouncer.unregisterService();
	}
	
	public int getNextAvailablePort()
	{
		int minPort = 5672;
		int maxPort =  49151;
		for (int i=minPort;i<maxPort; i++)
		{
			//try and create a server socket on port, if it is available then this will succeed
			ServerSocket ss = null;
			try {
				ss = new ServerSocket(i);
			} catch (IOException e) {
				continue;
			}

			//found open port, close it
			try {
				ss.close();
			} catch (IOException e) {
			}
			//return number
			return i;
		}
		
		//failed to find a port
		return -1;
	}
	
	public void addPlayerConnection(String playerID, ConnectionInfo playerInfo)
	{
		RabbitMQConnection connection = new RabbitMQConnection(playerInfo, true);
		System.err.println("add connection" + playerID);
		phoneConnections.put(playerID, connection);
	}
	public void addLobbyConnection(String lobbyID, ConnectionInfo lobbyInfo)
	{
		RabbitMQConnection connection = new RabbitMQConnection(lobbyInfo, true);
		lobbyConnections.put(lobbyID, connection);
	}
	public void removeLobbyConnection(String lobbyID)
	{
		lobbyConnections.remove(lobbyID);
	}
	public void removePlayerConnection(String playerID)
	{
		lobbyConnections.remove(playerID);
	}
	
	public boolean isHost()
	{
		return hostConnection == null;
	}
	
	public void connectToHost(ConnectionInfo hostInfo)
	{
		hostConnection = new RabbitMQConnection(hostInfo, true);
	}
	public String getID()
	{
		return lobbyConnection.getLobbyInfo().getName();
	}
	public void queueEvent(JSONEvent event)
	{
		try {
			System.out.println(event);
			outgoingEvents.put(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
