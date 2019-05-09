package ru.yandex.testopithecus.monkeys.complex.complex_action.splitter

import ru.yandex.testopithecus.monkeys.complex.complex_action.ComplexAction
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState


interface ComplexActionSplitter {

    fun isEmpty(): Boolean

    fun getNextAction(state: UiState): UiAction

    fun pushComplexAction(action: ComplexAction)

}