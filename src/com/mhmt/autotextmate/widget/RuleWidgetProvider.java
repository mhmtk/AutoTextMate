package com.mhmt.autotextmate.widget;

import com.mhmt.autotextmate.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

public class RuleWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		for (int i=0; i<appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];
			
			Toast.makeText(context, "updated widget " + i, Toast.LENGTH_SHORT).show();
			
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
			
		}
	}

}
