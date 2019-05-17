package ru.yandex.testopithecus.monkeys.complex.complex_action.matcher

import ru.yandex.testopithecus.monkeys.complex.complex_action.ElementDescription
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.util.*


class ExactElementMatcher: ElementMatcher {

    override fun match(state: UiState, elementDescription: ElementDescription): Optional<UiElement> {
        for (element in state.elements) {
            if (element.attributes == elementDescription.attributes) {
                return Optional.of(element)
            }
        }
        return Optional.empty()
    }

}