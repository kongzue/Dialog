package com.kongzue.dialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.listener.OnMenuItemClickListener;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.ProgressbarDialog;
import com.kongzue.dialog.v2.InputDialog;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.Notification;
import com.kongzue.dialog.v2.SelectDialog;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.kongzue.dialog.v2.BottomMenu;

import java.util.ArrayList;
import java.util.List;

import static com.kongzue.dialog.v2.DialogSettings.THEME_DARK;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;
import static com.kongzue.dialog.v2.DialogSettings.TYPE_IOS;
import static com.kongzue.dialog.v2.DialogSettings.TYPE_KONGZUE;
import static com.kongzue.dialog.v2.DialogSettings.TYPE_MATERIAL;
import static com.kongzue.dialog.v2.Notification.SHOW_TIME_SHORT;
import static com.kongzue.dialog.v2.Notification.TYPE_ERROR;
import static com.kongzue.dialog.v2.Notification.TYPE_FINISH;
import static com.kongzue.dialog.v2.Notification.TYPE_NORMAL;
import static com.kongzue.dialog.v2.Notification.TYPE_WARNING;

public class MainActivity extends AppCompatActivity {
    
    private MainActivity me;
    
    private int colorId;        //主题颜色
    
    private RadioGroup grp;
    private RadioButton rdoMaterial;
    private RadioButton rdoKongzue;
    private RadioButton rdoIos;
    private RadioGroup grpDialogTheme;
    private RadioButton rdoDialogThemeLight;
    private RadioButton rdoDialogThemeDark;
    private Button btnMsg;
    private Button btnInput;
    private Button btnSelect;
    private Button btnMsgWithCustom;
    private Button btnInputWithCustom;
    private Button btnSelectWithCustom;
    private RadioGroup grpTipTheme;
    private RadioButton rdoTipThemeLight;
    private RadioButton rdoTipThemeDark;
    private Button btnPsg;
    private Button btnTipOk;
    private Button btnTipWarning;
    private Button btnTipError;
    private RadioGroup grpNotification;
    private RadioButton rdoNormal;
    private RadioButton rdoFinish;
    private RadioButton rdoError;
    private RadioButton rdoWarning;
    private Button btnNotificationNormal;
    private Button btnNotificationWithTitle;
    private Button btnNotificationWithTitleAndIcon;
    private Button btnShowBottomMenu;
    private Button btnShowBottomMenuWithTitle;
    private Button btnShowBottomMenuWithCustom;
    private Button btnShowBottomMenuWithTitleWithCustom;
    private Button btnShowMultipleDialogs;
    
    private void initViews() {
        grp = findViewById(R.id.grp);
        rdoMaterial = findViewById(R.id.rdo_material);
        rdoKongzue = findViewById(R.id.rdo_kongzue);
        rdoIos = findViewById(R.id.rdo_ios);
        grpDialogTheme = findViewById(R.id.grp_dialog_theme);
        rdoDialogThemeLight = findViewById(R.id.rdo_dialog_theme_light);
        rdoDialogThemeDark = findViewById(R.id.rdo_dialog_theme_dark);
        btnMsg = findViewById(R.id.btn_msg);
        btnInput = findViewById(R.id.btn_input);
        btnSelect = findViewById(R.id.btn_select);
        btnMsgWithCustom = findViewById(R.id.btn_msg_withCustom);
        btnInputWithCustom = findViewById(R.id.btn_input_withCustom);
        btnSelectWithCustom = findViewById(R.id.btn_select_withCustom);
        grpTipTheme = findViewById(R.id.grp_tip_theme);
        rdoTipThemeLight = findViewById(R.id.rdo_tip_theme_light);
        rdoTipThemeDark = findViewById(R.id.rdo_tip_theme_dark);
        btnPsg = findViewById(R.id.btn_psg);
        btnTipOk = findViewById(R.id.btn_tip_ok);
        btnTipWarning = findViewById(R.id.btn_tip_warning);
        btnTipError = findViewById(R.id.btn_tip_error);
        grpNotification = findViewById(R.id.grp_notification);
        rdoNormal = findViewById(R.id.rdo_normal);
        rdoFinish = findViewById(R.id.rdo_finish);
        rdoError = findViewById(R.id.rdo_error);
        rdoWarning = findViewById(R.id.rdo_warning);
        btnNotificationNormal = findViewById(R.id.btn_notification_normal);
        btnNotificationWithTitle = findViewById(R.id.btn_notification_withTitle);
        btnNotificationWithTitleAndIcon = findViewById(R.id.btn_notification_withTitleAndIcon);
        btnShowBottomMenu = findViewById(R.id.btn_show_bottom_menu);
        btnShowBottomMenuWithTitle = findViewById(R.id.btn_show_bottom_menu_with_title);
        btnShowBottomMenuWithCustom = findViewById(R.id.btn_show_bottom_menu_withCustom);
        btnShowBottomMenuWithTitleWithCustom = findViewById(R.id.btn_show_bottom_menu_with_title_withCustom);
        btnShowMultipleDialogs = findViewById(R.id.btn_show_multiple_dialogs);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        me = this;
        
        initViews();
        initDatas();
        setEvents();
    }
    
