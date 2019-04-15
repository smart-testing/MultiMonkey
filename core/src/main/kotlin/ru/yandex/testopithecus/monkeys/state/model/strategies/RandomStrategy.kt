package ru.yandex.testopithecus.monkeys.state.model.strategies

import ru.yandex.testopithecus.monkeys.state.model.graph.Edge
import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex
import kotlin.random.Random


class RandomStrategy: Strategy {

    override fun getEdge(vertex: Vertex): Edge {
        val actions = vertex.getOutgoingEdges()
        return actions.entries.random().key
    }

}