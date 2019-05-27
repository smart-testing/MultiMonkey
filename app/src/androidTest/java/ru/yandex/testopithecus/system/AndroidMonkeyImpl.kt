package ru.yandex.testopithecus.system

import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState

class AndroidMonkeyImpl(private val modelMonkey: StateModelMonkey) : Monkey {

    override fun generateAction(uiState: UiState): UiAction {
        return modelMonkey.generateAction(uiState)
    }

    override fun feedback(feedback: UiFeedback) {
        modelMonkey.feedback(feedback)
    }
}