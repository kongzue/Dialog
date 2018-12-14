package com.kongzue.dialog.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.kongzue.dialog.listener.OnDismissListener;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/12/14 13:28
 */
public class KongzueDialogHelper extends DialogFragment {
    
    private AlertDialog alertDialog;
    private OnDismissListener onDismissListener;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return alertDialog;
    }
    
    public KongzueDialogHelper setAlertDialog(AlertDialog alertDialog, OnDismissListener onDismissListener) {
        this.alertDialog = alertDialog;
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) onDismissListener.onDismiss();
    }
}
