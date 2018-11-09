package com.kongzue.dialog.util;

import android.text.InputType;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/11/8 21:41
 */
public class InputInfo {
    
    private int MAX_LENGTH;
    private int inputType;          //类型详见 android.text.InputType
    
    public int getMAX_LENGTH() {
        return MAX_LENGTH;
    }
    
    public InputInfo setMAX_LENGTH(int MAX_LENGTH) {
        this.MAX_LENGTH = MAX_LENGTH;
        return this;
    }
    
    public int getInputType() {
        return inputType;
    }
    
    /**
     * 文本输入类型
     * {@link InputType}.
     * @see InputType
     */
    public InputInfo setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }
}
