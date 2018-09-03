package com.kongzue.dialog.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/3 19:07
 */
public abstract class ModalBaseDialog extends BaseDialog {
    
    protected static List<BaseDialog> modalDialogList = new ArrayList<>();         //对话框模态化队列

    protected static void showNextModalDialog(){
        Log.i("###", "showNextModalDialog: "+modalDialogList.size());
        modalDialogList.get(0).showDialog();
    }
}
