package com.example.android.poet.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(context.toString(), "onReceive: AlarmReceiver 'onReceiver'");
        NotificationUtils.remindUserBecauseCharging(context);

        Intent service = new Intent(context, NotificationUtils.class);
        service.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        context.startService(service);
    }
}