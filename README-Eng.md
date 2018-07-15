# Kongzue's Dialog 2.2
To the products that require Android to follow apple's design (XD

<a href="https://github.com/kongzue/Dialog/">
<img src="https://img.shields.io/badge/Kongzue%20Dialog-2.2.7-green.svg" alt="Kongzue Dialog">
</a> 
<a href="https://bintray.com/myzchh/maven/dialog/2.2.7/link">
<img src="https://img.shields.io/badge/Maven-2.2.7-blue.svg" alt="Maven">
</a> 
<a href="http://www.apache.org/licenses/LICENSE-2.0">
<img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="Maven">
</a> 
<a href="http://www.kongzue.com">
<img src="https://img.shields.io/badge/Homepage-Kongzue.com-brightgreen.svg" alt="Maven">
</a> 

Kongzue's Dialog have pop-up styles that provide the simplest way to invoke a message box, a selection box, an input box, a wait prompt, a warning prompt, a completion prompt, an error prompt, and so on. The following is a preview of all dialog styles currently included:

![Kongzue's Dialog](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.0.png)

The trial can be downloaded from http://fir.im/kongzueDial

![Kongzue's Dialog Demo](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/KongzueDialogDemoDownload.png)

For example,this repository has DialogDemo(Dialog/app/)is the demo project source code，Library（Dialog/dialog/）is source code of Kongzue's Dialog.

Maven: https://bintray.com/myzchh/maven/dialog

This project follows the Apache - 2.0 open source protocol, which can be referenced: http://www.opensource.org/licenses/apache2.0.php

## Maven & Gradle
Maven：
```
<dependency>
  <groupId>com.kongzue.dialog</groupId>
  <artifactId>dialog</artifactId>
  <version>2.2.7</version>
  <type>pom</type>
</dependency>
```
Gradle：
```
implementation 'com.kongzue.dialog:dialog:2.2.7'
```

## Explanation
1) Initialization style of Kongzue's Dialog:
```
DialogSettings.type = TYPE_MATERIAL;
```

Material style: DialogSettings.TYPE_MATERIAL，

Kongzue style: DialogSettings.TYPE_KONGZUE，

iOS style: DialogSettings.TYPE_IOS

Style settings are only for dialog boxes, not include prompt and wait boxes

2) Light & Dark Mode：
```
DialogSettings.tip_theme = THEME_LIGHT;         //Set the prompt box theme to a light color theme
DialogSettings.dialog_theme = THEME_DARK;       //Set the dialog box theme to a dark color theme
```

