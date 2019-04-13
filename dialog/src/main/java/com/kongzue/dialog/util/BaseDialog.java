package com.kongzue.dialog.util;

import android.app.Dialog;
import android.util.Log;

import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.kongzue.dialog.listener.OnDismissListener;

import java.util.ArrayList;
import java.util.List;

import static com.kongzue.dialog.v2.DialogSettings.DEBUGMODE;

public abstract class BaseDialog {
    
    protected static List<BaseDialog> dialogList = new ArrayList<>();         //对话框队列
    
    public boolean isDialogShown = false;
    
    private DialogLifeCycleListener dialogLifeCycleListener;
    private OnDismissListener onDismissListener;
    
    public void log(Object o) {
        if (DEBUGMODE) Log.i("DialogSDK >>>", o.toString());
    }
    
    public void setDialogLifeCycleListener(DialogLifeCycleListener listener) {
        dialogLifeCycleListener = listener;
    }
    
    public DialogLifeCycleListener getDialogLifeCycleListener() {
        if (dialogLifeCycleListener == null)
            dialogLifeCycleListener = new DialogLifeCycleListener() {
                @Override
                public void onCreate(Dialog alertDialog) {
                
                }
                
                @Override
                public void onShow(Dialog alertDialog) {
                
                }
                
                @Override
                public void onDismiss() {
                
                }
            };
        return dialogLifeCycleListener;
    }
    
    public void cleanDialogLifeCycleListener() {
        dialogLifeCycleListener = null;
    }
    
    public OnDismissListener getOnDismissListener() {
        if (onDismissListener==null)onDismissListener = new OnDismissListener() {
            @Override
            public void onDismiss() {
        
            }
        };
        return onDismissListener;
    }
    
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
    
    public abstract void showDialog();
    
    public abstract void doDismiss();
    
    public static void unloadAllDialog() {
        try {
            for (BaseDialog baseDialog : dialogList) {
                baseDialog.doDismiss();
            }
            dialogList = new ArrayList<>();
        } catch (Exception e) {
            if (DEBUGMODE) e.printStackTrace();
        }
    }
}
