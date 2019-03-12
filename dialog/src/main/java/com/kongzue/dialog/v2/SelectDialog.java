package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
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

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.kongzue.dialog.v2.DialogSettings.*;

public class SelectDialog extends ModalBaseDialog {
    
    private SelectDialog selectDialog;
    private AlertDialog alertDialog;
    private boolean isCanCancel = false;
    private int style = -1;
    
    private Context context;
    private String title;
    private String message;
    private String okButtonCaption = "确定";
    private String cancelButtonCaption = "取消";
    private DialogInterface.OnClickListener onOkButtonClickListener;
    private DialogInterface.OnClickListener onCancelButtonClickListener;
    
    private TextInfo customTitleTextInfo;
    private TextInfo customContentTextInfo;
    private TextInfo customButtonTextInfo;
    private TextInfo customOkButtonTextInfo;
    
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
    
    private KongzueDialogHelper kongzueDialogHelper;
    
    public void showDialog() {
        if (customTitleTextInfo == null) {
            customTitleTextInfo = dialogTitleTextInfo;
        }
        if (customContentTextInfo == null) {
            customContentTextInfo = dialogContentTextInfo;
        }
        if (customButtonTextInfo == null) {
            customButtonTextInfo = dialogButtonTextInfo;
        }
        if (customOkButtonTextInfo == null) {
            if (dialogOkButtonTextInfo == null) {
                customOkButtonTextInfo = customButtonTextInfo;
            } else {
                customOkButtonTextInfo = dialogOkButtonTextInfo;
            }
        }
        
        dialogList.add(selectDialog);
        modalDialogList.remove(selectDialog);
        log("显示选择对话框 -> " + message);
        if (style == -1) style = DialogSettings.style;
        
        AlertDialog.Builder builder;
        switch (style) {
            case STYLE_IOS:
                switch (dialog_theme) {
                    case THEME_DARK:
                        builder = new AlertDialog.Builder(context, R.style.darkMode);
                        break;
                    default:
                        builder = new AlertDialog.Builder(context, R.style.lightMode);
                        break;
                }
                break;
            case STYLE_MATERIAL:
                if (dialog_theme == THEME_DARK) {
                    builder = new AlertDialog.Builder(context, R.style.materialDialogDark);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                break;
            case STYLE_KONGZUE:
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
        
        View rootView;
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        kongzueDialogHelper = new KongzueDialogHelper().setAlertDialog(alertDialog, new OnDismissListener() {
            @Override
            public void onDismiss() {
                dialogList.remove(selectDialog);
                if (bkg != null) bkg.removeAllViews();
                if (customView != null) customView.removeAllViews();
                if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onDismiss();
                isDialogShown = false;
                context = null;
                
                if (!modalDialogList.isEmpty()) {
                    showNextModalDialog();
                }
            }
        });
        
        Window window = alertDialog.getWindow();
        switch (style) {
            case STYLE_KONGZUE:
                rootView = LayoutInflater.from(context).inflate(R.layout.dialog_select, null);
                alertDialog.setView(rootView);
                kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
                
                bkg = (LinearLayout) rootView.findViewById(R.id.bkg);
                txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
                txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
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
                        kongzueDialogHelper.dismiss();
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, BUTTON_POSITIVE);
                    }
                });
                btnSelectNegative.setText(cancelButtonCaption);
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kongzueDialogHelper.dismiss();
                        if (onCancelButtonClickListener != null)
                            onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                    }
                });
                
                useTextInfo(txtDialogTitle, customTitleTextInfo);
                useTextInfo(txtDialogTip, customContentTextInfo);
                useTextInfo(btnSelectNegative, customButtonTextInfo);
                useTextInfo(btnSelectPositive, customOkButtonTextInfo);
                
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
            case STYLE_MATERIAL:
                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                alertDialog.setButton(BUTTON_POSITIVE, okButtonCaption, onOkButtonClickListener);
                alertDialog.setButton(BUTTON_NEGATIVE, cancelButtonCaption, onCancelButtonClickListener);
                if (dialog_background_color != -1) {
                    alertDialog.getWindow().getDecorView().setBackgroundResource(dialog_background_color);
                }
                
                if (customView != null) alertDialog.setView(customView);
                
                kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
                break;
            case STYLE_IOS:
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
                        kongzueDialogHelper.dismiss();
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, BUTTON_POSITIVE);
                    }
                });
                btnSelectNegative.setVisibility(View.VISIBLE);
                btnSelectNegative.setText(cancelButtonCaption);
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kongzueDialogHelper.dismiss();
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
                    blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
                } else {
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
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
                useTextInfo(btnSelectNegative, customButtonTextInfo);
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
        Typeface font = Typeface.create(Typeface.SANS_SERIF, textInfo.isBold()?Typeface.BOLD:Typeface.NORMAL);
        textView.setTypeface(font);
    }
    
    @Override
    public void doDismiss() {
        if (kongzueDialogHelper != null) kongzueDialogHelper.dismiss();
    }
    
    public SelectDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (kongzueDialogHelper != null) kongzueDialogHelper.setCancelable(canCancel);
        return this;
    }
    
    public SelectDialog setCustomView(View view) {
        if (style == STYLE_MATERIAL) {
            customView = new RelativeLayout(context);
            customView.addView(view);
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
    
    public SelectDialog setTitleTextInfo(TextInfo textInfo) {
        this.customTitleTextInfo = textInfo;
        return this;
    }
    
    public SelectDialog setContentTextInfo(TextInfo textInfo) {
        this.customContentTextInfo = textInfo;
        return this;
    }
    
    public SelectDialog setButtonTextInfo(TextInfo textInfo) {
        this.customButtonTextInfo = textInfo;
        return this;
    }
    
    public SelectDialog setOkButtonTextInfo(TextInfo textInfo) {
        this.customOkButtonTextInfo = textInfo;
        return this;
    }
    
    public SelectDialog setDialogStyle(int style) {
        this.style = style;
        return this;
    }
    
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}
