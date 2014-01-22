package pcclient.model;

public interface PCClientListener {
	public void registerPhone();
	public void deregisterPhone();
	public void registerLobby();
	public void deregisterLobby();
	
}
