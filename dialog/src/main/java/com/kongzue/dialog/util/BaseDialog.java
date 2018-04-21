package com.kongzue.dialog.util;

import android.util.Log;

import com.kongzue.dialog.listener.DialogLifeCycleListener;

public class BaseDialog {

    public DialogLifeCycleListener dialogLifeCycleListener;

    public void log(Object o) {
        Log.i("DialogSDK >>>", o.toString());
    }

    public void setDialogLifeCycleListener(DialogLifeCycleListener dialogLifeCycleListener) {
        this.dialogLifeCycleListener = dialogLifeCycleListener;
    }

}
