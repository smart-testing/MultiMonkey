package ru.yandex.multimonkey.monkeys.state.identifier

import ru.yandex.multimonkey.ui.UiState


interface StateIdGenerator<out T : StateId> {

    fun getId(state: UiState) : T

}