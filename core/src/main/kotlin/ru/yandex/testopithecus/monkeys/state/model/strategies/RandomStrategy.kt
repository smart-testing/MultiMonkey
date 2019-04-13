package ru.yandex.testopithecus.monkeys.state.model.strategies

import ru.yandex.testopithecus.monkeys.state.model.Action
import ru.yandex.testopithecus.monkeys.state.model.State
import kotlin.random.Random


class RandomStrategy: Strategy {
    private val random = Random(0)

    override fun generateAction(state: State): Action {
        val actions = state.getFromActions()
        return actions[random.nextInt(actions.size)]
    }

}