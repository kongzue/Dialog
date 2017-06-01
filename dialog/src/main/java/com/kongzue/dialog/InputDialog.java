package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kongzue.dialog.listener.InputDialogCallbackClickListener;


/**
 * Created by ZhangChao on 2017/5/5.
 */

public class InputDialog {
    public static void show(Context context, final InputDialogCallbackClickListener positiveClick, String title, String hintText){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.setView(new EditText(context));
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_input);
        TextView tv_title = (TextView) window.findViewById(R.id.txt_dialog_title);
        tv_title.setText(title);
        final EditText editText = (EditText) window.findViewById(R.id.txt_dialog_tip);

        editText.setHint(hintText);

        editText.setText("");
        editText.setSelection(editText.length());

        TextView btn_selectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
        TextView btn_selectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);

        btn_selectPositive.setText("确定");
        btn_selectPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                positiveClick.onClick(v,editText.getText().toString());
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
}
