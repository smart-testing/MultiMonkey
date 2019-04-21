package ru.yandex.testopithecus.monkeys.log

import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGenerator
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGeneratorImpl
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import java.lang.RuntimeException
import java.util.*

class LogMonkey(private val userLogs: LinkedList<String>) : Monkey {

    private val actual = LinkedList<String>()
    private val expected = LinkedList<String>(userLogs)

    private val stack = LinkedList<State>()
    private var stackIndex = 0

    private val stateActionsGenerator : StateActionsGenerator = StateActionsGeneratorImpl()

    override fun generateAction(uiState: UiState): UiAction {
        synchronized(actual) {
            while (actual.isNotEmpty()) {
                val nextExpected = expected.removeFirst()
                val nextActual = actual.removeFirst()
                if (nextExpected != nextActual) {
                    if (stackIndex < stack.size) {
                        throw RuntimeException("Incorrect logs for remembered path")
                    }
                    while (stack.last.currentAction++ >= stack.last.actions.size) {
                        stack.removeLast()
                        if (stack.isEmpty()) {
                            throw RuntimeException("No more possible actions")
                        }
                    }
                    actual.clear()
                    expected.clear()
                    expected.addAll(userLogs)
                    stackIndex = 0
                    return UiAction(null, "RESTART", mapOf())
                }
            }
        }
        if (stackIndex < stack.size) {
            val state = stack[stackIndex++]
            return state.actions[state.currentAction]
        }
        stack.addLast(State(stateActionsGenerator.getActions(uiState), 0))
        return stack.last.actions[0]
    }

    override fun feedback() {
    }

    fun appendToLog(line: String) {
        synchronized(actual) {
            actual.addLast(line)
        }
    }

    data class State(val actions: List<UiAction>, var currentAction: Int)
}