package ru.yandex.testopithecus.system

import android.content.Context
import android.util.Base64
import androidx.test.core.app.ApplicationProvider
import androidx.test.uiautomator.UiDevice
import org.json.JSONObject
import ru.yandex.testopithecus.utils.deserializeAttributes
import java.io.File

import khttp.post as httpPost

object AndroidScreenshotManager {

    fun takeScreenshot(device: UiDevice, fake: Boolean) {
        if (fake) {
            val message = "{\"screenshot\": \"\"}"
            httpPost(URL + LOG, data = message)
            return
        }
        val scrFile = File(context.filesDir, "scr.png")
        device.takeScreenshot(scrFile)
        val bytes = scrFile.readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        val message = "{\"screenshot\": \"$base64\"}"
        httpPost(URL + LOG, data = message)
    }

    fun parseScreenshotAndSave(screenshot: String, name: String) {
        val file = File(context.filesDir, "$name.png")
        file.delete()
        val screenshotAsString = deserializeAttributes(JSONObject(screenshot))["screenshot"]
        file.writeBytes(Base64.decode(screenshotAsString, Base64.NO_WRAP))
    }

    private val context: Context = ApplicationProvider.getApplicationContext<Context>()
    private const val URL = "http://10.0.2.2:8080/"
    private const val LOG = "log"
}