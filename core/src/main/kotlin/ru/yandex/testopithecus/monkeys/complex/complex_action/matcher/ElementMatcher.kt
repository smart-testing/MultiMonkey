package ru.yandex.testopithecus.monkeys.complex.complex_action.matcher

import ru.yandex.testopithecus.monkeys.complex.complex_action.ElementDescription
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.util.*


interface ElementMatcher {

    fun match(state: UiState, elementDescription: ElementDescription): Optional<UiElement>

}