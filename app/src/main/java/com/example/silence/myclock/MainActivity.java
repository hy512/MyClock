package com.example.silence.myclock;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.silence.myclock.data.Observer;
import com.example.silence.myclock.entity.WindowLabel;
import com.example.silence.myclock.intent.XIntents;
import com.example.silence.myclock.util.LabelsFactory;
import com.example.silence.myclock.util.Logger;
import com.example.silence.myclock.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    LabIBinder mIBinder;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getContentResolver().registerContentObserver(
                MainProvider.NOTIFY_URI,
                false,
                new Observer(new Handler(), this));

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());


        Intent intent = new Intent(XIntents.X_CONTROL);
        intent.addCategory(XIntents.X_START);
        intent.setPackage(getPackageName());
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Logger.finest("bindConnected, %s.", name);
                MainActivity.this.mIBinder = (LabIBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Logger.finest("bindDisconnected, %s.", name);
            }
        }, BIND_AUTO_CREATE);
    }

    public void onCreate(View v) {
        if (mIBinder == null) {
            return;
        }
        mIBinder.put("datetime", LabelsFactory.init(getApplicationContext()).dateTime());
//        ContentResolver resolver = MainActivity.this.getContentResolver();
//        ContentValues value = new ContentValues();
//        resolver.insert(MainProvider.NOTIFY_URI, value);
//        Intent intent = new Intent(XIntents.X_CONTROL);
//        intent.addCategory(XIntents.X_START);
//        intent.setPackage(getPackageName());
//        intent.putExtra("identity", "datetime");
//        intent.putExtra("label",LabelsFactory.init(getApplicationContext()).dateTime());
//        startService(intent);

    }

    public void onStart(View v) {
        Intent intent = new Intent();
        intent.setAction(XIntents.X_CONTROL);
        intent.addCategory(XIntents.X_START);
        intent.setPackage(getPackageName());
        MainActivity.this.startService(intent);
    }

    public void onStop(View v) {
        Intent intent = new Intent();
        intent.setAction(XIntents.X_CONTROL);
        intent.addCategory(XIntents.X_STOP);
        intent.setPackage(getPackageName());
        MainActivity.this.startService(intent);
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
