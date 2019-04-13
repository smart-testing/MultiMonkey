package ru.yandex.testopithecus.monkeys.state.identifier

import ru.yandex.testopithecus.ui.UiState


interface StateIdGenerator<out T : StateId> {

    fun getId(state: UiState) : T

}