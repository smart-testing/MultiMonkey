package ru.yandex.testopithecus.businesslogictesting

import org.json.JSONObject
import ru.yandex.testopithecus.stateenricher.CvClient
import ru.yandex.testopithecus.utils.deserializeJSONObject
import java.lang.RuntimeException

class ButtonLifeInspector(private val disabled: Boolean = true,
                          private val screenshotPerformer: () -> String,
                          private val cvClient: CvClient = CvClient("")) {

    private var before: String = ""
    private var after: String = ""

    fun loadScreenshotBeforeAction() {
        if (disabled) return
        val screenshot = screenshotPerformer()
        before = screenshot
    }

    fun loadScreenshotAfterAction() {
        if (disabled) return
        val screenshot = screenshotPerformer()
        after = screenshot
    }

    private fun createRequest(): String {
        return "{ \"before\": \"$before\"," +
                "\"after\": \"$after\"" +
                "}"
    }

    fun assertButtonLives() {
        if (disabled) return
        if (before == "" || after == "") throw RuntimeException("ButtonLifeInspector: either \"before\" or \"after\" is empty")
        val request = createRequest()
        after = ""
        before = ""
        val response = cvClient.post(request)
        print(response)
        val isAlive = deserializeJSONObject(JSONObject(response))["button-alive"] as String
        assert(isAlive.toInt() == 1)
    }
}