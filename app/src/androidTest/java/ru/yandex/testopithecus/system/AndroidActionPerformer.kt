package ru.yandex.testopithecus.system

import android.util.Log
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import ru.yandex.testopithecus.exception.ServerErrorException
import ru.yandex.testopithecus.exception.SessionFinishedException
import ru.yandex.testopithecus.exception.TestFailException
import ru.yandex.testopithecus.exception.TestOkException
import ru.yandex.testopithecus.ui.ActionPerformer
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.utils.Reinstaller

class AndroidActionPerformer(
        private val device: UiDevice,
        private val pckg: String,
        private val apk: String,
        private val element: UiObject2?) : ActionPerformer {
    override fun perform(action: UiAction) {
        Log.d("ACTION PERFORMER", action.action)
        when (action.action) {
            "TAP" -> element?.click()
            "FILL" -> element!!.text = "aaa"
            "SKIP" -> {
            }
            "SCREENSHOT" -> {
                AndroidScreenshotManager.takeScreenshot(device, action.attributes["fake"]?.toBoolean() ?: false)
            }
            "FINISH" -> throw SessionFinishedException()
            "ERROR" -> throw ServerErrorException(action.attributes["message"])
            "RESTART" -> Reinstaller.reinstall(device, pckg, apk)
            "TEST_OK" -> throw TestOkException()
            "TEST_FAIL" -> throw TestFailException(action.attributes)
            else -> throw IllegalStateException("Unknown action '${action.action}'")
        }
    }
}