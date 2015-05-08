package com.mhmt.autotextmate.activities;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 8, 2015
 * 
 */
public class AddEditRule extends ActionBarActivity {

	private String logTag = "AddEditRule";

	private EditText editTextName;
	private EditText editTextDescription;
	private EditText editTextText;
	private CheckBox checkBoxContacts;
	private RadioGroup radioReplyTo;
	private ProgressBar progressBar;
	private LinearLayout fields;

	private DatabaseManager dbManager;

	private boolean edit;
	private String oldRuleName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit_rule);
		assignViewFields();

		// If this activity is launched with an editing intent, start the asynctask to populate the fields
		Intent intent = getIntent();
		if (intent.hasExtra("ruleName")) {
			Log.i(logTag, "AddEdit launched with intent that contains rulename extra: \n" + intent.toString());
			setTitle("Edit Rule");
			edit = true;
			oldRuleName = intent.getStringExtra("ruleName");
			new PopulateFieldsTask().execute(new String[] {oldRuleName});
		}

		// For up navigation thru the action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Loads the views on the activity
	 */
	private void assignViewFields() {
		editTextName = (EditText) findViewById(R.id.editText_name);
		editTextDescription = (EditText) findViewById(R.id.editText_description);
		editTextText = (EditText) findViewById(R.id.editText_text);
		checkBoxContacts = (CheckBox) findViewById(R.id.checkBox_contactsOnly);
		radioReplyTo = (RadioGroup) findViewById(R.id.radio_replyTo);
		progressBar = (ProgressBar) findViewById(R.id.addedit_progress_bar);
		fields = (LinearLayout) findViewById(R.id.addRule_fields);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_rule, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.add_action_save:
			saveButtonClicked();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Called when the save button on the AddEditRule Activity is clicked
	 * 
	 */
	public void saveButtonClicked() {
		Log.i(logTag, "Save button clicked with edit as " + edit);

		// TODO runnable
		
		String newRuleName = editTextName.getText().toString().trim(); //get the new rule name
		String ruleText = editTextText.getText().toString().trim(); //get the text

		// Validate input
		if (newRuleName == ""){
			Toast.makeText(getApplicationContext(), "Name field cannot be empty", Toast.LENGTH_SHORT).show();
		}
		else if(ruleText == ""){
			Toast.makeText(getApplicationContext(), "Text field cannot be empty", Toast.LENGTH_SHORT).show();
		}
		else { //Input is valid
			dbManager = new DatabaseManager(getApplicationContext()); //get a DB

			if (edit) { //Edit functionality
				try {

					// If the name of the rule is changed request the wID from the DB.
					// Then call for the widgets update if there's one
					if (oldRuleName != newRuleName) { //changed
						int wID = dbManager.editRule(true, oldRuleName, new Rule(newRuleName,
								editTextDescription.getText().toString().trim(),
								ruleText,
								checkBoxContacts.isChecked()));

						// If the rule has a widget, call to update it
						if (wID != AppWidgetManager.INVALID_APPWIDGET_ID) {
							Intent updateWidgetIntent = new Intent();
							updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{wID} ).setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
							this.sendBroadcast(updateWidgetIntent);
							Log.i(logTag, "Broadcasted " + updateWidgetIntent.toString());	
							Toast.makeText(getApplicationContext(), "Rule edited, its widget will automatically update.", Toast.LENGTH_SHORT).show();
						}
					}
					else { //if the name hasnt changed, dont request a wID
						dbManager.editRule(false, oldRuleName, new Rule(newRuleName,
								editTextDescription.getText().toString(),
								editTextText.getText().toString(),
								checkBoxContacts.isChecked()));
						Toast.makeText(getApplicationContext(), "Rule edited.", Toast.LENGTH_SHORT).show();
					}
					Log.i(logTag, "Rule edited");
					//return to homepage
					super.onBackPressed();
				}
				catch(SQLiteConstraintException ex){ //catch constraint exceptions, and give error feedback to user
					Toast.makeText(getApplicationContext(), "ERROR: A rule with that name already exists.", Toast.LENGTH_SHORT).show();
					Log.i(logTag, "Rule not added, cought " + ex);
				}
			}
			else { //Add functionality
				//add Rule to DB
				try {
					dbManager.addRule(new Rule(editTextName.getText().toString(),
							editTextDescription.getText().toString(),
							editTextText.getText().toString(),
							checkBoxContacts.isChecked()));
					Log.i(logTag, "Rule added successfully");
					Toast.makeText(getApplicationContext(), "Rule added", Toast.LENGTH_SHORT).show();
					//return to homepage
					super.onBackPressed();
				}
				catch(SQLiteConstraintException ex){ //catch constraint exceptions, and give error feedback to user
					Toast.makeText(getApplicationContext(), "ERROR: Rule NOT added, name must be unique!", Toast.LENGTH_SHORT).show();
					Log.i(logTag, "Rule not added, cought " + ex);
				}
			}
		}
	}

	/**
	 * AsyncTask to populate the fields if the activity was launched by an edit intent
	 * 
	 * @author Mehmet Kologlu
	 */
	private class PopulateFieldsTask extends AsyncTask <String,Void,Rule>{
		@Override
		protected void onPreExecute(){
			fields.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Rule doInBackground(String... ruleName) {
			// Return the rule matching the rule name from the DB
			dbManager = new DatabaseManager(getApplicationContext());
			Rule rule = dbManager.getRule(ruleName[0]);
			return rule;
		}

		@Override
		protected void onPostExecute(Rule rule) {
			// Populate the views
			editTextName.setText(rule.getName());
			editTextDescription.setText(rule.getDescription());
			editTextText.setText(rule.getText());
			checkBoxContacts.setChecked( (rule.getOnlyContacts() == 1) ? true : false);

			// Progress bar disappears
			progressBar.setVisibility(View.GONE);

			// The fields are shown
			fields.setVisibility(View.VISIBLE);
		}
	}
}