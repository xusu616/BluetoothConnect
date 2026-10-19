# 界面修改指南

- 首页布局：`app/src/main/res/layout/activity_main.xml`。设备和模式各占一栏，支持独立滚动。
- 主键盘布局：`app/src/main/res/layout/activity_keyboard_adaptive.xml`。五行等高；修改 `android:layout_weight` 调整键宽。按钮 ID 对应 `CustomScreenKeyboardActivity.kt` 中的键码映射。
- 功能键栏：`app/src/main/java/com/xvsu/bluetoothconnect/KeyboardFunctionBar.kt`。`keys` 列表控制标签、顺序和 Android 键码；前 13 项为 Esc 和 F1～F12，剩余项为导航键。
- 键帽样式：`app/src/main/res/values/styles.xml` 中的 `Widget.BluetoothConnect.Key` 和 `SpecialKey`。统一修改圆角、字号、间距和内边距。
- 浅色 / 深色配色：`app/src/main/res/values/ui_colors.xml` 和 `values-night/ui_colors.xml`。
- 系统输入法辅助键：`layout/activity_onscreenkeyboard.xml` 和 `layout-land/activity_onscreenkeyboard.xml`。
- 首页模式入口：`MainActivity.java` 的 `addMode` 调用和 `openKeyboard`。保留原 Java 文件语言，新辅助代码使用 Kotlin。

原来按 1280×720、2340×1080 分辨率区分的屏幕键盘入口已统一使用自适应布局。旧布局文件保留供参考，目前主键盘不再加载它们。

修饰键在点击后高亮，再次点击可以取消；普通按键发送并复位后取消高亮。布局调整不涉及蓝牙 HID 描述符。
