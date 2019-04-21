package ru.yandex.testopithecus.monkeys.log

import ru.yandex.testopithecus.monkeys.state.model.StateModel
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.DistanceToUnknownState
import ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy.MinimizeMetricStrategy
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import java.util.*

class LogMonkey(private val userLogs: LinkedList<String>) : Monkey {

    private val actual = LinkedList<String>()
    private val expected = LinkedList<String>(userLogs)

    private val model = StateModel(MinimizeMetricStrategy(), DistanceToUnknownState())

    override fun generateAction(uiState: UiState): UiAction {
        synchronized(actual) {
            while (actual.isNotEmpty()) {
                val nextExpected = expected.removeFirst()
                val nextActual = actual.removeFirst()
                if (nextExpected != nextActual) {
                    actual.clear()
                    expected.clear()
                    expected.addAll(userLogs)
                    return UiAction(null, "RESTART", mapOf())
                }
            }
        }
        return UiAction(null, "TODO", mapOf()) //TODO
    }

    override fun feedback() {
    }

    fun apendToLog(line: String) {
        synchronized(actual) {
            actual.addLast(line)
        }
    }
}