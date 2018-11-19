package com.example.silence.myclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
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

import java.io.FileDescriptor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainService extends Service {
    LabIBinder mBinder ;
    WindowManager mWindowManager;
    boolean isRun;

    Handler mHandler;
    ScheduledExecutorService mExecutor;
    Map<String, WindowLabel> labels;

    @Override
    public void onCreate() {
        Logger.finest("Service %s", "创建");

        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mHandler = new Handler();
        mExecutor = Executors.newScheduledThreadPool(6);
        labels = new HashMap<>();
        mBinder = new LabIBinder(mWindowManager, labels);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.finest("Service %s", "命令");

        Set<String> cates = null;
        if (intent != null) cates = intent.getCategories();
        if (cates != null) {

            if (cates.contains(XIntents.X_STOP)) {
                stopSelf();
                return START_NOT_STICKY;
            }
            if (cates.contains(XIntents.X_ADD)) {
                if (intent.hasExtra("label") && intent.hasExtra("identity")) {
                    Serializable label = intent.getSerializableExtra("label");
                    if (label instanceof WindowLabel) {
                        WindowLabel lab = (WindowLabel) label;
                        lab.create(mWindowManager);
                        labels.put(intent.getStringExtra("identity"), lab);
                    } else {
                        Logger.info("X_ADD 无效的 label.");
                    }
                } else {
                    Logger.info("X_ADD 不完整的信息.");
                }
            }
            if (cates.contains(XIntents.X_DELETE)) {
                if (intent.hasExtra("identity")) {
                    labels.remove(intent.getStringExtra("identity"));
                }
            }
            if (cates.contains(XIntents.X_START) && !isRun) {
                isRun = true;
                run();
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        Logger.finest("Service %s", "终止");
        mExecutor.shutdownNow();
        for (Map.Entry<String, WindowLabel> entry : labels.entrySet()) {
            mWindowManager.removeView(entry.getValue().getContainerView());
        }
    }

    void run() {
        for (Map.Entry<String, WindowLabel> entry : labels.entrySet()) {
            WindowLabel label = entry.getValue();
            mExecutor.scheduleWithFixedDelay(() -> {
                label.update(mWindowManager, label.getLayoutParams());
            }, 0, label.getUpdateDelay(), TimeUnit.MILLISECONDS);
        }
    }




}
