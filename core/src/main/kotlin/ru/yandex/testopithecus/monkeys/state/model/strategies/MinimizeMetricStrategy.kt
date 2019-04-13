package ru.yandex.testopithecus.monkeys.state.model.strategies

import ru.yandex.testopithecus.monkeys.state.model.Action
import ru.yandex.testopithecus.monkeys.state.model.State

class MinimizeMetricStrategy: Strategy {

    override fun generateAction(state: State): Action? {
        val actions = state.getFromActions()
        return actions.stream()
                .min { a1, a2 -> a1.compareTo(a2) }
                .orElse(null)
    }

}