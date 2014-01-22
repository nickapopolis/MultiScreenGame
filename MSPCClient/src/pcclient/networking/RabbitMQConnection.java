package pcclient.networking;


import java.io.IOException;

import pcclient.model.LogFile;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitMQConnection 
{
	Connection connection;
	Channel channel;
	ConnectionInfo lobbyInfo;
	boolean connected = false;
	public RabbitMQConnection(ConnectionInfo lobbyInfo, boolean keepTrying)
	{
		LogFile.write("Initialize rabbitmqConnection");
		this.lobbyInfo = lobbyInfo;
		
		//connect to rabbitmq
		while(!connected && keepTrying )
		{
			try {
				connectToLobby(lobbyInfo);
			} catch (Exception e) {
				LogFile.write("error connecting to rabbitmq");
				continue;
			}
			connected = true;
			System.out.println("Connected");
		}
	}
	public boolean connectToLobby(ConnectionInfo lobbyInfo) throws Exception
	{
		
		connection = RabbitMQConnectionFactory.createConnection(lobbyInfo);
		//connection succeeded
		channel = connection.createChannel();
		channel.queueDeclare(lobbyInfo.getName(), false, false, false, null);
		return true;
	}
	public QueueingConsumer beginListening()
	{
		QueueingConsumer consumer = new QueueingConsumer(channel);
	    try {
			channel.basicConsume(lobbyInfo.getName(), true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return consumer;
	}
	
	public void sendMessage(String message)
	{
		try {
			channel.basicPublish("", lobbyInfo.getName(), null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public boolean isActive()
	{
		return connection != null;
	}
}
