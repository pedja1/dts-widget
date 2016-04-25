package com.af.dtswidget;

import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;

public class DTSWidgetProvider extends AppWidgetProvider
{
    private static Map<Integer, Click> clicks = new HashMap<>();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++)
        {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, ClickCounterReceiver.class);
            intent.putExtra("wid", appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.root, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public static class ClickCounterReceiver extends BroadcastReceiver
    {
        public ClickCounterReceiver()
        {
            System.out.println("constructor");
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            int key = intent.getIntExtra("wid", -1);
            if (key <= 0)
                return;
            Click click;
            if (!clicks.containsKey(key))
            {
                clicks.put(key, click = new Click(System.currentTimeMillis(), 1));
            }
            else
            {
                click = clicks.get(key);
                if (System.currentTimeMillis() - click.lastClickTime > 1000)
                    click.clickCount = 1;
                else
                    click.clickCount++;
                click.lastClickTime = System.currentTimeMillis();
            }
            if(click.clickCount >= 2)
            {
                click.clickCount = 0;
                DevicePolicyManager mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                mDPM.lockNow();
            }
        }
    }

    public static class Click
    {
        long lastClickTime;
        int clickCount;

        Click(long lastClickTime, int clickCount)
        {
            this.lastClickTime = lastClickTime;
            this.clickCount = clickCount;
        }
    }
}