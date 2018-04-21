package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogThemeColor;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;


/**
 * Created by ZhangChao on 2017/5/5.
 */

@Deprecated
public class SelectDialog extends BaseDialog {

    private static SelectDialog selectDialog;

    private AlertDialog alertDialog;
    private int colorId = -1;
    private Context context;

    private String title;
    private String tipText;
    private String positiveButtonText;
    private String nativeButtonText;
    private View.OnClickListener positiveClick;
    private View.OnClickListener nativeClick;

    private boolean isCanCancel = true;

    public SelectDialog(Context context) {
        this.context = context;
        selectDialog = this;
    }

    public static void show(Context context, String title, String tipText, String positiveButtonText, String nativeButtonText, final View.OnClickListener positiveClick, final View.OnClickListener nativeClick) {

        if (selectDialog == null) {
            synchronized (SelectDialog.class) {
                if (selectDialog == null) {
                    selectDialog = new SelectDialog(context);
                }
            }
        }

        selectDialog.colorId = DialogThemeColor.normalColor;
        selectDialog.context = context;
        selectDialog.title = title;
        selectDialog.tipText = tipText;
        selectDialog.positiveButtonText = positiveButtonText;
        selectDialog.nativeButtonText = nativeButtonText;
        selectDialog.positiveClick = positiveClick;
        selectDialog.nativeClick = nativeClick;

        doShow();
    }

    private static void doShow() {
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(selectDialog.context).create();

            alertDialog.setCancelable(selectDialog.isCanCancel);

            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.dialog_select);
            TextView tv_title = (TextView) window.findViewById(R.id.txt_dialog_title);
            tv_title.setText(selectDialog.title);
            final TextView tip = (TextView) window.findViewById(R.id.txt_dialog_tip);
            tip.setText(selectDialog.tipText);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (selectDialog.tipText.contains("\n")) {
                    tip.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                } else {
                    tip.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                }
            }

            TextView btn_selectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
            TextView btn_selectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);

            if (selectDialog.colorId == -1) selectDialog.colorId = DialogThemeColor.normalColor;

            btn_selectPositive.setBackgroundResource(DialogThemeColor.getRes(selectDialog.colorId));
            btn_selectPositive.setText(selectDialog.positiveButtonText);
            btn_selectPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (selectDialog.positiveClick != null) selectDialog.positiveClick.onClick(v);
                }
            });

            btn_selectNegative.setText(selectDialog.nativeButtonText);
            btn_selectNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (selectDialog.nativeClick != null) selectDialog.nativeClick.onClick(v);
                }
            });

            final View pButton = btn_selectNegative;
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (selectDialog.nativeClick != null) selectDialog.nativeClick.onClick(pButton);
                }
            });
        } catch (Exception e) {

        }
    }

    public SelectDialog show() {
        if (context == null) {
            log("Error:context is null,please init Dialog first.");
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
