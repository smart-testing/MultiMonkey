package ru.yandex.testopithecus.monkeys.state.model

import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.state.identifier.StateId
import ru.yandex.testopithecus.ui.UiState
import java.lang.IllegalArgumentException

class State(val id: StateId, val uiState: UiState) {
    var metric: Int? = null
    private var mbt: MbtElement? = null
    val index = counter++

    fun hasMbt(): Boolean {
        return mbt != null
    }

    fun getMbt(): MbtElement {
        return mbt ?: throw IllegalArgumentException()
    }

    fun setMbt(mbt: MbtElement) {
        this.mbt = mbt
    }

    class FictiveStateId: StateId

    companion object {
        val NULL_STATE = State(FictiveStateId(), UiState(listOf(), mutableMapOf()))
        var counter = 0
    }
}