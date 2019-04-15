package ru.yandex.testopithecus.monkeys.state.model.strategies.metric

import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex


interface Metric {

    fun updateMetric(vertex: Vertex?)

}