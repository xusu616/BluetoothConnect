package com.xvsu.bluetoothconnect

import android.app.Activity
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton

/** 横屏辅助键栏：在这里修改标签、顺序和 Android 键码。 */
object KeyboardFunctionBar {
    private val keys = listOf(
        "Esc" to KeyEvent.KEYCODE_ESCAPE,
        "F1" to KeyEvent.KEYCODE_F1, "F2" to KeyEvent.KEYCODE_F2,
        "F3" to KeyEvent.KEYCODE_F3, "F4" to KeyEvent.KEYCODE_F4,
        "F5" to KeyEvent.KEYCODE_F5, "F6" to KeyEvent.KEYCODE_F6,
        "F7" to KeyEvent.KEYCODE_F7, "F8" to KeyEvent.KEYCODE_F8,
        "F9" to KeyEvent.KEYCODE_F9, "F10" to KeyEvent.KEYCODE_F10,
        "F11" to KeyEvent.KEYCODE_F11, "F12" to KeyEvent.KEYCODE_F12,
        "Insert" to KeyEvent.KEYCODE_INSERT, "Delete" to KeyEvent.KEYCODE_FORWARD_DEL,
        "Home" to KeyEvent.KEYCODE_MOVE_HOME, "End" to KeyEvent.KEYCODE_MOVE_END,
        "PgUp" to KeyEvent.KEYCODE_PAGE_UP, "PgDn" to KeyEvent.KEYCODE_PAGE_DOWN,
        "PrtSc" to KeyEvent.KEYCODE_SYSRQ, "Scroll" to KeyEvent.KEYCODE_SCROLL_LOCK,
        "Pause" to KeyEvent.KEYCODE_BREAK
    )

    fun install(activity: Activity, sendKey: (Int) -> Unit) {
        val content = activity.findViewById<ViewGroup>(android.R.id.content)
        val keyboard = content.getChildAt(0)
        content.removeView(keyboard)
        val density = activity.resources.displayMetrics.density
        fun dp(value: Int) = (value * density).toInt()
        val column = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(8), dp(4), dp(8), dp(4))
            setBackgroundResource(R.color.ui_background)
        }
        val titleRow = LinearLayout(activity).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
        }
        titleRow.addView(MaterialButton(activity, null, com.google.android.material.R.attr.borderlessButtonStyle).apply {
            text = "‹  设备"
            textSize = 12f
            minWidth = 0
            minimumWidth = 0
            setPadding(0, 0, 0, 0)
            insetTop = 0
            insetBottom = 0
            setOnClickListener { activity.finish() }
        }, LinearLayout.LayoutParams(dp(72), -1))
        titleRow.addView(android.widget.TextView(activity).apply {
            text = "蓝牙键盘"
            textSize = 15f
            setTextColor(androidx.core.content.ContextCompat.getColor(activity, R.color.ui_text))
        }, LinearLayout.LayoutParams(0, -2, 1f))
        titleRow.addView(android.widget.TextView(activity).apply {
            text = "修饰键：点选后与下一键组合"
            textSize = 11f
            setTextColor(androidx.core.content.ContextCompat.getColor(activity, R.color.ui_muted))
        })
        column.addView(titleRow, LinearLayout.LayoutParams(-1, dp(32)))
        // F1～F12 全部同时可见，导航键独立成行。
        for (group in listOf(keys.take(13), keys.drop(13))) {
            val row = LinearLayout(activity).apply { orientation = LinearLayout.HORIZONTAL }
            for ((label, code) in group) {
                val button = activity.layoutInflater.inflate(R.layout.keyboard_function_key, row, false) as MaterialButton
                button.text = label
                button.contentDescription = label
                button.setOnClickListener { sendKey(code) }
                row.addView(button)
            }
            column.addView(row, LinearLayout.LayoutParams(-1, dp(42)))
        }
        column.addView(keyboard, LinearLayout.LayoutParams(-1, 0, 1f))
        content.addView(column, ViewGroup.LayoutParams(-1, -1))
    }
}
