package com.kongzue.dialogdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kongzue.dialog.MessageDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MessageDialog.show(this,"提示","提示信息","关闭",null);
    }
}
