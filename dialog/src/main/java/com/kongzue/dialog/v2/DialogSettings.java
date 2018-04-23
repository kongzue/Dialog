package com.kongzue.dialog.v2;

public class DialogSettings {

    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    public static final int TYPE_MATERIAL = 0;
    public static final int TYPE_KONGZUE = 1;
    public static final int TYPE_IOS = 2;

    /*
     *  决定对话框的样式
     *  请使用 TYPE_MATERIAL、TYPE_KONGZUE、TYPE_IOS 赋值，
     */
    public static int type = 0;

    /*
     *  决定对话框的模式（亮色和暗色两种）
     *  请使用 THEME_LIGHT、THEME_DARK 赋值，
     */
    public static int dialog_theme = 0;

    /*
     *  决定提示框的模式（亮色和暗色两种）
     *  请使用 THEME_LIGHT、THEME_DARK 赋值，
     */
    public static int tip_theme = 1;

}
