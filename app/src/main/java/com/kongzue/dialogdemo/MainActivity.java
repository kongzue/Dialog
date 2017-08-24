package com.kongzue.dialogdemo;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kongzue.dialog.InputDialog;
import com.kongzue.dialog.MessageDialog;
import com.kongzue.dialog.ProgressbarDialog;
import com.kongzue.dialog.SelectDialog;
import com.kongzue.dialog.listener.InputDialogCallbackClickListener;
import com.kongzue.dialog.util.DialogThemeColor;

public class MainActivity extends AppCompatActivity {

    private int colorId;        //主题颜色

    private RadioGroup grp;
    private RadioButton rdoGreen;
    private RadioButton rdoBlue;
    private RadioButton rdoOrange;
    private Button btnMsg;
    private Button btnInput;
    private Button btnSelect;
    private Button btnPsg;
    private Button btnPsgInfo;

    private void initViews() {
        grp = (RadioGroup) findViewById(R.id.grp);
        rdoGreen = (RadioButton) findViewById(R.id.rdo_green);
        rdoBlue = (RadioButton) findViewById(R.id.rdo_blue);
        rdoOrange = (RadioButton) findViewById(R.id.rdo_orange);
        btnMsg = (Button) findViewById(R.id.btn_msg);
        btnInput = (Button) findViewById(R.id.btn_input);
        btnSelect = (Button) findViewById(R.id.btn_select);
        btnPsg = (Button) findViewById(R.id.btn_psg);
        btnPsgInfo = (Button) findViewById(R.id.btn_psg_info);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();
        initEvent();
    }

    private void initDatas() {
        //欢迎信息，快速调用对话框的方式
        MessageDialog.show(this, "欢迎", "欢迎使用Kongzue家的对话框，此案例提供常用的几种对话框样式\na 如有问题可以在https://github.com/kongzue/Dialog提交反馈", "关闭", null);
    }

    private ProgressbarDialog progressbarDialog;

    private void initEvent() {
        //选择主题配色
        grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (rdoGreen.getId() == checkedId) {
                    colorId = 0;        //原谅绿
                }
                if (rdoBlue.getId() == checkedId) {
                    colorId = 1;        //胖次蓝
                }
                if (rdoOrange.getId() == checkedId) {
                    colorId = 2;        //伊藤橙
                }
            }
        });

        //输入对话框
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //详细设置的写法
                InputDialog inputDialog = new InputDialog(MainActivity.this)
                        .setTitle("请输入文字")
                        .setInputHintText("这里是提示文字")
                        .setPositiveButtonText("积极选择")
                        .setNativeButtonText("消极选择")
                        .setThemeColor(colorId)                   //设置主题颜色
                        .setOnPositiveButtonClickListener(new InputDialogCallbackClickListener() {      //输入对话框回调方法
                            @Override
                            public void onClick(View v, String inputText) {
                                Toast.makeText(MainActivity.this, "你输入的是：" + inputText, Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();

                //快速调用方式
//                InputDialog.show(MainActivity.this, new InputDialogCallbackClickListener() {
//                    @Override
//                    public void onClick(View v, String inputText) {
//                        Toast.makeText(MainActivity.this, "你输入的是：" + inputText, Toast.LENGTH_LONG).show();
//                    }
//                },"请输入文字","这里是提示文字");
            }
        });

        //消息对话框
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = new MessageDialog(MainActivity.this)
                        .setTitle("消息提示框")
                        .setTipText("用于提示一些消息")
                        .setThemeColor(colorId)
                        .setPositiveButtonText("知道了")
                        .setPositiveButtonClickListener(null)          //如果没有要点击的事件可以直接传null
                        .show();

                //快速调用方式
//                MessageDialog.show(MainActivity.this,"消息提示框","用于提示一些消息","知道了",null);
            }
        });

        //选择对话框
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDialog selectDialog = new SelectDialog(MainActivity.this)
                        .setTitle("选择框")
                        .setTipText("请做出你的选择")
                        .setPositiveButtonText("积极选择")
                        .setPositiveButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "你做出了积极的选择", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNativeButtonText("消极选择")
                        .setNativeButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "你做出了消极的选择", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setThemeColor(colorId)
                        .show();

                //快速调用方式
//                SelectDialog.show(MainActivity.this, "选择框", "请做出你的选择", "积极选择", "消极选择", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this, "你做出了积极的选择", Toast.LENGTH_LONG).show();
//                    }
//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this, "你做出了消极的选择", Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        });

        //载入对话框
        btnPsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressbarDialog==null) progressbarDialog=new ProgressbarDialog(MainActivity.this);
                progressbarDialog.setInfo("");
                progressbarDialog.setCancelable(true);
                progressbarDialog.show();
            }
        });

        //带文字的载入对话框
        btnPsgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressbarDialog==null) progressbarDialog=new ProgressbarDialog(MainActivity.this);
                progressbarDialog.setInfo("载入中...");
                progressbarDialog.setCancelable(true);
                progressbarDialog.show();
            }
        });
    }

}
