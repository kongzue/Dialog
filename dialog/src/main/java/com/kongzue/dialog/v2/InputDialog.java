package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            inputDialog.log("装载输入对话框 -> " + message);
            dialogList.add(inputDialog);
            showNextDialog();
            return inputDialog;
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
        log("启动输入对话框 -> " + message);
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
        alertDialog.setView(new EditText(context));
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onCancelButtonClickListener != null)
                    onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                if (dialogLifeCycleListener != null) dialogLifeCycleListener.onDismiss();
                isDialogShown = false;
                dialogList.remove(0);
                showNextDialog();
            }
        });

        Window window = alertDialog.getWindow();
        switch (type) {
            case TYPE_KONGZUE:
                alertDialog.show();
                window.setContentView(R.layout.dialog_select);

                bkg = (LinearLayout) window.findViewById(R.id.bkg);
                txtDialogTitle = (TextView) window.findViewById(R.id.txt_dialog_title);
                txtDialogTip = (TextView) window.findViewById(R.id.txt_dialog_tip);
                txtInput = (EditText) window.findViewById(R.id.txt_input);
                btnSelectNegative = (TextView) window.findViewById(R.id.btn_selectNegative);
                btnSelectPositive = (TextView) window.findViewById(R.id.btn_selectPositive);

                txtDialogTitle.setText(title);
                txtDialogTip.setText(message);
                if (message.contains("\n")) {
                    txtDialogTip.setGravity(Gravity.LEFT);
                } else {
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

                if (dialog_theme == THEME_DARK) {
                    bkg.setBackgroundResource(R.color.dlg_bkg_dark);
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_kongzue_gray_dark);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_kongzue_blue_dark);
                    btnSelectNegative.setTextColor(Color.rgb(255, 255, 255));
                    btnSelectPositive.setTextColor(Color.rgb(255, 255, 255));
                    txtInput.setTextColor(Color.rgb(255, 255, 255));
                    txtInput.setBackgroundResource(R.drawable.editbox_bkg_dark);
                }

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

                if (dialog_theme == THEME_DARK) {
                    txtInput.setTextColor(Color.rgb(255, 255, 255));
                } else {
                    txtInput.setTextColor(Color.rgb(0, 0, 0));
                }
                break;
            case TYPE_IOS:
                window.setWindowAnimations(R.style.iOSAnimStyle);
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
                txtInput = (EditText) window.findViewById(R.id.txt_input);
                ImageView splitVertical = (ImageView) window.findViewById(R.id.split_vertical);
                splitVertical.setVisibility(View.VISIBLE);
                txtInput.setVisibility(View.VISIBLE);
                txtInput.setText(defaultInputText);
                txtInput.setHint(defaultInputHint);

                txtDialogTitle.setText(title);
                txtDialogTip.setText(message);
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
                    txtInput.setBackgroundResource(R.drawable.editbox_bkg_ios_dark);
                    txtInput.setTextColor(Color.rgb(255, 255, 255));
                } else {
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
                    txtInput.setBackgroundResource(R.drawable.editbox_bkg_ios);
                    txtInput.setTextColor(Color.rgb(0, 0, 0));
                }

                log(ios_normal_button_color);
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
            if (dialog_input_text_size > 0) {
                txtInput.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_input_text_size);
            }
            if (dialog_button_text_size > 0) {
                btnSelectNegative.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_button_text_size);
                btnSelectPositive.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_button_text_size);
            }
        }
        isDialogShown = true;
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

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
