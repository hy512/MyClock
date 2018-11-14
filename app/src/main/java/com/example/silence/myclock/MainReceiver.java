package com.example.silence.myclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MainReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent sendIntent = new Intent(context, MainService.class);
        context.startService(sendIntent);
    }
}
