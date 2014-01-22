package pcclient.events;

public class PlayerLobbyEventHandler extends EventHandler {

	public PlayerLobbyEventHandler()
	{
		eventType = "PlayerLobbyEvent";
	}
	@Override
	public void handleEvent(JSONEvent evt, EventListener e) 
	{
		PlayerLobbyEvent event = (PlayerLobbyEvent)evt;
		e.playerLobbyEvent(event.playerID, event.actionType);
	}

}
