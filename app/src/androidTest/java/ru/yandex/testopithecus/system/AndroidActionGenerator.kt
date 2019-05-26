package ru.yandex.testopithecus.system

import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState

interface AndroidActionGenerator {

    fun generateAction(uiState: UiState): UiAction

    fun feedback(feedback: UiFeedback)
}