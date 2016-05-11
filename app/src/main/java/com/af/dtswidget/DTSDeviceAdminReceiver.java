package com.af.dtswidget;

import android.app.admin.DeviceAdminReceiver;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by pedja on 25.4.16. 15.08.
 * This class is part of the DTSWidget
 * Copyright © 2016 ${OWNER}
 */
public class DTSDeviceAdminReceiver extends DeviceAdminReceiver
{
    @Override
    public void onEnabled(Context context, Intent intent)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, DTSWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent broadcastIntent = new Intent(context, DTSWidgetProvider.class);
        broadcastIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        broadcastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(broadcastIntent);
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, DTSWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent broadcastIntent = new Intent(context, DTSWidgetProvider.class);
        broadcastIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        broadcastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(broadcastIntent);
        super.onDisabled(context, intent);
    }
}
