package com.msandroidphoneclient.networking;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConnectionFactory {
	
	static ConnectionFactory connectionFactory = new ConnectionFactory();
	
	/**
	 * Creates a new connectin with the specified ip and port
	 * @param hostInfo
	 * @return
	 */
	public static Connection createConnection(ConnectionInfo hostInfo)
	{
		Connection connection = null;
		
	    try
	    {
	        connectionFactory.setHost(hostInfo.getIP());
	    	connectionFactory.setPort(hostInfo.getPort());
	        connection = connectionFactory.newConnection();
	        System.out.println(connection);
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return connection;
	}
	
}
