package ru.yandex.testopithecus.monkeys.state.actionGenerators

import ru.yandex.testopithecus.input.InputFiller
import ru.yandex.testopithecus.ui.*

class StateActionsGeneratorImpl : StateActionsGenerator {
    override fun getActions(state: UiState): List<UiAction> {
        val actions = mutableListOf<UiAction>()
        state.elements.stream()
                .forEach { element -> addActionsToList(actions, element, state) }
        actions.add(skipAction())
        return actions
    }

    private fun addActionsToList(actions: MutableList<UiAction>, element: UiElement, state: UiState) {
        if (element.possibleActions.contains("TAP")) {
            actions.add(constructTapAction(element))
        }
        if (element.possibleActions.contains("INPUT")) {
            actions.add(constructInputAction(element, state))
        }
    }

    private fun constructTapAction(element: UiElement): UiAction {
        return tapAction(element)
    }

    private fun constructInputAction(element: UiElement, state: UiState): UiAction {
        val suggestedText = InputFiller.suggestInput(element, state)
        return fillAction(element, suggestedText)
    }
}