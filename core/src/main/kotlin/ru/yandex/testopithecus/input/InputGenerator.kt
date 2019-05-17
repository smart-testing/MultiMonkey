package ru.yandex.testopithecus.input

import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState

interface InputGenerator {
    fun suggestInput(input : UiElement, state : UiState) : String
}