    private View customView;
    
    private void initDatas() {
        DialogSettings.use_blur = true;                 //设置是否启用模糊
        
        DialogSettings.type = TYPE_MATERIAL;
        DialogSettings.tip_theme = THEME_DARK;
        DialogSettings.dialog_theme = THEME_LIGHT;
        
        DialogSettings.dialog_title_text_size = -1;     //设置对话框标题文字大小，<=0不启用
        DialogSettings.dialog_message_text_size = -1;   //设置对话框内容文字大小，<=0不启用
        DialogSettings.dialog_button_text_size = -1;    //设置对话框按钮文字大小，<=0不启用
        DialogSettings.tip_text_size = -1;              //设置提示框文字大小，<=0不启用
        DialogSettings.ios_normal_button_color = -1;    //设置iOS风格默认按钮文字颜色，=-1不启用
        DialogSettings.dialog_menu_text_size = -1;      //设置菜单默认字号，<=0不启用
        
        MessageDialog.show(me, "欢迎", "欢迎使用Kongzue家的对话框，此案例提供常用的几种对话框样式。\n如有问题可以在https://github.com/kongzue/Dialog提交反馈");
        
        customView = LayoutInflater.from(me).inflate(R.layout.layout_custom, null);
    }
    
    private ProgressbarDialog progressbarDialog;
    
    private int notifactionType = TYPE_NORMAL;
    
