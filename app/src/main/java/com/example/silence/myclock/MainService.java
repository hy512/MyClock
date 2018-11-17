package com.example.silence.myclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.silence.myclock.entity.WindowLabel;
import com.example.silence.myclock.intent.XIntents;
import com.example.silence.myclock.util.Logger;
import com.example.silence.myclock.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainService extends Service {
    IBinder mBinder;
    WindowManager mWindowManager;

    Handler mHandler;
    ExecutorService mExecutor;
    Map<String, WindowLabel> labels;

    @Override
    public void onCreate() {
        Logger.finest("Service %s", "创建");

        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mHandler = new Handler();
        mExecutor = Executors.newSingleThreadExecutor();
        labels = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.finest("Service %s", "命令");

        Set<String> cates = null;
        if (intent != null) cates = intent.getCategories();
        if (cates != null) {
            if (cates.contains(XIntents.X_STOP)) {
                stopSelf();
                return  START_NOT_STICKY;
            }
        } else {
            run();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        Logger.finest("Service %s", "终止");

        for (Map.Entry<String, WindowLabel> entry : labels.entrySet()) {
            mWindowManager.removeView(entry.getValue().getContainerView());
        }
    }

    void run() {
        if (mWindowManager != null) return;

        mExecutor.execute(() -> {
            Util util = new Util();
            while (true) {
                try {
//                    mHandler.post(() -> mText.setText(util.timeStr()));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
    }
}
