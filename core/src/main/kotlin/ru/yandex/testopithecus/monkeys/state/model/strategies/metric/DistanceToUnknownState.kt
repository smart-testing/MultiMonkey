package ru.yandex.testopithecus.monkeys.state.model.strategies.metric

import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex


class DistanceToUnknownState: Metric {

    override fun updateMetric(vertex: Vertex?) {
        updateMetricWithDepth(vertex, 2)
    }

    private fun updateMetricWithDepth(vertex: Vertex?, depth: Int) {
        if (vertex == null || depth <= 0) {
            return
        }
        val metric = vertex.metric
        val newMetric = evaluateMetric(vertex)
        if (newMetric != metric) {
            vertex.metric = newMetric
            vertex.getIncomingEdges().forEach { updateMetricWithDepth(it.value, depth - 1) }
        }
    }

    private fun evaluateMetric(vertex: Vertex?): Int {
        if (vertex == null) {
            return 0
        }
        return vertex.getOutgoingEdges().entries
                .stream()
                .mapToInt { getMetric(it.value) + 1 }
                .min()
                .orElse(Int.MAX_VALUE)
    }

    private fun getMetric(vertex: Vertex?): Int {
        return vertex?.metric ?: 0
    }

}