package ru.yandex.testopithecus.monkeys.state.model

import ru.yandex.testopithecus.monkeys.state.model.strategies.MinimizeMetricStrategy
import ru.yandex.testopithecus.monkeys.state.model.strategies.Strategy
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.DistanceToUnknownState
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.Metric

object ModelConfig {
    val METRIC: Metric = DistanceToUnknownState()
    val STRATEGY: Strategy = MinimizeMetricStrategy()
}
