package com.msandroidclient;

import java.util.Vector;

import com.msandroidphoneclient.networking.ConnectionInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PCClientsArrayAdapter extends ArrayAdapter<ConnectionInfo> {
	
	private final Context context;
	private final Vector<ConnectionInfo> connections;

	public PCClientsArrayAdapter(Context context, Vector<ConnectionInfo> connections) {
		super(context, R.layout.pc_clients_list_item, connections);
		this.context = context;
		this.connections = connections;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("Added a lobby view" + connections.get(position).toString());
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.pc_clients_list_item, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.clientName);
		textView.setText(connections.get(position).getName());
		
		return rowView;
	}
}
