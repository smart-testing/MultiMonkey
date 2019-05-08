package ru.yandex.testopithecus.stateFilter

import org.json.JSONObject
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utilsCore.deserializeJSONObject
import ru.yandex.testopithecus.utilsCore.serializeUiState

class Filter(cvServerAddress: String) {

    private val cvClient = CvClient(cvServerAddress)

    private fun parseServerResponse(response: String): List<Any> {
        val jsonObject = deserializeJSONObject(JSONObject(response))
        return jsonObject["detected"] as List<Any>
    }

    private fun performRequest(uiState: UiState): List<Any> {
        val json = serializeUiState(uiState)
        val response = cvClient.post(json.toString())
        return parseServerResponse(response)
    }

    private fun applyFilter(uiState: UiState, toFilter: List<Int>): UiState {
        uiState.elements.forEachIndexed { index, _ -> index !in toFilter }
        return uiState
    }

    fun filterStates(uiState: UiState): UiState {
        val toFilter = performRequest(uiState)
        return applyFilter(uiState, toFilter as List<Int>)
    }
}