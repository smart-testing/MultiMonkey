package ru.yandex.testopithecus.system

import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState

class AndroidActionGeneratorLocal(private val modelMonkey: StateModelMonkey) : AndroidActionGenerator {

    override fun generateAction(uiState: UiState): UiAction {
        return modelMonkey.generateAction(uiState)
    }

    override fun feedback(feedback: UiFeedback) {
        modelMonkey.feedback(feedback)
    }
}