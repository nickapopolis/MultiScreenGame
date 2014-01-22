package com.msandroidphoneclient.networking;

import java.io.IOException;




import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitMQConnection 
{
	Connection connection;
	Channel channel;
	ConnectionInfo lobbyInfo;
	
	boolean active = false;
	boolean connected = false;
	
	/**
	 * A connection for communicating with a game lobby
	 * @param lobbyInfo
	 * @param keepTrying
	 */
	public RabbitMQConnection(ConnectionInfo lobbyInfo, boolean keepTrying)
	{
		this.lobbyInfo = lobbyInfo;
		while(!connected && keepTrying )
		{
			try {
				connectToLobby();
			} catch (Exception e) {
				continue;
			}
			connected = true;
			System.out.println("Connected");
		}
	}
	public synchronized boolean connectToLobby()
	{
		Thread t = new Thread()
		{
			public void run()
			{
				try {
					connection = RabbitMQConnectionFactory.createConnection(lobbyInfo);
					channel = connection.createChannel();
					System.out.println(channel);
					channel.queueDeclare(lobbyInfo.getName(), false, false, false, null);	
					//connection succeeded
				} catch (IOException e) {
					//failed to connect
				}
			}
			
			
		};
		if (channel != null)
		{
			active = true;
		}
		t.start();
		return true;
	}
	public QueueingConsumer beginListening()
	{
		while(channel == null)
		{
		}

		try {
			channel.queueDeclare(lobbyInfo.getName(), false, false, false, null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		QueueingConsumer consumer = new QueueingConsumer(channel);
	    try {
			channel.basicConsume(lobbyInfo.getName(), true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return consumer;
	}
	public synchronized void sendMessage(final String message)
	{
		Thread t = new Thread()
		{
			public void run()
			{
				while(!isActive())
				{
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("sending");
				System.out.println(channel);
				System.out.println(message);
				System.out.println(lobbyInfo.getName());
				
				try {
					channel.basicPublish("", lobbyInfo.getName(), null, message.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		
	}
	public Connection getConnection() {
		return connection;
	}
	public Channel getChannel() {
		return channel;
	}
	public ConnectionInfo getLobbyInfo() {
		return lobbyInfo;
	}
	public String getChannelName()
	{
		return lobbyInfo.getName();
	}
	public void reconnect()
	{
		connectToLobby();
	}
	public boolean isActive()
	{
		return connection != null;
	}
}
