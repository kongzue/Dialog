package com.kongzue.dialogdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;

import java.util.Timer;
import java.util.TimerTask;

public class DismissTestActivity extends AppCompatActivity {
    
    private Button btnExit;
    private WaitDialog waitDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss_test);
        
        btnExit = findViewById(R.id.btn_exit);
        
        waitDialog = WaitDialog.show(DismissTestActivity.this, "载入中...");
        
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        runCountdown();
    }
    
    private Timer timer;
    private int sec = 3;
    
    private void runCountdown() {
        if (timer != null) timer.cancel();
        timer = new Timer();
        sec = 3;
        waitDialog.setText(sec + "");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sec--;
                        if (sec == 0) {
                            WaitDialog.dismiss();
                            TipDialog.show(DismissTestActivity.this, "完成", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                        } else {
                            waitDialog.setText(sec + "");
                        }
                    }
                });
            }
        }, 1000, 1000);
    }
}
