package ru.yandex.multimonkey.system

import androidx.test.uiautomator.*
import ru.yandex.multimonkey.net.UiAction
import org.json.JSONObject

class SystemAction(action: UiAction, private val device: UiDevice) {

    private val activity : () -> Unit

    init {
        val type = action.data.get("type")
        activity = when (type) {
            "TAP" -> parseTap(action.data)
            else -> {
                {}
            }
        }
    }

    private fun parseTap(data: JSONObject): () -> Unit {
        val position = data.getJSONObject("position")
        return { device.click(position.getInt("x"), position.getInt("y")) }
    }

    fun perform() {
        activity()
    }

}