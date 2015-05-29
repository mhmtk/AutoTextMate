package com.mhmt.autoreplymate.widget;

import com.mhmt.autoreplymate.database.DatabaseManager;
import com.mhmt.autoreplymate.dataobjects.Rule;
import com.mhmt.autoreplymate.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 
 * @author Mehmet Kologlu
 * @version May 29, 2015
 */
public class RuleWidgetProvider extends AppWidgetProvider {

	private static String WIDGET_ONCLICK_ACTION = "AUTO_REPLY_MATE.WIGDET_ONCLICK_ACTION";
	private String logTag = "WidgetProvider";
	private DatabaseManager dbManager; 

	@Override
	public void onEnabled(Context context){
		super.onEnabled(context);
		Log.i(logTag, "onEnabled was called");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i(logTag, "onUpdate called");
		for (int i=0; i<appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];

			Log.i(logTag, "Widget onUpdate called for " + appWidgetId);

			//Get a dbManager
			dbManager = new DatabaseManager(context);
			Rule rule = dbManager.getRule(appWidgetId);
			
			RemoteViews rm = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
			
			if (rule != null) { //if there's a rule associated with the widgetID
				//Update the background image to match the status of the rule in the DB
				rm.setImageViewResource(R.id.widget_backgroundImage, 
						((rule.getStatus()==1) ? R.drawable.widget_button_green : R.drawable.widget_button_red));
				//Update the widget text (useful after relaunch)
				rm.setTextViewText(R.id.widget_button,
						rule.getName());
				
			}
			else {
				Log.w(logTag, "No rule associated with wID " + appWidgetId);
				rm.setTextViewText(R.id.widget_button, "ERROR");
			}
			appWidgetManager.updateAppWidget(appWidgetId, rm);				
			Log.i(logTag, "Updated " + appWidgetId);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(logTag, "Widget received " + intent);
		super.onReceive(context, intent);

		if (WIDGET_ONCLICK_ACTION.equals(intent.getAction())) {
			Log.i(logTag, "The broadcast matches the widget onClick action");

			//Make DB manager
			dbManager = new DatabaseManager(context);

			//Get the rule name and widget ID from the intent
			String ruleName = intent.getStringExtra("rule_name");
			int widgetID = intent.getIntExtra("widget_ID", AppWidgetManager.INVALID_APPWIDGET_ID);
			
			//Change the status of the rule in the database
			dbManager.toggleRuleStatus(ruleName);
			
			//documentation and feedback
			Log.i(logTag, "Rule: " + ruleName + ", wID: " + widgetID + " changed");
			
			//Call for a widget update thru the onUpdate method (faster than broadcasting)
			onUpdate(context, AppWidgetManager.getInstance(context), new int[]{widgetID});			
		}
	}
	
	/**
	 * Calls its super method, then also removes the deleted widget's ID from the DB
	 */
	@Override
	public void onDeleted(Context context, final int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		dbManager = new DatabaseManager(context);

		Log.i(logTag, "Deleting widget(s) " + appWidgetIds.toString());

		// Delete widget IDs from DB
		new Runnable() {
			@Override
			public void run() {
				dbManager.resetWidgetIDs(appWidgetIds);
				Log.i(logTag, "Widgets deleted from DB.");
			}
		}.run();

	}
}