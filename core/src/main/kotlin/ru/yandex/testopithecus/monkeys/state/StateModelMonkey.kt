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

class StateModelMonkey : Monkey {

    private val model = StateModel()
    private val stateIdGenerator : StateIdGenerator<StateId> = ElementsStateIdGenerator()
    private val stateActionsGenerator : StateActionsGenerator = StateActionsGeneratorImpl()

    override fun generateAction(uiState: UiState): UiAction {
        val stateId : StateId = stateIdGenerator.getId(uiState)
        if (!model.hasState(stateId)) {
            val uiActions = stateActionsGenerator.getActions(uiState)
            model.registerState(stateId, uiState, uiActions)
        }
        return model.generateAction(stateId)
    }

    override fun feedback() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}