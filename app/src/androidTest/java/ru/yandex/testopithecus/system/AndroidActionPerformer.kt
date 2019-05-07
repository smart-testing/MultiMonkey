package ru.yandex.testopithecus.system

import androidx.test.uiautomator.*
import ru.yandex.testopithecus.ui.ActionPerformer
import ru.yandex.testopithecus.ui.UiAction

class AndroidActionPerformer(private val element: UiObject2) : ActionPerformer {
    override fun perform(action: UiAction) {
        when (action.action) {
            "TAP" -> {
                element.click()
            }
            "INPUT" -> {
                element.text = action.attributes["text"]
                element.click()
            }
            else -> throw IllegalStateException("Unknown action '${action.action}'")
        }
    }
}