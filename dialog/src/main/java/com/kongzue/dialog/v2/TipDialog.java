package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.util.TextInfo;

import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;
import static com.kongzue.dialog.v2.DialogSettings.blur_alpha;
import static com.kongzue.dialog.v2.DialogSettings.tipTextInfo;
import static com.kongzue.dialog.v2.DialogSettings.tip_theme;
import static com.kongzue.dialog.v2.DialogSettings.use_blur;

public class TipDialog extends BaseDialog {
    
    public static final int SHOW_TIME_SHORT = 0;
    public static final int SHOW_TIME_LONG = 1;
    
    public static final int TYPE_CUSTOM_DRAWABLE = -2;
    public static final int TYPE_CUSTOM_BITMAP = -1;
    public static final int TYPE_WARNING = 0;
    public static final int TYPE_ERROR = 1;
    public static final int TYPE_FINISH = 2;
    
    private AlertDialog alertDialog;
    private TipDialog tipDialog;
    private Drawable customDrawable;
    private Bitmap customBitmap;
    private boolean isCanCancel = false;
    
    private TextInfo customTextInfo;
    
    private Context context;
    private String tip;
    
    private TipDialog() {
    }
    
    private int howLong = 0;
    private int type = 0;
    
    //Fast Function
    public static TipDialog show(Context context, String tip) {
        TipDialog tipDialog = build(context, tip, SHOW_TIME_SHORT, TYPE_WARNING);
        tipDialog.showDialog();
        return tipDialog;
    }
    
    public static TipDialog show(Context context, String tip, int type) {
        TipDialog tipDialog = build(context, tip, SHOW_TIME_SHORT, type);
        tipDialog.showDialog();
        return tipDialog;
    }
    
    public static TipDialog show(Context context, String tip, int howLong, int type) {
        TipDialog tipDialog = build(context, tip, howLong, type);
        tipDialog.showDialog();
        return tipDialog;
    }
    
    public static TipDialog build(Context context, String tip, int howLong, int type) {
        synchronized (TipDialog.class) {
            TipDialog tipDialog = new TipDialog();
            tipDialog.cleanDialogLifeCycleListener();
            tipDialog.context = context;
            tipDialog.tip = tip;
            tipDialog.howLong = howLong;
            tipDialog.type = type;
            tipDialog.log("装载提示对话框 -> " + tip);
            tipDialog.tipDialog = tipDialog;
            return tipDialog;
        }
    }
    
    public static TipDialog show(Context context, String tip, int howLong, Drawable customDrawable) {
        TipDialog tipDialog = build(context, tip, howLong, customDrawable);
        tipDialog.showDialog();
        return tipDialog;
    }
    
    public static TipDialog build(Context context, String tip, int howLong, Drawable customDrawable) {
        synchronized (TipDialog.class) {
            TipDialog tipDialog = new TipDialog();
            tipDialog.cleanDialogLifeCycleListener();
            tipDialog.context = context;
            tipDialog.tip = tip;
            tipDialog.customDrawable = customDrawable;
            tipDialog.howLong = howLong;
            tipDialog.type = TYPE_CUSTOM_DRAWABLE;
            tipDialog.log("装载提示对话框 -> " + tip);
            tipDialog.tipDialog = tipDialog;
            return tipDialog;
        }
    }
    
    public static TipDialog show(Context context, String tip, int howLong, Bitmap customBitmap) {
        TipDialog tipDialog = build(context, tip, howLong, customBitmap);
        tipDialog.showDialog();
        return tipDialog;
    }
    
    public static TipDialog build(Context context, String tip, int howLong, Bitmap customBitmap) {
        synchronized (TipDialog.class) {
            TipDialog tipDialog = new TipDialog();
            tipDialog.cleanDialogLifeCycleListener();
            tipDialog.context = context;
            tipDialog.tip = tip;
            tipDialog.customBitmap = customBitmap;
            tipDialog.howLong = howLong;
            tipDialog.type = TYPE_CUSTOM_BITMAP;
            tipDialog.log("装载提示对话框 -> " + tip);
            tipDialog.tipDialog = tipDialog;
            return tipDialog;
        }
    }
    
    private RelativeLayout boxInfo;
    private RelativeLayout boxBkg;
    private ImageView image;
    private TextView txtInfo;
    
    private BlurView blur;
    private int blur_front_color;
    
    public void showDialog() {
        if (customTextInfo == null) {
            customTextInfo = tipTextInfo;
        }
        
        AlertDialog.Builder builder;
        log("显示提示对话框 -> " + tip);
        dialogList.add(tipDialog);
        
        int bkgResId;
        switch (tip_theme) {
            case THEME_LIGHT:
                builder = new AlertDialog.Builder(context, R.style.lightMode);
                bkgResId = R.drawable.rect_light;
                blur_front_color = Color.argb(blur_alpha - 50, 255, 255, 255);
                break;
            default:
                builder = new AlertDialog.Builder(context, R.style.darkMode);
                bkgResId = R.drawable.rect_dark;
                blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
                break;
        }
        builder.setCancelable(isCanCancel);
        
        alertDialog = builder.create();
        if (getDialogLifeCycleListener() != null)
            getDialogLifeCycleListener().onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_tip);
        
        boxInfo = window.findViewById(R.id.box_info);
        boxBkg = window.findViewById(R.id.box_bkg);
        image = window.findViewById(R.id.image);
        txtInfo = window.findViewById(R.id.txt_info);
        
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
            boxInfo.setBackgroundResource(bkgResId);
        }
        
        switch (type) {
            case TYPE_WARNING:
                if (tip_theme == THEME_LIGHT) {
                    image.setImageResource(R.mipmap.img_warning_dark);
                } else {
                    image.setImageResource(R.mipmap.img_warning);
                }
                break;
            case TYPE_ERROR:
                if (tip_theme == THEME_LIGHT) {
                    image.setImageResource(R.mipmap.img_error_dark);
                } else {
                    image.setImageResource(R.mipmap.img_error);
                }
                break;
            case TYPE_FINISH:
                if (tip_theme == THEME_LIGHT) {
                    image.setImageResource(R.mipmap.img_finish_dark);
                } else {
                    image.setImageResource(R.mipmap.img_finish);
                }
                break;
            case TYPE_CUSTOM_BITMAP:
                image.setImageBitmap(customBitmap);
                break;
            case TYPE_CUSTOM_DRAWABLE:
                image.setImageDrawable(customDrawable);
                break;
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
        if (customTextInfo.getFontColor() != -1) {
            txtInfo.setTextColor(customTextInfo.getFontColor());
        }
        if (customTextInfo.getGravity() != -1) {
            txtInfo.setGravity(customTextInfo.getGravity());
        }
        txtInfo.getPaint().setFakeBoldText(customTextInfo.isBold());
        
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogList.remove(tipDialog);
                if (boxBkg != null) boxBkg.removeAllViews();
                if (getDialogLifeCycleListener() != null) {
                    getDialogLifeCycleListener().onDismiss();
                }
            }
        });
        alertDialog.show();
        if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onShow(alertDialog);
        
        int time = 1500;
        switch (howLong) {
            case SHOW_TIME_SHORT:
                time = 1500;
                break;
            case SHOW_TIME_LONG:
                time = 3000;
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog != null) alertDialog.dismiss();
            }
        }, time);
    }
    
    @Override
    public void doDismiss() {
        if (alertDialog != null) alertDialog.dismiss();
    }
    
    public TipDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }
    
    public TipDialog setTxtInfo(TextView txtInfo) {
        this.txtInfo = txtInfo;
        return this;
    }
}
