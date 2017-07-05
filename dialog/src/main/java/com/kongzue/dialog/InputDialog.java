package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kongzue.dialog.listener.InputDialogCallbackClickListener;
import com.kongzue.dialog.util.DialogThemeColor;
import com.kongzue.dialog.util.Log;


/**
 * Created by ZhangChao on 2017/5/5.
 */

public class InputDialog {

    private static AlertDialog alertDialog;
    private static int colorId = -1;
    private static Context context;

    private static String title = "";
    private static String inputHintText = "";
    private static String positiveButtonText;
    private static String nativeButtonText;
    private static InputDialogCallbackClickListener positiveClick;
    private static View.OnClickListener nativeClick;

    private static boolean isCanCancel = true;

    public InputDialog(Context context) {
        this.context = context;
    }

    public static void show(Context context, String title, String hintText,
                            String positiveButtonText, String nativeButtonText, final InputDialogCallbackClickListener positiveClick, final View.OnClickListener nativeClick) {
        InputDialog.title = title;
        InputDialog.colorId = DialogThemeColor.normalColor;
        InputDialog.inputHintText = hintText;
        InputDialog.context = context;
        InputDialog.positiveButtonText = positiveButtonText;
        InputDialog.nativeButtonText = nativeButtonText;
        InputDialog.positiveClick = positiveClick;
        InputDialog.nativeClick = nativeClick;

        doShow();
    }

    private static void doShow() {
        try {
            alertDialog = new AlertDialog.Builder(context).create();

            alertDialog.setCancelable(isCanCancel);

            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            alertDialog.setView(new EditText(context));
            alertDialog.show();

            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.dialog_input);
            TextView tv_title = (TextView) window.findViewById(R.id.txt_dialog_title);
            tv_title.setText(title);
            final EditText editText = (EditText) window.findViewById(R.id.txt_dialog_tip);

            editText.setHint(inputHintText);

            editText.setText("");
            editText.setSelection(editText.length());

            TextView btn_selectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
            TextView btn_selectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);

            if (colorId == -1) colorId = DialogThemeColor.normalColor;

            btn_selectPositive.setBackgroundResource(DialogThemeColor.getRes(colorId));
            btn_selectPositive.setText(positiveButtonText);
            btn_selectPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (positiveClick != null)
                        positiveClick.onClick(v, editText.getText().toString());
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
        } catch (Exception e) {

        }
    }

    public InputDialog show() {
        if (context == null) {
            Log.println("Error:context is null,please init Dialog first.");
            return null;
        }
        doShow();
        return this;
    }

    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    public InputDialog setPositiveButtonText(String positiveButtonText) {
        InputDialog.positiveButtonText = positiveButtonText;
        return this;
    }

    public String getNativeButtonText() {
        return nativeButtonText;
    }

    public InputDialog setNativeButtonText(String nativeButtonText) {
        InputDialog.nativeButtonText = nativeButtonText;
        return this;
    }

    public View.OnClickListener getNativeClick() {
        return nativeClick;
    }

    public InputDialog setNativeClick(View.OnClickListener nativeClick) {
        InputDialog.nativeClick = nativeClick;
        return this;
    }

    public InputDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public InputDialog setInputHintText(String inputHintText) {
        this.inputHintText = inputHintText;
        return this;
    }

    public InputDialog setOnPositiveButtonClickListener(InputDialogCallbackClickListener positiveClick) {
        this.positiveClick = positiveClick;
        return this;
    }

    public InputDialog setThemeColor(int colorId) {
        this.colorId = colorId;
        return this;
    }

    public boolean isCanCancel() {
        return isCanCancel;
    }

    public InputDialog setIsCanCancel(boolean isCanCancel) {
        this.isCanCancel = isCanCancel;
        return this;
    }

}
