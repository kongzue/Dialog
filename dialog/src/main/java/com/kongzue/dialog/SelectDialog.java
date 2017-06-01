package com.kongzue.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


/**
 * Created by ZhangChao on 2017/5/5.
 */

public class SelectDialog {
    public static void show(Context context, String title, String tipText,String positiveButtonText,String nativeButtonText, final View.OnClickListener positiveClick, final View.OnClickListener nativeClick){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_select);
        TextView tv_title = (TextView) window.findViewById(R.id.txt_dialog_title);
        tv_title.setText(title);
        final TextView tip = (TextView) window.findViewById(R.id.txt_dialog_tip);
        tip.setText(tipText);

        TextView btn_selectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
        TextView btn_selectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);

        btn_selectPositive.setText(positiveButtonText);
        btn_selectPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                positiveClick.onClick(v);
            }
        });

        btn_selectNegative.setText(nativeButtonText);
        btn_selectNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                nativeClick.onClick(v);
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
}
