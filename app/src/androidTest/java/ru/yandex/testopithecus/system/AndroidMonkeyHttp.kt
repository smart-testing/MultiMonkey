package ru.yandex.testopithecus.system

import androidx.test.uiautomator.UiDevice
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utilsCore.deserializeAction
import ru.yandex.testopithecus.utilsCore.serializeUiState

import khttp.post as httpPost

class AndroidMonkeyHttp(device: UiDevice, applicationPackage: String, apk: String) :
        AndroidMonkey(device, applicationPackage, apk) {

    override fun generateAction(uiState: UiState): UiAction {
        val response = httpPost(URL, json = serializeUiState(uiState))
        return deserializeAction(response.jsonObject)
    }

    companion object {
        private const val URL = "http://10.0.2.2:8080/generate-action"
    }
}