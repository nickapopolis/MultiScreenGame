
package com.msandroidclient;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;

import com.msandroidphoneclient.networking.ConnectionInfo;


public class Configuration
{

	protected static String[] reservedKeys = {"PhoneName", "PhonePort", "PhoneIP"};
	/**
	 * Initializes event handlers from configuration file
	 * @param fileName
	 */
	public static ArrayList<String> getEventHandlers(Context context, String fileName)
	{
		ArrayList<String> handlers = new ArrayList<String>();
		
		try{
			Properties p = new Properties();
			
			AssetManager am = context.getAssets();
			InputStream IS = am.open(fileName);

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
	
	private static boolean isReservedKey(String key)
	{
		for(int i=0; i<reservedKeys.length; i++)
		{
			if(key.equals(reservedKeys[i]))
				return true;
		}
		return false;
	}
	
	public static String getProperty(Context context, String fileName, String key)
	{
		String address = null;
			
		//instantiates a fileStream Object to read file
		InputStream IS;
		try {
			AssetManager am = context.getAssets();
			IS = am.open(fileName);
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
	public static Properties readProperties(Context context, String fileName)
	{
		Properties p = null;
		//instantiates a fileStream Object to read file
		InputStream IS;
		try {
			AssetManager am = context.getAssets();
			IS = am.open(fileName);
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
