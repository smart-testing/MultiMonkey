package ru.yandex.testopithecus.system

import androidx.test.uiautomator.UiDevice
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utils.deserializeAction
import ru.yandex.testopithecus.utils.serializeFeedback
import ru.yandex.testopithecus.utils.serializeUiState

import khttp.post as httpPost

class AndroidMonkeyHttp(
        mode: String,
        device: UiDevice,
        applicationPackage: String,
        apk: String, file:
        String? = null
) : AndroidMonkey(device, applicationPackage, apk) {

    init {
        httpPost("$URL$INIT$mode/${file ?: ""}")
    }

    override fun generateAction(uiState: UiState): UiAction {
        val response = httpPost(URL + GENERATE_ACTION, json = serializeUiState(uiState))
        return deserializeAction(response.jsonObject)
    }

    override fun feedback(feedback: UiFeedback) {
        httpPost(URL + FEEDBACK_URL, json = serializeFeedback(feedback))
    }

    companion object {
        private const val URL = "http://10.0.2.2:8080/"
        private const val GENERATE_ACTION = "generate-action/"
        private const val FEEDBACK_URL = "feedback/"
        private const val INIT = "init/"
    }
}