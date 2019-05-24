package ru.yandex.testopithecus.monkeys.log

import org.json.JSONObject
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGenerator
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGeneratorImpl
import ru.yandex.testopithecus.ui.*
import ru.yandex.testopithecus.utils.deserializeAction
import java.io.File
import java.util.*

class ReplayMonkey(private val filename: String, private val logFile: File) : Monkey {

    private val actions = LinkedList<UiAction>(
            File("tests/$filename.monkey").readLines().map {
                deserializeAction(JSONObject(it))
            }
    )
    private val stateActionsGenerator: StateActionsGenerator = StateActionsGeneratorImpl()

    override fun generateAction(uiState: UiState): UiAction {
        if (actions.isEmpty()) {
            return validate()
        }
        val nextAction = actions.removeFirst()
        if (nextAction.id != null && !stateActionsGenerator.getActions(uiState).contains(nextAction)) {
            throw RuntimeException("could not find replay action: $nextAction")
        }
        return nextAction
    }

    private fun validate(): UiAction {
        val actual = logFile.readLines()
        val (actualScreenshots, actualLogs) = actual.partition { it.startsWith("{\"screenshot\":") }
        val expectedScreenshots = File("tests/$filename.screenshots").readLines()
        val expectedLogs = File("logs/$filename.log")
                .readLines()
                .filter { !it.startsWith("{\"screenshot\":") }
        val badLog = validateLogs(expectedLogs, actualLogs)
        if (badLog >= 0) {
            return testFailAction("expected log not found: " + expectedLogs[badLog])
        }
        val badScreenshot = validateScreenshots(expectedScreenshots, actualScreenshots)
        if (badScreenshot >= 0) {
            return testFailAction(
                    "screenshots differ",
                    expectedScreenshots.getOrElse(badScreenshot) { "" },
                    actualScreenshots.getOrElse(badScreenshot) { "" }
            )
        }
        return testOkAction()
    }

    private fun validateLogs(expected: List<String>, actual: List<String>): Int {
        var e = 0
        var a = 0
        while (e < expected.size && a < actual.size) {
            if (expected[e] == actual[a++]) {
                e++
            }
        }
        if (e >= expected.size) {
            e = -1
        }
        return e
    }

    private fun validateScreenshots(expected: List<String>, actual: List<String>): Int {
        for (i in 0 until expected.size) {
            if (i >= actual.size || expected[i] != actual[i]) {
                return i
            }
        }
        if (expected.size < actual.size) {
            return expected.size
        }
        return -1
    }

    override fun feedback(feedback: UiFeedback) {}
}