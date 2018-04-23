package com.kongzue.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by ZhangChao on 2017/3/28.
 */

@Deprecated
public class ProgressbarDialog extends Dialog {

    private Context context;
    private String info="";

    public ProgressbarDialog(Context context) {
        super(context, R.style.darkMode);

        // TODO Auto-generated constructor stub
        this.context = context;

        setCancelable(false);
    }

    public ProgressbarDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;

        setCancelable(false);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
        if (boxInfo!=null && txtInfo!=null) {
            if (!info.isEmpty()) {
                boxInfo.setVisibility(View.VISIBLE);
                txtInfo.setText(info);
            } else {
                boxInfo.setVisibility(View.GONE);
            }
        }
    }

    private RelativeLayout boxInfo;
    private TextView txtInfo;
    private ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_progressbar);

        boxInfo = (RelativeLayout) findViewById(R.id.box_info);
        txtInfo = (TextView) findViewById(R.id.txt_info);
        progressBar1 = (ProgressBar) findViewById(R.id.psgBar);

        if (!info.isEmpty()){
            boxInfo.setVisibility(View.VISIBLE);
            txtInfo.setText(info);
        }else{
            boxInfo.setVisibility(View.GONE);
        }
    }

}
