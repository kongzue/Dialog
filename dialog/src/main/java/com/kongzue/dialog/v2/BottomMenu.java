package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.BlurView;

import java.util.List;

import static com.kongzue.dialog.v2.DialogSettings.*;

public class BottomMenu extends BaseDialog {

    private List<String> menuText;
    private static BottomMenu bottomMenu;
    private AlertDialog alertDialog;
    private AppCompatActivity activity;
    private boolean isShowCancelButton = false;
    private OnMenuItemClickListener onMenuItemClickListener;

    private BottomMenu() {
    }

    //Fast Function
    public static BottomMenu show(AppCompatActivity activity, List<String> menuText) {
        return show(activity, menuText, null, true);
    }

    public static BottomMenu show(AppCompatActivity activity, List<String> menuText, OnMenuItemClickListener onMenuItemClickListener) {
        return show(activity, menuText, onMenuItemClickListener, true);
    }

    public static BottomMenu show(AppCompatActivity activity, List<String> menuText, OnMenuItemClickListener onMenuItemClickListener, boolean isShowCancelButton) {
        synchronized (BottomMenu.class) {
            if (bottomMenu == null) bottomMenu = new BottomMenu();
            bottomMenu.activity = activity;
            bottomMenu.menuText = menuText;
            bottomMenu.isShowCancelButton = isShowCancelButton;
            bottomMenu.onMenuItemClickListener = onMenuItemClickListener;
            if (menuText.isEmpty()) {
                bottomMenu.log("未启动底部菜单 -> 没有可显示的内容");
                return bottomMenu;
            }
            bottomMenu.showDialog();
            return bottomMenu;
        }
    }

    private MyBottomSheetDialog bottomSheetDialog;

    private ListView listMenu;
    private TextView btnCancel;
    private ViewGroup boxCancel;

    private BlurView blurList;
    private BlurView blurCancel;

    private RelativeLayout boxList;

    @Override
    public void showDialog() {
        log("启动底部菜单 -> " + menuText.toString());
        if (type == TYPE_MATERIAL) {
            bottomSheetDialog = new MyBottomSheetDialog(activity);
            View box_view = LayoutInflater.from(activity).inflate(R.layout.bottom_menu_material, null);

            listMenu = box_view.findViewById(R.id.list_menu);
            btnCancel = box_view.findViewById(R.id.btn_cancel);

            ArrayAdapter arrayAdapter = new ArrayAdapter(activity, R.layout.item_bottom_menu_material, menuText);
            listMenu.setAdapter(arrayAdapter);

            listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener.onClick(menuText.get(position), position);
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            bottomSheetDialog.setContentView(box_view);
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.show();
        } else {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(activity, R.style.bottom_menu);
            builder.setCancelable(true);
            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = display.getWidth();
            window.setGravity(Gravity.BOTTOM);
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.bottomMenuAnimStyle);

            int resId = R.layout.bottom_menu_kongzue;
            int item_resId = R.layout.item_bottom_menu_kongzue;
            switch (type) {
                case TYPE_KONGZUE:
                    resId = R.layout.bottom_menu_kongzue;
                    item_resId = R.layout.item_bottom_menu_kongzue;
                    break;
                case TYPE_IOS:
                    resId = R.layout.bottom_menu_ios;
                    item_resId = R.layout.item_bottom_menu_ios;
                    break;
            }
            window.setContentView(resId);

            listMenu = window.findViewById(R.id.list_menu);
            btnCancel = window.findViewById(R.id.btn_cancel);
            switch (type) {
                case TYPE_KONGZUE:
                    boxCancel = (LinearLayout) window.findViewById(R.id.box_cancel);
                    break;
                case TYPE_IOS:
                    boxList = window.findViewById(R.id.box_list);
                    boxCancel = (RelativeLayout) window.findViewById(R.id.box_cancel);
                    if (use_blur) {
                        boxList.post(new Runnable() {
                            @Override
                            public void run() {
                                blurList = new BlurView(activity, null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxList.getHeight());
                                blurList.setOverlayColor(Color.argb(185, 255, 255, 255));
                                blurList.setRadius(activity, 11, 11);
                                boxList.addView(blurList, 0, params);
                            }
                        });
                        boxCancel.post(new Runnable() {
                            @Override
                            public void run() {
                                blurCancel = new BlurView(activity, null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxCancel.getHeight());
                                blurCancel.setOverlayColor(Color.argb(185, 255, 255, 255));
                                blurCancel.setRadius(activity, 11, 11);
                                boxCancel.addView(blurCancel, 0, params);
                            }
                        });
                    } else {
                        boxList.setBackgroundResource(R.drawable.rect_button_bottom_menu_ios);
                        boxCancel.setBackgroundResource(R.drawable.rect_button_bottom_menu_ios);
                    }
                    break;
            }

            if (isShowCancelButton) {
                if (boxCancel != null) boxCancel.setVisibility(View.VISIBLE);
            } else {
                if (boxCancel != null) boxCancel.setVisibility(View.GONE);
            }

            switch (type) {
                case TYPE_KONGZUE:
                    ArrayAdapter arrayAdapter = new ArrayAdapter(activity, item_resId, menuText);
                    listMenu.setAdapter(arrayAdapter);
                    break;
                case TYPE_IOS:
                    if (ios_normal_button_color != -1) {
                        btnCancel.setTextColor(ios_normal_button_color);
                    }
                    MenuArrayAdapter menuArrayAdapter = new MenuArrayAdapter(activity, item_resId, menuText);
                    listMenu.setAdapter(menuArrayAdapter);
                    break;
            }

            listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener.onClick(menuText.get(position), position);
                    alertDialog.dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    class MenuArrayAdapter extends ArrayAdapter {

        private int resoureId;
        private List<String> objects;
        private Context context;

        public MenuArrayAdapter(Context context, int resourceId, List<String> objects) {
            super(context, resourceId, objects);
            this.objects = objects;
            this.resoureId = resourceId;
            this.context = context;
        }

        private class ViewHolder {
            TextView textView;
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public String getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(resoureId, null);
                viewHolder.textView = convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String text = objects.get(position);
            if (null != text) {
                viewHolder.textView.setText(text);
                if (ios_normal_button_color != -1) {
                    viewHolder.textView.setTextColor(ios_normal_button_color);
                }
                if (objects.size() == 1) {
                    viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_all);
                } else {
                    if (position == 0) {
                        viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_top);
                    } else if (position == objects.size() - 1) {
                        viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_bottom);
                    } else {
                        viewHolder.textView.setBackgroundResource(R.drawable.button_menu_ios_middle);
                    }
                }
            }

            return convertView;
        }
    }

    class MyBottomSheetDialog extends BottomSheetDialog {

        public MyBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public MyBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
            super(context, theme);
        }

        protected MyBottomSheetDialog(@NonNull Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            int screenHeight = getScreenHeight(getContext());
            int statusBarHeight = getStatusBarHeight(getContext());
            int dialogHeight = screenHeight;//- statusBarHeight;
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }

        public int getStatusBarHeight(Context context) {
            int result = 0;
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        public int getScreenHeight(Context context) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            return height;
        }
    }

}
