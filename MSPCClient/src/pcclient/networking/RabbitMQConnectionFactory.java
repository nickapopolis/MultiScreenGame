package pcclient.networking;


import java.net.InetAddress;

import pcclient.model.LogFile;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConnectionFactory {
	
	static ConnectionFactory connectionFactory = new ConnectionFactory();
	
	public static Connection createConnection(ConnectionInfo hostInfo)
	{
		Connection connection = null;
		
	    try
	    {
	        connectionFactory.setHost(hostInfo.getIP());
	    	connectionFactory.setPort(hostInfo.getPort());
	        connection = connectionFactory.newConnection();
	        
	    }
	    catch (Exception e)
	    {
	    	System.out.println("Could Not connect");
	    	//LogFile.write("Could not connect to broker" + hostInfo.toString());
	    }
	    return connection;
	}
	public static void main()
	{
		Connection connection = RabbitMQConnectionFactory.createConnection(new ConnectionInfo("nick","174.93.43.14" , 8008));
	}
	
}
