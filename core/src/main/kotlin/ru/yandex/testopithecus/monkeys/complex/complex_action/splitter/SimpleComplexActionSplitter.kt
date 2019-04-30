package ru.yandex.testopithecus.monkeys.complex.complex_action.splitter

import ru.yandex.testopithecus.monkeys.complex.complex_action.ActionDescription
import ru.yandex.testopithecus.monkeys.complex.complex_action.ComplexAction
import ru.yandex.testopithecus.monkeys.complex.complex_action.ElementDescription
import ru.yandex.testopithecus.monkeys.complex.complex_action.matcher.ElementMatcher
import ru.yandex.testopithecus.monkeys.complex.complex_action.matcher.ExactElementMatcher
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import java.util.*


class SimpleComplexActionSplitter: ComplexActionSplitter {

    private val elementMatcher: ElementMatcher = ExactElementMatcher()
    private var complexAction: Optional<ComplexAction> = Optional.empty()
    private var index = 0

    override fun isEmpty(): Boolean {
        return !complexAction.isPresent
    }

    override fun getNextAction(state: UiState): UiAction {
        val (elementDescription, actionDescription) = complexAction.get().actionDescriptions[index]
        val id = getIdForAction(elementDescription, actionDescription, state)
        val action = UiAction(id, actionDescription.action, actionDescription.attributes)
        ++index
        updateOptionalIfEmpty()
        return action
    }

    override fun pushComplexAction(action: ComplexAction) {
        complexAction = Optional.of(action)
        index = 0
        updateOptionalIfEmpty()
    }

    private fun updateOptionalIfEmpty() {
        if (index == complexAction.get().actionDescriptions.size) {
            complexAction = Optional.empty()
        }
    }

    private fun getIdForAction(elementDescription: ElementDescription, actionDescription: ActionDescription, state: UiState): String? {
        if (actionDescription.attributes["global"] == "true") {
            return null
        }
        val element = elementMatcher.match(state, elementDescription)
        if (!element.isPresent) {
            throw NoMatchingElementFoundException()
        }
        return element.get().id
    }

}