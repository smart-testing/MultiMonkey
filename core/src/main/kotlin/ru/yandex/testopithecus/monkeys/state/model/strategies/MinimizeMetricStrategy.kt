package ru.yandex.testopithecus.monkeys.state.model.strategies

import ru.yandex.testopithecus.monkeys.state.model.graph.Edge
import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex

class MinimizeMetricStrategy: Strategy {

    override fun getEdge(vertex: Vertex): Edge {
//        val actions = vertex.getOutgoingEdges()
//        return actions.stream()
//                .min { a1, a2 -> a1.compareTo(a2) }
//                .orElse(null)
        val actions = vertex.getOutgoingEdges()
        return actions.entries.random().key
    }

}