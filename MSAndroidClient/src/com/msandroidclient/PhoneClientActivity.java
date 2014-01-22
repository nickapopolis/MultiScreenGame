package com.msandroidclient;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;

import com.msandroidphoneclient.events.EventListener;
import com.msandroidphoneclient.events.PlayerLobbyEvent;
import com.msandroidphoneclient.game.GLGameView;
import com.msandroidphoneclient.networking.ConnectionInfo;
import com.msandroidphoneclient.networking.ConnectionManager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PhoneClientActivity extends FragmentActivity implements Observer,
		OnItemClickListener, EventListener, OnCheckedChangeListener,
		OnClickListener {

	ConnectionManager connectionManager;
	ViewPager viewPager;
	PCClientsArrayAdapter pcClientsArrayAdapter;
	GLGameView gameView;
	public static final int JOIN_LOBBY = 0;
	public static final int LEAVE_LOBBY = 1;
	public static final int READY = 2;
	public static final int UNREADY = 3;
	public static final int JOIN_GAME = 4;
	public static final int LEAVE_GAME = 5;

	public static final int VIEW_LOBBY = 0;
	public static final int VIEW_OPTIONS = 1;
	public static final int VIEW_GAME = 2;

	final int STATE_MENU = 0;
	final int STATE_SERVER_SELECTION = 1;
	final int STATE_LOBBY = 2;
	final int STATE_GAME = 3;

	ViewState viewState;

	MenuPagerAdapter menuPagerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_client);

		viewState = new ViewState(this);
		gameView = new GLGameView(this.getApplicationContext());

		connectionManager = ConnectionManager.getInstance(this,
				getApplicationContext());

		// initialize pager for menus
		viewPager = (ViewPager) findViewById(R.id.pager);

		// set the adapter for the pager
		MenuPagerAdapter menuPagerAdapter = new MenuPagerAdapter(
				getSupportFragmentManager(), this);
		viewPager.setAdapter(menuPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_phone_client, menu);
		return true;
	}

	/**
	 * list of lobbies has been updated
	 */
	public void update(Observable observable, Object data) {

		if (observable == viewState) {
			//view state object has changed, which is required due to thread restrictions
			runOnUiThread(new Runnable() {
				public void run() {
					if (viewState.getState() != VIEW_GAME)
						viewPager.setCurrentItem(viewState.getState());
					else
						setContentView(gameView);
				};

			});
		} else {
			//lobby list has been changed
			runOnUiThread(new Runnable() {
				public void run() {
					pcClientsArrayAdapter.notifyDataSetChanged();
					System.out.println("Lobby List changed");
				};

			});
		}

	}

	/**
	 * menu item has been clicked
	 */
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		ConnectionInfo lobbyInfo = connectionManager.getConnections().get(
				position);
		connectionManager.initializeConnection(lobbyInfo);

		sendLobbyEvent(JOIN_LOBBY);
		viewPager.setCurrentItem(1);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.joinButton:
			sendLobbyEvent(JOIN_GAME);
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.readyCheckBox:
			if (isChecked)
				sendLobbyEvent(READY);
			else
				sendLobbyEvent(UNREADY);
		}
	}

	public void loadLevel() {

	}

	public void changeView(int view) {
		System.out.println("changing view");
		switch (view) {
		case STATE_MENU:
			viewState.setState(VIEW_LOBBY);
			break;
		case STATE_SERVER_SELECTION:
			break;
		case STATE_LOBBY:
			viewState.setState(VIEW_OPTIONS);
			break;
		case STATE_GAME:
			viewState.setState(VIEW_GAME);
			break;
		}

	}

	/**
	 * Send a message to the lobby when we stop the process to let it know that
	 * we are no longer connected
	 */
	protected void onStop() {
		super.onStop();

		sendLobbyEvent(LEAVE_LOBBY);
	}

	private void sendLobbyEvent(int lobbyAction) {
		PlayerLobbyEvent evt;
		try {
			evt = new PlayerLobbyEvent(connectionManager
					.getLobbyConnectionInfo().getName(), connectionManager
					.getPhoneConnectionInfo().getName(), connectionManager
					.getPhoneConnectionInfo().getName(), lobbyAction);
			if (connectionManager != null) {
				connectionManager.queueEvent(evt);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
