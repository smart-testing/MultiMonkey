package ru.yandex.multimonkey.monkeys.state.actionGenerators

import ru.yandex.multimonkey.ui.UiAction
import ru.yandex.multimonkey.ui.UiState


interface StateActionsGenerator {

    fun getActions(state: UiState) : List<UiAction>
}