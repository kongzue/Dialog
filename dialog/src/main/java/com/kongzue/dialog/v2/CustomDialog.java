package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.kongzue.dialog.R;
import com.kongzue.dialog.util.ModalBaseDialog;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/11/19 17:09
 */
public class CustomDialog extends ModalBaseDialog {
    
    private boolean isCanCancel = false;
    private CustomDialog customDialog;
    private AlertDialog alertDialog;
    private View rootView;
    private Context context;
    private BindView bindView;
    
    private CustomDialog() {
    }
    
    public static CustomDialog show(Context context, int layoutResId) {
        View customView = LayoutInflater.from(context).inflate(layoutResId, null);
        CustomDialog customDialog = build(context, customView, null);
        customDialog.showDialog();
        return customDialog;
    }
    
    public static CustomDialog show(Context context, int layoutResId, BindView bindView) {
        View customView = LayoutInflater.from(context).inflate(layoutResId, null);
        CustomDialog customDialog = build(context, customView, bindView);
        customDialog.showDialog();
        return customDialog;
    }
    
    public static CustomDialog show(Context context, View rootView, BindView bindView) {
        CustomDialog customDialog = build(context, rootView, bindView);
        customDialog.showDialog();
        return customDialog;
    }
    
    public static CustomDialog build(Context context, View rootView, BindView bindView) {
        synchronized (CustomDialog.class) {
            CustomDialog customDialog = new CustomDialog();
            customDialog.cleanDialogLifeCycleListener();
            customDialog.alertDialog = null;
            customDialog.context = context;
            customDialog.bindView = bindView;
            customDialog.rootView = rootView;
            customDialog.log("装载自定义对话框");
            customDialog.customDialog = customDialog;
            modalDialogList.add(customDialog);
            return customDialog;
        }
    }
    
    @Override
    public void showDialog() {
        log("启动自定义对话框");
        dialogList.add(customDialog);
        modalDialogList.remove(customDialog);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, R.style.lightMode);
        builder.setCancelable(isCanCancel);
        alertDialog = builder.create();
        alertDialog.setView(rootView);
        
        if (getDialogLifeCycleListener() != null)
            getDialogLifeCycleListener().onCreate(alertDialog);
        
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogList.remove(customDialog);
                rootView = null;
                if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onDismiss();
                isDialogShown = false;
                context = null;
                
                if (!modalDialogList.isEmpty()) {
                    showNextModalDialog();
                }
            }
        });
        
        Window window = alertDialog.getWindow();
        alertDialog.show();
        
        if (getDialogLifeCycleListener() != null)
            getDialogLifeCycleListener().onShow(alertDialog);
        
        //window.setContentView(rootView);
        if (bindView != null) bindView.onBind(rootView);
    }
    
    @Override
    public void doDismiss() {
        if (alertDialog != null) alertDialog.dismiss();
    }
    
    public CustomDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }
    
    public interface BindView {
        void onBind(View rootView);
    }
}
