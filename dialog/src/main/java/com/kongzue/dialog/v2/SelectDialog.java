package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private LinearLayout bkg;
    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private EditText txtInput;
    private ImageView splitHorizontal;
    private TextView btnSelectNegative;
    private ImageView splitVertical;
    private TextView btnSelectPositive;

    public void showDialog() {
        AlertDialog.Builder builder;
        switch (type) {
            case TYPE_IOS:
                switch (dialog_theme) {
                    case THEME_DARK:
                        builder = new AlertDialog.Builder(context, R.style.darkMode);
                        break;
                    default:
                        builder = new AlertDialog.Builder(context, R.style.lightMode);
                        break;
                }
                break;
            case TYPE_MATERIAL:
                if (dialog_theme == THEME_DARK) {
                    builder = new AlertDialog.Builder(context, R.style.materialDialogDark);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                break;
            case TYPE_KONGZUE:
                switch (dialog_theme) {
                    case THEME_DARK:
                        builder = new AlertDialog.Builder(context, R.style.materialDialogDark);
                        break;
                    default:
                        builder = new AlertDialog.Builder(context, R.style.materialDialogLight);
                        break;
                }
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

                bkg = (LinearLayout) window.findViewById(R.id.bkg);
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

                if (dialog_theme==THEME_DARK){
                    bkg.setBackgroundResource(R.color.dlg_bkg_dark);
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_kongzue_gray_dark);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_kongzue_blue_dark);
                    btnSelectNegative.setTextColor(Color.rgb(255,255,255));
                    btnSelectPositive.setTextColor(Color.rgb(255,255,255));
                }

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

                bkg = (LinearLayout) window.findViewById(R.id.bkg);
                txtDialogTitle = (TextView) window.findViewById(R.id.txt_dialog_title);
                txtDialogTip = (TextView) window.findViewById(R.id.txt_dialog_tip);
                txtInput = (EditText) window.findViewById(R.id.txt_input);
                splitHorizontal = (ImageView) window.findViewById(R.id.split_horizontal);
                btnSelectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);
                splitVertical = (ImageView) window.findViewById(R.id.split_vertical);
                btnSelectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);
                splitVertical.setVisibility(View.VISIBLE);

                txtDialogTitle.setText(title);
                txtDialogTip.setText(message);
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
                btnSelectNegative.setText(cancelButtonCaption);
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onCancelButtonClickListener != null)
                            onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                    }
                });

                if (dialog_theme == THEME_DARK) {
                    bkg.setBackgroundResource(R.drawable.rect_dlg_dark);
                    splitHorizontal.setBackgroundResource(R.color.ios_dialog_split_dark);
                    splitVertical.setBackgroundResource(R.color.ios_dialog_split_dark);
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left_dark);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right_dark);
                }else{
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
                }

                if (ios_normal_button_color != -1) {
                    btnSelectNegative.setTextColor(ios_normal_button_color);
                    btnSelectPositive.setTextColor(ios_normal_button_color);
                }

                break;
        }

        if (type != TYPE_MATERIAL) {
            if (dialog_title_text_size > 0) {
                txtDialogTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_title_text_size);
            }
            if (dialog_message_text_size > 0) {
                txtDialogTip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_message_text_size);
            }
            if (dialog_button_text_size > 0) {
                btnSelectNegative.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_button_text_size);
                btnSelectPositive.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_button_text_size);
            }
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
