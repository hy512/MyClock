package com.example.silence.myclock.util;

import java.lang.annotation.Native;

public class Util {
    static
    {
        System.loadLibrary("datetime");
    }
    public native String hello();
    public native String timeStr();
    public native String timeStr(String format);
}
