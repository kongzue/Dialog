# 空祖家的Dialog
空祖家的通用对话框。此项目是用来演示如何封装一个Android Library的，具体封装和发布步骤详见简书：http://www.jianshu.com/p/ccac7ac0b819

空祖家的对话框拥有简洁鲜明的样式，提供绿色、蓝色、橙色和灰色4种主题颜色以及消息提示框、选择框、输入框三种，以及一种载入对话框，各自提供两种调用方式。

![提示框](http://upload-images.jianshu.io/upload_images/1976622-ee730c81091a968f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![选择框](http://upload-images.jianshu.io/upload_images/1976622-6d4a84d821415b2a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![输入框](http://upload-images.jianshu.io/upload_images/1976622-ac15660c751edffb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

本例中，包含DialogDemo（Dialog/app/）是对话框的演示项目源代码，以及Library库（Dialog/dialog/）是封装的空祖家对话框的源代码。

项目托管的Maven仓库在https://bintray.com/myzchh/maven/dialog

本项目遵循Apache-2.0开源协议，具体可参考：http://www.opensource.org/licenses/apache2.0.php

## Maven仓库或Gradle的引用方式
Maven仓库：
```
<dependency>
  <groupId>com.kongzue.dialog</groupId>
  <artifactId>dialog</artifactId>
  <version>1.1.0</version>
  <type>pom</type>
</dependency>
```
Gradle：
先在app项目的build.gradle中添加
```
repositories {
    maven {
        url  "http://dl.bintray.com/myzchh/maven"
    }
}
```
在dependencies{}中添加引用：
```
compile 'com.kongzue.dialog:dialog:1.1.0'
```

## 使用说明
三种主体色的配置值在DialogThemeColor类中，下文中的colorId就是从中获取的，

蓝色对应DialogThemeColor.COLOR_BLUE，

绿色对应DialogThemeColor.COLOR_GREEN，

橙色对应DialogThemeColor.COLOR_ORANGE，

灰色对应DialogThemeColor.COLOR_GRAY。

使用快速模式的情况下可以设置默认主题颜色：

```
DialogThemeColor.normalColor = DialogThemeColor.COLOR_BLUE;
```

### 调用消息对话框的方法：
```
MessageDialog messageDialog = new MessageDialog(MainActivity.this)
                        .setTitle("消息提示框")
                        .setTipText("用于提示一些消息")
                        .setThemeColor(colorId)
                        .setPositiveButtonText("知道了")
                        .setPositiveButtonClickListener(null)          //如果没有要点击的事件可以直接传null
                        .show();
```
或者可以采用快速调用方式：
```
MessageDialog.show(MainActivity.this,"消息提示框","用于提示一些消息","知道了",null);
```

### 调用选择对话框的方法：
```
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
```
或者可以采用快速调用方式：
```
SelectDialog.show(MainActivity.this, "选择框", "请做出你的选择", "积极选择", "消极选择", new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(MainActivity.this, "你做出了积极的选择", Toast.LENGTH_LONG).show();
    }
}, new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(MainActivity.this, "你做出了消极的选择", Toast.LENGTH_LONG).show();
    }
});
```

### 调用输入对话框：
```
InputDialog inputDialog = new InputDialog(MainActivity.this)
                        .setTitle("请输入文字")
                        .setInputHintText("这里是提示文字")
                        .setThemeColor(colorId)                   //设置主题颜色
                        .setOnPositiveButtonClickListener(new InputDialogCallbackClickListener() {      //输入对话框回调方法
                            @Override
                            public void onClick(View v, String inputText) {
                                Toast.makeText(MainActivity.this, "你输入的是：" + inputText, Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
```
或者可以采用快速调用方式：
```
InputDialog.show(MainActivity.this, new InputDialogCallbackClickListener() {
                    @Override
                    public void onClick(View v, String inputText) {
                        Toast.makeText(MainActivity.this, "你输入的是：" + inputText, Toast.LENGTH_LONG).show();
                    }
                },"请输入文字","这里是提示文字");
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
