package com.kongzue.dialogdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongzue.dialog.v2.TipDialog;

public class JumpTestActivity extends AppCompatActivity {

    private Button justDoIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_test);
        justDoIt = (Button) findViewById(R.id.justDoIt);

        justDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.show(JumpTestActivity.this, "提示！", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_WARNING);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);
            }
        });
    }
}
