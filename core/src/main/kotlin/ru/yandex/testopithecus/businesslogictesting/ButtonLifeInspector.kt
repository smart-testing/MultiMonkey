package ru.yandex.testopithecus.businesslogictesting

import org.json.JSONObject
import ru.yandex.testopithecus.stateenricher.CvClient
import ru.yandex.testopithecus.utils.deserializeJSONObject
import java.lang.RuntimeException

class ButtonLifeInspector(private val cvClient: CvClient = CvClient("")) {

    private var before: String = ""
    private var after: String = ""

    fun loadScreenshotBeforeAction(screenshot: String) {
        before = screenshot
    }

    fun loadScreenshotAfterAction(screenshot: String) {
        after = screenshot
    }

    private fun createRequest(): String {
        return "{ \"before\": \"$before\"," +
                "\"after\": \"$after\"" +
                "}"
    }

    fun assertButtonLives() {
        if (before == "" || after == "") throw RuntimeException("ButtonLifeInspector: either \"before\" or \"after\" is empty")
        val request = createRequest()
        after = ""
        before = ""
        val response = cvClient.post(request)
        print(response)
        val isAlive = deserializeJSONObject(JSONObject(response))["button-alive"] as String
        if (isAlive.toInt() != 1){
            throw RuntimeException("Button is not responding")
        }
    }
}