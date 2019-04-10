package ru.yandex.multimonkey.monkeys.state.model

import ru.yandex.multimonkey.ui.UiAction

class Action(val from: State, var to: State?, val uiAction : UiAction) {

    val metric
        get() = to?.metric ?: ModelConfig.METRIC.getMetricForNull()

    fun compareTo(other: Action): Int {
        return metric.compareTo(other.metric)
    }

}