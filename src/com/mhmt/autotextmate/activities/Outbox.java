package com.mhmt.autotextmate.activities;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.SMS;

import android.app.ListActivity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class Outbox extends ListActivity {

	private DatabaseManager dbManager;
	private ArrayList<SMS> smsArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outbox);

		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		// instantiate fields
		dbManager = new DatabaseManager(getApplicationContext());
		smsArray = dbManager.getSMSArray();

		// Set the list adapter
		setListAdapter(new ArrayAdapter<SMS>(this, android.R.layout.simple_list_item_1, smsArray));
	}
}