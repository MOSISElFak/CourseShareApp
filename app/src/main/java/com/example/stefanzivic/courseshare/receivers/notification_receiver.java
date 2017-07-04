package com.example.stefanzivic.courseshare.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Stefan Zivic on 7/4/2017.
 */

public class notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("On rec","On rec");
    }
}
