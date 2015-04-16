package com.mhmt.autotextmate.activities;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 16, 2015
 * 
 */
public class Main extends ActionBarActivity {

	private ListView ruleListView;
	private DatabaseManager dbManager;
	private ArrayList<Rule> ruleArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Populate the list view of the activity with the items from the database
		dbManager = new DatabaseManager(getApplicationContext());

		ruleListView = (ListView) findViewById(R.id.listview_rulelist);

		populateDbView();
	}
	
	/**
	 * populates the ListView with the database data
	 */
	public void populateDbView(){
		ruleArray = dbManager.getRulesArray();

		if(ruleArray.isEmpty()) //if the loaded database is empty
		{
			//error toast
			Toast.makeText(getApplicationContext(), "You have no saved rules to view", Toast.LENGTH_SHORT).show();
		}
		else //make adapter with the patron array from the manager
		{
			ArrayAdapter<Rule> arrayAdapter = new ArrayAdapter<Rule>(this, android.R.layout.simple_list_item_1, ruleArray);
			//set adapter
			ruleListView.setAdapter(arrayAdapter);
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
		super.onResume();
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
		Toast.makeText(null, "Item " + mPosition + " clicked",Toast.LENGTH_LONG).show();
	}
}