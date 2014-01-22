package com.msandroidphoneclient.networking;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.apache.http.conn.util.InetAddressUtils;

import com.msandroidclient.PhoneClientActivity;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;
import android.widget.ArrayAdapter;

public class ConnectionDiscoverer extends Observable implements
		DiscoveryListener, ResolveListener, RegistrationListener {

	String serviceName = "gameApp2";
	String tcp = "_tcp";
	String udp = "_udp";
	NsdManager nsdManager;
	public Vector<ConnectionInfo> SERVICES = new Vector<ConnectionInfo>();

	/**
	 * ConnectionDiscoverer will search for services on the local network, specifically game lobbies.
	 * Once a service is found, the ConnectionManager will add it to the list of lobbies already found so that the
	 * user can select which it is they want to join.
	 * @param activity
	 * @param context
	 */
	public ConnectionDiscoverer(Activity activity, Context context) {
		super();
		SERVICES = new Vector<ConnectionInfo>();

		this.addObserver(((PhoneClientActivity) activity));

		// The name is subject to change based on conflicts
		// with other services advertised on the same network.
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		MulticastLock lock = wifi.createMulticastLock("flin_lock");
		lock.setReferenceCounted(true);
		System.out.println("acquiring lock");
		lock.acquire();

		System.out.println("acquired lock");
		nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
		System.out.println("discovering");
		nsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD,
				this);
		System.out.println("discovered");
		lock.release();

	}

	// Called as soon as service discovery begins.
	public void onDiscoveryStarted(String regType) {
		System.out.println("starting discovery");
	}

	public void onDiscoveryStopped(String serviceType) {
		System.out.println("discovery stopped");
	}

	public void onServiceFound(NsdServiceInfo service) {
		System.out.println("found service" + service.getServiceName());

		if (service.getServiceName().contains("MSLobby")) {
			String info[] = service.getServiceName().split("/");
			ConnectionInfo connectionInfo = new ConnectionInfo(info[1], // lobby
																		// name
					info[2], // ip
					Integer.parseInt(info[3]));// port

			SERVICES.add(connectionInfo);
			this.setChanged();
			this.notifyObservers();
		}

	}

	public void onServiceLost(NsdServiceInfo serviceInfo) {
		System.out.println("lost service " + serviceInfo.getServiceName());
		
		//If our service contains the MSLobby, we know that this service is associated with a game lobby
		if (serviceInfo.getServiceName().contains("MSLobby")) {
			String serviceInfoName = serviceInfo.getServiceName().split("/")[1];

			// When the network service is no longer available.
			// Internal bookkeeping code goes here.
			for (int i = 0; i < SERVICES.size(); i++) {
				if (SERVICES.get(i).getName().equals(serviceInfoName)) {
					SERVICES.remove(i);
					this.setChanged();
					this.notifyObservers();
				}

			}
		}

	}

	public void onServiceResolved(NsdServiceInfo serviceInfo) {

		System.err.println(serviceInfo.getServiceName() + " service resolved");
		String serviceName = serviceInfo.getServiceName();
		
		//If our service contains the MSLobby, we know that this service is associated with a game lobby
		if (serviceName.contains("MSLobby")) {
			System.out.println("adding service");
			ConnectionInfo connectionInfo = new ConnectionInfo(
					serviceInfo.getServiceName(), serviceInfo.getHost()
							.getHostAddress(), serviceInfo.getPort());

		}
	}

	public void onStartDiscoveryFailed(String serviceType, int errorCode) {

	}

	public void onStopDiscoveryFailed(String serviceType, int errorCode) {
	}

	public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

	}

	public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

	}

	public void onServiceRegistered(NsdServiceInfo serviceInfo) {

	}

	public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
	}

	public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
	}

}
