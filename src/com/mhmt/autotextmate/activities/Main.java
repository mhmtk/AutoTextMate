package com.mhmt.autotextmate.activities;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.adapters.RuleListViewAdapter;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 7, 2015
 * 
 */
public class Main extends ActionBarActivity {

	private ListView ruleListView;
	private ProgressBar progressBar;
	
	private DatabaseManager dbManager;
	private ArrayList<Rule> ruleArray;
	private String logTag = "Main";
	private RuleListViewAdapter mListAdapter;
	private boolean runResume;
	private boolean listLoaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(logTag, "onCreate called");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		//Instantiate view(s)
		ruleListView = (ListView) findViewById(R.id.main_list);
		progressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
		
		runResume = false;
		
		// Call for listview population
		new PopulateListTask().execute(true);
	}
	
	@Override
	public void onResume(){
		Log.i(logTag, "onResume called");
		super.onResume();
		if(runResume) {
			Log.i(logTag, "onResume is going to populate the list");
			new PopulateListTask().execute(false);
		}
		else
			runResume = true;
	}
	
	/**
	 * AsyncTask to populate the listview with a list of rules
	 * 
	 * @author Mehmet Kologlu
	 */
	private class PopulateListTask extends AsyncTask <Boolean,Void,ArrayList<Rule>>{
		@Override
		protected void onPreExecute(){
			listLoaded = false; // The list is not loaded
			Log.i(logTag, "PopulateListTask is started.");
			ruleListView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected ArrayList<Rule> doInBackground(Boolean... getDB) {
			Log.i(logTag, "PopulateListTask background task started with runResume = " + runResume);
			// If runResume == false, then onCreate called this, so get a DBmanager
			if (getDB[0]) { 
				dbManager = new DatabaseManager(getApplicationContext());
			}
			//Get data from DB
			ruleArray = dbManager.getRulesArray();
			Log.i(logTag, "Retreived rule array from the DB.");
			return ruleArray;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Rule> ruleArray) {
			// Populate the listview before completing the task
			populateListView(ruleArray);
			// Hide the progress bar
			progressBar.setVisibility(View.GONE);
			// Show the list
			ruleListView.setVisibility(View.VISIBLE);
			listLoaded = true; //The list is now loaded
			Log.i(logTag, "PopulateListTask complete.");
		}
	}

	/**
	 * populates the ListView with the database data
	 */
	private void populateListView(ArrayList<Rule> ruleArray){
		Log.i(logTag, "populateListView called.");

		if(ruleArray.isEmpty()) //if the loaded rule array is empty
		{
			//Feedback
			Toast.makeText(getApplicationContext(), "You have no saved rules to view", Toast.LENGTH_SHORT).show();
		}

		//pass the adapter with the array to the list view
		mListAdapter = new RuleListViewAdapter(this, ruleArray, getResources());
		ruleListView.setAdapter(mListAdapter);
		Log.i(logTag, "Adapter to rulelistview set");
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
	 * onClick method for Add button from the action bar, launches the AddRule activity.
	 * Will only launch the activity if the list is populated.
	 */
	private void launchAddRuleActivity() {
		if (listLoaded)
			startActivity(new Intent (this, AddRule.class));
		else
			Toast.makeText(getApplicationContext(), "Please wait until the list is loaded to add another rule", Toast.LENGTH_SHORT).show();
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
		Log.i(logTag, "Toggle item of " + mName + " set to " + isChecked + ".");
		Toast.makeText(getApplicationContext(), "Rule " + mName + " turned " + ( (isChecked) ? "on" : "off"),Toast.LENGTH_SHORT).show();

		//Change the status of the rule in the database-
		int wID = dbManager.setRuleStatus(mName, isChecked);

		if (wID != AppWidgetManager.INVALID_APPWIDGET_ID) {
			//Send a broadcast for the widget to update itself
			Intent updateWidgetIntent = new Intent();
			updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{wID} ).setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			this.sendBroadcast(updateWidgetIntent);
			Log.i(logTag, "Broadcasted " + updateWidgetIntent.toString());			
		}
		else
			Log.i(logTag, "Did not broadcast widget update b/c " + mName + " has no widget");

	}

	/**
	 * onLongClick of each row of the listView, called thru the RuleListViewAdapter
	 * 
	 * @param mName the position of the item on the list, 0 indexed
	 */
	public void onLongItemClick(final String ruleName) {
		Log.i(logTag, "Long click detected at " + ruleName);

		new AlertDialog.Builder(this)
		.setTitle(ruleName)
		.setPositiveButton(R.string.dialog_edit, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// TODO open edit activity
				launchEditRule(ruleName);
			}
		})
		.setNegativeButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				deleteRule(ruleName);
			}
		})
		.show();
	}

	/**
	 * 
	 * @param ruleName
	 */
	private void launchEditRule(String ruleName) {
		Intent editIntent = new Intent(this, AddRule.class);
		editIntent.putExtra("ruleName", ruleName);
		startActivity(editIntent);
	}
	
	/**
	 * Queries to delete the rule with the given name.
	 * If there is a widget associated with the rule, prompts the user for its removal and
	 * broadcasts the widget to update itself
	 * 
	 * Then re-populates the listView
	 * 
	 * @param ruleName Name of the rule to be deleted
	 */
	public void deleteRule(String ruleName){

		//Delete the rule from the DB
		int wID = dbManager.deleteRule(ruleName);
		if (wID != AppWidgetManager.INVALID_APPWIDGET_ID) { //if there is a widget associated with the rule
			// Prompt the user to remove it manually
			Toast t = Toast.makeText(getApplicationContext(), "Remember to remove the widget associated with the deleted rule: " + ruleName, Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 50);
			t.show();
			
			// Broadcsat widget Update so the text sets to ERROR
			Intent updateWidgetIntent = new Intent();
			updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{wID} ).setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			this.sendBroadcast(updateWidgetIntent);
			Log.i(logTag, "Broadcasted " + updateWidgetIntent.toString());	
		}
		
		// Feedback
		Toast.makeText(getApplicationContext(), "Deleted rule: " + ruleName, Toast.LENGTH_SHORT).show();			
		
		// Reconstruct view
		new PopulateListTask().execute(false);
	}

	//	/**
	//	 * onClick of each row of the listView, called thru the RuleListViewAdapter
	//	 * 
	//	 * @param mPosition the position of the item on the list, 0 indexed
	//	 */
	//	public void onItemClick(int mPosition)
	//	{
	//		//documentation and feedback
	//		Log.i(logTag, "Item " + mPosition + " clicked.");
	//		Toast.makeText(getApplicationContext(), "Item " + mPosition + " clicked.",Toast.LENGTH_SHORT).show();
	//		// Edit window?
	//		// more info?
	//	}
}