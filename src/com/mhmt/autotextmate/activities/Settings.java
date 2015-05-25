package com.mhmt.autotextmate.activities;

import com.mhmt.autotextmate.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 25, 2015
 * 
 */
public class Settings extends Activity {

	private String[] spinnerArray;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sPrefEditor;
	private String logTag = "Settings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Instantiate view fields
		Spinner muteSpinner = (Spinner) findViewById(R.id.settings_spinner_mute);

		// Get shared preferences, and it's editor
		sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key),Context.MODE_PRIVATE);
		sPrefEditor = sharedPref.edit();

		// Instantiate data fields
		spinnerArray = getResources().getStringArray(R.array.settings_mute_spinner_array);

		// Create an array adapter for the usage with the mute spinner
		ArrayAdapter<CharSequence> muteAdapter = ArrayAdapter.createFromResource(this,
				R.array.settings_mute_spinner_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		muteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		muteSpinner.setAdapter(muteAdapter);

		// Set onItemSelectedListener of the mute spinner
		muteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(logTag, "Mute spinner - onItemSelected called with position: " + position);
				if (position !=0) {
					sPrefEditor.putInt(getString(R.string.settings_mute_key), Integer.valueOf(spinnerArray[position]) * 1000); // convert to seconds
					if (!sPrefEditor.commit()) {
						Log.e(logTag, "Error while committing mute delay into shared preferences");
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.i(logTag, "Mute spinner - onNothingSelected called");
			}
		});
	}
}