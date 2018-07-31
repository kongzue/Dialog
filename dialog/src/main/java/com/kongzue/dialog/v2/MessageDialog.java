package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.BlurView;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.kongzue.dialog.v2.DialogSettings.*;

public class MessageDialog extends BaseDialog {
    
    private AlertDialog alertDialog;
    private static MessageDialog messageDialog;
    private boolean isCanCancel = true;
    
    private Context context;
    private String title;
    private String message;
    private String buttonCaption = "确定";
    private DialogInterface.OnClickListener onOkButtonClickListener;
    
    private MessageDialog() {
    }
    
    //Fast Function
    public static MessageDialog show(Context context, String title, String message) {
        return show(context, title, message, "确定", null);
    }
    
    public static MessageDialog show(Context context, String title, String message, String buttonCaption, DialogInterface.OnClickListener onOkButtonClickListener) {
        synchronized (MessageDialog.class) {
            messageDialog = new MessageDialog();
            messageDialog.cleanDialogLifeCycleListener();
            messageDialog.alertDialog = null;
            messageDialog.context = context;
            messageDialog.title = title;
            messageDialog.buttonCaption = buttonCaption;
            messageDialog.message = message;
            messageDialog.onOkButtonClickListener = onOkButtonClickListener;
            messageDialog.log("装载消息对话框 -> " + message);
            dialogList.add(messageDialog);
            showNextDialog();
            return messageDialog;
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
        log("启动消息对话框 -> " + message);
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
                if (bkg != null) bkg.removeAllViews();
                if (customView != null) customView.removeAllViews();
                customView = null;
                if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onDismiss();
                isDialogShown = false;
                dialogList.remove(MessageDialog.this);
                context = null;
                showNextDialog();
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
                txtInput = window.findViewById(R.id.txt_input);
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
                
                btnSelectNegative.setVisibility(View.GONE);
                btnSelectPositive.setText(buttonCaption);
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, BUTTON_POSITIVE);
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
                alertDialog.setButton(BUTTON_POSITIVE, buttonCaption, onOkButtonClickListener);
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
                
                btnSelectNegative.setVisibility(View.GONE);
                splitVertical.setVisibility(View.GONE);
                btnSelectPositive.setText(buttonCaption);
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, BUTTON_POSITIVE);
                    }
                });
                
                int bkgResId;
                if (dialog_theme == THEME_DARK) {
                    splitHorizontal.setBackgroundResource(R.color.ios_dialog_split_dark);
                    splitVertical.setBackgroundResource(R.color.ios_dialog_split_dark);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_one_dark);
                    bkgResId = R.drawable.rect_dlg_dark;
                    blur_front_color = Color.argb(200, 0, 0, 0);
                } else {
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_one);
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
    
    public MessageDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }
    
    public MessageDialog setCustomView(View view) {
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
