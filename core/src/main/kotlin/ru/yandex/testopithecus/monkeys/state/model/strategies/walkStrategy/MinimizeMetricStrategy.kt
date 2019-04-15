package ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy

import ru.yandex.testopithecus.monkeys.state.model.graph.Edge
import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex

class MinimizeMetricStrategy: WalkStrategy {

    override fun getEdge(vertex: Vertex): Pair<Edge, Vertex?> {
        val edges = vertex.getOutgoingEdges()
        return edges.entries.stream()
                .map { e -> e.toPair() }
                .min { e1, e2 -> compareMetric(e1, e2) }
                .orElse(null)
    }

    private fun compareMetric(p1: Pair<Edge, Vertex?>, p2: Pair<Edge, Vertex?>): Int {
        val firstValue = p1.second?.metric
        val secondValue = p2.second?.metric
        if (firstValue == null) {
            return -1
        }
        if (secondValue == null) {
            return -1
        }
        return firstValue - secondValue
    }


}