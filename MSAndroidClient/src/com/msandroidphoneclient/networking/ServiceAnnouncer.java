package com.msandroidphoneclient.networking;


public interface ServiceAnnouncer
{
	public void registerService();
	public void unregisterService();
	public boolean isRegistered();
}
