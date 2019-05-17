package ru.yandex.testopithecus.monkeys.log

import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGenerator
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGeneratorImpl
import ru.yandex.testopithecus.ui.*
import ru.yandex.testopithecus.utils.serializeAction
import java.io.File
import java.nio.file.Files
import java.util.*
import java.util.stream.Collectors

class RestoreMonkey(private val filename: String) : Monkey {

    private val userLogs = LinkedList<String>(File("logs/$filename.log").readLines())

    private val actual = LinkedList<String>()
    private val expected = LinkedList<String>(userLogs)

    private val stack = LinkedList<State>()
    private var stackIndex = 0

    private val stateActionsGenerator: StateActionsGenerator = StateActionsGeneratorImpl()

    private var state = MonkeyState.INIT

    override fun generateAction(uiState: UiState): UiAction {
        when (state) {
            MonkeyState.INIT -> {
                state = MonkeyState.LEARN
                return restart()
            }
            MonkeyState.FINISH -> {
                if (stackIndex >= stack.size) {
                    state = MonkeyState.DEAD
                    outputScreenshots()
                    return finishAction()
                }
                val state = stack[stackIndex++]
                val action = state.actions[state.currentAction]
                return if (action == fakeScreenshotAction()) screenshotAction() else action
            }
            MonkeyState.DEAD -> return finishAction()
            else -> {
            }
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
            outputSteps()
            state = MonkeyState.FINISH
            stackIndex = 0
            actual.clear()
            return restartAction()
        }
        if (stackIndex < stack.size) {
            val state = stack[stackIndex++]
            return state.actions[state.currentAction]
        }
        if (!stateChanged) {
            chooseNextBranch()
        } else if (expected.first.contains("screenshot")) {
            stack.addLast(State(listOf(fakeScreenshotAction()), 0))
            stackIndex = stack.size
        } else {
            stack.addLast(State(stateActionsGenerator.getActions(uiState).sortedBy { it.id }, 0))
            stackIndex = stack.size
        }
        return stack.last.actions[stack.last.currentAction]
    }

    private fun outputScreenshots() {
        val outputFile = File("tests/$filename.screenshots")
        Files.createDirectories(outputFile.parentFile.toPath())
        if (!outputFile.exists()) {
            outputFile.createNewFile()
        }
        outputFile.writeText(
                actual.stream()
                        .filter { it.startsWith("{\"screenshot\":") }
                        .collect(Collectors.joining("\n"))
        )
    }

    private fun outputSteps() {
        val outputFile = File("tests/$filename.monkey")
        Files.createDirectories(outputFile.parentFile.toPath())
        outputFile.delete()
        stack.addFirst(State(listOf(restartAction()), 0))
        outputFile.writeText(
                stack.stream()
                        .map { it.actions[it.currentAction] }
                        .map { if (it == fakeScreenshotAction()) screenshotAction() else it }
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
        INIT, LEARN, FINISH, DEAD
    }
}