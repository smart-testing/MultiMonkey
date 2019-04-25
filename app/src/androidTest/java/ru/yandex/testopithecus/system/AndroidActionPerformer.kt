package ru.yandex.testopithecus.system

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import ru.yandex.testopithecus.exception.SessionFinishedException
import ru.yandex.testopithecus.ui.ActionPerformer
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.utils.Reinstaller

class AndroidActionPerformer(
        private val device: UiDevice,
        private val pckg: String,
        private val apk: String,
        private val element: UiObject2?) : ActionPerformer {
    override fun perform(action: UiAction) {
        when (action.action) {
            "TAP" -> element?.click()
            "FILL" -> element!!.text = "aaa"
            "SKIP" -> {
            }
            "FINISH" -> throw SessionFinishedException()
            "RESTART" -> Reinstaller.reinstall(device, pckg, apk)
            else -> throw IllegalStateException("Unknown action '${action.action}'")
        }
    }
}