package com.kongzue.dialog.util;

import android.util.Log;

import com.kongzue.dialog.listener.DialogLifeCycleListener;

import java.util.ArrayList;
import java.util.List;

import static com.kongzue.dialog.v2.DialogSettings.DEBUGMODE;

public abstract class BaseDialog {
    
    protected static List<BaseDialog> dialogList = new ArrayList<>();         //对话框队列
    
    public boolean isDialogShown = false;
    
    private DialogLifeCycleListener dialogLifeCycleListener;
    
    public void log(Object o) {
        if (DEBUGMODE) Log.i("DialogSDK >>>", o.toString());
    }
    
    public void setDialogLifeCycleListener(DialogLifeCycleListener listener) {
        dialogLifeCycleListener = listener;
    }
    
    public DialogLifeCycleListener getDialogLifeCycleListener() {
        return dialogLifeCycleListener;
    }
    
    public void cleanDialogLifeCycleListener() {
        dialogLifeCycleListener = null;
    }
    
    public abstract void showDialog();
    
    public abstract void doDismiss();
    
    public static void unloadAllDialog(){
        try{
            for (BaseDialog baseDialog:dialogList){
                baseDialog.doDismiss();
            }
            dialogList = new ArrayList<>();
        }catch (Exception e){
            if (DEBUGMODE)e.printStackTrace();
        }
    }
}