Preview:
![Kongzue's Dialog Light&DarkMode](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.0_dark.png)

3) Starting with version 2.0.4, provides notification capabilities that do not require suspend window permissions and can be displayed across domains, as shown in the following figure:
![Kongzue's Dialog Notifaction](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.0.4_notification.png)

4) Starting with version 2.1.0, the bottom menu is available and is available through com.kongzue.dialog.v2.BottomMenu, as shown in the following figure:
![Kongzue's Dialog BottomMenu](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.1.0_bottommenu.png)

5) Starting with version 2.1.1, iOS style dialogs, prompt boxes, and bottom menus add instant blur effects:
![Kongzue's Dialog Blur](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/Kongzue%20Dialog%202.1.1_blur.png)

## About v2 packages
In the empty ancestors dialog component, still retains the generation of component library but is no longer recommended, this is to maintain compatibility, if forced to use you will see the name of the relevant class has a strikethrough.

For more efficient development, we now strongly recommend using the v2 component library directly, which contains package addresses of: com.kongzue.dialog.v2

### About blur mode
You can set blur mode by properties:
```
DialogSettings.use_blur = true;                 //Sets whether obfuscation is enabled
```
If you need to start, also need in your build.gradle(app) add code：
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
The blur effect is currently valid only for three dialog boxes, prompt boxes, wait boxes, and bottom menus when DialogSettings.type = TYPE_IOS.

### Message Dialog：
```
MessageDialog.show(me, "Title", "message", "ok", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
});
```
Fast Function：
```
MessageDialog.show(me, "Welcome", "Welcome to the kongzu home dialog box, which provides several common dialog box styles.\nIf you have any questions can be in https://github.com/kongzue/Dialog submit feedback");
```

### Selection Dialog：
```
SelectDialog.show(me, "Title", "message", "ok", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(me, "You click ok button.", Toast.LENGTH_SHORT).show();
    }
}, "Cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(me, "You click cancel button.", Toast.LENGTH_SHORT).show();
    }
});
```
Fast Function：
```
SelectDialog.show(me, "Title", "message", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(me, "You click ok button.", Toast.LENGTH_SHORT).show();
    }
});
```

### Input Dialog：
```
InputDialog.show(me, "Set a nickname", "Set a nice name", "ok", new InputDialogOkButtonClickListener() {
    @Override
    public void onClick(Dialog dialog, String inputText) {
        Toast.makeText(me, "You entered:" + inputText, Toast.LENGTH_SHORT).show();
    }
}, "Cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
});
```
Fast Function：
```
InputDialog.show(me, "Set a nickname", "Set a nice name", new InputDialogOkButtonClickListener() {
                    @Override
                    public void onClick(Dialog dialog, String inputText) {
                        Toast.makeText(me, "You entered:" + inputText, Toast.LENGTH_SHORT).show();
                    }
                });
```

### Wait Dialog：
```
WaitDialog.show(me, "Loading...");
```

### Tip Dialog - Finish：
```
TipDialog.show(me, "Finish", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
```

### Tip Dialog - Warning：
```
TipDialog.show(me, "Warning", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_WARNING);
```

### Tip Dialog - Error：
```
TipDialog.show(me, "Error", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_ERROR);
```

### Notifaction：

Note: that the notification class from com.kongzue.dialog.v2 is used here.

The main difference between the notification message and the prompt frame is that the prompt frame will interrupt the operation used, and the message notification will not. the message notification is suitable for the business scenario in which the user needs to be reminded whether to process the message or not, and the prompt frame is suitable for the business scenario in which the user operation is blocked and the user is reminded of the current situation.

```
Notification.show(me, id, iconResId, getString(R.string.app_name), "This is a message", Notification.SHOW_TIME_LONG, notifactionType)
                        .setOnNotificationClickListener(new Notification.OnNotificationClickListener() {
                            @Override
                            public void OnClick(int id) {
                                Toast.makeText(me,"Click the notification",SHOW_TIME_SHORT).show();
                            }
                        })
                ;
```
Where the id is the notification message id and is called back in the OnNotificationClickListener after the user clicks on the notification.

Note: that the message type colorType here is currently valid only for the Kongzue Style, and the styles provided are:

type | color | Default
---|---|---
TYPE_NORMAL | Grey black | Yes
TYPE_FINISH | Green | No
TYPE_WARNING | Orange | No
TYPE_ERROR | Red | No

After version 2.0.6, if the colorType is not the above setting, you can use the autoshape value, which is the return value of the Color class.

Fast Function：
```
Notification.show(me, 0, "", "This is a message", Notification.SHOW_TIME_SHORT, notifactionType);
```

### Bottom Menu：

Note: that the BottomMenu class from com.kongzue.dialog.v2 is used here.
```
List<String> list = new ArrayList<>();
list.add("Menu 1");
list.add("Menu 2");
list.add("Menu 3");
BottomMenu.show(me, list, new OnMenuItemClickListener() {
    @Override
    public void onClick(String text, int index) {
        Toast.makeText(me,"Menu " + text + " be click.",SHOW_TIME_SHORT).show();
    }
},true);
```

Add title:
```
BottomMenu.show(me, list).setTitle("这里是标题测试");
```

Note: This menu is temporarily unaffected by night mode (THEME_DARK) and only provides light theme, but does not preclude subsequent versions from updating it.

When using iOS style,DialogSettings.ios_normal_button_color affects the color of the menu content text, and other topics are not affected by this property.

Fast Function：
```
List<String> list = new ArrayList<>();
list.add("Menu 1");
list.add("Menu 2");
list.add("Menu 3");
BottomMenu.show(me, list);
```

## Custom Layout：
Custom layouts for message dialog, select dialog, input dialog, and bottom menu are supported from version 2.2.3. for some reasons, only dialog boxes using custom layouts are supported when selecting the material style.

exp.
```
//initViews：
View customView = LayoutInflater.from(me).inflate(R.layout.layout_custom, null);
//startDialog
MessageDialog.show(me, null, null, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCustomView(customView);
```

## additional function：
In any kind of dialog box can use .setCanCancel(boolean) to set whether you can click on the dialog box outside the area closed dialog box, prompt class is prohibited by default, select, input dialog box is prohibited by default, the message dialog box is allowed by default.

Use the method to refer to the following code:
```
WaitDialog.show(me, "Loading...").setCanCancel(true);
```
In the empty ancestors dialog component, you can use the listener to listen to the dialog lifecycle, demo is as follows:
```
WaitDialog.show(me, "Loading...").setCanCancel(true).setDialogLifeCycleListener(new DialogLifeCycleListener() {
    @Override
    public void onCreate(AlertDialog alertDialog) {
        //dialog building
    }
    @Override
    public void onShow(AlertDialog alertDialog) {
        //dialog show
    }
    @Override
    public void onDismiss() {
        //dialog close
    }
});
```

Additional custom properties are available:
```
DialogSettings.dialog_title_text_size = -1;     //Set dialog title text size, <= 0 is not enabled
DialogSettings.dialog_message_text_size = -1;   //Set dialog box content text size, <= 0 is not enabled
DialogSettings.dialog_button_text_size = -1;    //Set dialog button text size, <= 0 is not enabled
DialogSettings.dialog_menu_text_size = -1;      //Set bottom menu text size, <= 0 is not enabled
DialogSettings.tip_text_size = -1;              //Set prompt box text size, <= 0 is not enabled
DialogSettings.ios_normal_button_color = -1;    //Set IOs style default button text color, = -1 not enabled
```

## License
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
