package com.msandroidclient;

import java.util.Observable;
import java.util.Observer;


public class ViewState extends Observable{
	int state = 0;
	
	public ViewState(Observer obs)
	{
		this.addObserver(obs);
	}
	public void setState(int state)
	{
		this.state = state;
		this.setChanged();
		this.notifyObservers();
	}
	public int getState()
	{
		return state;
	}
}
