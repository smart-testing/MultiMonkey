package ru.yandex.multimonkey.`state-monkey`.`action-generator`

import ru.yandex.multimonkey.net.UiAction
import ru.yandex.multimonkey.net.UiState


interface StateActionsGenerator {

    fun getActions(state: UiState) : List<UiAction>
}