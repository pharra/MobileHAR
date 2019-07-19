package com.example.mobilehar.log;

import android.util.Log;

public class Logger {

    public static boolean debug = true;

    public static void i(String tag, String msg) {
        if (debug) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (debug) {
            Log.d(tag, msg);
        }
    }
}
