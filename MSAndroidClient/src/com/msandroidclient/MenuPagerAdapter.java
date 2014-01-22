package com.msandroidclient;

import com.msandroidphoneclient.networking.ConnectionManager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class MenuPagerAdapter extends FragmentStatePagerAdapter {

	LobbyListFragment lobbyListFragment;
	LobbyJoinFragment lobbyJoinFragment;
	LobbyReadyFragment lobbyReadyFragment;
	static PhoneClientActivity listener;
	
	public MenuPagerAdapter(FragmentManager fm, PhoneClientActivity listener) {
		super(fm);

		// create our lobby list fragment
		lobbyListFragment = new LobbyListFragment();

		// create lobby join fragment
		lobbyJoinFragment = new LobbyJoinFragment();

		// create lobby ready fragment
		lobbyReadyFragment = new LobbyReadyFragment();
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null;
		switch (i) {
		case 0:
			fragment = lobbyListFragment;
			break;
		case 1:
			fragment = lobbyJoinFragment;
			break;
		case 2:
			fragment = lobbyReadyFragment;
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title = "";
		switch (position) {
		case 0:
			title = "Select Lobby";
			break;
		case 1:
			title = "Join Game";
			break;
		case 2:
			title = "Player Ready";
			break;
		}
		return title;
	}

	public Fragment getLobbyJoinFragment() {
		return lobbyJoinFragment;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class LobbyListFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		ListView lobbyList;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// get arguments
			Activity activity = getActivity();
			ConnectionManager connectionManager = ConnectionManager
					.getInstance(activity, activity.getApplicationContext());

			// create our view root
			lobbyList = new ListView(activity);
			lobbyList.setAdapter(new PCClientsArrayAdapter(activity,
					connectionManager.getConnections()));
			lobbyList.setOnItemClickListener( listener);
			return lobbyList;
		}

	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class LobbyJoinFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		private Button joinButton;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.lobby_join_layout, container,
					false);
			joinButton = (Button) view.findViewById(R.id.joinButton);
			joinButton.setOnClickListener(listener);
			return view;
		}
		public void setListener(Object onClickListener) {
			
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class LobbyReadyFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		private CheckBox readyBox;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.lobby_ready_layout,
					container, false);
			readyBox = (CheckBox) view.findViewById(R.id.readyCheckBox);
			readyBox.setOnCheckedChangeListener(listener);
			return view;

		}
	}
}
