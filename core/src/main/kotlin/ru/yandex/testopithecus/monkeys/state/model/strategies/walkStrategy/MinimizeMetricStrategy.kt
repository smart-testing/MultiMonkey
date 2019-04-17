package ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy

import org.jgrapht.Graph
import ru.yandex.testopithecus.monkeys.state.model.Action
import ru.yandex.testopithecus.monkeys.state.model.State

class MinimizeMetricStrategy: WalkStrategy {

    override fun getAction(graph: Graph<State, Action>, state: State): Action? {
        val edges = graph.outgoingEdgesOf(state)
        return edges.stream()
                .min { e1, e2 -> compareMetric(graph, e1, e2) }
                .orElse(null)
    }

    private fun compareMetric(graph: Graph<State, Action>, a1: Action, a2: Action): Int {
        val firstValue = graph.getEdgeTarget(a1)?.metric
        val secondValue = graph.getEdgeTarget(a2)?.metric
        if (firstValue == null) {
            return -1
        }
        if (secondValue == null) {
            return -1
        }
        return firstValue - secondValue
    }


}