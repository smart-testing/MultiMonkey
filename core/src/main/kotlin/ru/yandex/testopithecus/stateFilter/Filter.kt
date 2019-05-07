package ru.yandex.testopithecus.stateFilter

import org.json.JSONArray
import org.json.JSONObject
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utils.deserializeJSONArray
import ru.yandex.testopithecus.utils.deserializeJSONObject
import ru.yandex.testopithecus.utils.serializeUiState

class Filter(private val cvServerAddress: String) {


    private val cvClient = CvClient(cvServerAddress)

    private fun parseServerResponse(response: String): List<Any> {
        val jsonObject = deserializeJSONObject(JSONObject(response))
        return deserializeJSONArray(jsonObject["detected"] as JSONArray)
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