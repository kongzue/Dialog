package com.kongzue.dialog.util;

import android.util.Log;

import com.kongzue.dialog.listener.DialogLifeCycleListener;

import static com.kongzue.dialog.v2.DialogSettings.DEBUGMODE;

public abstract class BaseDialog {

    public boolean isDialogShown = false;

    public DialogLifeCycleListener dialogLifeCycleListener;

    public void log(Object o) {
        if (DEBUGMODE)Log.i("DialogSDK >>>", o.toString());
    }

    public void setDialogLifeCycleListener(DialogLifeCycleListener dialogLifeCycleListener) {
        this.dialogLifeCycleListener = dialogLifeCycleListener;
    }

    public abstract void showDialog();
    
    public abstract void doDismiss();
}
