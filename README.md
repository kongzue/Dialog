# 空祖家的对话框 V2
献给要求我们安卓照着苹果设计稿做开发的产品们（手动滑稽

<a href="https://github.com/kongzue/Dialog/">
<img src="https://img.shields.io/badge/Kongzue%20Dialog-2.4.9-green.svg" alt="Kongzue Dialog">
</a> 
<a href="https://bintray.com/myzchh/maven/dialog/2.4.9/link">
<img src="https://img.shields.io/badge/Maven-2.4.9-blue.svg" alt="Maven">
</a> 
<a href="http://www.apache.org/licenses/LICENSE-2.0">
<img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="License">
</a> 
<a href="http://www.kongzue.com">
<img src="https://img.shields.io/badge/Homepage-Kongzue.com-brightgreen.svg" alt="Homepage">
</a>

空祖家的对话框2.0拥有提供最简单的调用方式以实现消息框、选择框、输入框、等待提示、警告提示、完成提示、错误提示等弹出样式。以下是目前包含的所有对话框样式预览图：

![Kongzue's Dialog](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.0.png)

试用版可以前往 <https://fir.im/dialogV2> 下载

本例中，包含DialogDemo（Dialog/app/）是对话框的演示项目源代码，以及Library库（Dialog/dialog/）是封装的空祖家对话框的源代码。

项目托管的Maven仓库在<https://bintray.com/myzchh/maven/dialog>

本项目遵循Apache-2.0开源协议，具体可参考：<http://www.opensource.org/licenses/apache2.0.php>

# 🆙 新版本V3
Kongzue Dialog V3 现已上传正式版本，请前往 <https://github.com/kongzue/DialogV3> 查看。

# 目录

· <a href="#Maven仓库或Gradle的引用方式">Maven仓库或Gradle的引用方式</a>

· <a href="#使用说明">使用说明</a>

· <a href="#关于v2组件包">关于v2组件包</a>

· <a href="#关于模糊效果">关于模糊效果</a>

· <a href="#使用方法">使用方法</a>

···· <a href="#调用消息对话框的方法">调用消息对话框的方法</a>

···· <a href="#调用选择对话框的方法">调用选择对话框的方法</a>

···· <a href="#调用输入对话框">调用输入对话框</a>

···· <a href="#调用等待提示框">调用等待提示框</a>

···· <a href="#调用完成提示框">调用完成提示框</a>

···· <a href="#调用警告提示框">调用警告提示框</a>

···· <a href="#调用错误提示框">调用错误提示框</a>

···· <a href="#调用消息通知">调用消息通知</a>

···· <a href="#调用底部菜单">调用底部菜单</a>

···· <a href="#气泡提示">气泡提示</a>

· <a href="#自定义对话框">自定义对话框</a>

· <a href="#自定义布局">自定义布局</a>

· <a href="#附加功能">附加功能</a>

· <a href="#modal">模态化（序列化）</a>

· <a href="#一些建议">一些建议</a>

· <a href="#开源协议">开源协议</a>

· <a href="#更新日志">更新日志</a>


## Maven仓库或Gradle的引用方式
Maven仓库：
```
<dependency>
  <groupId>com.kongzue.dialog</groupId>
  <artifactId>dialog</artifactId>
  <version>2.4.9</version>
  <type>pom</type>
</dependency>
```
Gradle：
在dependencies{}中添加引用：
```
implementation 'com.kongzue.dialog:dialog:2.4.9'
```

部分 Material 组件需要依赖：
```
implementation 'com.android.support:appcompat-v7:28.0.0'
implementation 'com.android.support:design:28.0.0'
```

⚠现有 Android X 版本提供，具体请转至 <a href="https://github.com/kongzue/Dialog/tree/AndroidX">Android X分支</a> 查看。

此外，

若需要使用 v1 兼容库的老版本，可使用：
```
implementation 'com.kongzue.dialog:dialog:2.2.8'        //警告：不再提供更新
```

若需要降低包体积，可使用不带模糊的版本：
```
implementation 'com.kongzue.dialog:dialog:2.1.0'        //警告：不再提供更新
```

## 使用说明
1) 组件启用前请先初始化全局的风格样式，具体方法为
```
DialogSettings.style = STYLE_MATERIAL;
```

Material 风格对应 DialogSettings.STYLE_MATERIAL，

Kongzue 风格对应 DialogSettings.STYLE_KONGZUE，

