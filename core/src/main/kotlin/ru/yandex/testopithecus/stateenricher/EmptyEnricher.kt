package ru.yandex.testopithecus.stateenricher

import ru.yandex.testopithecus.ui.UiState

class EmptyEnricher : Enricher {

    override fun enrichState(uiState: UiState): UiState {
        return uiState
    }
}