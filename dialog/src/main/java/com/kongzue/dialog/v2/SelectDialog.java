package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.util.BaseDialog;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.kongzue.dialog.v2.DialogSettings.*;

public class SelectDialog extends BaseDialog {

    private AlertDialog alertDialog;
    private static SelectDialog selectDialog;
    private boolean isCanCancel = false;

    private Context context;
    private String title;
    private String message;
    private String okButtonCaption = "确定";
    private String cancelButtonCaption = "取消";
    private DialogInterface.OnClickListener onOkButtonClickListener;
    private DialogInterface.OnClickListener onCancelButtonClickListener;

    private SelectDialog() {
    }

    //Fast Function
    public static SelectDialog show(Context context, String title, String message, DialogInterface.OnClickListener onOkButtonClickListener) {
        return show(context, title, message, "确定", onOkButtonClickListener, "取消", null);
    }

    public static SelectDialog show(Context context, String title, String message, String okButtonCaption, DialogInterface.OnClickListener onOkButtonClickListener) {
        return show(context, title, message, okButtonCaption, onOkButtonClickListener, "取消", null);
    }

    public static SelectDialog show(Context context, String title, String message, String okButtonCaption, DialogInterface.OnClickListener onOkButtonClickListener,
                                    String cancelButtonCaption, DialogInterface.OnClickListener onCancelButtonClickListener) {
        synchronized (SelectDialog.class) {
            if (selectDialog == null) selectDialog = new SelectDialog();
            selectDialog.context = context;
            selectDialog.title = title;
            selectDialog.message = message;
            selectDialog.okButtonCaption = okButtonCaption;
            selectDialog.cancelButtonCaption = cancelButtonCaption;
            selectDialog.onOkButtonClickListener = onOkButtonClickListener;
            selectDialog.onCancelButtonClickListener = onCancelButtonClickListener;
            selectDialog.showDialog();
            selectDialog.log("显示选择对话框 -> " + message);
            return selectDialog;
        }
    }

    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private TextView btnSelectNegative;
    private TextView btnSelectPositive;

    private void showDialog() {
        AlertDialog.Builder builder;
        switch (type) {
            case TYPE_IOS:
                builder = new AlertDialog.Builder(context, R.style.processDialog);
                break;
            default:
                builder = new AlertDialog.Builder(context);
                break;
        }
        builder.setCancelable(isCanCancel);

        alertDialog = builder.create();
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);

        Window window = alertDialog.getWindow();
        switch (type) {
            case TYPE_KONGZUE:
                alertDialog.show();
                window.setContentView(R.layout.dialog_select);

                txtDialogTitle = (TextView) window.findViewById(R.id.txt_dialog_title);
                txtDialogTip = (TextView) window.findViewById(R.id.txt_dialog_tip);
                btnSelectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);
                btnSelectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);

                txtDialogTitle.setText(title);
                txtDialogTip.setText(message);
                if (message.contains("\n")){
                    txtDialogTip.setGravity(Gravity.LEFT);
                }else{
                    txtDialogTip.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                btnSelectNegative.setVisibility(View.VISIBLE);
                btnSelectPositive.setText(okButtonCaption);
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, BUTTON_POSITIVE);
                    }
                });
                btnSelectNegative.setText(cancelButtonCaption);
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onCancelButtonClickListener != null)
                            onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                    }
                });

                break;
            case TYPE_MATERIAL:
                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                alertDialog.setButton(BUTTON_POSITIVE, okButtonCaption, onOkButtonClickListener);
                alertDialog.setButton(BUTTON_NEGATIVE, cancelButtonCaption, onCancelButtonClickListener);
                alertDialog.show();
                break;
            case TYPE_IOS:
                alertDialog.show();
                window.setContentView(R.layout.dialog_select_ios);

                txtDialogTitle = (TextView) window.findViewById(R.id.txt_dialog_title);
                txtDialogTip = (TextView) window.findViewById(R.id.txt_dialog_tip);
                btnSelectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);
                btnSelectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
                ImageView splitVertical = (ImageView) window.findViewById(R.id.split_vertical);
                splitVertical.setVisibility(View.VISIBLE);

                txtDialogTitle.setText(title);
                txtDialogTip.setText(message);
                btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
                btnSelectPositive.setText(okButtonCaption);
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, BUTTON_POSITIVE);
                    }
                });
                btnSelectNegative.setVisibility(View.VISIBLE);
                btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left);
                btnSelectNegative.setText(cancelButtonCaption);
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onCancelButtonClickListener != null)
                            onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                    }
                });

                break;
        }

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dialogLifeCycleListener != null) dialogLifeCycleListener.onDismiss();
            }
        });
        alertDialog.show();
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onShow(alertDialog);
    }

    public SelectDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }
}
