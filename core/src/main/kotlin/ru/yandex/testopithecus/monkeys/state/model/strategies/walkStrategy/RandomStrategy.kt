package ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy

import org.jgrapht.Graph
import ru.yandex.testopithecus.monkeys.state.model.Action
import ru.yandex.testopithecus.monkeys.state.model.State


class RandomStrategy: WalkStrategy {

    override fun getAction(graph: Graph<State?, Action>, state: State): Action {
        val actions = graph.outgoingEdgesOf(state)
        return actions.random()
    }

}