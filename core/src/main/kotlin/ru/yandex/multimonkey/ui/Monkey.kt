package ru.yandex.multimonkey.ui

import ru.yandex.multimonkey.ui.UiAction
import ru.yandex.multimonkey.ui.UiState

interface Monkey {

    fun generateAction(uiState: UiState): UiAction

    fun feedback()
}