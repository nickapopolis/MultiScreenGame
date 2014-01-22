package com.msandroidphoneclient.networking;
public class ConnectionInfo
{
	String connectionName;
	String connectionIP;
	int connectionPort;
	
	/**
	 * generic class for storing connection information relating to JMS or RMI
	 * @param name
	 * @param ip
	 * @param port
	 */
	public ConnectionInfo(String name, String ip, int port)
	{
		connectionName = name;
		connectionIP = ip;
		connectionPort = port;
	}
	
	/**
	 * generic class for storing connection information relating to JMS or RMI
	 * @param name
	 * @param ip
	 * @param port
	 */
	public ConnectionInfo(String name, String ip, String port)
	{
		connectionName = name;
		connectionIP = ip;
		connectionPort = Integer.parseInt(port);
	}
	public String getName()
	{
		return connectionName;
	}
	public String getIP()
	{
		return connectionIP;
	}
	public int getPort()
	{
		return connectionPort;
		
	}
	public boolean equals(ConnectionInfo c2)
	{
		return connectionName.equals(c2.connectionName) && connectionIP.equals(c2.connectionName) && connectionPort == c2.connectionPort;
	}
	
	public String toConnectionString()
	{
		return connectionIP+":" + connectionPort;
	}
}
