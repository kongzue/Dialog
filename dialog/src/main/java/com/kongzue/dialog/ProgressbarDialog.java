package com.kongzue.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


/**
 * Created by ZhangChao on 2017/3/28.
 */

public class ProgressbarDialog extends Dialog {
    private Context context;

    public ProgressbarDialog(Context context) {
        super(context, R.style.processDialog);

        // TODO Auto-generated constructor stub
        this.context = context;

        setCancelable(false);
    }

    public ProgressbarDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;

        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_progressbar);
    }

}