    private void setEvents() {
        
        btnShowBottomMenuWithCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                list.add("菜单1");
                list.add("菜单2");
                list.add("菜单3");
                BottomMenu.show(me, list, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        Toast.makeText(me, "菜单 " + text + " 被点击了", SHOW_TIME_SHORT).show();
                    }
                }, true).setCustomView(customView);
            }
        });
        
        btnShowBottomMenuWithTitleWithCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                list.add("菜单1");
                list.add("菜单2");
                list.add("菜单3");
                BottomMenu.show(me, list, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        Toast.makeText(me, "菜单 " + text + " 被点击了", SHOW_TIME_SHORT).show();
                    }
                }, true).setTitle("这里是标题测试").setCustomView(customView);
            }
        });
        
        btnMsgWithCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog.show(me, null, null, "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    
                    }
                }).setCustomView(customView).setCanCancel(true);
            }
        });
        
        btnSelectWithCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDialog.show(me, null, null, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(me, "您点击了确定按钮", Toast.LENGTH_SHORT).show();
                    }
                }, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(me, "您点击了取消按钮", Toast.LENGTH_SHORT).show();
                    }
                }).setCustomView(customView).setCanCancel(true);
            }
        });
        
        btnInputWithCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.show(me, null, null, new InputDialogOkButtonClickListener() {
                    @Override
                    public void onClick(Dialog dialog, String inputText) {
                        if (!inputText.equals("kongzue")) {
                            TipDialog.show(me, "错误的用户名", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                            Notification.show(me, 0, "小提示：用户名是：kongzue");
                        } else {
                            TipDialog.show(me, "您已通过", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                            dialog.dismiss();
                        }
                    }
                }).setCustomView(customView).setCanCancel(true);
            }
        });
        
        btnShowBottomMenuWithTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                list.add("菜单1");
                list.add("菜单2");
                list.add("菜单3");
                BottomMenu.show(me, list, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        Toast.makeText(me, "菜单 " + text + " 被点击了", SHOW_TIME_SHORT).show();
                    }
                }, true).setTitle("这里是标题测试");
            }
        });
        
        btnShowBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                list.add("菜单1");
                list.add("菜单2");
                list.add("菜单3");
                BottomMenu.show(me, list, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        Toast.makeText(me, "菜单 " + text + " 被点击了", SHOW_TIME_SHORT).show();
                    }
                }, true);
            }
        });
        
        btnShowMultipleDialogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog.show(me, "提示", "一次启动多个对话框，他们会按顺序显示出来");
                MessageDialog.show(me, "提示", "弹出时，会模拟阻塞的情况，此时主线程并不受影响，但对话框会建立队列，然后逐一显示");
                SelectDialog.show(me, "提示", "多种类型对话框亦支持", "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    
                    }
                }, "选择2", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    
                    }
                });
                InputDialog.show(me, "提示", "这是最后一个对话框，序列即将结束", new InputDialogOkButtonClickListener() {
                    @Override
                    public void onClick(Dialog dialog, String inputText) {
                        dialog.dismiss();
                    }
                });
            }
        });
        
        grpNotification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdoNormal.getId() == checkedId) {
                    notifactionType = TYPE_NORMAL;
                }
                if (rdoFinish.getId() == checkedId) {
                    notifactionType = TYPE_FINISH;
                }
                if (rdoWarning.getId() == checkedId) {
                    notifactionType = TYPE_WARNING;
                }
                if (rdoError.getId() == checkedId) {
                    notifactionType = TYPE_ERROR;
                }
            }
        });
        
        btnNotificationWithTitleAndIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.show(me, 2, R.mipmap.ico_wechat, getString(R.string.app_name), "这是一条消息", Notification.SHOW_TIME_LONG, notifactionType)
                        .setOnNotificationClickListener(new Notification.OnNotificationClickListener() {
                            @Override
                            public void OnClick(int id) {
                                Toast.makeText(me, "点击了通知", SHOW_TIME_SHORT).show();
                            }
                        })
                ;
            }
        });
        
        btnNotificationWithTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.show(me, 1, getString(R.string.app_name), "这是一条消息", Notification.SHOW_TIME_SHORT, notifactionType);
            }
        });
        
        btnNotificationNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.show(me, 0, "这是一条消息", notifactionType);
            }
        });
        
        grpDialogTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdoDialogThemeDark.getId() == checkedId) {
                    DialogSettings.dialog_theme = THEME_DARK;
                }
                if (rdoDialogThemeLight.getId() == checkedId) {
                    DialogSettings.dialog_theme = THEME_LIGHT;
                }
            }
        });
        
        grpTipTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdoTipThemeDark.getId() == checkedId) {
                    DialogSettings.tip_theme = THEME_DARK;
                }
                if (rdoTipThemeLight.getId() == checkedId) {
                    DialogSettings.tip_theme = THEME_LIGHT;
                }
            }
        });
        
        grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdoMaterial.getId() == checkedId) {
                    DialogSettings.type = TYPE_MATERIAL;
                }
                if (rdoKongzue.getId() == checkedId) {
                    DialogSettings.type = TYPE_KONGZUE;
                }
                if (rdoIos.getId() == checkedId) {
                    DialogSettings.type = TYPE_IOS;
                }
            }
        });
        
        //输入对话框
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.show(me, "验证", "请出入正确的用户名：", new InputDialogOkButtonClickListener() {
                    @Override
                    public void onClick(Dialog dialog, String inputText) {
                        if (!inputText.equals("kongzue")) {
                            TipDialog.show(me, "错误的用户名", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                            Notification.show(me, 0, "小提示：用户名是：kongzue");
                        } else {
                            TipDialog.show(me, "您已通过", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        
        //消息对话框
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog.show(me, "消息提示框", "用于提示一些消息", "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    
                    }
                });
            }
        });
        
        //选择对话框
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDialog.show(me, "提示", "请做出你的选择", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(me, "您点击了确定按钮", Toast.LENGTH_SHORT).show();
                    }
                }, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(me, "您点击了取消按钮", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        
        //调用等待提示框
        btnPsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show(me, "载入中...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WaitDialog.dismiss();
                        TipDialog.show(me, "完成", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                    }
                }, 3000);
            }
        });
        
        //完成提示
        btnTipOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.show(me, "完成", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
            }
        });
        
        //警告提示
        btnTipWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.show(me, "请输入密码", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
            }
        });
        
        //错误提示
        btnTipError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.show(me, "禁止访问", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_ERROR);
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogSettings.unloadAllDialog();           //卸载掉所有对话框
    }
}
