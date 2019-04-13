package ru.yandex.testopithecus.system

import androidx.test.uiautomator.*
import ru.yandex.testopithecus.ui.UiAction

class SystemAction(action: UiAction, element: UiObject2?, private val device: UiDevice) {

    private val activity : () -> Unit

    init {
        val type = action.action
        activity = when (type) {
            "TAP" -> parseTap(element)
            else -> {
                {}
            }
        }
    }

    private fun parseTap(element: UiObject2?): () -> Unit {
        return { element?.click() }
    }

    fun perform() {
        activity()
    }

}