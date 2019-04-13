package ru.yandex.testopithecus.monkeys.state.model.strategies.metric

import ru.yandex.testopithecus.monkeys.state.model.State


interface Metric {

    fun getInitialMetric(): Int

    fun evaluateMetric(state: State?): Int

    fun getMetricForNull(): Int

    fun updateMetric(state: State?)
}