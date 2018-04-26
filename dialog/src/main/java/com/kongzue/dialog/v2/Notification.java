package com.kongzue.dialog.v2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialog.R;

import java.lang.reflect.Field;

public class Notification {

    public static final int SHOW_TIME_SHORT = 0;
    public static final int SHOW_TIME_LONG = 1;

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_WARNING = 2;
    public static final int TYPE_ERROR = 3;

    private int howLong = 0;
    private int type = 0;

    private static Notification notification;
    private Toast toast;

    private Context context;
    private int id;
    private String title;
    private String message;
    private int iconResId = 0;
    private Drawable iconDrawable;
    private Bitmap iconBitmap;

    private Notification() {
    }

    //Fast Function

    public static Notification show(Context context, int id, String message) {
        return show(context, id, "", message, SHOW_TIME_SHORT, SHOW_TIME_LONG);
    }

    public static Notification show(Context context, int id, String title, String message, int howLong, int type) {
        synchronized (Notification.class) {
            if (notification == null) notification = new Notification();
            notification.context = context;
            notification.id = id;
            notification.title = title;
            notification.message = message;
            notification.iconResId = 0;
            notification.iconDrawable = null;
            notification.iconBitmap = null;
            notification.howLong = howLong;
            notification.type = type;
            notification.showDialog();
            notification.log("显示通知 -> " + message);
            return notification;
        }
    }

    public static Notification show(Context context, int id, int iconResId, String title, String message, int howLong, int type) {
        synchronized (Notification.class) {
            if (notification == null) notification = new Notification();
            notification.context = context;
            notification.id = id;
            notification.title = title;
            notification.iconResId = iconResId;
            notification.iconDrawable = null;
            notification.iconBitmap = null;
            notification.howLong = howLong;
            notification.type = type;
            notification.message = message;
            notification.showDialog();
            notification.log("显示通知 -> " + message);
            return notification;
        }
    }

    public static Notification show(Context context, int id, Drawable iconDrawable, String title, String message, int howLong, int type) {
        synchronized (Notification.class) {
            if (notification == null) notification = new Notification();
            notification.context = context;
            notification.id = id;
            notification.title = title;
            notification.iconResId = 0;
            notification.iconBitmap = null;
            notification.iconDrawable = iconDrawable;
            notification.howLong = howLong;
            notification.type = type;
            notification.message = message;
            notification.showDialog();
            notification.log("显示通知 -> " + message);
            return notification;
        }
    }

    public static Notification show(Context context, int id, Bitmap iconBitmap, String title, String message, int howLong, int type) {
        synchronized (Notification.class) {
            if (notification == null) notification = new Notification();
            notification.context = context;
            notification.id = id;
            notification.title = title;
            notification.iconResId = 0;
            notification.iconDrawable = null;
            notification.iconBitmap = iconBitmap;
            notification.howLong = howLong;
            notification.type = type;
            notification.message = message;
            notification.showDialog();
            notification.log("显示通知 -> " + message);
            return notification;
        }
    }

    private LinearLayout btnNotic;
    private ImageView imgIcon;
    private TextView txtTitle;
    private TextView txtMessage;

    private void showDialog() {

        switch (DialogSettings.type) {
            case DialogSettings.TYPE_IOS:
                showIosNotification();
                break;
            case DialogSettings.TYPE_MATERIAL:
                showMaterialNotification();
                break;
            default:
                showKongzueNotification();
                break;
        }
    }

