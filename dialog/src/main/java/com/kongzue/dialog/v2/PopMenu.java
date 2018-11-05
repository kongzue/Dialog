package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kongzue.dialog.R;

import static com.kongzue.dialog.v2.DialogSettings.DEBUGMODE;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/11/3 23:28
 */
public class PopMenu {
    
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
    private PopMenu popMenu;
    private String content;
    private int colorType;
    private PopupWindow.OnDismissListener onDismissListener;
    
    public static PopMenu build(Context context, View view, String content, int showWhere, int colorType) {
        synchronized (PopMenu.class) {
            PopMenu p = new PopMenu();
            p.popMenu = p;
            p.context = context;
            p.content = content;
            p.colorType = colorType;
            p.showWhere = showWhere;
            p.view = view;
            return p;
        }
    }
    
    //显示提示用
    public static PopMenu show(Context context, View view, String content, int showWhere, int colorType) {
        synchronized (PopMenu.class) {
            PopMenu popMenu = build(context, view, content, showWhere, colorType);
            popMenu.showPop();
            return popMenu;
        }
    }
    
    //快速方法
    public static PopMenu show(Context context, View view, String content, int showWhere) {
        return show(context, view, content, showWhere, COLOR_TYPE_NORMAL);
    }
    
    public static PopMenu show(Context context, View view, String content) {
        return show(context, view, content, SHOW_UP, COLOR_TYPE_NORMAL);
    }
    
    private PopupWindow popupWindow;
    private View popupView;
    
    private ImageView imgPopUp;
    private ImageView imgPopLeft;
    private LinearLayout boxBody;
    private TextView txtPopContent;
    private ImageView imgPopRight;
    private ImageView imgPopBottom;
    
    private int setLeft = 0;
    
    public void showPop() {
        showPop(true);
    }
    
    private int beforeTextViewRenderLintCount = 1;        //文本渲染前的行数
    
    public void showPop(boolean canCancel) {
        popupView = LayoutInflater.from(context).inflate(R.layout.layout_normal_pop, null);
        
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
        
        popupWindow.setClippingEnabled(true);  //设置是否允许PopupWindow的范围超过屏幕范围
        
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow = null;
                popMenu = null;
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
        
        beforeTextViewRenderLintCount = txtPopContent.getLineCount();
        
        popupView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    int[] viewOnscreen = new int[2];
                    view.getLocationOnScreen(viewOnscreen);
                    
                    int width = popupView.getMeasuredWidth();
                    int height = popupView.getMeasuredHeight();
                    switch (showWhere) {
                        case SHOW_UP:
                            setLeft = view.getWidth() / 2 - width / 2;
                            popupWindow.update(viewOnscreen[0]+setLeft, viewOnscreen[1]+view.getTop() - height + dp2px(10), width, height);
                            break;
                        case SHOW_DOWN:
                            setLeft = view.getWidth() / 2 - width / 2;
                            break;
                    }
                    
                    int[] outLocation = new int[2];
                    popupWindow.getContentView().getLocationOnScreen(outLocation);
                    
                    int realLeft = outLocation[0];
                    int needLeft = view.getLeft() + setLeft;
                    
                    int setX = (needLeft - realLeft) + popupWindow.getContentView().getWidth() / 2 + dp2px(2);
                    
                    imgPopBottom.setX(setX);
                    imgPopUp.setX(setX);
                } catch (Exception e) {
                }
            }
        });
        
        switch (showWhere) {
            case SHOW_UP:
                imgPopBottom.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setLeft = view.getWidth() / 2 - width / 2;
                    popupWindow.showAsDropDown(view, setLeft, -view.getHeight() - height, Gravity.START);
                } else {
                    popupWindow.showAsDropDown(view);
                    log("当 Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT 位置才可生效");
                }
                break;
            case SHOW_DOWN:
                imgPopUp.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setLeft = view.getWidth() / 2 - width / 2;
                    popupWindow.showAsDropDown(view, setLeft, -dp2px(10), Gravity.START);
                } else {
                    popupWindow.showAsDropDown(view);
                    log("当 Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT 位置才可生效");
                }
                break;
            case SHOW_LEFT:
                int maxLeftWidth = view.getLeft() - dp2px(20);
                if (maxLeftWidth < 0) maxLeftWidth = 0;
                txtPopContent.setMaxWidth(maxLeftWidth);
                
                popupView.measure(makeDropDownMeasureSpec(popupWindow.getWidth()), makeDropDownMeasureSpec(popupWindow.getHeight()));
                width = popupWindow.getContentView().getMeasuredWidth();
                height = popupWindow.getContentView().getMeasuredHeight();
                
                imgPopRight.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setLeft = -width - dp2px(5);
                    popupWindow.showAsDropDown(view, setLeft, -view.getHeight() / 2 - height / 2, Gravity.START);
                } else {
                    popupWindow.showAsDropDown(view);
                    log("当 Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT 位置才可生效");
                }
                break;
            case SHOW_RIGHT:
                int maxRightWidth = getDisplayWidth() - (view.getLeft() + view.getWidth()) - dp2px(50);
                if (maxRightWidth < 0) maxRightWidth = 0;
                txtPopContent.setMaxWidth(maxRightWidth);
                
                popupView.measure(makeDropDownMeasureSpec(popupWindow.getWidth()), makeDropDownMeasureSpec(popupWindow.getHeight()));
                height = popupWindow.getContentView().getMeasuredHeight();
                
                imgPopLeft.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setLeft = view.getWidth();
                    popupWindow.showAsDropDown(view, setLeft, -view.getHeight() / 2 - height / 2, Gravity.START);
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
    
    public int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
    
    public float px2dp(int pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }
    
    public int getDisplayWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        return outP.x;
    }
    
}
