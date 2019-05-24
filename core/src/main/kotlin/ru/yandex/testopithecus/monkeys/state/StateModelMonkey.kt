package ru.yandex.testopithecus.monkeys.state

import ru.yandex.testopithecus.monkeys.state.model.StateModel
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGenerator
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGeneratorImpl
import ru.yandex.testopithecus.monkeys.state.identifier.ElementsStateIdGenerator
import ru.yandex.testopithecus.monkeys.state.identifier.SmartElementsStateIdGenerator
import ru.yandex.testopithecus.monkeys.state.identifier.StateId
import ru.yandex.testopithecus.monkeys.state.identifier.StateIdGenerator
import ru.yandex.testopithecus.ui.UiFeedback

class StateModelMonkey : Monkey {

    private val model = StateModel()
    private val stateIdGenerator : StateIdGenerator = SmartElementsStateIdGenerator()
    private val stateActionsGenerator : StateActionsGenerator = StateActionsGeneratorImpl()

    override fun generateAction(uiState: UiState): UiAction {
        val stateId : StateId = stateIdGenerator.getId(uiState)
        if (!model.hasState(stateId)) {
            val uiActions = stateActionsGenerator.getActions(uiState)
            model.registerState(stateId, uiState, uiActions)
        }
        return model.generateAction(stateId)
    }

    override fun feedback(feedback: UiFeedback) {
        if (feedback.status == "OK") {
            val state = feedback.state
            val stateId = stateIdGenerator.getId(state)
            if (!model.hasState(stateId)) {
                val uiActions = stateActionsGenerator.getActions(state)
                model.registerState(stateId, state, uiActions)
            }
            model.feedback(stateId)
        }
    }
}