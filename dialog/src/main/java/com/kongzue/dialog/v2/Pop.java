package com.kongzue.dialog.v2;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;

import static com.kongzue.dialog.v2.DialogSettings.DEBUGMODE;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/17 19:58
 */
public class Pop {
    
    //支持的特殊颜色，colorType = COLOR_TYPE_NORMAL 时按照 DialogSettings.tip_theme 显示，colorType 非 COLOR_TYPE_NORMAL 时启用下列特殊颜色：
    public static final int COLOR_TYPE_NORMAL = 0;
    public static final int COLOR_TYPE_FINISH = 1;
    public static final int COLOR_TYPE_WARNING = 2;
    public static final int COLOR_TYPE_ERROR = 3;
    
    public final static int SHOW_UP = 0;
    public final static int SHOW_LEFT = 1;
    public final static int SHOW_RIGHT = 2;
    public final static int SHOW_DOWN = 3;
    
    private Context context;
    private View view;
    private int showWhere;
    private Pop pop;
    private String content;
    private int colorType;
    private PopupWindow.OnDismissListener onDismissListener;
    
    public static Pop build(Context context, View view, String content, int showWhere, int colorType) {
        synchronized (Pop.class) {
            Pop p = new Pop();
            p.pop = p;
            p.context = context;
            p.content = content;
            p.colorType = colorType;
            p.showWhere = showWhere;
            p.view = view;
            return p;
        }
    }
    
    //显示提示用
    public static Pop show(Context context, View view, String content, int showWhere, int colorType) {
        synchronized (Pop.class) {
            Pop pop = build(context, view, content, showWhere, colorType);
            pop.showPop();
            return pop;
        }
    }
    
    //快速方法
    public static Pop show(Context context, View view, String content, int showWhere) {
        return show(context, view, content, showWhere, COLOR_TYPE_NORMAL);
    }
    
    public static Pop show(Context context, View view, String content) {
        return show(context, view, content, SHOW_UP, COLOR_TYPE_NORMAL);
    }
    
    private PopupWindow popupWindow;
    
    private ImageView imgPopUp;
    private ImageView imgPopLeft;
    private RelativeLayout boxBody;
    private TextView txtPopContent;
    private ImageView imgPopRight;
    private ImageView imgPopBottom;
    
    public void showPop() {
        showPop(true);
    }
    
