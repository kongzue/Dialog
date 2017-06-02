package com.kongzue.dialog.util;

import com.kongzue.dialog.DialogPlugin;

/**
 * Created by ZhangChao on 2017/6/1.
 */

public class Log {

    public static void println(Object msg){
        if(DialogPlugin.isDebug == false){
            return;
        }
        android.util.Log.d("kongzue.dialog>>>",msg.toString());
    }

}