iOS 风格对应 DialogSettings.STYLE_IOS

需要注意的是风格设置仅针对对话框，提示框样式不会改变。

2) 要启用 Light & Dark 黑白主题模式，请调用以下语句实现：
```
DialogSettings.tip_theme = THEME_LIGHT;         //设置提示框主题为亮色主题
DialogSettings.dialog_theme = THEME_DARK;       //设置对话框主题为暗色主题
```

具体预览图如下：
![Kongzue's Dialog Light&DarkMode](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.0_dark.png)

3) 从 2.0.4 版本开始，提供不需要悬浮窗权限，且可以跨域显示的通知功能，如下图所示：
![Kongzue's Dialog Notifaction](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.0.4_notification.png)

4) 从 2.1.0 版本开始，提供底部菜单，可以通过 com.kongzue.dialog.v2.BottomMenu 进行使用，如下图所示：
![Kongzue's Dialog BottomMenu](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.1.0_bottommenu.png)

5) 从 2.1.1 版本开始，iOS风格的对话框、提示框、底部菜单新增即时模糊效果：
![Kongzue's Dialog Blur](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.1.1_blur.png)

6) 从 2.2.3 版本开始，MessageDialog、SelectDialog、InputDialog和BottomMenu 支持自定义布局：
![Kongzue's Dialog CustomView](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.2.3_customView.png)

7) 从 2.3.4 版本开始，提供气泡提示功能：
![Kongzue's Dialog CustomView](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.3.4_pop.png)

## 关于v2组件包
在空祖家的对话框组件中，依然保留了一代的组件库但不再推荐使用，这是为了保持兼容性，若强行使用您会看到相关类的名称上有删除线。

为了更有效率的开发，我们现在强烈推荐直接使用v2组件库，其包含的包地址为：com.kongzue.dialog.v2

## 关于模糊效果
从2.1.1版本起可使用即时模糊效果。

您可以通过以下属性设置开关：
```
DialogSettings.use_blur = true;                 //设置是否启用模糊
```
如果需要启动，还需要在您的 app 的 build.gradle 中添加以下代码：
```
android {
    ...
    defaultConfig {
        ...

        renderscriptTargetApi 19
        renderscriptSupportModeEnabled true
    }
}
```
模糊效果目前仅对当 DialogSettings.style = STYLE_IOS 时三种对话框、提示框、等待框以及底部菜单有效。

可以通过以下方法修改模糊透明度：
```
/*
 *  决定等待框、提示框以及iOS风格的对话框的模糊背景透明度（50-255）
 */
DialogSettings.blur_alpha = 200;
```

## 使用方法

提示：所有对话框组件均提供相应的 build(...) 方法用于预先创建，如需要对对话框部分参数进行微调的，可使用 build(...) 方法先创建对话框，然后调用相应的方法进行属性设置，再使用 showDialog() 方法显示对话框。

### 调用消息对话框的方法：
```
MessageDialog.show(context, "消息提示框", "用于提示一些消息", "知道了", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
});
```
或者可以采用快速调用方式：
```
MessageDialog.show(context, "欢迎", "欢迎使用Kongzue家的对话框，此案例提供常用的几种对话框样式。\n如有问题可以在https://github.com/kongzue/Dialog提交反馈");
```

### 调用选择对话框的方法：
```
SelectDialog.show(context, "提示", "请做出你的选择", "确定", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(context, "您点击了确定按钮", Toast.LENGTH_SHORT).show();
    }
}, "取消", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(context, "您点击了取消按钮", Toast.LENGTH_SHORT).show();
    }
});
```
或者可以采用快速调用方式：
```
SelectDialog.show(context, "提示", "请做出你的选择", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(context, "您点击了确定按钮", Toast.LENGTH_SHORT).show();
    }
});
```

### 调用输入对话框：
```
InputDialog.show(context, "设置昵称", "设置一个好听的名字吧", "确定", new InputDialogOkButtonClickListener() {
    @Override
    public void onClick(Dialog dialog, String inputText) {
        Toast.makeText(context, "您输入了：" + inputText, Toast.LENGTH_SHORT).show();
    }
}, "取消", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
});
```
或者可以采用快速调用方式：
```
InputDialog.show(context, "设置昵称", "设置一个好听的名字吧", new InputDialogOkButtonClickListener() {
                    @Override
                    public void onClick(Dialog dialog, String inputText) {
                        Toast.makeText(context, "您输入了：" + inputText, Toast.LENGTH_SHORT).show();
                    }
                });
```

从 2.3.6 版本起，对于输入文本长度限制和文本类型限制，可通过 setInputInfo(InputInfo) 方法进行设置：
```
import com.kongzue.dialog.util.InputInfo;                           //此处需要引入使用的包

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
}).setInputInfo(new InputInfo()
        .setMAX_LENGTH(10)                                          //设置最大长度10位
        .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)       //设置密码输入类型（注意此处请勿使用“InputType.TYPE_CLASS_TEXT | InputType.{YOUR_TYPE}”位运算，若不清楚此问题请按照范例使用即可）
    );
```

### 调用等待提示框：
```
WaitDialog.show(context, "载入中...");
```

从 2.3.8 版本起，等待提示框支持自定义布局，可通过以下代码调用：

```
View customView = LayoutInflater.from(me).inflate(R.layout.layout_custom, null);
WaitDialog.show(context, "载入中...", customView);
```

要动态修改正在显示的文本，可以先使用 show(...) 方法获取 WaitDialog 对象，然后使用 setText(...) 修改正在显示的文本。

但请注意，设置自定义布局并不会隐藏提示文本信息。

### 调用完成提示框：
```
TipDialog.show(context, "完成", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
```

### 调用警告提示框：
```
TipDialog.show(context, "请输入密码", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
```

### 调用错误提示框：
```
TipDialog.show(context, "禁止访问", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_ERROR);
```

### 调用消息通知：

注意，此处使用的是来自com.kongzue.dialog.v2 的 Notification 类。

通知消息（v2.Notification）与提示框（v2.TipDialog）的主要区别是提示框会打断用的操作，而消息通知不会，消息通知适合于并发需要提醒用户是否处理消息的业务场景，而提示框适用于阻断用户操作，提醒用户当前发生的情况的业务场景。

```
Notification.show(context, id, iconResId, getString(R.string.app_name), "这是一条消息", Notification.SHOW_TIME_LONG, notifactionType)
                        .setOnNotificationClickListener(new Notification.OnNotificationClickListener() {
                            @Override
                            public void OnClick(int id) {
                                Toast.makeText(context,"点击了通知",SHOW_TIME_SHORT).show();
                            }
                        })
                ;
```
其中，id为通知消息id，在用户点击该通知后，会在OnNotificationClickListener中进行回调。

字段 | 含义 | 是否必须
---|---|---
context | 上下文索引 | 必须
iconResId | 图标 | 可选
title | 通知标题 | 可选
message | 通知内容 | 必须
colorType | 消息类型 | 必须
OnNotificationClickListener | 下载监听器 | 可选

注意，此处的消息类型 colorType 目前仅对“Kongzue 风格”有效，且提供的风格有：

字段 | 含义 | 是否默认
---|---|---
TYPE_NORMAL | 默认灰黑色 | 默认
TYPE_FINISH | 绿色 | 可选
TYPE_WARNING | 橙色 | 可选
TYPE_ERROR | 红色 | 可选

在2.0.6版本后，若 colorType 不为上述设定值，则可以使用自选颜色值，可为 Color 类的返回值。

另外，可以采用快速调用方式：
```
Notification.show(context, 0, "", "这是一条消息", Notification.SHOW_TIME_SHORT, notifactionType);
```

### 调用底部菜单：

注意，此处使用的是来自com.kongzue.dialog.v2 的 BottomMenu 类。
```
List<String> list = new ArrayList<>();
list.add("菜单1");
list.add("菜单2");
list.add("菜单3");
BottomMenu.show(context, list, new OnMenuItemClickListener() {
    @Override
    public void onClick(String text, int index) {
        Toast.makeText(context,"菜单 " + text + " 被点击了",SHOW_TIME_SHORT).show();
    }
},true);
```
包含的参数如下：

字段 | 含义 | 是否必须
---|---|---
activity | 必须继承自 AppCompatActivity  | 必须
list | 泛型为 String 的列表 | 必须
OnMenuItemClickListener | 点击回调 | 可选
isShowCancelButton | 是否显示“取消”按钮，注意，STYLE_MATERIAL 风格对此无效 | 可选
cancelButtonCaption | 设置“取消”按钮的文字 | 可选

另外，本菜单暂时对夜间模式（THEME_DARK）不受影响，只提供Light Theme，但不排除接下来的版本对此更新。

使用 iOS 主题时，DialogSettings.ios_normal_button_color 会对菜单内容文字的颜色产生影响，其他主题不受此属性影响。

或可以使用快速调用：
```
List<String> list = new ArrayList<>();
list.add("菜单1");
list.add("菜单2");
list.add("菜单3");
BottomMenu.show(context, list);
```

为底部对话框添加标题：
```
BottomMenu.show(context, list).setTitle("这里是标题测试");
```

### 气泡提示：
从 2.3.4 版本起，新增气泡提示功能。

使用方法如下：
```
Pop.show(context, view, "这是一个提示", showWhere, colorType);
```
view 为气泡提示需要跟随的布局，可选参数 showWhere 可选：

字段 | 含义 | 是否默认
---|---|---
SHOW_UP  | 在跟随布局上方显示  | 是
SHOW_LEFT  | 在跟随布局左侧显示  | 否
SHOW_RIGHT | 在跟随布局右侧显示  | 否
SHOW_DOWN  | 在跟随布局下方显示  | 否

可选参数 colorType 控制气泡颜色，当其值为 COLOR_TYPE_NORMAL 时受 DialogSettings.tip_theme 设置影响，会显示暗色或亮色主题，另外可选：

字段 | 含义 | 是否默认
---|---|---
COLOR_TYPE_NORMAL  | 默认颜色，此时会受 DialogSettings.tip_theme 影响  | 是
COLOR_TYPE_FINISH  | 绿色  | 否
COLOR_TYPE_WARNING | 橙色  | 否
COLOR_TYPE_ERROR   | 红色  | 否

其次可使用 setText(...) 方法对已经创建的气泡提示进行文字修改，范例如下：
```
pop.setText("至少填写6位，还差" + (6 - editPop.getText().toString().length()) + "位");
```

可使用 setOnDismissListener(...) 方法对气泡消失事件进行监听：
```
pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
    @Override
    public void onDismiss() {

    }
});
```

可使用 dismiss(); 方法直接关闭已创建的气泡：
```
pop.dismiss();
```

从 2.3.5 版本起，针对复杂特殊文本显示进行了适配：

![Kongzue's Dialog Pop](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/kongzue_dialog_pop.png)

## 自定义对话框：

![Kongzue's Custom Dialog](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_custom_dialog.png)

从 2.3.9 版本起，支持自定义对话框的创建：
```
CustomDialog.show(context, R.layout.layout_custom_dialog, new CustomDialog.BindView() {
    @Override
    public void onBind(CustomDialog dialog, View rootView) {
        //绑定布局
        ImageView btnOk = rootView.findViewById(R.id.btn_ok);
        //绑定事件
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Example：让对话框消失
                dialog.doDismiss();
            }
        });
    }
});
```

CustomDialog 的创建方式支持通过 layout 资源 ID 或直接使用 View 创建，其也支持对话框的模态化以及生命周期管理。

需要在自定义的对话框中关闭 CustomDialog，您可以在全局接收 CustomDialog 的 show 或 build 返回的组件，并执行其 doDismiss() 方法：
```
private CustomDialog customDialog;

//...

customDialog = CustomDialog.show(me, R.layout.layout_custom_dialog, new CustomDialog.BindView() {
            @Override
            public void onBind(View rootView) {
                ImageView btnOk = rootView.findViewById(R.id.btn_ok);

                //...

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.doDismiss();
                    }
                });
            }
        });
```

## 自定义布局：
从 2.2.3 版本起支持 MessageDialog、SelectDialog、InputDialog和BottomMenu 的自定义布局。

由于调用逻辑顺序不同，在 type 为 STYLE_KONGZUE 和 STYLE_IOS 使用方法举例：
```
//初始化布局：
View customView = LayoutInflater.from(context).inflate(R.layout.layout_custom, null);
//启动对话框
MessageDialog.show(context, null, null, "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCustomView(customView);
```

在 type 为 STYLE_MATERIAL 时，请先使用 build(...) 方法创建 Dialog，接着设置好自定义布局后再执行 showDialog() 方法，使用方法举例：
```
//初始化布局：
View customView = LayoutInflater.from(me).inflate(R.layout.layout_custom, null);
//启动对话框
MessageDialog.build(context, null, null, "知道了", new DialogInterface.OnClickLis
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}).setCustomView(customView)
  .setCanCancel(true)
  .showDialog();
```

等待对话框的自定义布局请参考：<a href="#调用等待提示框">调用等待提示框</a>

## 附加功能：
在任何一种对话框中都可以使用.setCanCancel(boolean)来设置是否可以点击对话框以外的区域关闭对话框，提示类默认都是禁止的，选择、输入对话框默认也是禁止的，消息对话框默认是允许的。

使用方法可以参考以下代码：
```
WaitDialog.show(context, "载入中...").setCanCancel(true);
```

在空祖家的对话框组件中，您可以使用监听器来监听对话框的生命周期，Demo如下：
```
WaitDialog.show(context, "载入中...").setCanCancel(true).setDialogLifeCycleListener(new DialogLifeCycleListener() {
    @Override
    public void onCreate(AlertDialog alertDialog) {
        //此时对话框创建
    }
    @Override
    public void onShow(AlertDialog alertDialog) {
        //此时对话框显示
    }
    @Override
    public void onDismiss() {
        //此时对话框关闭
    }
});
```

另外提供一些定制属性：
```
DialogSettings.dialog_background_color = R.color.white;     //控制 STYLE_MATERIAL 和 STYLE_KONGZUE 两种风格时对话框的背景色，=-1时不启用

//v2.3.1新增：
DialogSettings.dialog_cancelable_default = -1;              //控制消息对话框、选择对话框和输入对话框默认是否可点击外部遮罩层区域关闭，=-1不启用

//属性 notification_text_size、notification_text_color、ios_normal_button_color、ios_normal_ok_button_color、dialog_button_text_size、tip_text_size、dialog_menu_text_size、dialog_title_text_size、dialog_message_text_size 在 2.3.7 版本起已经弃用
//v2.3.7新增：
DialogSettings.dialogTitleTextInfo                          //决定对话框标题文字样式
DialogSettings.dialogContentTextInfo                        //决定对话框内容文字样式
DialogSettings.dialogButtonTextInfo                         //决定对话框按钮文字样式
DialogSettings.dialogOkButtonTextInfo                       //决定对话框积极按钮（一般为确定按钮）文字样式，若未设置此样式则会使用 dialogButtonTextInfo 代替
DialogSettings.tipTextInfo                                  //决定提示框文本样式
DialogSettings.menuTextInfo                                 //决定菜单文字样式
DialogSettings.notificationTextInfo                         //决定 Notification 默认文字样式信息

//TextInfo(com.kongzue.dialog.util.TextInfo) 为 2.3.7 版本新增的文字样式属性类，其创建方式范例：
DialogSettings.dialogContentTextInfo = new TextInfo()
        .setBold(true)                                      //粗体开关
        .setFontColor(Color.rgb(253,130,255))               //颜色设置
        .setFontSize(10)                                    //字号（单位dp）
;
//默认不修改或值为 -1 时使用默认样式
```

若需要对某个对话框、提示框等进行单独的文字样式设置，请通过相应对话框的 build 创建该对话框，在调用 showDialog() 方法前进行 setTextInfo()：
```
MessageDialog.build(me, "标题", "内容", "知道了", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}).setCanCancel(true)
  .setTitleTextInfo(new TextInfo()
      .setBold(true)
      .setFontColor(Color.rgb(253, 130, 255))
      .setFontSize(10)
  )
  .showDialog();
```

额外的，若您需要自定义对话框的标题、内容、按钮，请使用相对应的方法进行设置：
```
.setTitleTextInfo(new TextInfo())           //设置标题文字样式
.setContentTextInfo(new TextInfo())         //设置内容文字样式
.setButtonTextInfo(new TextInfo())          //设置按钮文字样式
.setOkButtonTextInfo(new TextInfo())        //针对选择对话框、输入对话框的“确定”按钮进行文字样式单独设置，不设置则按照 ButtonTextInfo 的样式处理
.setTxtInfo(new TextInfo())                 //提示框（TipDialog）或通知可直接使用此方法设置文本样式
//由于等待提示 WaitDialog 没有 build 创建方法，您可以直接在其 show(...) 方法中以参数形式设置文字样式：
WaitDialog.show(me, "载入中...",new TextInfo());
```

## <a name="modal">模态化（序列化）</a>
模态化（序列化）是指一次性弹出多个对话框时不一次性全部显示，而是按照队列一个关闭后再显示下一个的启动方式。

Kongzue Dialog 是从 2.0.9 版本起支持模态化的，但在 2.3.0 版本起我们做了更多的修改，以保证此方法可能引发的内存泄露问题得以解决，从 2.3.0 版本起，Kongzue Dialog 默认关闭了模态化，要是用模态化，请详细阅读以下文档：

从 2.3.0 版本起，我们提供了“创建”对话框的方法 build(...) ，通过此方法可以在内存中创建 Dialog 而不启动它，以下代码演示了两个消息框、一个选择框和一个输入框模态化显示的方法：
```
MessageDialog.build(context, "提示", "一次启动多个对话框，他们会按顺序显示出来","确定",null).showDialog();                           //第一个要显示的 Dialog 请调用其 showDialog() 将它显示出来
MessageDialog.build(context, "提示", "弹出时，会模拟阻塞的情况，此时主线程并不受影响，但对话框会建立队列，然后逐一显示","确定",null);
SelectDialog.build(context, "提示", "多种类型对话框亦支持", "知道了", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}, "选择2", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
});
InputDialog.build(context, "提示", "这是最后一个对话框，序列即将结束", "提交", new InputDialogOkButtonClickListener() {
    @Override
    public void onClick(Dialog dialog, String inputText) {
        Log.i(">>>", "InputDialogOkButtonClickListener-ok");
        dialog.dismiss();
    }
}, "取消", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.i(">>>", "InputDialogOkButtonClickListener-cancel");
    }
});
```
需要模态化的 Dialog 只需要使用 build 创建，而不会直接将它显示出来，支持模态化的有消息对话框（MessageDialog）、选择对话框（SelectDialog）和输入对话框（InputDialog），其他暂不支持。

上述代码在执行时默认就会按照一个关闭再先试下一个的方式进行。

## 一些建议
从 2.4.2 版本起，使用全新的Dialog创建方式，此方式在您finish掉Activity的情况下不会引发WindowLeaked错误，因此无需像之前的方式一样执行 unloadAllDialog(); 方法。

若您使用 2.4.2 版本前，我的建议如下：
~~由于 Dialog 的模态化实现、等待对话框（WaitDialog）、提示对话框（TipDialog）都需要至少显示一段时间，而 Dialog 的显示是依赖于 Context（更准确说是Activity） 的，那么在这段时间内，若 Activity 被卸载，则有可能发生 WindowLeaked 错误，针对此问题，建议在 Activity 的 onDestroy() 加入以下代码以确保所有 Dialog 完全卸载：~~
```
@Override
protected void onDestroy() {
    super.onDestroy();
    DialogSettings.unloadAllDialog();           //卸载掉所有对话框
}
```

## 开源协议
```
Copyright Kongzue Dialog

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## 更新日志：
v2.4.9:
- 修复部分bug；
- 新增 setOnDismissListener(...) 可单独设置对话框关闭事件；

(beta)v2.4.8.2:
- 迁移至 Android X（已回档，要使用Android X版本请切换分支拉取项目）；

&emsp;&emsp;注意，此版本尚处测试阶段，且不确定若出现重大 Bug 反档。

(beta)v2.4.8.1:
- 修复了其他各组件 setCanCancel(boolean) 无效的bug；

v2.4.8:
- CustomDialog 支持使用 build(...) 方法创建，并修复了 setCanCancel(boolean) 无效的bug；
- CustomDialog 的 BindView 增加提供参数 CustomDialog 以简化进行相应的业务处理；
- BottomMenu 提供额外的 build(...) 方法创建；
- 修复了 TipDialog 非主线程活跃情况导致不消失的问题；
- WaitDialog 支持 tip 设置为 null；

v2.4.7:
- WaitDialog 默认情况下，在show(...)方法后使用setCanCancel(boolean) 则为单独设置本次是否可取消，如果在show(...)方法前使用setCanCancelGlobal(boolean) 方法则可设置全局是否可取消。
- 修复因改变屏幕横竖向可能引发的崩溃问题；
- 升级 Gradle 版本至 3.3.0；
- 升级 buildToolsVersion 至 28.0.3；

v2.4.6:
- 修复了iOS模式下标题粗体设置无效的问题；
- 输入对话框默认为单行模式；

v2.4.5:
- ⚠设置行为变更警告！

&emsp;&emsp;请仔细阅读以下说明：

&emsp;&emsp;为解决可能引发的歧义问题，现将 DialogSettings 中的 TYPE_MATERIAL、TYPE_KONGZUE 和 TYPE_IOS，值不发生任何变化
```
DialogSettings.STYLE_MATERIAL = 0;
DialogSettings.STYLE_KONGZUE = 1;
DialogSettings.STYLE_IOS = 2;
```
&emsp;&emsp;设置参数由 DialogSettings.type 变更为 DialogSettings.style

&emsp;&emsp;并且任何 Dialog 组件中可以使用 setDialogStyle(int style) 来临时单独设置改变当前对话框的风格；


v2.4.4:
- 兼容性和稳定性更新。

v2.4.3:
- 修复了 TextInfo 中设置文字颜色为纯白色无效的 bug；
- WaitDialog 新增 setText(...) 方法，用于动态改变等待对话框中的文字；
- 对对话框组件（不包含底部菜单、通知和Pop提示），新增 getAlertDialog() 方法以获取实例化的 alertDialog 对象以便于实现更多骚操作；

v2.4.2:
- 使用了新的对话框组件创建模式，不再需要在 Activity 的 onDestroy() 中执行 DialogSettings.unloadAllDialog() 亦可保证不会引发 WindowLeaked 错误；
- 修复了一些遗留 bug；

v2.4.1:
- 修复了 CustomDialog 若用户自定义布局中有输入框无法弹出软键盘的问题；
- WaitDialog 新增生命周期 DialogLifeCycleListener 参数；

v2.4.0:
- 组件升级至兼容 API-28；
- 各对话框组件新增方法 setType(...) 可独立设置对话框风格；

v2.3.9.1:
- 修复了输入对话框在使用 KONGZUE 风格时背景颜色不启用的 bug；

v2.3.9：
- 新增自定义对话框，详情请参考章节：<a href="#自定义对话框">自定义对话框</a>；

v2.3.8：
- WaitDialog 现已支持自定义布局，详情请参考章节：<a href="#调用等待提示框">调用等待提示框</a>；
- 修复了因 tipTextInfo 未初始化导致崩溃的 bug；

v2.3.7：
- 新增 TextInfo(com.kongzue.dialog.util.TextInfo) 用于统一管理文字样式设置；
- 修改了 DialogSettings 中对于各组件文字样式的设置方式，具体请参照章节：<a href="#附加功能">附加功能</a>；

v2.3.6：
- 新增 Pop 提示阴影效果（阴影组件来源 @GIGAMOLE(<https://github.com/Devlight/ShadowLayout>) ，开源协议：Apache License 2.0）；
- InputDialog 新增 setInputInfo(InputInfo) 方法，对输入框进行设置，具体请查看章节：<a href="#调用输入对话框">调用输入对话框</a>；
- 修复其他 bug；

v2.3.5：
- WaitDialog 新增 setOnBackPressListener(...) 可以在用户按返回键时响应事件；
- ~~新增 DialogSettings.ios_normal_ok_button_color 控制iOS对话框确认按钮颜色；~~(已在2.3.7版本弃用，具体请参照相关更新日志)
- Pop 针对复杂特殊文本显示适配；

v2.3.4:
- 新增气泡提示功能，具体请参考<a href="#气泡提示">气泡提示</a>；

v2.3.3:
- 修复当 type == STYLE_MATERIAL 时自定义布局的问题，具体请参考章节：<a href="#自定义布局">自定义布局</a>；
- 新增模糊度调节，具体亲参考章节：<a href="#关于模糊效果">关于模糊效果</a>；

v2.3.2:
- BottomMenu 支持直接使用 String[] 作为参数创建菜单；

v2.3.1:
- 新增 DialogSettings.dialog_cancelable_default 控制消息对话框、选择对话框和输入对话框默认是否可点击外部遮罩层区域关闭；
- 新增 DialogSettings.notification_text_color 控制 Notification 标题和消息默认颜色；
- 新增 DialogSettings.notification_text_size 控制 Notification 标题和消息默认字号；

v2.3.0:
- 对之前可能存在的内存泄漏的问题进行了修复；
- 重写对话框构造器，现在起，Kongzue Dialog 默认将不再采用模态化弹出方式，要需要模态化的弹出方式，请参阅<a href="#modal">“模态化”</a>章节；
- 此版本起，除 Notification 外的 Dialog 均提供额外的 build 方法用以只创建而不弹出，具体请参考<a href="#modal">“模态化”</a>章节；

v2.2.9.9:
- 修复 InputDialog 事件问题；

v2.2.9.8:
- 修复一些bug；

v2.2.9.7:
- 修复一些bug；

v2.2.9.6:
- 修复一些bug；

v2.2.9.5:
- 修复对话框的内存释放问题，提升健壮性；

v2.2.9.4:
- 修复TipDialog、WaitDialog释放问题，提升健壮性；

v2.2.9.3:
- 修复bug；
- 兼容性更新；

v2.2.9:
- 不再支持v1版本对话框库；

v2.2.8:
- 修复了对话框生命周期 dialogLifeCycleListener 存在的问题；

v2.2.7:
- 暂时移除了 WaitDialog.dismiss(); 后的延迟，该延迟可能导致 WindowLeaked 异常，解决方案后续版本更新；
- 修复对话框生命周期监听器不会重置的问题，现在在启动一个新的对话框时监听器会重置；
- TipDialog 提供了 show(context, tip, type) 快速创建方法；

v2.2.6:
- DialogSettings 新增属性 DEBUGMODE 以决定框架是否打印 log 日志；
- InputDialog 在点击确定后自动关闭输入法；

v2.2.5:
- 优化执行流程，修复可能出现的死锁问题：
```
经测试，在 Dialog 显示的情况下，遇到 Activity 被 finish() 掉后导致出现问题：android.view.WindowLeaked
此问题导致接下来用户程序在未结束掉全部进程的情况下，再无法重新启动任何对话框
在 2.2.5 版本中我们将提供方法 DialogSettings.unloadAllDialog()，您可以在 Activity 的 onDestroy() 事件执行此方法来关闭所有在队列中的 Dialog。
```
- 新增 DialogSettings.dialog_background_color 可控制 STYLE_MATERIAL 和 STYLE_KONGZUE 两种风格时对话框的背景色。

v2.2.4:
- TipDialog 和 WaitDialog 现在可以支持更多文字的扩展了，且最大行数限定为3行；
- 调整白色界面时模糊不透明度 180 → 200；

v2.2.3:
- 修复bug；
- 新增添加自定义布局，目前支持 MessageDialog、SelectDialog、InputDialog和BottomMenu，因为一些原因，选择 Material 风格时仅支持对话框全部使用自定义布局。

v2.2.2:
- InputDialog 点击确定后默认不关闭对话框，可以作出判断后再使用 dialog.dismiss() 关闭；

v2.2.1:
- 修复多次重复调用 WaitDialog 和 TipDialog 时可能存在的叠加问题；
- 底部菜单 BottomMenu 现已全部拒绝使用margin\padding - Horizontal\Vertical以避免部分设备上的兼容问题导致的布局显示错误；
- 统一背景遮罩层 40% 不透明度；

v2.2.0:
- 修复bug；
- 底部菜单支持取消按钮文字设置；
- ~~底部菜单支持菜单字号设置（dialog_menu_text_size）；~~(已在2.3.7版本弃用，具体请参照相关更新日志)

v2.1.9:
- Android Support 支持库升级到 27.1.0；

v2.1.8:
- 修复显示标题的底部菜单情况下第一位菜单按下状态不符的bug；

v2.1.7:
- 优化iOS对话框消失时的动画，与最新版本的iOS保持一致；
- 底部菜单新增标题功能；

v2.1.6:
- 底部菜单iOS风格时的线条颜色优化；

v2.1.5:
- iOS风格对话框线条颜色优化；

v2.1.4:
- 底部菜单iOS风格时的透明度优化；

v2.1.3:
- WaitDialog和TipDialog现在可以衔接了；
- 优化iOS模式下对话框的透明度和模糊程度；

v2.1.2:
- Android Support 支持库升级到 26.1.0；

v2.1.1:
- iOS主题的对话框、等待框以及提示框、底部菜单增加即时模糊效果；

v2.1.0:
- 新增底部菜单（BottomMenu）;
- 新增iOS对话框出现、消失动画；
- 修复一些细节bug；

v2.0.9:
- 序列启动对话框；

v2.0.8:
- 修复bug；

v2.0.7:
- 修复bug；

v2.0.6:
- 修复bug；

v2.0.5:
- 新增通知消息在使用Kongzue主题时可自定义颜色；

v2.0.4:
- 提供消息与通知功能；

v2.0.3:
- 提供部分文字大小和颜色修改；

v2.0.2:
- 提供Light和Dark两种黑白主题模式；

v2.0.1:
- TipDialog提供自定义提示图标；
- WaitDialog可通过dismiss()方法关闭等待提示框；

v2.0.0:
- 新增v2组件库；
