package pcclient.networking;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import pcclient.model.LogFile;

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

		validateIP();
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
		connectionPort = Integer.parseInt(port.trim());
		validateIP();
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
	
	/**
	 * Validates IP address. If invalid IP sets to own
	 */
	public void validateIP()
	{
		//validate ip
		if(!validIP(connectionIP))
		{
			LogFile.write("invalid IP Address: " +connectionIP + " for host: " + connectionName );
			try {
				connectionIP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				LogFile.write("Could not get host address" );
			}
		}
		
	}
	public static boolean validIP(String ip) {
	    if (ip == null || ip.isEmpty()) return false;
	    ip = ip.trim();
	    if ((ip.length() < 6) & (ip.length() > 15)) return false;

	    try {
	        Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	        Matcher matcher = pattern.matcher(ip);
	        return matcher.matches();
	    } catch (PatternSyntaxException ex) {
	        return false;
	    }
	}
	
	public String toString()
	{
		return "name: "+ connectionName + " address: " + connectionIP + " port: "+ connectionPort;
	}
	
	public void setIP(String ip)
	{
		connectionIP = ip;
	}
	public void setName(String name)
	{
		connectionName = name;
	}
}
