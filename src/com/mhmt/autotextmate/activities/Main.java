package com.mhmt.autotextmate.activities;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.adapters.RuleListViewAdapter;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.appwidget.AppWidgetManager;
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
 * @version November May 1, 2015
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
		ruleListView = (ListView) findViewById(R.id.main_list);

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
			ruleListView.setAdapter(new RuleListViewAdapter(this, ruleArray, getResources()));
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

	/**
	 * onClick button of each row of the listView, called thru the RuleListViewAdapter
	 * 
	 * @param mPosition the position of the item on the list, 0 indexed
	 */
	public void onItemClick(int mPosition)
	{
		//documentation and feedback
		Log.i("Main", "Item " + mPosition + " clicked.");
		Toast.makeText(getApplicationContext(), "Item " + mPosition + " clicked.",Toast.LENGTH_LONG).show();
		// Edit window?
		// more info?
	}

	/**
	 * onClick-ish method for the togglebutton in each row of the listView, 
	 * called thru the RuleListViewAdapter 
	 * 
	 * @param mName the position of the toggle's item on the list, 0 indexed
	 * @param isChecked True if toggle is on, false otherwise
	 */
	public void onItemToggleClicked(String mName, boolean isChecked) {
		//Documentation and feedback
		Log.i("Main", "Toggle item of " + mName + " set to " + isChecked + ".");
		Toast.makeText(getApplicationContext(), "Rule " + mName + " turned " + ( (isChecked) ? "on" : "off"),Toast.LENGTH_LONG).show();

		//Change the status of the rule in the database-
		int wID = dbManager.setRuleStatus(mName, isChecked);
		
		if (wID != AppWidgetManager.INVALID_APPWIDGET_ID) {
			//Send a broadcast for the widget to update itself
			Intent updateWidgetIntent = new Intent();
			updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{wID} );
			updateWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			this.sendBroadcast(updateWidgetIntent);
			Log.i("Main", "Broadcasted " + updateWidgetIntent.toString());			
		}
		else
			Log.i("Main", "Did not broadcast widget update b/c " + mName + " has no widget");
		
	}

}