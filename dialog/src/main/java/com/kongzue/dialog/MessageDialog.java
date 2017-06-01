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

public class MessageDialog {

    public static void show(Context context, String title, String tipText, String positiveButtonText, final View.OnClickListener positiveClick) {
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

        btn_selectNegative.setVisibility(View.GONE);

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
    }

}
