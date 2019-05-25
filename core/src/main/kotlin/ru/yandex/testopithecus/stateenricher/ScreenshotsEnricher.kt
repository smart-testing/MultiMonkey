package ru.yandex.testopithecus.stateenricher

import org.json.JSONObject
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utils.deserializeJSONObject
import ru.yandex.testopithecus.utils.serializeUiState

class ScreenshotsEnricher(cvServerAddress: String, enricher: Enricher) : EnricherDecorator(enricher) {

    private val cvClient = if (cvServerAddress != "") CvClient(cvServerAddress) else null

    private fun parseServerResponse(response: String): List<Int> {
        val jsonObject = deserializeJSONObject(JSONObject(response))
        @Suppress("UNCHECKED_CAST")
        return jsonObject["detected"] as List<Int>
    }

    private fun performRequest(uiState: UiState): List<Int> {
        val json = serializeUiState(uiState)
        val response = cvClient!!.post(json.toString())
        return parseServerResponse(response)
    }

    private fun applyEnrichment(uiState: UiState, toFilter: List<Int>): UiState {
        return UiState(uiState.elements.filter { it.id.toInt() !in toFilter }, uiState.global)
    }

    override fun enrichState(uiState: UiState): UiState {
        if (cvClient == null) return uiState
        val toFilter = performRequest(uiState)
        val result = applyEnrichment(uiState, toFilter)
        return super.enrichState(result)
    }
}