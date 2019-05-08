package ru.yandex.testopithecus.monkeys.log

import org.json.JSONObject
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGenerator
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGeneratorImpl
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utilsCore.deserializeAction
import java.io.File
import java.lang.RuntimeException
import java.util.*

class ReplayMonkey(filename: String) : Monkey {

    private val actions = LinkedList<String>(File("results/$filename.monkey").readLines())
    private val stateActionsGenerator: StateActionsGenerator = StateActionsGeneratorImpl()


    override fun generateAction(uiState: UiState): UiAction {
        if (actions.isEmpty()) {
            return UiAction(null, "FINISH", mapOf())
        }
        val nextAction = deserializeAction(JSONObject(actions.removeFirst()))
        if (!stateActionsGenerator.getActions(uiState).contains(nextAction)) {
            throw RuntimeException("could not find replay action: $nextAction")
        }
        return nextAction
    }

    override fun feedback() {
    }
}