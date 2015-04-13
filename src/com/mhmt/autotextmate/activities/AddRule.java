package com.mhmt.autotextmate.activities;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 7, 2015
 * 
 */
public class AddRule extends ActionBarActivity {

	private EditText editTextName;
	private EditText editTextDescription;
	private EditText editTextText;
	private CheckBox checkBoxContacts;
	private RadioGroup radioReplyTo;

	DatabaseManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_rule);

		assignViewVariables();
	}

	/**
	 * Loads the views on the activity
	 */
	private void assignViewVariables() {
		editTextName = (EditText) findViewById(R.id.editText_name);
		editTextDescription = (EditText) findViewById(R.id.editText_description);
		editTextText = (EditText) findViewById(R.id.editText_text);
		checkBoxContacts = (CheckBox) findViewById(R.id.checkBox_contactsOnly);
		radioReplyTo = (RadioGroup) findViewById(R.id.radio_replyTo);

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

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Called when the add button on the AddRule Activity is clicked
	 * @param view
	 */
	public void addButtonClicked(View view) {
		//add Rule to DB
		dbManager = new DatabaseManager(getApplicationContext());
		dbManager.addRule(new Rule(editTextName.getText().toString(),
				editTextDescription.getText().toString(),
				editTextText.getText().toString(),
				checkBoxContacts.isChecked() ));
		Log.d("Add Rule", "Called dbmanager addrule method");
		//return to homepage


		//toast to give feedback of success
		Toast.makeText(getApplicationContext(), "Rule added", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Called when the cancel button on the AddRule Activity is clicked
	 * @param view
	 */
	public void cancelButtonClicked(View view) {
		//cancel
		//return to homepage
	}
}
