package com.kongzue.dialog.listener;

import android.app.Dialog;

public interface DialogLifeCycleListener {

    void onCreate(Dialog alertDialog);

    void onShow(Dialog alertDialog);

    void onDismiss();

}
