package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kongzue.dialog.listener.InputDialogCallbackClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogThemeColor;


/**
 * Created by ZhangChao on 2017/5/5.
 */

@Deprecated
public class InputDialog extends BaseDialog {

    private static InputDialog inputDialog;

    private AlertDialog alertDialog;
    private int colorId = -1;
    private Context context;

    private String title = "";
    private String inputHintText = "";
    private String positiveButtonText;
    private String nativeButtonText;
    private InputDialogCallbackClickListener positiveClick;
    private View.OnClickListener nativeClick;

    private boolean isCanCancel = true;

    public InputDialog(Context context) {
        this.context = context;
        inputDialog = this;
    }

    public static void show(Context context, String title, String hintText,
                            String positiveButtonText, String nativeButtonText, final InputDialogCallbackClickListener positiveClick, final View.OnClickListener nativeClick) {

        if (inputDialog == null) {
            synchronized (InputDialog.class) {
                if (inputDialog == null) {
                    inputDialog = new InputDialog(context);
                }
            }
        }

        inputDialog.title = title;
        inputDialog.colorId = DialogThemeColor.normalColor;
        inputDialog.inputHintText = hintText;
        inputDialog.context = context;
        inputDialog.positiveButtonText = positiveButtonText;
        inputDialog.nativeButtonText = nativeButtonText;
        inputDialog.positiveClick = positiveClick;
        inputDialog.nativeClick = nativeClick;

        doShow();
    }

    private static void doShow() {
        try {
            inputDialog.alertDialog = new AlertDialog.Builder(inputDialog.context).create();

            inputDialog.alertDialog.setCancelable(inputDialog.isCanCancel);

            inputDialog.alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            inputDialog.alertDialog.setView(new EditText(inputDialog.context));
            inputDialog.alertDialog.show();

            Window window = inputDialog.alertDialog.getWindow();
            window.setContentView(R.layout.dialog_input);
            TextView tv_title = (TextView) window.findViewById(R.id.txt_dialog_title);
            tv_title.setText(inputDialog.title);
            final EditText editText = (EditText) window.findViewById(R.id.txt_dialog_tip);

            editText.setHint(inputDialog.inputHintText);

            editText.setText("");
            editText.setSelection(editText.length());

            TextView btn_selectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
            TextView btn_selectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);

            if (inputDialog.colorId == -1) inputDialog.colorId = DialogThemeColor.normalColor;

            btn_selectPositive.setBackgroundResource(DialogThemeColor.getRes(inputDialog.colorId));
            btn_selectPositive.setText(inputDialog.positiveButtonText);
            btn_selectPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputDialog.alertDialog.dismiss();
                    if (inputDialog.positiveClick != null)
                        inputDialog.positiveClick.onClick(v, editText.getText().toString());
                }
            });

            btn_selectNegative.setText(inputDialog.nativeButtonText);
            btn_selectNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputDialog.alertDialog.dismiss();
                    if (inputDialog.nativeClick != null) inputDialog.nativeClick.onClick(v);
                }
            });

            final View pButton = btn_selectNegative;
            inputDialog.alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (inputDialog.nativeClick != null) inputDialog.nativeClick.onClick(pButton);
                }
            });
        } catch (Exception e) {

        }
    }

    public InputDialog show() {
        if (context == null) {
            log("Error:context is null,please init Dialog first.");
            return null;
        }
        doShow();
        return this;
    }

    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    public InputDialog setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public String getNativeButtonText() {
        return nativeButtonText;
    }

    public InputDialog setNativeButtonText(String nativeButtonText) {
        this.nativeButtonText = nativeButtonText;
        return this;
    }

    public View.OnClickListener getNativeClick() {
        return nativeClick;
    }

    public InputDialog setNativeClick(View.OnClickListener nativeClick) {
        this.nativeClick = nativeClick;
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
