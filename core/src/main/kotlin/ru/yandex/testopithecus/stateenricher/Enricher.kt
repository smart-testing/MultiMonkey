package ru.yandex.testopithecus.stateenricher

import ru.yandex.testopithecus.ui.UiState

interface Enricher {

    fun enrichState(uiState: UiState): UiState
}