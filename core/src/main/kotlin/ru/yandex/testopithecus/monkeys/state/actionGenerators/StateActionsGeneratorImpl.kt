package ru.yandex.testopithecus.monkeys.state.actionGenerators

import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState

class StateActionsGeneratorImpl : StateActionsGenerator {

    override fun getActions(state: UiState) : List<UiAction> {
        val actions = mutableListOf<UiAction>()
        state.elements.stream()
            .forEach  { element -> addActionsToList(actions, element) }
        actions.add(UiAction(null, "SKIP", mapOf()))
        return actions
    }

    private fun addActionsToList(actions: MutableList<UiAction>, element: UiElement) {
        if (element.possibleActions.contains("TAP")) {
            actions.add(constructTapAction(element))
        }
    }

    private fun constructTapAction(element: UiElement): UiAction {
        return UiAction(element.id, "TAP", mapOf())
    }

}