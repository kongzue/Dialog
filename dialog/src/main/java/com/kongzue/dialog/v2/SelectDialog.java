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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.util.ModalBaseDialog;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.kongzue.dialog.v2.DialogSettings.*;

public class SelectDialog extends ModalBaseDialog {
    
    private SelectDialog selectDialog;
    private AlertDialog alertDialog;
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
        SelectDialog selectDialog = build(context, title, message, "确定", onOkButtonClickListener, "取消", null);
        selectDialog.showDialog();
        return selectDialog;
    }
    
    public static SelectDialog show(Context context, String title, String message, String okButtonCaption, DialogInterface.OnClickListener onOkButtonClickListener) {
        SelectDialog selectDialog = build(context, title, message, okButtonCaption, onOkButtonClickListener, "取消", null);
        selectDialog.showDialog();
        return selectDialog;
    }
    
    public static SelectDialog show(Context context, String title, String message, String okButtonCaption, DialogInterface.OnClickListener onOkButtonClickListener,
                                    String cancelButtonCaption, DialogInterface.OnClickListener onCancelButtonClickListener) {
        SelectDialog selectDialog = build(context, title, message, okButtonCaption, onOkButtonClickListener, cancelButtonCaption, onCancelButtonClickListener);
        selectDialog.showDialog();
        return selectDialog;
    }
    
    public static SelectDialog build(Context context, String title, String message, String okButtonCaption, DialogInterface.OnClickListener onOkButtonClickListener,
                                     String cancelButtonCaption, DialogInterface.OnClickListener onCancelButtonClickListener) {
        synchronized (SelectDialog.class) {
            SelectDialog selectDialog = new SelectDialog();
            selectDialog.cleanDialogLifeCycleListener();
            selectDialog.alertDialog = null;
            selectDialog.context = context;
            selectDialog.title = title;
            selectDialog.message = message;
            selectDialog.okButtonCaption = okButtonCaption;
            selectDialog.cancelButtonCaption = cancelButtonCaption;
            selectDialog.onOkButtonClickListener = onOkButtonClickListener;
            selectDialog.onCancelButtonClickListener = onCancelButtonClickListener;
            selectDialog.isCanCancel = dialog_cancelable_default;
            selectDialog.log("装载选择对话框 -> " + message);
            selectDialog.selectDialog = selectDialog;
            modalDialogList.add(selectDialog);
            return selectDialog;
        }
    }
    
    private BlurView blur;
    private ViewGroup bkg;
    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private EditText txtInput;
    private ImageView splitHorizontal;
    private TextView btnSelectNegative;
    private ImageView splitVertical;
    private TextView btnSelectPositive;
    private RelativeLayout customView;
    
    int blur_front_color;
    
    public void showDialog() {
        dialogList.add(selectDialog);
        modalDialogList.remove(selectDialog);
        log("显示选择对话框 -> " + message);
        
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
        if (getDialogLifeCycleListener() != null)
            getDialogLifeCycleListener().onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogList.remove(selectDialog);
                if (bkg != null) bkg.removeAllViews();
                if (customView != null) customView.removeAllViews();
                if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onDismiss();
                isDialogShown = false;
                context = null;
    
                if (!modalDialogList.isEmpty()){
                    showNextModalDialog();
                }
            }
        });
        
        Window window = alertDialog.getWindow();
        switch (type) {
            case TYPE_KONGZUE:
                alertDialog.show();
                window.setContentView(R.layout.dialog_select);
                
                bkg = (LinearLayout) window.findViewById(R.id.bkg);
                txtDialogTitle = window.findViewById(R.id.txt_dialog_title);
                txtDialogTip = window.findViewById(R.id.txt_dialog_tip);
                btnSelectNegative = window.findViewById(R.id.btn_selectNegative);
                btnSelectPositive = window.findViewById(R.id.btn_selectPositive);
                customView = window.findViewById(R.id.box_custom);
                
                if (isNull(title)) {
                    txtDialogTitle.setVisibility(View.GONE);
                } else {
                    txtDialogTitle.setVisibility(View.VISIBLE);
                    txtDialogTitle.setText(title);
                }
                if (isNull(message)) {
                    txtDialogTip.setVisibility(View.GONE);
                } else {
                    txtDialogTip.setVisibility(View.VISIBLE);
                    txtDialogTip.setText(message);
                    if (message.contains("\n")) {
                        txtDialogTip.setGravity(Gravity.LEFT);
                    } else {
                        txtDialogTip.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
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
                
                if (dialog_theme == THEME_DARK) {
                    bkg.setBackgroundResource(R.color.dlg_bkg_dark);
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_kongzue_gray_dark);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_kongzue_blue_dark);
                    btnSelectNegative.setTextColor(Color.rgb(255, 255, 255));
                    btnSelectPositive.setTextColor(Color.rgb(255, 255, 255));
                }
                
                if (dialog_background_color != -1) {
                    bkg.setBackgroundResource(dialog_background_color);
                }
                break;
            case TYPE_MATERIAL:
                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                alertDialog.setButton(BUTTON_POSITIVE, okButtonCaption, onOkButtonClickListener);
                alertDialog.setButton(BUTTON_NEGATIVE, cancelButtonCaption, onCancelButtonClickListener);
                if (dialog_background_color != -1) {
                    alertDialog.getWindow().getDecorView().setBackgroundResource(dialog_background_color);
                }
                alertDialog.show();
                break;
            case TYPE_IOS:
                window.setWindowAnimations(R.style.iOSAnimStyle);
                alertDialog.show();
                window.setContentView(R.layout.dialog_select_ios);
                
                bkg = (RelativeLayout) window.findViewById(R.id.bkg);
                txtDialogTitle = window.findViewById(R.id.txt_dialog_title);
                txtDialogTip = window.findViewById(R.id.txt_dialog_tip);
                txtInput = window.findViewById(R.id.txt_input);
                splitHorizontal = window.findViewById(R.id.split_horizontal);
                btnSelectNegative = window.findViewById(R.id.btn_selectNegative);
                splitVertical = window.findViewById(R.id.split_vertical);
                btnSelectPositive = window.findViewById(R.id.btn_selectPositive);
                customView = window.findViewById(R.id.box_custom);
                
                splitVertical.setVisibility(View.VISIBLE);
                
                if (isNull(title)) {
                    txtDialogTitle.setVisibility(View.GONE);
                } else {
                    txtDialogTitle.setVisibility(View.VISIBLE);
                    txtDialogTitle.setText(title);
                }
                if (isNull(message)) {
                    txtDialogTip.setVisibility(View.GONE);
                } else {
                    txtDialogTip.setVisibility(View.VISIBLE);
                    txtDialogTip.setText(message);
                }
                
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
                
                int bkgResId;
                if (dialog_theme == THEME_DARK) {
                    splitHorizontal.setBackgroundResource(R.color.ios_dialog_split_dark);
                    splitVertical.setBackgroundResource(R.color.ios_dialog_split_dark);
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left_dark);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right_dark);
                    bkgResId = R.drawable.rect_dlg_dark;
                    blur_front_color = Color.argb(200, 0, 0, 0);
                } else {
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
                    bkgResId = R.drawable.rect_light;
                    blur_front_color = Color.argb(200, 255, 255, 255);      //白
                }
                
                if (use_blur) {
                    bkg.post(new Runnable() {
                        @Override
                        public void run() {
                            blur = new BlurView(context, null);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bkg.getHeight());
                            blur.setOverlayColor(blur_front_color);
                            bkg.addView(blur, 0, params);
                        }
                    });
                } else {
                    bkg.setBackgroundResource(bkgResId);
                }
                
                if (ios_normal_button_color != -1) {
                    btnSelectNegative.setTextColor(ios_normal_button_color);
                    btnSelectPositive.setTextColor(ios_normal_button_color);
                }
                
                if (dialog_background_color != -1) {
                    bkg.setBackgroundResource(dialog_background_color);
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
        isDialogShown = true;
        if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onShow(alertDialog);
    }
    
    @Override
    public void doDismiss() {
        if (alertDialog != null) alertDialog.dismiss();
    }
    
    public SelectDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }
    
    public SelectDialog setCustomView(View view) {
        if (type == TYPE_MATERIAL) {
            customView = new RelativeLayout(context);
            customView.addView(view);
            alertDialog.setContentView(customView);
        } else {
            if (alertDialog != null && view != null) {
                customView.setVisibility(View.VISIBLE);
                customView.addView(view);
            }
        }
        return this;
    }
    
    private boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null")) {
            return true;
        }
        return false;
    }
}
