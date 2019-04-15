package ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy

import ru.yandex.testopithecus.monkeys.state.model.graph.Edge
import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex


class RandomStrategy: WalkStrategy {

    override fun getEdge(vertex: Vertex): Pair<Edge, Vertex?> {
        val actions = vertex.getOutgoingEdges()
        return actions.entries.random().toPair()
    }

}