package com.kongzue.dialog;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by ZhangChao on 2017/6/1.
 */

public class DialogPlugin extends Application {

    private static Context context;
    private static Handler handler;
    public static boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

}
