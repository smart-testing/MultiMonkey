package ru.yandex.testopithecus.monkeys.state.identifier

import ru.yandex.testopithecus.ui.UiState


interface StateIdGenerator {

    fun getId(state: UiState) : StateId

}