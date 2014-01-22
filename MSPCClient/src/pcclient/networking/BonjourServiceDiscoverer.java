package pcclient.networking;
import java.util.Vector;

import com.apple.dnssd.*;
public class BonjourServiceDiscoverer implements BrowseListener
{
	DNSSDService browser;
	String serviceName = "gameApp1";
	String tcp = "_tcp";
	String udp = "_udp";
	
	Vector<ConnectionInfo> services;
	
	public BonjourServiceDiscoverer()
	{
		services = new Vector<ConnectionInfo>();
		try {
			browser = DNSSD.browse("_http._tcp.", this);
		} 
		catch (DNSSDException e) {
			e.printStackTrace();
		} 
	}
	@Override
	public void operationFailed(DNSSDService browser, int arg1)
	{
		System.out.println("failed");
	}

	@Override
	public void serviceFound(DNSSDService browser, int flags, int ifIndex, String serviceName, String regType, String domain)
	{
		System.out.println("Service found" + serviceName + "ifindex: " +ifIndex + "regtype: " + regType + "domain: " + domain);
		String[] serviceInfo = serviceName.split("/");
		services.add(new ConnectionInfo(
				serviceInfo[0], //lobby name
				serviceInfo[1], //ip
				Integer.parseInt(serviceInfo[2])//port
				
		));
		/*InetAddress theAddress;
		try {
			theAddress = InetAddress.getByName(serviceName);
			System.err.println(theAddress.toString());
		} catch (UnknownHostException e) {
			
		}*/
	
	}

	@Override
	public void serviceLost(DNSSDService browser, int flags, int ifIndex, String serviceName, String regType, String domain)
	{
		System.out.println("Service lost" + serviceName);
	}
	public static void main(String args[])
	{
		new BonjourServiceDiscoverer();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
