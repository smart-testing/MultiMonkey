package ru.yandex.testopithecus.system

import androidx.test.uiautomator.UiDevice
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utils.deserializeAction
import ru.yandex.testopithecus.utils.serializeFeedback
import ru.yandex.testopithecus.utils.serializeUiState

import khttp.post as httpPost

class AndroidMonkeyHttp(device: UiDevice, applicationPackage: String, apk: String) :
        AndroidMonkey(device, applicationPackage, apk) {
    override fun generateAction(uiState: UiState): UiAction {
        val response = httpPost(FEEDBACK_URL, json = serializeUiState(uiState))
        return deserializeAction(response.jsonObject)
    }

    override fun feedback(feedback: UiFeedback) {
        httpPost(FEEDBACK_URL, json = serializeFeedback(feedback))
    }

    companion object {
        private const val GENERATE_ACTION_URL = "http://10.0.2.2:8080/generate-action"
        private const val FEEDBACK_URL = "http://10.0.2.2:8080/feedback"
    }
}