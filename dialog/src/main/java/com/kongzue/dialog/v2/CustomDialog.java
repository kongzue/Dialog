package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.OnDismissListener;
import com.kongzue.dialog.util.KongzueDialogHelper;
import com.kongzue.dialog.util.ModalBaseDialog;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

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
    
    public static CustomDialog build(Context context, int layoutResId, BindView bindView) {
        View customView = LayoutInflater.from(context).inflate(layoutResId, null);
        return build(context, customView, bindView);
    }
    
    private KongzueDialogHelper kongzueDialogHelper;
    
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
        
            getDialogLifeCycleListener().onCreate(alertDialog);
        
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        kongzueDialogHelper = new KongzueDialogHelper().setAlertDialog(alertDialog, new OnDismissListener() {
            @Override
            public void onDismiss() {
                dialogList.remove(customDialog);
                rootView = null;
                getDialogLifeCycleListener().onDismiss();
                getOnDismissListener().onDismiss();
                isDialogShown = false;
                context = null;
                
                if (!modalDialogList.isEmpty()) {
                    showNextModalDialog();
                }
            }
        });
        
            getDialogLifeCycleListener().onShow(alertDialog);
        
        if (bindView != null) bindView.onBind(this, rootView);
        
        kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
        kongzueDialogHelper.setCancelable(isCanCancel);
    }
    
    @Override
    public void doDismiss() {
        if (kongzueDialogHelper != null) kongzueDialogHelper.dismissAllowingStateLoss();
    }
    
    public CustomDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (kongzueDialogHelper!=null) kongzueDialogHelper.setCancelable(isCanCancel);
        return this;
    }
    
    public interface BindView {
        void onBind(CustomDialog dialog, View rootView);
    }
    
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}
