package ru.yandex.testopithecus.monkeys.state.actionGenerators

import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState


interface StateActionsGenerator {

    fun getActions(state: UiState) : List<UiAction>
}