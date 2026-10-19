package com.xvsu.bluetoothconnect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import java.util.ArrayList;

import static android.content.res.Configuration.KEYBOARD_QWERTY;

/** 首页只负责模式选择和设备入口。布局、颜色分别在 activity_main.xml 和 ui_colors.xml。 */
public class MainActivity extends AppCompatActivity {
    private String screenSize = "1280x720";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bluetooth_settings).setOnClickListener(view ->
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS)));

        if (savedInstanceState != null) {
            screenSize = savedInstanceState.getString("keyboard_mode", "1280x720");
        } else if (getResources().getConfiguration().keyboard == KEYBOARD_QWERTY) {
            screenSize = "otg_real_keyboard";
        }

        // 原有按分辨率区分的屏幕键盘已统一自适应；首页只保留实际不同的输入方式。
        RadioGroup modes = findViewById(R.id.choose_size);
        addMode(modes, "完整屏幕键盘", "1280x720");
        addMode(modes, "系统输入法 + 辅助按键", "onscreen_keyboard");
        addMode(modes, "外接键盘 · 直接转发", "otg_real_keyboard");
        addMode(modes, "外接键盘 · 输入法转发", "otg_keyboard");
    }

    private void addMode(RadioGroup group, String label, String value) {
        MaterialRadioButton button = new MaterialRadioButton(this);
        button.setId(android.view.View.generateViewId());
        button.setText(label);
        button.setTextSize(14);
        group.addView(button, new RadioGroup.LayoutParams(-1, -2));
        button.setChecked(value.equals(screenSize));
        button.setOnCheckedChangeListener((view, checked) -> {
            if (checked) screenSize = value;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从系统蓝牙设置返回时刷新，显示刚刚配对的设备。
        LinearLayout devices = findViewById(R.id.choose_target);
        devices.removeAllViews();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || !adapter.isEnabled()) {
            showDeviceHint(devices, "蓝牙尚未开启，请在蓝牙设置中开启后返回。");
            return;
        }
        for (BluetoothDevice device : adapter.getBondedDevices()) {
            MaterialButton button = new MaterialButton(this, null,
                    com.google.android.material.R.attr.materialButtonOutlinedStyle);
            button.setText(device.getName());
            button.setAllCaps(false);
            button.setOnClickListener(view -> openKeyboard(device.getName()));
            devices.addView(button, new LinearLayout.LayoutParams(-1, -2));
        }
        if (devices.getChildCount() == 0) {
            showDeviceHint(devices, "暂无已配对设备\n请先在蓝牙设置中配对电脑，然后返回此页。");
        }
    }

    private void showDeviceHint(LinearLayout devices, String message) {
        TextView hint = new TextView(this);
        hint.setText(message);
        hint.setTextSize(14);
        hint.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.ui_muted));
        devices.addView(hint);
    }

    private void openKeyboard(String deviceName) {
        Class<?> activity;
        switch (screenSize) {
            case "onscreen_keyboard": activity = OnScreenKeyboardActivity.class; break;
            case "otg_real_keyboard": activity = OTGRealKeyboardActivity.class; break;
            case "otg_keyboard": activity = OTGKeyboardActivity.class; break;
            default: activity = CustomScreenKeyboardActivity.class; break;
        }
        ArrayList<String> name = new ArrayList<>();
        name.add(deviceName);
        name.add(screenSize);
        startActivity(new Intent(this, activity).putStringArrayListExtra("name", name));
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putString("keyboard_mode", screenSize);
        super.onSaveInstanceState(state);
    }
}
