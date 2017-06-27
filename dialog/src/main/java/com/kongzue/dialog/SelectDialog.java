package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kongzue.dialog.util.DialogThemeColor;
import com.kongzue.dialog.util.Log;


/**
 * Created by ZhangChao on 2017/5/5.
 */

public class SelectDialog {

    private static AlertDialog alertDialog;
    private static int colorId = -1;
    private static Context context;

    private static String title;
    private static String tipText;
    private static String positiveButtonText;
    private static String nativeButtonText;
    private static View.OnClickListener positiveClick;
    private static View.OnClickListener nativeClick;

    private static boolean isCanCancel = true;

    public SelectDialog(Context context) {
        this.context = context;
    }

    public static void show(Context context, String title, String tipText, String positiveButtonText, String nativeButtonText, final View.OnClickListener positiveClick, final View.OnClickListener nativeClick) {

        SelectDialog.colorId = DialogThemeColor.normalColor;
        SelectDialog.context = context;
        SelectDialog.title = title;
        SelectDialog.tipText = tipText;
        SelectDialog.positiveButtonText = positiveButtonText;
        SelectDialog.nativeButtonText = nativeButtonText;
        SelectDialog.positiveClick = positiveClick;
        SelectDialog.nativeClick = nativeClick;

        doShow();
    }

    private static void doShow() {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setCancelable(isCanCancel);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_select);
        TextView tv_title = (TextView) window.findViewById(R.id.txt_dialog_title);
        tv_title.setText(title);
        final TextView tip = (TextView) window.findViewById(R.id.txt_dialog_tip);
        tip.setText(tipText);

        TextView btn_selectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
        TextView btn_selectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);

        if (colorId == -1) colorId = DialogThemeColor.normalColor;

        btn_selectPositive.setBackgroundResource(DialogThemeColor.getRes(colorId));
        btn_selectPositive.setText(positiveButtonText);
        btn_selectPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (positiveClick != null) positiveClick.onClick(v);
            }
        });

        btn_selectNegative.setText(nativeButtonText);
        btn_selectNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (nativeClick != null) nativeClick.onClick(v);
            }
        });

        final View pButton = btn_selectNegative;
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (nativeClick != null) nativeClick.onClick(pButton);
            }
        });
    }

    public SelectDialog show() {
        if (context == null) {
            Log.println("Error:context is null,please init Dialog first.");
            return null;
        }
        doShow();
        return this;
    }

    public SelectDialog setThemeColor(int colorId) {
        this.colorId = colorId;
        return this;
    }

    public SelectDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public SelectDialog setTipText(String tipText) {
        this.tipText = tipText;
        return this;
    }

    public SelectDialog setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public SelectDialog setNativeButtonText(String nativeButtonText) {
        this.nativeButtonText = nativeButtonText;
        return this;
    }

    public SelectDialog setPositiveButtonClickListener(View.OnClickListener positiveClick) {
        this.positiveClick = positiveClick;
        return this;
    }

    public SelectDialog setNativeButtonClickListener(View.OnClickListener nativeClick) {
        this.nativeClick = nativeClick;
        return this;
    }

    public boolean isCanCancel() {
        return isCanCancel;
    }

    public SelectDialog setIsCanCancel(boolean isCanCancel) {
        this.isCanCancel = isCanCancel;
        return this;
    }
}
