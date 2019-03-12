package com.kongzue.dialog.v2;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;

import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.TextInfo;

public class DialogSettings {
    
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    
    @Deprecated
    public static final int TYPE_MATERIAL = 0;
    @Deprecated
    public static final int TYPE_KONGZUE = 1;
    @Deprecated
    public static final int TYPE_IOS = 2;
    
    public static final int STYLE_MATERIAL = 0;
    public static final int STYLE_KONGZUE = 1;
    public static final int STYLE_IOS = 2;
    
    //是否打印日志
    public static boolean DEBUGMODE = true;
    
    //此方法用于关闭所有已加载的 Dialog，防止 WindowLeaked 错误，建议将它加在你的 Activity 的 onDestroy() 里调用
    @Deprecated
    public static void unloadAllDialog() {
        BaseDialog.unloadAllDialog();
    }
    
    //决定等待框、提示框以及iOS风格的对话框是否启用模糊背景
    public static boolean use_blur = true;
    
    //决定等待框、提示框以及iOS风格的对话框的模糊背景透明度（50-255）
    public static int blur_alpha = 200;
    
    //决定对话框的默认样式，请使用 STYLE_MATERIAL、STYLE_KONGZUE、STYLE_IOS 赋值
    public static int style = 0;
    
    //决定对话框的模式（亮色和暗色两种），请使用 THEME_LIGHT、THEME_DARK 赋值
    public static int dialog_theme = 0;
    
    //决定对话框的默认背景色
    public static int dialog_background_color = -1;
    
    //决定提示框的模式（亮色和暗色两种），请使用 THEME_LIGHT、THEME_DARK 赋值
    public static int tip_theme = 1;
    
    /*
     *  文字大小设定
     *  注意，此值必须大于0才生效，否则使用默认值。另外，我们使用的是dp单位，非sp单位，若有特殊需要请自行转换
     *  另外，暂时不支持Material风格对话框设定字体大小
     */
    
    //决定对话框标题文字样式
    public static TextInfo dialogTitleTextInfo = new TextInfo();
    
    //决定对话框内容文字样式
    public static TextInfo dialogContentTextInfo = new TextInfo();
    
    //决定对话框按钮文字样式
    public static TextInfo dialogButtonTextInfo = new TextInfo();
    
    //决定对话框积极按钮（一般为确定按钮）文字样式，若未设置此样式则会使用 dialogButtonTextInfo 代替
    public static TextInfo dialogOkButtonTextInfo;
    
    //决定提示框文本样式
    public static TextInfo tipTextInfo = new TextInfo();
    
    //决定菜单文字样式
    public static TextInfo menuTextInfo = new TextInfo();
    
    //决定 Notification 默认文字样式信息
    public static TextInfo notificationTextInfo = new TextInfo();
    
    //决定输入框输入文本字样大小（单位：dp），当值<=0时使用默认大小
    public static int dialog_input_text_size = 0;
    
    //决定对话框组件默认是否可点击遮罩区域关闭
    public static boolean dialog_cancelable_default = false;
    
//    @Deprecated
//    public static int notification_text_size = -1;
//
//    @Deprecated
//    public static int notification_text_color = -1;
//
//    @Deprecated
//    public static int ios_normal_button_color = -1;
//
//    @Deprecated
//    public static int ios_normal_ok_button_color = -1;
//
//    @Deprecated
//    public static int dialog_button_text_size = 0;
//
//    @Deprecated
//    public static int tip_text_size = 0;
//
//    @Deprecated
//    public static int dialog_menu_text_size = 0;
//
//    @Deprecated
//    public static int dialog_title_text_size = 0;
//
//    @Deprecated
//    public static int dialog_message_text_size = 0;
}