    public void showPop(boolean canCancel) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.layout_normal_pop, null);
        
        imgPopUp = popupView.findViewById(R.id.img_pop_up);
        imgPopLeft = popupView.findViewById(R.id.img_pop_left);
        boxBody = popupView.findViewById(R.id.box_body);
        txtPopContent = popupView.findViewById(R.id.txt_pop_content);
        imgPopRight = popupView.findViewById(R.id.img_pop_right);
        imgPopBottom = popupView.findViewById(R.id.img_pop_bottom);
        
        switch (colorType) {
            case COLOR_TYPE_NORMAL:
                if (DialogSettings.tip_theme == THEME_LIGHT) {
                    boxBody.setBackgroundResource(R.drawable.rect_pop_bkg_light);
                    imgPopUp.setImageResource(R.drawable.tri_pop_vertical_light);
                    imgPopBottom.setImageResource(R.drawable.tri_pop_vertical_light);
                    imgPopLeft.setImageResource(R.drawable.tri_pop_horizontal_light);
                    imgPopRight.setImageResource(R.drawable.tri_pop_horizontal_light);
                    txtPopContent.setTextColor(context.getResources().getColor(R.color.progress_dlg_bkg));
                }
                break;
            case COLOR_TYPE_FINISH:
                boxBody.setBackgroundResource(R.drawable.rect_pop_bkg_green);
                imgPopUp.setImageResource(R.drawable.tri_pop_vertical_green);
                imgPopBottom.setImageResource(R.drawable.tri_pop_vertical_green);
                imgPopLeft.setImageResource(R.drawable.tri_pop_horizontal_green);
                imgPopRight.setImageResource(R.drawable.tri_pop_horizontal_green);
                txtPopContent.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case COLOR_TYPE_WARNING:
                boxBody.setBackgroundResource(R.drawable.rect_pop_bkg_orange);
                imgPopUp.setImageResource(R.drawable.tri_pop_vertical_orange);
                imgPopBottom.setImageResource(R.drawable.tri_pop_vertical_orange);
                imgPopLeft.setImageResource(R.drawable.tri_pop_horizontal_orange);
                imgPopRight.setImageResource(R.drawable.tri_pop_horizontal_orange);
                txtPopContent.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case COLOR_TYPE_ERROR:
                boxBody.setBackgroundResource(R.drawable.rect_pop_bkg_red);
                imgPopUp.setImageResource(R.drawable.tri_pop_vertical_red);
                imgPopBottom.setImageResource(R.drawable.tri_pop_vertical_red);
                imgPopLeft.setImageResource(R.drawable.tri_pop_horizontal_red);
                imgPopRight.setImageResource(R.drawable.tri_pop_horizontal_red);
                txtPopContent.setTextColor(context.getResources().getColor(R.color.white));
                break;
        }
        
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(canCancel);
        
        //popupWindow.setAnimationStyle(R.style.AnimDown);
        
        popupWindow.setClippingEnabled(false);  //设置是否允许PopupWindow的范围超过屏幕范围
        
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow = null;
                pop = null;
                if (onDismissListener != null) onDismissListener.onDismiss();
            }
        });
        
        if (content != null) {
            txtPopContent.setVisibility(View.VISIBLE);
            txtPopContent.setText(content);
        }
        
        //预测量View宽高
        popupView.measure(makeDropDownMeasureSpec(popupWindow.getWidth()), makeDropDownMeasureSpec(popupWindow.getHeight()));
        int width = popupWindow.getContentView().getMeasuredWidth();
        int height = popupWindow.getContentView().getMeasuredHeight();
        
        switch (showWhere) {
            case SHOW_UP:
                imgPopBottom.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.showAsDropDown(view, view.getWidth() / 2 - width / 2, -view.getHeight() - height, Gravity.START);
                } else {
                    popupWindow.showAsDropDown(view);
                    log("当 Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT 位置才可生效");
                }
                break;
            case SHOW_DOWN:
                imgPopUp.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.showAsDropDown(view, view.getWidth() / 2 - width / 2, 0, Gravity.START);
                } else {
                    popupWindow.showAsDropDown(view);
                    log("当 Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT 位置才可生效");
                }
                break;
            case SHOW_LEFT:
                imgPopRight.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.showAsDropDown(view, -width, -view.getHeight() / 2 - height / 2, Gravity.START);
                } else {
                    popupWindow.showAsDropDown(view);
                    log("当 Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT 位置才可生效");
                }
                break;
            case SHOW_RIGHT:
                imgPopLeft.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.showAsDropDown(view, view.getWidth(), -view.getHeight() / 2 - height / 2, Gravity.START);
                } else {
                    popupWindow.showAsDropDown(view);
                    log("当 Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT 位置才可生效");
                }
                break;
            default:
                log("您需要使用 Pop.TYPE_SHOW_UP、Pop.TYPE_SHOW_LEFT、Pop.TYPE_SHOW_RIGHT 或 Pop.TYPE_SHOW_DOWN 来赋值参数“type”");
                break;
        }
    }
    
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
    
    public void setText(String c) {
        if (txtPopContent != null) {
            content = c;
            txtPopContent.setVisibility(View.VISIBLE);
            txtPopContent.setText(c);
            if (popupWindow == null) showPop(false);
        }
    }
    
    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }
    
    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }
    
    public void log(Object o) {
        if (DEBUGMODE) Log.i("DialogSDK >>>", o.toString());
    }
}
