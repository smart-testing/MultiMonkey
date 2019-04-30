package ru.yandex.testopithecus.monkeys.complex

import ru.yandex.testopithecus.monkeys.complex.complex_action.ActionDescription
import ru.yandex.testopithecus.monkeys.complex.complex_action.ComplexAction
import ru.yandex.testopithecus.monkeys.complex.complex_action.ElementDescription
import ru.yandex.testopithecus.monkeys.complex.complex_action.splitter.ComplexActionSplitter
import ru.yandex.testopithecus.monkeys.complex.complex_action.splitter.SimpleComplexActionSplitter
import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState


class ComplexMonkey: Monkey {

    private val baseMonkey: Monkey = StateModelMonkey()
    private val complexActionSplitter: ComplexActionSplitter = SimpleComplexActionSplitter()

    override fun generateAction(uiState: UiState): UiAction {
        if (complexActionSplitter.isEmpty()) {
            val nextAction = baseMonkey.generateAction(uiState)
            val simpleAction = parseAction(nextAction, uiState) // Temporary
            complexActionSplitter.pushComplexAction(ComplexAction(listOf(simpleAction)))
        }
        return complexActionSplitter.getNextAction(uiState)
    }

    override fun feedback() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun parseAction(action: UiAction, state: UiState): Pair<ElementDescription, ActionDescription> {
        val actionDescription = ActionDescription(action.action, action.attributes)
        val element = state.elements.find { it.id == action.id }
        val elementDescription = ElementDescription(element?.attributes ?: mapOf())
        return elementDescription to actionDescription
    }

}