package com.mhmt.autotextmate.activities;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.SMS;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 20, 2015
 * 
 */
public class Outbox extends ActionBarActivity {

	
	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mListFragment fragment = new mListFragment();
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
	}

	public static class mListFragment extends ListFragment {

		private DatabaseManager dbManager;
		private ArrayList<SMS> smsArray;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        return inflater.inflate(R.layout.activity_outbox, container, false);
	    }
		
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// Set the list adapter
			dbManager = new DatabaseManager(context);
			smsArray = dbManager.getSMSArray();
			setListAdapter(new ArrayAdapter<SMS>(context, android.R.layout.simple_list_item_1, smsArray));
		}

		public void onListItemClick(ListView listView, View view, int position, long id) {
		}
	}
}