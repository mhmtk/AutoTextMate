package com.mhmt.autotextmate.activities;

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
 * @version November April 13, 2015
 * 
 */
public class Main extends ActionBarActivity {

	private ListView ruleListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	 * populates the List View with the database data
	 */
	public void populateDbView(){
		
		//get the list view layout element
		ruleListView = (ListView) findViewById(R.id.listview_rulelist);
		
		//get a database manager
		DatabaseManager dbManager = new DatabaseManager(getApplicationContext());
		
		if(dbManager.getRulesArray().isEmpty()) //if the loaded database is empty
		{
			//error toast
			Toast.makeText(getApplicationContext(), "Database empty! Load one or enter info!", Toast.LENGTH_SHORT).show();
		}
		else //make adapter with the patron array from the manager
		{
		ArrayAdapter<Rule> arrayAdapter = new ArrayAdapter<Rule>(this, android.R.layout.simple_list_item_1, dbManager.getRulesArray());
		//set adapter
		ruleListView.setAdapter(arrayAdapter);
		}
	}
	private void launchAddRuleActivity() {
		startActivity(new Intent (this, AddRule.class));
	}
}