package com.mhmt.autotextmate.widget;

import com.mhmt.autotextmate.R;
import com.mhmt.autotextmate.activities.Main;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class RuleWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int i=0; i<appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, Main.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

			// Get the layout for the App Widget and attach an on-click listener to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
			views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

			//perform an update on teh current app widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

}
