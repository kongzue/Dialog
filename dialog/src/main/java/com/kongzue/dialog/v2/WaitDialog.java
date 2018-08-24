package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.util.ProgressView;

import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;
import static com.kongzue.dialog.v2.DialogSettings.tip_text_size;
import static com.kongzue.dialog.v2.DialogSettings.tip_theme;
import static com.kongzue.dialog.v2.DialogSettings.use_blur;

public class WaitDialog extends BaseDialog {
    
    private AlertDialog alertDialog;
    private static WaitDialog waitDialog;
    private boolean isCanCancel = false;
    
    private Context context;
    private String tip;
    
    private WaitDialog() {
    }
    
    public static WaitDialog show(Context context, String tip) {
        synchronized (WaitDialog.class) {
            if (waitDialog == null) waitDialog = new WaitDialog();
            waitDialog.cleanDialogLifeCycleListener();
            waitDialog.context = context;
            waitDialog.tip = tip;
            waitDialog.showDialog();
            waitDialog.log("显示等待对话框 -> " + tip);
            return waitDialog;
        }
    }
    
    private BlurView blur;
    private RelativeLayout boxInfo;
    private RelativeLayout boxBkg;
    private TextView txtInfo;
    private ProgressView progress;
    private int blur_front_color;
    
    public void showDialog() {
        
        if (waitDialog != null) {
            if (waitDialog.alertDialog != null) {
                waitDialog.alertDialog.dismiss();
            }
        }
        
        AlertDialog.Builder builder;
        int bkgResId;
        switch (tip_theme) {
            case THEME_LIGHT:
                builder = new AlertDialog.Builder(context, R.style.lightMode);
                bkgResId = R.drawable.rect_light;
                blur_front_color = Color.argb(150, 255, 255, 255);
                break;
            default:
                builder = new AlertDialog.Builder(context, R.style.darkMode);
                bkgResId = R.drawable.rect_dark;
                blur_front_color = Color.argb(200, 0, 0, 0);
                break;
        }
        builder.setCancelable(isCanCancel);
        
        alertDialog = builder.create();
        
        if (getDialogLifeCycleListener() != null)
            getDialogLifeCycleListener().onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_wait);
        
        boxInfo = window.findViewById(R.id.box_info);
        boxBkg = window.findViewById(R.id.box_bkg);
        txtInfo = window.findViewById(R.id.txt_info);
        progress = window.findViewById(R.id.progress);
        
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
        if (tip_text_size > 0) {
            txtInfo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tip_text_size);
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (boxBkg != null) boxBkg.removeAllViews();
                if (getDialogLifeCycleListener() != null) {
                    getDialogLifeCycleListener().onDismiss();
                    alertDialog = null;
                }
            }
        });
        alertDialog.show();
        if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onShow(alertDialog);
    }
    
    @Override
    public void doDismiss() {
        if (alertDialog != null) alertDialog.dismiss();
    }
    
    public WaitDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }
    
    public static void dismiss() {
        synchronized (WaitDialog.class) {
            if (waitDialog != null) {
                if (waitDialog.alertDialog != null) {
                    try {
                        waitDialog.boxBkg.removeAllViews();
                        waitDialog.alertDialog.dismiss();
                        waitDialog.context = null;
                        waitDialog = null;
                    } catch (Throwable throwable) {
                        if (DialogSettings.DEBUGMODE) throwable.printStackTrace();
                    }
                }
            }
        }
    }
}
