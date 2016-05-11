package com.af.dtswidget;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pedja on 11.5.16. 10.29.
 * This class is part of the dts-widget
 * Copyright © 2016 ${OWNER}
 */
public class WidgetConfigureActivity extends Activity
{
    private static final int REQUEST_CODE_ENABLE_ADMIN = 127;
    private int mAppWidgetId;

    private ComponentName mDeviceAdmin;
    private DevicePolicyManager mDPM;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
        {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        tvStatus = (TextView) findViewById(R.id.tvStatus);

        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdmin = new ComponentName(this, DTSDeviceAdminReceiver.class);
        setStatus();
    }

    private void setStatus()
    {
        if(mDPM.isAdminActive(mDeviceAdmin))
        {
            tvStatus.setTextColor(getResources().getColor(R.color.admin_enabled));
            tvStatus.setText(R.string.enabled);
        }
        else
        {
            tvStatus.setTextColor(getResources().getColor(R.color.admin_disabled));
            tvStatus.setText(R.string.not_enabled);
        }
    }

    public void enableDeviceAdmin(View view)
    {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
        //intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, mActivity.getString(R.string.add_admin_extra_app_text));
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_ENABLE_ADMIN:
                setStatus();
                if(resultCode == RESULT_OK)
                {
                    updateWidgets();
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateWidgets()
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, DTSWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent intent = new Intent(this, DTSWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        sendBroadcast(intent);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
    }

    @Override
    public void onBackPressed()
    {
        updateWidgets();
        super.onBackPressed();
    }
}
