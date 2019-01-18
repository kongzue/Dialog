package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.kongzue.dialog.listener.OnBackPressListener;
import com.kongzue.dialog.listener.OnDismissListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.util.KongzueDialogHelper;
import com.kongzue.dialog.util.ProgressView;
import com.kongzue.dialog.util.TextInfo;

import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;
import static com.kongzue.dialog.v2.DialogSettings.blur_alpha;
import static com.kongzue.dialog.v2.DialogSettings.tipTextInfo;
import static com.kongzue.dialog.v2.DialogSettings.tip_theme;
import static com.kongzue.dialog.v2.DialogSettings.use_blur;

public class WaitDialog extends BaseDialog {
    
    private OnBackPressListener onBackPressListener;
    private AlertDialog alertDialog;
    private WaitDialog waitDialog;
    private static boolean isCanCancel = false;
    
    private View customView;
    private TextInfo customTextInfo;
    
    private Context context;
    private String tip;
    
    private WaitDialog() {
    }
    
    public static WaitDialog show(Context context, String tip) {
        return show(context, tip, null, null, null);
    }
    
    public static WaitDialog show(Context context, String tip, DialogLifeCycleListener lifeCycleListener) {
        return show(context, tip, null, null, lifeCycleListener);
    }
    
    public static WaitDialog show(Context context, String tip, View customView) {
        return show(context, tip, customView, null, null);
    }
    
    public static WaitDialog show(Context context, String tip, View customView, DialogLifeCycleListener lifeCycleListener) {
        return show(context, tip, customView, null, lifeCycleListener);
    }
    
    public static WaitDialog show(Context context, String tip, TextInfo textInfo) {
        return show(context, tip, null, textInfo, null);
    }
    
    public static WaitDialog show(Context context, String tip, TextInfo textInfo, DialogLifeCycleListener lifeCycleListener) {
        return show(context, tip, null, textInfo, lifeCycleListener);
    }
    
    public static WaitDialog show(Context context, String tip, View customView, TextInfo textInfo) {
        return show(context, tip, customView, textInfo, null);
    }
    
    public static WaitDialog show(Context context, String tip, View customView, TextInfo textInfo, DialogLifeCycleListener lifeCycleListener) {
        synchronized (WaitDialog.class) {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.cleanDialogLifeCycleListener();
            waitDialog.context = context;
            waitDialog.tip = tip;
            waitDialog.log("装载等待对话框 -> " + tip);
            waitDialog.waitDialog = waitDialog;
            waitDialog.customView = customView;
            waitDialog.customTextInfo = textInfo;
            waitDialog.setDialogLifeCycleListener(lifeCycleListener);
            waitDialog.showDialog();
            return waitDialog;
        }
    }
    
    private BlurView blur;
    private int blur_front_color;
    private int font_color;
    
    private RelativeLayout boxInfo;
    private RelativeLayout boxBkg;
    private RelativeLayout boxProgress;
    private ProgressView progress;
    private TextView txtInfo;
    
    public void showDialog() {
        if (customTextInfo == null) {
            customTextInfo = tipTextInfo;
        }
        
        dialogList.add(waitDialog);
        log("显示等待对话框 -> " + tip);
        
        AlertDialog.Builder builder;
        int bkgResId;
        switch (tip_theme) {
            case THEME_LIGHT:
                builder = new AlertDialog.Builder(context, R.style.lightMode);
                bkgResId = R.drawable.rect_light;
                blur_front_color = Color.argb(blur_alpha - 50, 255, 255, 255);
                font_color = Color.rgb(0, 0, 0);
                break;
            default:
                builder = new AlertDialog.Builder(context, R.style.darkMode);
                bkgResId = R.drawable.rect_dark;
                blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
                font_color = Color.rgb(255, 255, 255);
                break;
        }
        
        alertDialog = builder.create();
        
        if (getDialogLifeCycleListener() != null)
            getDialogLifeCycleListener().onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        KongzueDialogHelper kongzueDialogHelper = new KongzueDialogHelper().setAlertDialog(alertDialog, new OnDismissListener() {
            @Override
            public void onDismiss() {
                dialogList.remove(waitDialog);
                if (boxProgress != null) boxProgress.removeAllViews();
                if (boxBkg != null) boxBkg.removeAllViews();
                if (getDialogLifeCycleListener() != null) {
                    getDialogLifeCycleListener().onDismiss();
                    alertDialog = null;
                }
            }
        });
        
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_wait, null);
        alertDialog.setView(rootView);
        
