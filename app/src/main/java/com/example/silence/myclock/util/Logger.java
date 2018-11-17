package com.example.silence.myclock.util;

import java.util.logging.Level;

public class Logger {
    static java.util.logging.Logger l;

    static {
        l = java.util.logging.Logger.getLogger("procedure");
        l.setLevel(Level.FINEST);
        l.addHandler(new LogHandler());
    }

    public static void finest(String msg) {
        l.finest(msg);
    }
    public  static void finest( String format, Object... msg) {
        l.log(Level.FINEST, format , msg );
    }
}
