package com.mhmt.autotextmate.widget;

import java.util.ArrayList;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;

/**
 * 
 * @author Mehmet Kologlu
 * @version April 26, 2015
 */
public class ConfigureWidget extends Activity {

	private ConfigureWidget context;
	int widgetID;
	private ListView ruleListView;
	private DatabaseManager dbManager; 
	private ArrayList<Rule> ruleArray;

	public static String WIDGET_ONCLICK_ACTION = "WidgetOnClickAction";

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

		//OnClick of the rule list items
		ruleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,final int position, long id) {
				
				Log.i("Configure", "pos: " + position + ", id: " + id);

				final Rule selectedRule = ruleArray.get(position);

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

				//get an instance of the app widget manager and remoteviews
				final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
				final RemoteViews rm = new RemoteViews(context.getPackageName(), R.layout.layout_widget);

				//TODO delete this, it's useless 
				//				Button widgetButton = (Button) findViewById(R.id.widget_button);

				//set the text and background of the button
				rm.setImageViewResource(R.id.widget_backgroundImage, (selectedRule.getStatus()==1 ? R.drawable.widget_button_green : R.drawable.widget_button_red));
				rm.setTextViewText(R.id.widget_button, selectedRule.getName());

				//Create intent and pending intent for the onClick action
				Intent onClickIntent = new Intent(context, RuleWidgetProvider.class);
				PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onClickIntent, 0);

				
				
				
				
				rm.setOnClickPendingIntent(R.id.widget_button, onClickPendingIntent);


				//				//TODO change to use pending intent thru remoteviews
				//				widgetButton.setOnClickListener(new View.OnClickListener() {
				//					public void onClick(View v) {
				//						
				//						//TODO et the rules current status from db
				//						
				//						//TODO Change the background image
				//						
				//						//TODO call toggleRule
				//						
				//					}
				//				});

				
				
				
				
				
				
				
				//update the app widget
				widgetManager.updateAppWidget(widgetID, rm);

				//create return intent, finish the activity
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
				setResult(RESULT_OK, resultValue);
				finish();
			}}); //end of new onClick and it's onClick method
	} //end of onCreate
}//end of class