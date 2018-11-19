package com.example.silence.myclock.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
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
    public static void warning(String msg){
        l.warning(msg);
    }
    public static void warning(String msg, Exception e){
        StringWriter writer = new StringWriter();
        PrintWriter print = new PrintWriter(writer);
        e.printStackTrace(print);
        l.log(Level.WARNING, "%s\r%s.", new Object[]{msg, writer.toString()});
        print.close();
    }
    public static void info (String msg) {
        l.info(msg);
    }
}
