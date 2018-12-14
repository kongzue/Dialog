package com.kongzue.dialogdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;

public class DismissTestActivity extends AppCompatActivity {
    
    private Button btnExit;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss_test);
        
        btnExit = findViewById(R.id.btn_exit);
    
        WaitDialog.show(DismissTestActivity.this, "载入中...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WaitDialog.dismiss();
                TipDialog.show(DismissTestActivity.this, "完成", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
            }
        }, 3000);
    
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
