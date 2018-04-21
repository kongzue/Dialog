package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogThemeColor;


/**
 * Created by ZhangChao on 2017/5/5.
 */

@Deprecated
public class MessageDialog extends BaseDialog {

    private static MessageDialog messageDialog;

    private AlertDialog alertDialog;
    private int colorId = -1;
    private Context context;

    private String title = "";
    private String tipText = "";
    private String positiveButtonText = "";
    private View.OnClickListener positiveClick;

    private boolean isCanCancel = true;

    public MessageDialog(Context context) {
        this.context = context;
        messageDialog = this;
    }

    public static void show(Context context, String title, String tipText, String positiveButtonText, final View.OnClickListener positiveClick) {

        if (messageDialog == null) {
            synchronized (MessageDialog.class) {
                if (messageDialog == null) {
                    messageDialog = new MessageDialog(context);
                }
            }
        }

        messageDialog.colorId = DialogThemeColor.normalColor;
        messageDialog.context = context;
        messageDialog.title = title;
        messageDialog.tipText = tipText;
        messageDialog.positiveButtonText = positiveButtonText;
        messageDialog.positiveClick = positiveClick;

        doShow();
    }

    private static void doShow() {
        try {
            messageDialog.alertDialog = new AlertDialog.Builder(messageDialog.context).create();

            messageDialog.alertDialog.setCancelable(messageDialog.isCanCancel);

            messageDialog.alertDialog.show();

            Window window = messageDialog.alertDialog.getWindow();
            window.setContentView(R.layout.dialog_select);
            TextView tv_title = (TextView) window.findViewById(R.id.txt_dialog_title);
            tv_title.setText(messageDialog.title);
            final TextView tip = (TextView) window.findViewById(R.id.txt_dialog_tip);
            tip.setText(messageDialog.tipText);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (messageDialog.tipText.contains("\n")) {
                    tip.setGravity(Gravity.START);
                } else {
                    tip.setGravity(Gravity.CENTER);
                }
            }


            TextView btn_selectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
            TextView btn_selectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);

            btn_selectNegative.setVisibility(View.GONE);

            if (messageDialog.colorId == -1) messageDialog.colorId = DialogThemeColor.normalColor;

            btn_selectPositive.setBackgroundResource(DialogThemeColor.getRes(messageDialog.colorId));
            btn_selectPositive.setText(messageDialog.positiveButtonText);
            btn_selectPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageDialog.alertDialog.dismiss();
                    if (messageDialog.positiveClick != null) messageDialog.positiveClick.onClick(v);
                }
            });

            final View pButton = btn_selectPositive;
            messageDialog.alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (messageDialog.positiveClick != null)
                        messageDialog.positiveClick.onClick(pButton);
                }
            });
        } catch (Exception e) {

        }
    }

    public MessageDialog show() {
        if (context == null) {
            log("Error:context is null,please init Dialog first.");
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
        this.isCanCancel = isCanCancel;
        return this;
    }
}
