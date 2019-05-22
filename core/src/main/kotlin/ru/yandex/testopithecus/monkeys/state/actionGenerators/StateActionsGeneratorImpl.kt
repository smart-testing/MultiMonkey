package ru.yandex.testopithecus.monkeys.state.actionGenerators

import ru.yandex.testopithecus.ui.*

class StateActionsGeneratorImpl : StateActionsGenerator {

    override fun getActions(state: UiState) : List<UiAction> {
        val actions = mutableListOf<UiAction>()
        state.elements.stream()
            .forEach  { element -> addActionsToList(actions, element) }
        actions.add(skipAction())
        return actions
    }

    private fun addActionsToList(actions: MutableList<UiAction>, element: UiElement) {
        if (element.possibleActions.contains("TAP")) {
            actions.add(tapAction(element))
        }
        if (element.possibleActions.contains("INPUT")) {
            actions.add(inputAction(element, "text"))
        }
    }
}