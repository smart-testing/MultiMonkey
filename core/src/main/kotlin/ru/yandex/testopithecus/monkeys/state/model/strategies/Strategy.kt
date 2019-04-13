package ru.yandex.testopithecus.monkeys.state.model.strategies

import ru.yandex.testopithecus.monkeys.state.model.Action
import ru.yandex.testopithecus.monkeys.state.model.State


interface Strategy {

    fun generateAction(state: State): Action?

}