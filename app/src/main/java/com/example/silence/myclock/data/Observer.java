package com.example.silence.myclock.data;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.example.silence.myclock.intent.XIntents;
import com.example.silence.myclock.util.Logger;

public class Observer extends ContentObserver {
Context context;
Handler handler;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public Observer(Handler handler, Context context) {
        super(handler);
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        Logger.finest("Observer onChange");
        Intent intent = new Intent(XIntents.X_CONTROL);
        intent.addCategory(XIntents.X_RELOAD);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
    }

}
