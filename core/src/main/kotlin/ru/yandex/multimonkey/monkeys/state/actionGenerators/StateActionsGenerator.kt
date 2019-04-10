package ru.yandex.multimonkey.monkeys.state.actionGenerators

import ru.yandex.multimonkey.net.UiAction
import ru.yandex.multimonkey.net.UiState


interface StateActionsGenerator {

    fun getActions(state: UiState) : List<UiAction>
}