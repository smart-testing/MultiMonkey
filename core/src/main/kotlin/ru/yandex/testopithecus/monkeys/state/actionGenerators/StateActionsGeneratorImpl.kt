package ru.yandex.testopithecus.monkeys.state.actionGenerators

import ru.yandex.testopithecus.input.InputFiller
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.util.stream.Collectors.toList

class StateActionsGeneratorImpl : StateActionsGenerator {

    override fun getActions(state: UiState) : List<UiAction> {
        val actions = mutableListOf<UiAction>()
        state.elements.stream()
            .forEach  { element -> addActionsToList(actions, element, state.elements) }
        actions.add(UiAction(null, "SKIP", mapOf()))
        return actions
    }

    private fun addActionsToList(actions: MutableList<UiAction>, element: UiElement, elements: List<UiElement>) {
        if (element.possibleActions.contains("TAP")) {
            actions.add(constructTapAction(element))
        }
        if (element.possibleActions.contains("INPUT")) {
            actions.add(constructInputAction(element, elements))
        }
    }

    private fun constructTapAction(element: UiElement): UiAction {
        return UiAction(element.id, "TAP", mapOf())
    }

    private fun constructInputAction(element: UiElement, elements: List<UiElement>): UiAction {
        val allTextLabels = elements.parallelStream().filter{x -> x.attributes["isLabel"] == true}.collect(toList())
        println("-p выбрали input")
        println("-p size ${elements.parallelStream().filter{x->x.attributes["isLabel"] == true}.count()}")
        InputFiller.fillInput(element, allTextLabels)
        return UiAction(element.id, "INPUT", mapOf("text" to element.attributes["text"] as String))
    }
}