package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
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
    private static int colorId = 0;
    private static Context context;

    private static String title = "";
    private static String inputHintText = "";
    private static InputDialogCallbackClickListener positiveClick;

    public InputDialog(Context context) {
        this.context = context;
    }

    public static void show(Context context, final InputDialogCallbackClickListener positiveClick, String title, String hintText) {
        InputDialog.title = title;
        InputDialog.colorId = DialogThemeColor.normalColor;
        InputDialog.inputHintText = inputHintText;
        InputDialog.positiveClick = positiveClick;
        InputDialog.context = context;

        doShow();
    }

    private static void doShow() {
        alertDialog = new AlertDialog.Builder(context).create();
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

        btn_selectPositive.setBackgroundResource(DialogThemeColor.getRes(colorId));
        btn_selectPositive.setText("确定");
        btn_selectPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (positiveClick != null) positiveClick.onClick(v, editText.getText().toString());
            }
        });

        btn_selectNegative.setText("取消");
        btn_selectNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public InputDialog show() {
        if (context == null) {
            Log.println("Error:context is null,please init Dialog first.");
            return null;
        }
        doShow();
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

    public InputDialog setThemeColor(int colorId){
        this.colorId = colorId;
        return this;
    }

}
