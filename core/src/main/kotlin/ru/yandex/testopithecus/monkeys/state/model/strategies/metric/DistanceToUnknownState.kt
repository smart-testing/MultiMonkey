package ru.yandex.testopithecus.monkeys.state.model.strategies.metric

import org.jgrapht.Graph
import ru.yandex.testopithecus.monkeys.state.model.Action
import ru.yandex.testopithecus.monkeys.state.model.State


class DistanceToUnknownState: Metric {

    override fun updateMetric(graph: Graph<State, Action>, state: State) {
        updateMetricWithDepth(graph, state, 2)
    }

    private fun updateMetricWithDepth(graph: Graph<State, Action>, state: State, depth: Int) {
        if (state == State.NULL_STATE || depth <= 0) {
            return
        }
        val metric = state.metric
        val newMetric = evaluateMetric(graph, state)
        if (newMetric != metric) {
            state.metric = newMetric
            graph.incomingEdgesOf(state).forEach {
                updateMetricWithDepth(graph, graph.getEdgeSource(it), depth - 1)
            }
        }
    }

    private fun evaluateMetric(graph: Graph<State, Action>, state: State): Int {
        if (state == State.NULL_STATE) {
            return 0
        }
        return graph.outgoingEdgesOf(state).stream()
                .mapToInt { getMetric(graph.getEdgeTarget(it)) + 1 }
                .min()
                .orElse(Int.MAX_VALUE)
    }

    private fun getMetric(state: State): Int {
        return if (state == State.NULL_STATE) 0 else state.metric ?: 0
    }

}