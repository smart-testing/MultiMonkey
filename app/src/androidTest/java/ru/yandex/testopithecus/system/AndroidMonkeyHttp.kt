package ru.yandex.testopithecus.system

import khttp.post
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utils.deserializeAction
import ru.yandex.testopithecus.utils.serializeFeedback
import ru.yandex.testopithecus.utils.serializeUiState

class AndroidMonkeyHttp(url: String = "", mode: String = "", file: String? = null) : Monkey {

    companion object {
        private const val URL = "http://10.0.2.2:8080/"
        private const val FEEDBACK_URL = "feedback/"
        private const val GENERATE_ACTION = "generate-action/"
        private const val INIT = "init/"
    }

    init {
        if (url == "") {
            post("$URL$INIT$mode/${file ?: ""}")
        }
    }

    override fun generateAction(uiState: UiState): UiAction {
        val response = post(URL + GENERATE_ACTION, json = serializeUiState(uiState))
        return deserializeAction(response.jsonObject)
    }

    override fun feedback(feedback: UiFeedback) {
        post(URL + FEEDBACK_URL, json = serializeFeedback(feedback))
    }
}