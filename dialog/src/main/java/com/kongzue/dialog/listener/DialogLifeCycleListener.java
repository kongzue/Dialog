package com.kongzue.dialog.listener;

import android.support.v7.app.AlertDialog;

public interface DialogLifeCycleListener {

    void onCreate(AlertDialog alertDialog);

    void onShow(AlertDialog alertDialog);

    void onDismiss();

}
