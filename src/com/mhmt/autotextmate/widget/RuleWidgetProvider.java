package com.mhmt.autotextmate.widget;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.database.DatabaseManager;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 
 * @author Mehmet Kologlu
 * @version April 27, 2015
 */
public class RuleWidgetProvider extends AppWidgetProvider {

	private static String WIDGET_ONCLICK_ACTION = "AUTO_TEXT_MATE.WIGDET_ONCLICK_ACTION";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i("Widget", "onUpdate called");
		for (int i=0; i<appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];
			
			Log.i("Widget", "Updating " + appWidgetId);

			// Create the intent to launch at button onClick 
//			Intent onClickIntent = new Intent(WIDGET_ONCLICK_ACTION);
//			PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Set the widget button to launch the onClickPendingIntent 
//			RemoteViews rm = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
//			rm.setOnClickPendingIntent(R.id.widget_button, onClickPendingIntent);

			//perform an update on the current app widget
//			appWidgetManager.updateAppWidget(appWidgetId, rm);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		Log.i("Widget", "Widget received a broadcast");

		if (WIDGET_ONCLICK_ACTION.equals(intent.getAction())) {
			Log.i("Widget", "The broadcast matches the widget onClick action");

			//Make DB manager
			DatabaseManager dbManager = new DatabaseManager(context);

			//get the name of the rule the widget is on
			String ruleName = intent.getStringExtra("rule_name");
			int widgetID = intent.getIntExtra("widget_ID", AppWidgetManager.INVALID_APPWIDGET_ID);

			Boolean statusToSet = true; //TODO

			//Change the status of the rule in the database
			dbManager.toggleRule(ruleName, statusToSet);

			//change the background of the widget 
			RemoteViews rm = new RemoteViews(context.getPackageName(),R.layout.layout_widget);
			rm.setImageViewResource(R.id.widget_backgroundImage, 
					(statusToSet ? R.drawable.widget_button_green : R.drawable.widget_button_red));

			AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
			widgetManager.updateAppWidget(widgetID, rm);

			//documentation and feedback
			Log.i("Widget", "Rule: " + ruleName + ", wID: " + widgetID + " changed");
		}
	}
}