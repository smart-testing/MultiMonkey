package ru.yandex.multimonkey.monkeys.state.model.strategies

import ru.yandex.multimonkey.monkeys.state.model.Action
import ru.yandex.multimonkey.monkeys.state.model.State


interface Strategy {

    fun generateAction(state: State): Action?

}