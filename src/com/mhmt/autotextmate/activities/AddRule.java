package com.mhmt.autotextmate.activities;

import com.mhmt.autotextmate.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 7, 2015
 * 
 */
public class AddRule extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_rule);
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
		
		//return to homepage
		
		//toast to give feedback of success
	}
	
	/**
	 * Called when the cancel button on the AddRule Activity is clicked
	 * @param view
	 */
	public void cancelButtonClicked(View view) {
		//cancel
	}
}
