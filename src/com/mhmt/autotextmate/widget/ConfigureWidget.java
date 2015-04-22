package com.mhmt.autotextmate.widget;

import com.mhmt.autotextmate.R;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

public class ConfigureWidget extends Activity {

	private ConfigureWidget context;
	int widgetID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure_widget);
		
		//set the initial result to canceled in case the user hits the back button
		setResult(RESULT_CANCELED);
		
		//set the context
		context = this;
		
		//Get the intent that launched the activity
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		//Get App Widget ID from the intent
		if (extras != null) {
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		//CONFIGURE
		
		//get an instance of the app widget manager
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		
		//update the app widget
		
		//create return intent, finish the activity
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
		setResult(RESULT_OK, resultValue);
		finish();
	}
}