package ru.yandex.testopithecus.stateFilter

import org.json.JSONObject
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utils.deserializeJSONObject
import ru.yandex.testopithecus.utils.serializeUiState

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
        return UiState(uiState.elements.filter { it.id.toInt() !in toFilter }, uiState.global)
    }

    fun filterStates(uiState: UiState): UiState {
        val toFilter = performRequest(uiState) as List<Int>
        return applyFilter(uiState, toFilter)
    }
}