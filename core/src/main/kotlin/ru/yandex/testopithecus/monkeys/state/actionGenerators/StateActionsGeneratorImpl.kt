package ru.yandex.testopithecus.monkeys.state.actionGenerators
import ru.yandex.testopithecus.ui.*

class StateActionsGeneratorImpl : StateActionsGenerator {
    override fun getActions(state: UiState): List<UiAction> {
        val actions = mutableListOf<UiAction>()
        state.elements.stream()
                .forEach { element -> addActionsToList(actions, element, state) }
        actions.add(UiAction(null, "SKIP", mapOf()))
        return actions
    }

    private fun addActionsToList(actions: MutableList<UiAction>, element: UiElement, state: UiState) {
        if (element.possibleActions.contains("TAP")) {
            actions.add(tapAction(element))
        }
        if (element.possibleActions.contains("INPUT")) {
            actions.add(constructInputAction(element, state))
        }
    }

    private fun constructTapAction(element: UiElement): UiAction {
        return UiAction(element.id, "TAP", mapOf())
    }
    private fun constructInputAction(element: UiElement, state: UiState): UiAction {
        element.attributes["text"] = InputFiller.suggestInput(element, state)
        return UiAction(element.id, "INPUT", mapOf("text" to element.attributes["text"] as String))
    }
}