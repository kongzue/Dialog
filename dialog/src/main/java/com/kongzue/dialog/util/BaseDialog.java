package com.kongzue.dialog.util;

import android.util.Log;

import com.kongzue.dialog.listener.DialogLifeCycleListener;

import static com.kongzue.dialog.v2.DialogSettings.DEBUGMODE;

public abstract class BaseDialog {
    
    public boolean isDialogShown = false;
    
    private static DialogLifeCycleListener dialogLifeCycleListener;
    
    public void log(Object o) {
        if (DEBUGMODE) Log.i("DialogSDK >>>", o.toString());
    }
    
    public static void setDialogLifeCycleListener(DialogLifeCycleListener listener) {
        dialogLifeCycleListener = listener;
    }
    
    public static DialogLifeCycleListener getDialogLifeCycleListener() {
        return dialogLifeCycleListener;
    }
    
    public static void cleanDialogLifeCycleListener() {
        dialogLifeCycleListener = null;
    }
    
    public abstract void showDialog();
    
    public abstract void doDismiss();
}
