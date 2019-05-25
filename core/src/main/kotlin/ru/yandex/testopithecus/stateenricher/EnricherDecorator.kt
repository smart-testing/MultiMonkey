package ru.yandex.testopithecus.stateenricher

import ru.yandex.testopithecus.ui.UiState

abstract class EnricherDecorator(protected val enricher: Enricher) : Enricher {

    override fun enrichState(uiState: UiState): UiState {
        return enricher.enrichState(uiState)
    }
}