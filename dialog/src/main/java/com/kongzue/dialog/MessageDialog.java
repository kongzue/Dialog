package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialog.util.DialogThemeColor;
import com.kongzue.dialog.util.Log;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.TEXT_ALIGNMENT_INHERIT;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;


/**
 * Created by ZhangChao on 2017/5/5.
 */

public class MessageDialog {

    private static AlertDialog alertDialog;
    private static int colorId = -1;
    private static Context context;

    private static String title = "";
    private static String tipText = "";
    private static String positiveButtonText = "";
    private static View.OnClickListener positiveClick;

    private static boolean isCanCancel = true;

    public MessageDialog(Context context) {
        this.context = context;
    }

    public static void show(Context context, String title, String tipText, String positiveButtonText, final View.OnClickListener positiveClick) {

        MessageDialog.colorId = DialogThemeColor.normalColor;
        MessageDialog.context = context;
        MessageDialog.title = title;
        MessageDialog.tipText = tipText;
        MessageDialog.positiveButtonText = positiveButtonText;
        MessageDialog.positiveClick = positiveClick;

        doShow();
    }

    private static void doShow() {
        try {
            alertDialog = new AlertDialog.Builder(context).create();

            alertDialog.setCancelable(isCanCancel);

            alertDialog.show();

            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.dialog_select);
            TextView tv_title = (TextView) window.findViewById(R.id.txt_dialog_title);
            tv_title.setText(title);
            final TextView tip = (TextView) window.findViewById(R.id.txt_dialog_tip);
            tip.setText(tipText);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (tipText.contains("\n")) {
                    tip.setGravity(Gravity.START);
                } else {
                    tip.setGravity(Gravity.CENTER);
                }
            }


            TextView btn_selectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
            TextView btn_selectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);

            btn_selectNegative.setVisibility(View.GONE);

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

            final View pButton = btn_selectPositive;
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (positiveClick != null) positiveClick.onClick(pButton);
                }
            });
        } catch (Exception e) {

        }
    }

    public MessageDialog show() {
        if (context == null) {
            Log.println("Error:context is null,please init Dialog first.");
            return null;
        }
        doShow();
        return this;
    }

    public MessageDialog setThemeColor(int colorId) {
        this.colorId = colorId;
        return this;
    }

    public MessageDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MessageDialog setTipText(String tipText) {
        this.tipText = tipText;
        return this;
    }

    public MessageDialog setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public MessageDialog setPositiveButtonClickListener(View.OnClickListener positiveClick) {
        this.positiveClick = positiveClick;
        return this;
    }

    public boolean isCanCancel() {
        return isCanCancel;
    }

    public MessageDialog setIsCanCancel(boolean isCanCancel) {
        MessageDialog.isCanCancel = isCanCancel;
        return this;
    }
}
