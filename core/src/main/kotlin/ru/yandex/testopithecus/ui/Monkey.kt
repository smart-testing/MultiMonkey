package ru.yandex.testopithecus.ui

interface Monkey {

    fun generateAction(uiState: UiState): UiAction

    fun feedback()
}