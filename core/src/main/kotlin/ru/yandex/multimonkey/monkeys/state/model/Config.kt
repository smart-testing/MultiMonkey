package ru.yandex.multimonkey.monkeys.state.model

import ru.yandex.multimonkey.monkeys.state.model.strategies.MinimizeMetricStrategy
import ru.yandex.multimonkey.monkeys.state.model.strategies.Strategy
import ru.yandex.multimonkey.monkeys.state.model.strategies.metric.DistanceToUnknownState
import ru.yandex.multimonkey.monkeys.state.model.strategies.metric.Metric

object ModelConfig {
    val METRIC: Metric = DistanceToUnknownState()
    val STRATEGY: Strategy = MinimizeMetricStrategy()
}
