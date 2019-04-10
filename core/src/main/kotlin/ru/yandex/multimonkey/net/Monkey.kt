package ru.yandex.multimonkey.net

interface Monkey {

    fun generateAction(uiState: UiState): UiAction

    fun feedback()
}