    private void showMaterialNotification() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notification_material, null);

        btnNotic = (LinearLayout) view.findViewById(R.id.btn_notic);
        imgIcon = (ImageView) view.findViewById(R.id.img_icon);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtMessage = (TextView) view.findViewById(R.id.txt_message);

        btnNotic.setPadding(dip2px(context, 15), getStatusBarHeight()+dip2px(context, 15), dip2px(context, 15), dip2px(context, 15));

        if (isNull(title)) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(title);
        }

        if (iconResId == 0 && iconDrawable == null && iconBitmap == null) {
            imgIcon.setVisibility(View.GONE);
        } else {
            imgIcon.setVisibility(View.VISIBLE);
            if (iconResId != 0) {
                imgIcon.setImageDrawable(context.getResources().getDrawable(iconResId));
            } else if (iconDrawable != null) {
                imgIcon.setImageDrawable(iconDrawable);
            } else if (iconBitmap != null) {
                imgIcon.setImageBitmap(iconBitmap);
            }
        }

        txtMessage.setText(message);
        if (isNull(title) && iconResId == 0 && iconDrawable == null && iconBitmap == null) {
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(true);
        } else {
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(false);
        }

        new kToast().show(context, view);
    }

    private void showIosNotification() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notification_ios, null);

        RelativeLayout boxBody = (RelativeLayout) view.findViewById(R.id.box_body);
        LinearLayout boxTitle = (LinearLayout) view.findViewById(R.id.box_title);
        btnNotic = (LinearLayout) view.findViewById(R.id.btn_notic);
        imgIcon = (ImageView) view.findViewById(R.id.img_icon);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtMessage = (TextView) view.findViewById(R.id.txt_message);

        boxBody.setPadding(0, getStatusBarHeight(), 0, 0);

        if (isNull(title)) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(title);
        }

        if (iconResId == 0 && iconDrawable == null && iconBitmap == null) {
            imgIcon.setVisibility(View.GONE);
        } else {
            imgIcon.setVisibility(View.VISIBLE);
            if (iconResId != 0) {
                imgIcon.setImageDrawable(context.getResources().getDrawable(iconResId));
            } else if (iconDrawable != null) {
                imgIcon.setImageDrawable(iconDrawable);
            } else if (iconBitmap != null) {
                imgIcon.setImageBitmap(iconBitmap);
            }
        }

        txtMessage.setText(message);
        if (isNull(title) && iconResId == 0 && iconDrawable == null && iconBitmap == null) {
            boxTitle.setVisibility(View.GONE);
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(true);
        } else {
            boxTitle.setVisibility(View.VISIBLE);
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(false);
        }

        new kToast().show(context, view);
    }

    private void showKongzueNotification() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notification_kongzue, null);

        btnNotic = (LinearLayout) view.findViewById(R.id.btn_notic);
        imgIcon = (ImageView) view.findViewById(R.id.img_icon);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtMessage = (TextView) view.findViewById(R.id.txt_message);

        btnNotic.setPadding(dip2px(context, 10), getStatusBarHeight(), dip2px(context, 10), 0);

        if (isNull(title)) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(title);
        }

        if (iconResId == 0 && iconDrawable == null && iconBitmap == null) {
            imgIcon.setVisibility(View.GONE);
        } else {
            imgIcon.setVisibility(View.VISIBLE);
            if (iconResId != 0) {
                imgIcon.setImageDrawable(context.getResources().getDrawable(iconResId));
            } else if (iconDrawable != null) {
                imgIcon.setImageDrawable(iconDrawable);
            } else if (iconBitmap != null) {
                imgIcon.setImageBitmap(iconBitmap);
            }
        }

        txtMessage.setText(message);
        if (isNull(title) && iconResId == 0 && iconDrawable == null && iconBitmap == null) {
            txtMessage.setGravity(Gravity.CENTER);
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(true);
        } else {
            txtMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(false);
        }

        switch (type) {
            case TYPE_NORMAL:
                btnNotic.setBackgroundResource(R.color.notification_normal);
                break;
            case TYPE_FINISH:
                btnNotic.setBackgroundResource(R.color.notification_finish);
                break;
            case TYPE_WARNING:
                btnNotic.setBackgroundResource(R.color.notification_warning);
                break;
            case TYPE_ERROR:
                btnNotic.setBackgroundResource(R.color.notification_error);
                break;
        }

        new kToast().show(context, view);
    }

    public void log(Object o) {
        Log.i("DialogSDK >>>", o.toString());
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class kToast {
        private LinearLayout btn;

        public void show(final Context context, View view) {
            if (toast != null) toast.cancel();
            toast = null;
            if (toast == null) {
                LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                btn = (LinearLayout) view.findViewById(R.id.btn_notic);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onNotificationClickListener != null)
                            onNotificationClickListener.OnClick(id);
                        if (toast != null) toast.cancel();
                    }
                });
                toast = new Toast(context.getApplicationContext());
                toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
                toast.setDuration(howLong);
                toast.setView(view);
                toast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            try {
                Object mTN;
                mTN = getField(toast, "mTN");
                if (mTN != null) {
                    Object mParams = getField(mTN, "mParams");
                    if (mParams != null && mParams instanceof WindowManager.LayoutParams) {
                        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                        params.windowAnimations = R.style.custom_toast_anim_view;
                        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            toast.show();
        }

        private Object getField(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
            Field field = object.getClass().getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(object);
            }
            return null;
        }
    }

    public boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null")) {
            return true;
        }
        return false;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private OnNotificationClickListener onNotificationClickListener;

    public OnNotificationClickListener getOnNotificationClickListener() {
        return onNotificationClickListener;
    }

    public Notification setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        this.onNotificationClickListener = onNotificationClickListener;
        return this;
    }

    public interface OnNotificationClickListener {
        void OnClick(int id);
    }
}
