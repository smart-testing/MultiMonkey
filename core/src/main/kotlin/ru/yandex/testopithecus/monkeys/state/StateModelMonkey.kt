package ru.yandex.testopithecus.monkeys.state

import ru.yandex.testopithecus.monkeys.state.model.StateModel
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGenerator
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGeneratorImpl
import ru.yandex.testopithecus.monkeys.state.identifier.ElementsStateIdGenerator
import ru.yandex.testopithecus.monkeys.state.identifier.StateId
import ru.yandex.testopithecus.monkeys.state.identifier.StateIdGenerator

class StateModelMonkey(private val cvServerAddress: String? = null) : Monkey {

    private val model = StateModel()
    private val useScreenshots: Boolean = cvServerAddress != null
    private val stateIdGenerator: StateIdGenerator<StateId> = ElementsStateIdGenerator()
    private val stateActionsGenerator: StateActionsGenerator = StateActionsGeneratorImpl()

    private fun filterStates(uiState: UiState): UiState {
        return uiState
    }

    override fun generateAction(uiState: UiState): UiAction {
        var uiStateLocal = uiState
        if (useScreenshots) {
            uiStateLocal = filterStates(uiState)
        }
        val stateId: StateId = stateIdGenerator.getId(uiStateLocal)
        if (!model.hasState(stateId)) {
            val uiActions = stateActionsGenerator.getActions(uiStateLocal)
            model.registerState(stateId, uiActions)
        }
        return model.generateAction(stateId)
    }

    override fun feedback() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}