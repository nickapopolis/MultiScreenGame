package com.msandroidphoneclient.events;

public class LobbyStateHandler extends EventHandler {

	public LobbyStateHandler()
	{
		eventType = "LobbyStateEvent";
	}
	@Override
	public void handleEvent(JSONEvent evt, EventListener e) {
		LobbyStateEvent event = (LobbyStateEvent)evt;
		System.out.println("handling event "+ event.menuID);
		e.changeView(event.menuID);
	}

}
