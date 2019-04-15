package ru.yandex.testopithecus.monkeys.state.model.strategies.metric

import org.jgrapht.Graph
import ru.yandex.testopithecus.monkeys.state.model.Action
import ru.yandex.testopithecus.monkeys.state.model.State

interface Metric {

    fun updateMetric(graph: Graph<State?, Action>, state: State?)

}