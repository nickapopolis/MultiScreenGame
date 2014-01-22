package com.msandroidphoneclient.events;


public class GameLoadHandler extends EventHandler {

	
	public GameLoadHandler()
	{
		eventType = "GameLoadEvent";
	}
	@Override
	public void handleEvent(JSONEvent evt, EventListener e) {
		GameLoadEvent event  = (GameLoadEvent)evt;
		e.loadLevel();

	}

}
