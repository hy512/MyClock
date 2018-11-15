package com.example.silence.myclock;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.silence.myclock.intent.XIntents;
import com.example.silence.myclock.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    public void onStart(View v) {
        Intent intent = new Intent();
        intent.setAction(XIntents.X_CONTROL);
        intent.addCategory(XIntents.X_START);
        intent.setPackage(getPackageName());
        MainActivity.this.startService(intent);
    }
    public void onStop (View v) {
        Intent intent = new Intent();
        intent.setAction(XIntents.X_CONTROL);
        intent.addCategory(XIntents.X_STOP);
        intent.setPackage(getPackageName());
        MainActivity.this. startService(intent);
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
