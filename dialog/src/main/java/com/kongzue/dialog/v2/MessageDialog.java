package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.OnDismissListener;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.util.KongzueDialogHelper;
import com.kongzue.dialog.util.ModalBaseDialog;
import com.kongzue.dialog.util.TextInfo;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.kongzue.dialog.v2.DialogSettings.*;

public class MessageDialog extends ModalBaseDialog {
    
    private MessageDialog messageDialog;
    private AlertDialog alertDialog;
    private boolean isCanCancel = true;
    private int type = -1;
    
    private Context context;
    private String title;
    private String message;
    private String buttonCaption = "确定";
    private DialogInterface.OnClickListener onOkButtonClickListener;
    
    private TextInfo customTitleTextInfo;
    private TextInfo customContentTextInfo;
    private TextInfo customOkButtonTextInfo;
    
    private MessageDialog() {
    }
    
    //Fast Function
    public static MessageDialog show(Context context, String title, String message) {
        MessageDialog messageDialog = build(context, title, message, "确定", null);
        messageDialog.showDialog();
        return messageDialog;
    }
    
    public static MessageDialog show(Context context, String title, String message, String buttonCaption, DialogInterface.OnClickListener onOkButtonClickListener) {
        MessageDialog messageDialog = build(context, title, message, buttonCaption, onOkButtonClickListener);
        messageDialog.showDialog();
        return messageDialog;
    }
    
    public static MessageDialog build(Context context, String title, String message, String buttonCaption, DialogInterface.OnClickListener onOkButtonClickListener) {
        synchronized (MessageDialog.class) {
            MessageDialog messageDialog = new MessageDialog();
            messageDialog.cleanDialogLifeCycleListener();
            messageDialog.alertDialog = null;
            messageDialog.context = context;
            messageDialog.title = title;
            messageDialog.buttonCaption = buttonCaption;
            messageDialog.message = message;
            messageDialog.onOkButtonClickListener = onOkButtonClickListener;
            messageDialog.isCanCancel = dialog_cancelable_default;
            messageDialog.log("装载消息对话框 -> " + message);
            messageDialog.messageDialog = messageDialog;
            modalDialogList.add(messageDialog);
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
        if (customTitleTextInfo == null) {
            customTitleTextInfo = dialogTitleTextInfo;
        }
        if (customContentTextInfo == null) {
            customContentTextInfo = dialogContentTextInfo;
        }
        if (customOkButtonTextInfo == null) {
            customOkButtonTextInfo = dialogButtonTextInfo;
        }
        
        log("启动消息对话框 -> " + message);
        if (type == -1) type = DialogSettings.type;
        dialogList.add(messageDialog);
        modalDialogList.remove(messageDialog);
        
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
        
        alertDialog = builder.create();
        if (getDialogLifeCycleListener() != null)
            getDialogLifeCycleListener().onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        
        Window window = alertDialog.getWindow();
    
        View rootView;
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        KongzueDialogHelper kongzueDialogHelper = new KongzueDialogHelper().setAlertDialog(alertDialog, new OnDismissListener() {
            @Override
            public void onDismiss() {
                dialogList.remove(messageDialog);
                if (bkg != null) bkg.removeAllViews();
                if (customView != null) customView.removeAllViews();
                customView = null;
                if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onDismiss();
                isDialogShown = false;
                context = null;
    
                if (!modalDialogList.isEmpty()) {
                    showNextModalDialog();
                }
            }
        });
    
        switch (type) {
            case TYPE_KONGZUE:
                rootView = LayoutInflater.from(context).inflate(R.layout.dialog_select, null);
                alertDialog.setView(rootView);
                kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
                
                bkg = (LinearLayout) rootView.findViewById(R.id.bkg);
                txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
                txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
                txtInput = rootView.findViewById(R.id.txt_input);
                btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
                btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
                customView = rootView.findViewById(R.id.box_custom);
                
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
                
                useTextInfo(txtDialogTitle, customTitleTextInfo);
                useTextInfo(txtDialogTip, customContentTextInfo);
                useTextInfo(btnSelectPositive, customOkButtonTextInfo);
                
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
                if (customView != null) alertDialog.setView(customView);
               
                kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
                break;
            case TYPE_IOS:
                rootView = LayoutInflater.from(context).inflate(R.layout.dialog_select_ios, null);
                alertDialog.setView(rootView);
                kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
                
                window.setWindowAnimations(R.style.iOSAnimStyle);
                
                bkg = (RelativeLayout) rootView.findViewById(R.id.bkg);
                txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
                txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
                txtInput = rootView.findViewById(R.id.txt_input);
                splitHorizontal = rootView.findViewById(R.id.split_horizontal);
                btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
                splitVertical = rootView.findViewById(R.id.split_vertical);
                btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
                customView = rootView.findViewById(R.id.box_custom);
                
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
                    blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
                } else {
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_one);
                    bkgResId = R.drawable.rect_light;
                    blur_front_color = Color.argb(blur_alpha, 255, 255, 255);      //白
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
                
                useTextInfo(txtDialogTitle, customTitleTextInfo);
                useTextInfo(txtDialogTip, customContentTextInfo);
                useTextInfo(btnSelectPositive, customOkButtonTextInfo);
                
                if (dialog_background_color != -1) {
                    bkg.setBackgroundResource(dialog_background_color);
                }
                
                break;
        }
        isDialogShown = true;
        if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onShow(alertDialog);
        kongzueDialogHelper.setCancelable(isCanCancel);
    }
    
    private void useTextInfo(TextView textView, TextInfo textInfo) {
        if (textInfo.getFontSize() > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textInfo.getFontSize());
        }
        if (textInfo.getFontColor() != 1) {
            textView.setTextColor(textInfo.getFontColor());
        }
        if (textInfo.getGravity() != -1) {
            textView.setGravity(textInfo.getGravity());
        }
        textView.getPaint().setFakeBoldText(textInfo.isBold());
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
        } else {
            if (alertDialog != null && view != null) {
                customView.removeAllViews();
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
    
    public MessageDialog setTitleTextInfo(TextInfo textInfo) {
        this.customTitleTextInfo = textInfo;
        return this;
    }
    
    public MessageDialog setContentTextInfo(TextInfo textInfo) {
        this.customContentTextInfo = textInfo;
        return this;
    }
    
    public MessageDialog setOkButtonTextInfo(TextInfo textInfo) {
        this.customOkButtonTextInfo = textInfo;
        return this;
    }
    
    public MessageDialog setType(int type) {
        this.type = type;
        return this;
    }
    
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}
