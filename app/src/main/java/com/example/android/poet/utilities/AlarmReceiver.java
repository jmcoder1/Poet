package com.example.android.poet.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.android.poet.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        Toast.makeText(context, "I'm running", Toast.LENGTH_LONG).show();

        NotificationUtils.remindUserBecauseCharging(context);

        Intent service1 = new Intent(context, NotificationUtils.class);
        service1.setData((Uri.parse("custom://"+ System.currentTimeMillis())));
        context.startService(service1);

    }
}