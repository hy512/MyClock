package com.example.silence.myclock;

import android.app.Application;
import android.os.Handler;

import com.example.silence.myclock.data.Observer;
import com.example.silence.myclock.util.Logger;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.finest("程序运行");
    }

}
