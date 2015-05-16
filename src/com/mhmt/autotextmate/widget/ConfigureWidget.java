package com.mhmt.autotextmate.widget;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

/**
 * 
 * @author Mehmet Kologlu
 * @version May 16, 2015
 */
public class ConfigureWidget extends ListActivity {

	private ConfigureWidget context;
	int widgetID;
//	private ListView ruleListView;
	private DatabaseManager dbManager;
	private ArrayList<Rule> ruleArray;
	private String logTag = "Configure";

	private static String WIDGET_ONCLICK_ACTION = "AUTO_TEXT_MATE.WIGDET_ONCLICK_ACTION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure_widget);

		//set the initial result to canceled in case the user hits the back button
		setResult(RESULT_CANCELED);

		//instantiate fields
		dbManager = new DatabaseManager(getApplicationContext());
		ruleArray = dbManager.getRulesArray();

		//set the context
		context = this;

		//populate the list
		setListAdapter(new ArrayAdapter<Rule>(this, android.R.layout.simple_list_item_1, ruleArray));
	} //end of onCreate
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {	    
		//Store the selected Rule's name
		final Rule selectedRule = ruleArray.get(position);

		//Log
		Log.i(logTag, "Widget of rule " + selectedRule.getName() + " with ID "+ id + " clicked.");
		
		//Get the intent that launched the activity
		Intent launchedIntent = getIntent();
		Bundle extras = launchedIntent.getExtras();

		//Get App Widget ID from the intent
		if (extras != null) {
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If they gave us an intent without the widget id, just bail.
		if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

		//Add the widget ID to the database
		dbManager.setWidgetID(selectedRule.getName(), widgetID);
		
		//get an instance of the app widget manager and remoteviews
		final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
		final RemoteViews rm = new RemoteViews(context.getPackageName(), R.layout.layout_widget);

		//set the text and background of the button
		rm.setImageViewResource(R.id.widget_backgroundImage, (selectedRule.getStatus()==1 ? R.drawable.widget_button_green : R.drawable.widget_button_red));
		rm.setTextViewText(R.id.widget_button, selectedRule.getName());

		// Create the intent (add the rule name as an extra) to launch at button onClick 
		Intent onClickIntent = new Intent(WIDGET_ONCLICK_ACTION);
		onClickIntent.putExtra("rule_name", selectedRule.getName());
		onClickIntent.putExtra("widget_ID", widgetID);
		PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, widgetID, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Set the widget button to launch the onClickPendingIntent 
		rm.setOnClickPendingIntent(R.id.widget_button, onClickPendingIntent);
		
		//update the app widget
		widgetManager.updateAppWidget(widgetID, rm);

		//create return intent, finish the activity
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		setResult(RESULT_OK, resultValue);
		finish();
	} //end of onListItemClick
	
}//end of class