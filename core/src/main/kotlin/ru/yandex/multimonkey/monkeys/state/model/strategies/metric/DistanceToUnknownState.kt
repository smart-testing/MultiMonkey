package ru.yandex.multimonkey.monkeys.state.model.strategies.metric

import ru.yandex.multimonkey.monkeys.state.model.State


class DistanceToUnknownState: Metric {
    override fun getInitialMetric(): Int {
        return Int.MAX_VALUE
    }

    override fun getMetricForNull(): Int {
        return 0
    }

    override fun evaluateMetric(state: State?): Int {
        if (state == null) {
            return getMetricForNull()
        }
        return state.getFromActions()
                .stream()
                .min { a1, a2 -> a1.compareTo(a2) }
                .map { a -> a.metric + 1 }
                .orElse(Int.MAX_VALUE)
    }

    override fun updateMetric(state: State?) {
        updateMetricWithDepth(state, 2)
    }

    private fun updateMetricWithDepth(state: State?, depth: Int) {
        if (state == null || depth <= 0) {
            return
        }
        val metric = state.metric
        val newMetric = evaluateMetric(state)
        if (newMetric != metric) {
            state.metric = newMetric
            state.getToActions().forEach { updateMetricWithDepth(it.from, depth - 1) }
        }
    }

}