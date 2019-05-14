package ru.yandex.testopithecus.monkeys.state.model

class Action(val isModelAction: Boolean) {

    private val index = counter++

    override fun toString(): String {
        return index.toString()
    }

    companion object {
        var counter = 0
    }
}