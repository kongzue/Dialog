package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.kongzue.dialog.v2.DialogSettings.*;

public class InputDialog extends BaseDialog {

    private AlertDialog alertDialog;
    private static InputDialog inputDialog;
    private boolean isCanCancel = false;

    private Context context;
    private String title;
    private String message;
    private String defaultInputText = "";
    private String defaultInputHint = "";
    private String okButtonCaption = "确定";
    private String cancelButtonCaption = "取消";
    private InputDialogOkButtonClickListener onOkButtonClickListener;
    private DialogInterface.OnClickListener onCancelButtonClickListener;

    private InputDialog() {
    }

    //Fast Function
    public static InputDialog show(Context context, String title, String message, InputDialogOkButtonClickListener onOkButtonClickListener) {
        return show(context, title, message, "确定", onOkButtonClickListener, "取消", null);
    }

    public static InputDialog show(Context context, String title, String message, String okButtonCaption, InputDialogOkButtonClickListener onOkButtonClickListener,
                                   String cancelButtonCaption, DialogInterface.OnClickListener onCancelButtonClickListener) {
        synchronized (InputDialog.class) {
            if (inputDialog == null) inputDialog = new InputDialog();
            inputDialog.context = context;
            inputDialog.title = title;
            inputDialog.message = message;
            inputDialog.okButtonCaption = okButtonCaption;
            inputDialog.cancelButtonCaption = cancelButtonCaption;
            inputDialog.onOkButtonClickListener = onOkButtonClickListener;
            inputDialog.onCancelButtonClickListener = onCancelButtonClickListener;
            inputDialog.showDialog();
            inputDialog.log("显示输入对话框 -> " + message);
            return inputDialog;
        }
    }

    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private EditText txtInput;
    private TextView btnSelectNegative;
    private TextView btnSelectPositive;

    private void showDialog() {
        AlertDialog.Builder builder;
        switch (type) {
            case TYPE_IOS:
                builder = new AlertDialog.Builder(context, R.style.iOSDialog);
                break;
            default:
                builder = new AlertDialog.Builder(context);
                break;
        }
        builder.setCancelable(isCanCancel);

        alertDialog = builder.create();
        alertDialog.setView(new EditText(context));
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);

        Window window = alertDialog.getWindow();
        switch (type) {
            case TYPE_KONGZUE:
                alertDialog.show();
                window.setContentView(R.layout.dialog_select);

                txtDialogTitle = (TextView) window.findViewById(R.id.txt_dialog_title);
                txtDialogTip = (TextView) window.findViewById(R.id.txt_dialog_tip);
                txtInput = (EditText) window.findViewById(R.id.txt_input);
                btnSelectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);
                btnSelectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);

                txtDialogTitle.setText(title);
                txtDialogTip.setText(message);
                if (message.contains("\n")){
                    txtDialogTip.setGravity(Gravity.LEFT);
                }else{
                    txtDialogTip.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                txtInput.setVisibility(View.VISIBLE);
                txtInput.setText(defaultInputText);
                txtInput.setHint(defaultInputHint);

                btnSelectNegative.setVisibility(View.VISIBLE);
                btnSelectPositive.setText(okButtonCaption);
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, txtInput.getText().toString());
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
                txtInput = new EditText(context);
                txtInput.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) txtInput.getLayoutParams();
                        p.setMargins(dip2px(context, 20), 0, dip2px(context, 20), 0);
                        txtInput.requestLayout();
                    }
                });
                txtInput.setText(defaultInputText);
                txtInput.setHint(defaultInputHint);

                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                alertDialog.setView(txtInput);
                alertDialog.setButton(BUTTON_POSITIVE, okButtonCaption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, txtInput.getText().toString());
                    }
                });
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
                txtInput = (EditText) window.findViewById(R.id.txt_input);
                ImageView splitVertical = (ImageView) window.findViewById(R.id.split_vertical);
                splitVertical.setVisibility(View.VISIBLE);
                txtInput.setVisibility(View.VISIBLE);
                txtInput.setText(defaultInputText);
                txtInput.setHint(defaultInputHint);

                txtDialogTitle.setText(title);
                txtDialogTip.setText(message);
                btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
                btnSelectPositive.setText(okButtonCaption);
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, txtInput.getText().toString());
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
                if (onCancelButtonClickListener != null)
                    onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                if (dialogLifeCycleListener != null) dialogLifeCycleListener.onDismiss();
            }
        });
        alertDialog.show();
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onShow(alertDialog);
    }

    public InputDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }

    public InputDialog setDefaultInputText(String defaultInputText) {
        this.defaultInputText = defaultInputText;
        if (alertDialog != null) {
            txtInput.setText(defaultInputText);
            txtInput.setHint(defaultInputHint);
        }
        return this;
    }

    public InputDialog setDefaultInputHint(String defaultInputHint) {
        this.defaultInputHint = defaultInputHint;
        if (alertDialog != null) {
            txtInput.setText(defaultInputText);
            txtInput.setHint(defaultInputHint);
        }
        return this;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
