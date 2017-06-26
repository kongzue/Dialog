package com.kongzue.dialog.util;

import com.kongzue.dialog.R;

/**
 * Created by ZhangChao on 2017/6/2.
 */

public class DialogThemeColor {

    public static int normalColor = 0;

    public static int COLOR_GREEN = 0;
    public static int COLOR_BLUE = 1;
    public static int COLOR_ORANGE = 2;
    public static int COLOR_GRAY = 3;

    public static int getRes(int colorId){
        switch (colorId){
            case 0:
                return R.drawable.button_selectordialog_green;
            case 1:
                return R.drawable.button_selectordialog_blue;
            case 2:
                return R.drawable.button_selectordialog_orange;
            case 3:
                return R.drawable.button_selectordialog_gray;
            default:
                return R.drawable.button_selectordialog_green;
        }
    }
}
