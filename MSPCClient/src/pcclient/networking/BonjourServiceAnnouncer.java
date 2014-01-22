package pcclient.networking;
import com.apple.dnssd.*;


// if there is an issue with having restricted access to dns_sd, remove and add jre system library
public class BonjourServiceAnnouncer implements ServiceAnnouncer, RegisterListener
{
	private DNSSDRegistration serviceRecord;
	private boolean registered;
	ConnectionInfo info;
	
	public  BonjourServiceAnnouncer(ConnectionInfo info)
	{
		this.info = info;
		registerService();
	}
	public boolean isRegistered(){
		return registered;
	}

	public void registerService()  {
		try {
			String ip = info.getIP();
			String lobbyName = info.getName();
			int port = info.getPort();
			String serviceName = "MSLobby" +"/"+ lobbyName +"/"+ip+"/"+ Integer.toString(port) ;
			
			
			//serviceRecord = DNSSD.register(0,0,serviceName,"_gameApp2._tcp", null,null,1234,null,this);
			serviceRecord = DNSSD.register(0,0,serviceName,"_http._tcp.", null,null,port,null,this);
		} catch (DNSSDException e) {
			System.err.println("unable to register service");
			e.printStackTrace();
		}
	}

	public void unregisterService(){
		serviceRecord.stop();
		registered = false;
		System.out.println("unregistered service");
	}

	public void serviceRegistered(DNSSDRegistration registration, int flags,String serviceName, String regType, String domain){
		registered = true;
		System.out.println("Service Registered: "+ serviceName + " " + regType+ " "+ domain);
	}

	public void operationFailed(DNSSDService registration, int error){
		// handle error
	}
}
