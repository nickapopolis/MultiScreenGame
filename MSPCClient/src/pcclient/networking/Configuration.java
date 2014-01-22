
package pcclient.networking;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;


public class Configuration
{

	protected static String[] reservedKeys = {
		"ServerPort",
		"ServerIP",
		"ServerName",
		"LobbyPort",
		"LobbyIP",
		"LobbyName",
		"RabbitMQDir"};
	
	
	/**
	 * Initializes event handlers from configuration file
	 * @param fileName
	 */
	public static ArrayList<String> getEventHandlers(String fileName)
	{
		ArrayList<String> handlers = new ArrayList<String>();
		
		try{
			Properties p = new Properties();
			
			//=====added to get absolute path for config file==//
			//gets the file
			File f1 = new File(fileName);  
							
			//instantiates a fileStream Object to read file
			InputStream IS = new FileInputStream(f1); 
			//=================================================//

			p.load(IS);
			
			//removes hard coded event handlers
			Enumeration<Object> keys = p.keys();
			while (keys.hasMoreElements())
			{
				String next = keys.nextElement().toString();
				if(!isReservedKey(next))
				{
					handlers.add((String)p.get(next).toString());
				}
			}
			IS.close();
		}catch (Exception e){
			String error = e.toString();
			System.err.println(String.format("%s \n%s", "Error initializing event handlers:", error));
		}
		return handlers;
	}
	/**
	 * Parses a configuration file to get a list of names of viable servers the Client can connect to.
	 * @param fileName
	 * @return
	 */
	public static ArrayList<ConnectionInfo> getServerNames(String fileName)
	{
		
		try {	
			//instantiates a fileStream Object to read file
			InputStream IS;
			IS = new FileInputStream(new File(fileName));
			
			//Create new properties object to parse file
			Properties p = new Properties();
			p.load(IS);
			
			//add ServerNames to a list
			Enumeration e = p.keys();
			String key = "ServerName";
			String servers = p.getProperty(key);
			String[] names = servers.split("[,]");
			
			ArrayList<ConnectionInfo> nameList = new ArrayList<ConnectionInfo>();
			
			for(int i=0;i<names.length;i++)
			{
				String[] info = names[i].split("[;]");
				nameList.add(new ConnectionInfo(info[0].trim(), info[1].trim(), (int)Double.parseDouble(info[2].trim())));
			}
			return nameList;
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * get the port that our broker will connect to 
	 * from the file
	 * @param fileName
	 * @return
	 */
	public static int getBrokerPort(String fileName)
	{
		int port = -1;
			
		//instantiates a fileStream Object to read file
		InputStream IS;
		try {
			IS = new FileInputStream(new File(fileName));
			//Create new properties object to parse file
			Properties p = new Properties();
			p.load(IS);
			
			//add ServerNames to a list
			String key = "BrokerPort";
			if(p.containsKey(key))
			{
				port = (int)Double.parseDouble(p.getProperty(key));
			}
			IS.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return port;
	}
	/**
	 * get the address that our broker will connect to 
	 * from the file
	 * @param fileName
	 * @return
	 */
	public static String getBrokerAddress(String fileName)
	{
		String address = null;
			
		//instantiates a fileStream Object to read file
		InputStream IS;
		try {
			IS = new FileInputStream(new File(fileName));
			//Create new properties object to parse file
			Properties p = new Properties();
			p.load(IS);
			
			//add ServerNames to a list
			String key = "BrokerAddress";
			if(p.containsKey(key))
			{
				address = p.getProperty(key);
			}
			IS.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return address;
	}
	private static boolean isReservedKey(String key)
	{
		for(int i=0; i<reservedKeys.length; i++)
		{
			if(key.equals(reservedKeys[i]))
				return true;
		}
		return false;
	}
	
	public static String getProperty(String fileName, String key)
	{
		String address = null;
			
		//instantiates a fileStream Object to read file
		InputStream IS;
		try {
			IS = new FileInputStream(new File(fileName));
			//Create new properties object to parse file
			Properties p = new Properties();
			p.load(IS);
			//add ServerNames to a list
			if(p.containsKey(key))
			{
				address = p.getProperty(key);
			}
			IS.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return address;
	}
	public static Properties readProperties(String fileName)
	{
		Properties p = null;
		//instantiates a fileStream Object to read file
		InputStream IS;
		try {
			IS = new FileInputStream(new File(fileName));
			//Create new properties object to parse file
			p = new Properties();
			p.load(IS);
		}catch (IOException e) {
			e.printStackTrace();
		} 
		return p;
	}
	public static ConnectionInfo getConnectionInfo(Properties p, String source)
	{
		String name, ip, port;
		name = (String) p.get(source+"Name");
		ip = (String) p.get(source+"IP");
		port = (String) p.get(source+"Port");
		ConnectionInfo c = new ConnectionInfo(name, ip, port);
		return c;
	}
}
