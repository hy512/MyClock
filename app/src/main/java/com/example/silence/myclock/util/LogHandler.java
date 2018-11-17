package com.example.silence.myclock.util;

import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogHandler  extends Handler{

    {
        setLevel(Level.FINEST);
    }
    @Override
    public void publish(LogRecord record) {
        Log.d(record.getLoggerName(), String.format(record.getMessage(), record.getParameters()));
    }

    @Override
    public void flush() { }

    @Override
    public void close() throws SecurityException { }
}