        boxInfo = rootView.findViewById(R.id.box_info);
        boxBkg = rootView.findViewById(R.id.box_bkg);
        boxProgress = rootView.findViewById(R.id.box_progress);
        progress = rootView.findViewById(R.id.progress);
        txtInfo = rootView.findViewById(R.id.txt_info);
        
        txtInfo.setTextColor(font_color);
        
        if (customView != null) {
            progress.setVisibility(View.GONE);
            boxProgress.removeAllViews();
            boxProgress.addView(customView);
        }
        
        if (tip_theme == THEME_LIGHT) {
            progress.setStrokeColors(new int[]{Color.rgb(0, 0, 0)});
        } else {
            progress.setStrokeColors(new int[]{Color.rgb(255, 255, 255)});
        }
        
        if (use_blur) {
            blur = new BlurView(context, null);
            boxBkg.post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    blur.setLayoutParams(params);
                    blur.setOverlayColor(blur_front_color);
                    
                    ViewGroup.LayoutParams boxBkgLayoutParams = boxBkg.getLayoutParams();
                    boxBkgLayoutParams.width = boxInfo.getWidth();
                    boxBkgLayoutParams.height = boxInfo.getHeight();
                    boxBkg.setLayoutParams(boxBkgLayoutParams);
                    
                    boxBkg.addView(blur, 0, params);
                }
            });
        } else {
            boxBkg.setBackgroundResource(bkgResId);
        }
        
        if (!tip.isEmpty()) {
            boxInfo.setVisibility(View.VISIBLE);
            txtInfo.setText(tip);
        } else {
            boxInfo.setVisibility(View.GONE);
        }
        
        if (customTextInfo.getFontSize() > 0) {
            txtInfo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, customTextInfo.getFontSize());
        }
        if (customTextInfo.getFontColor() != 1) {
            txtInfo.setTextColor(customTextInfo.getFontColor());
        }
        if (customTextInfo.getGravity() != -1) {
            txtInfo.setGravity(customTextInfo.getGravity());
        }
        
        Typeface font = Typeface.create(Typeface.SANS_SERIF, customTextInfo.isBold() ? Typeface.BOLD : Typeface.NORMAL);
        txtInfo.setTypeface(font);
        
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (onBackPressListener != null) {
                            onBackPressListener.OnBackPress(alertDialog);
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        
        if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onShow(alertDialog);
        
        kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
        kongzueDialogHelper.setCancelable(isCanCancel);
    }
    
    @Override
    public void doDismiss() {
        if (alertDialog != null) alertDialog.dismiss();
    }
    
    public WaitDialog setCanCancel(boolean canCancel) {
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }
    
    public static void setCanCancelGlobal(boolean canCancel) {
        isCanCancel = canCancel;
    }
    
    public WaitDialog setOnBackPressListener(OnBackPressListener onBackPressListener) {
        this.onBackPressListener = onBackPressListener;
        return this;
    }
    
    public static void dismiss() {
        for (BaseDialog dialog : dialogList) {
            if (dialog instanceof WaitDialog) {
                dialog.doDismiss();
            }
        }
    }
    
    public void setText(String s) {
        if (waitDialog != null && waitDialog.txtInfo != null) {
            waitDialog.txtInfo.setText(s);
        }
    }
    
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}
