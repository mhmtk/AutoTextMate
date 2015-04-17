package com.mhmt.autotextmate.activities;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.adapters.CustomAdapter;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 17, 2015
 * 
 */
public class Main extends ActionBarActivity {

	private ListView ruleListView;
	private DatabaseManager dbManager;
	private ArrayList<Rule> ruleArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("Main", "onCreate called");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Populate the list view of the activity with the items from the database
		dbManager = new DatabaseManager(getApplicationContext());

		//Instantiate view(s)
		ruleListView = (ListView) findViewById(R.id.listview_rulelist);

		populateListView();
	}
	
	/**
	 * populates the ListView with the database data
	 */
	private void populateListView(){
		//Get DB Data
		ruleArray = dbManager.getRulesArray();
		Log.i("Main", "rule array loaded");

		if(ruleArray.isEmpty()) //if the loaded database is empty
		{
			//error toast
			Toast.makeText(getApplicationContext(), "You have no saved rules to view", Toast.LENGTH_SHORT).show();
		}
		else //there's stuff in the DB, go ahead and populate the view
		{	
            //pass the adapter with the array to the list view
            ruleListView.setAdapter(new CustomAdapter(this, ruleArray, getResources()));
            Log.i("Main", "adapter to rulelistview set");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume(){
		Log.i("Main", "onResume called");
		
		super.onResume();
		
		populateListView();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_new:
			launchAddRuleActivity();
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * onClick method for Add button from the action bar, launches the AddRule activity
	 */
	private void launchAddRuleActivity() {
		startActivity(new Intent (this, AddRule.class));
	}

	/*****************  This function used by adapter ****************/
	public void onItemClick(int mPosition)
	{
		Toast.makeText(getApplicationContext(), "Item " + mPosition + " clicked.",Toast.LENGTH_LONG).show();
	}
}