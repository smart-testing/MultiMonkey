package ru.yandex.testopithecus.monkeys.log

import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGenerator
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGeneratorImpl
import ru.yandex.testopithecus.ui.*
import ru.yandex.testopithecus.utils.serializeAction
import java.io.File
import java.nio.file.Files
import java.util.*
import java.util.stream.Collectors

class LogMonkey(private val filename: String) : Monkey {

    private val userLogs = LinkedList<String>(File("logs/$filename.log").readLines())

    private val actual = LinkedList<String>()
    private val expected = LinkedList<String>(userLogs)

    private val stack = LinkedList<State>()
    private var stackIndex = 0

    private val stateActionsGenerator: StateActionsGenerator = StateActionsGeneratorImpl()

    private var state = MonkeyState.INIT

    override fun generateAction(uiState: UiState): UiAction {
        if (state == MonkeyState.INIT) {
            state = MonkeyState.LEARN
            return restart()
        } else if (state == MonkeyState.FINISH) {
            if (stackIndex >= stack.size) {
                return finishAction()
            }
            val state = stack[stackIndex++]
            return state.actions[state.currentAction]
        }
        var stateChanged: Boolean
        synchronized(actual) {
            stateChanged = actual.isNotEmpty()
            while (expected.isNotEmpty() && actual.isNotEmpty()) {
                val nextExpected = expected.removeFirst()
                val nextActual = actual.removeFirst()
                if (nextExpected != nextActual) {
                    chooseNextBranch()
                    return restart()
                }
            }
        }
        if (expected.isEmpty()) {
            outputResult()
            state = MonkeyState.FINISH
            stackIndex = 0
            return skipAction()
        }
        if (stackIndex < stack.size) {
            val state = stack[stackIndex++]
            return state.actions[state.currentAction]
        }
        if (!stateChanged) {
            chooseNextBranch()
        } else if (expected.first.contains("screenshot")) {
            stack.addLast(State(listOf(screenshotAction(fake = true)), 0))
            stackIndex = stack.size
        } else {
            stack.addLast(State(stateActionsGenerator.getActions(uiState).sortedBy { it.id }, 0))
            stackIndex = stack.size
        }
        return stack.last.actions[stack.last.currentAction]
    }

    private fun outputResult() {
        val outputFile = File("tests/$filename.monkey")
        Files.createDirectories(outputFile.parentFile.toPath())
        if (!outputFile.exists()) {
            outputFile.createNewFile()
        }
        stack.forEach {
            val action = it.actions[it.currentAction]
            if (action == screenshotAction(true)) {
                action.attributes
            }
        }
        outputFile.writeText(
                stack.stream()
                        .map { it.actions[it.currentAction] }
                        .map { if (it == screenshotAction(true)) screenshotAction(false) else it }
                        .map { serializeAction(it).toString() }
                        .collect(Collectors.joining("\n"))
        )
    }

    private fun chooseNextBranch() {
        if (stackIndex < stack.size) {
            throw RuntimeException("Incorrect logs for remembered path")
        }
        while (stack.isNotEmpty() && stack.last.currentAction++ >= stack.last.actions.size) {
            stack.removeLast()
            if (stack.isEmpty()) {
                throw RuntimeException("No more possible actions")
            }
        }
        stackIndex = stack.size
    }

    private fun restart(): UiAction {
        actual.clear()
        expected.clear()
        expected.addAll(userLogs)
        stackIndex = 0
        return restartAction()
    }

    override fun feedback() {
    }

    fun appendToLog(line: String) {
        synchronized(actual) {
            println(line)
            actual.addLast(line)
        }
    }

    data class State(val actions: List<UiAction>, var currentAction: Int)

    enum class MonkeyState {
        INIT, LEARN, FINISH
    }
}