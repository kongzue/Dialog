package com.kongzue.dialog.v2;

import android.graphics.Color;

public class DialogSettings {

    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    public static final int TYPE_MATERIAL = 0;
    public static final int TYPE_KONGZUE = 1;
    public static final int TYPE_IOS = 2;

    /*
     *  决定对话框的样式
     *  请使用 TYPE_MATERIAL、TYPE_KONGZUE、TYPE_IOS 赋值
     */
    public static int type = 0;

    /*
     *  决定对话框的模式（亮色和暗色两种）
     *  请使用 THEME_LIGHT、THEME_DARK 赋值
     */
    public static int dialog_theme = 0;

    /*
     *  决定提示框的模式（亮色和暗色两种）
     *  请使用 THEME_LIGHT、THEME_DARK 赋值
     */
    public static int tip_theme = 1;


    //文字大小设定
    //注意，此值必须大于0才生效，否则使用默认值。另外，我们使用的是dp单位，非sp单位，若有特殊需要请自行转换
    //另外，暂时不支持Material风格对话框设定字体大小
    /*
     *  决定对话框标题字样大小
     *  当值<=0时使用默认大小
     */
    public static int dialog_title_text_size = 0;
    /*
     *  决定对话框内容文字字样大小
     *  当值<=0时使用默认大小
     */
    public static int dialog_message_text_size = 0;
    /*
     *  决定输入框输入文本字样大小
     *  当值<=0时使用默认大小
     */
    public static int dialog_input_text_size = 0;
    /*
     *  决定输入框按钮字样大小
     *  当值<=0时使用默认大小
     */
    public static int dialog_button_text_size = 0;
    /*
     *  决定提示框字样大小
     *  当值<=0时使用默认大小
     */
    public static int tip_text_size = 0;

    /*
     *  决定iOS风格时，默认按钮文字颜色(Color)
     *  当值=-1时使用默认蓝色
     */
    public static int ios_normal_button_color = -1;

}
