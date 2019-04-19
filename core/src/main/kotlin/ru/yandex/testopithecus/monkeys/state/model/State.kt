package ru.yandex.testopithecus.monkeys.state.model

import ru.yandex.testopithecus.monkeys.state.identifier.StateId

class State(val id: StateId) {
    var metric: Int? = null

    class FictiveStateId: StateId

    companion object {
        val NULL_STATE = State(FictiveStateId())
    }
}