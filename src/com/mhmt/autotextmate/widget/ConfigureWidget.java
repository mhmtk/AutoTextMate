package com.mhmt.autotextmate.widget;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;

public class ConfigureWidget extends Activity {

	private ConfigureWidget context;
	int widgetID;
	private ListView ruleListView;
	private DatabaseManager dbManager; 
	private ArrayList<Rule> ruleArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure_widget);

		//set the initial result to canceled in case the user hits the back button
		setResult(RESULT_CANCELED);

		//instantiate fields
		dbManager = new DatabaseManager(getApplicationContext());
		ruleArray = dbManager.getRulesArray();
		ruleListView = (ListView) findViewById(R.id.configure_list);

		//set the context
		context = this;

		//populate the list
		ruleListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,
				ruleArray));

		//TODO add onclick to list items
		ruleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,final int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				//DO EVERYTHING
				Log.i("Configure", "pos: " + position + "id: " + id);

				//Get the intent that launched the activity
				Intent intent = getIntent();
				Bundle extras = intent.getExtras();

				//Get App Widget ID from the intent
				if (extras != null) {
					widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
							AppWidgetManager.INVALID_APPWIDGET_ID);
				}

				//get an instance of the app widget manager and remoteviews
				final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
				final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget);

				Button widgetButton = (Button) findViewById(R.id.widget_button);
				
				widgetButton.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		            	 dbManager.toggleRule(ruleArray.get(position).getName(), true); //TODO ONLY SETS TRUE ATM
		            	 
		            	 //TODO change the color of the status image
		             }
		         });

				//TODO configure

				//update the app widget
				widgetManager.updateAppWidget(widgetID, views);

				//create return intent, finish the activity
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
				setResult(RESULT_OK, resultValue);
				finish();
			}});
	}
}