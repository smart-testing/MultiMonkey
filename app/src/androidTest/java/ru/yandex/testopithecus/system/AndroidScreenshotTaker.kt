package ru.yandex.testopithecus.system

import android.content.Context
import android.util.Base64
import androidx.test.core.app.ApplicationProvider
import androidx.test.uiautomator.UiDevice
import java.io.File

import khttp.post as httpPost

object AndroidScreenshotTaker {

    fun takeScreenshot(device: UiDevice, fake: Boolean) {
        if (fake) {
            val message = "{\"screenshot\": \"\"}"
            httpPost(URL + LOG, data = message)
            return
        }
        val context = ApplicationProvider.getApplicationContext<Context>()
        val scrFile = File(context.filesDir, "scr.png")
        device.takeScreenshot(scrFile)
        val bytes = scrFile.readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
        val message = "{\"screenshot\": \"$base64\"}"
        httpPost(URL + LOG, data = message)
    }

    private const val URL = "http://10.0.2.2:8080/"
    private const val LOG = "log"
}