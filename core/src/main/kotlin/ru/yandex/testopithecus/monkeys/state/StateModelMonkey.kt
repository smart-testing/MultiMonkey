package ru.yandex.testopithecus.monkeys.state

import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGenerator
import ru.yandex.testopithecus.monkeys.state.actionGenerators.StateActionsGeneratorImpl
import ru.yandex.testopithecus.monkeys.state.identifier.ElementsStateIdGenerator
import ru.yandex.testopithecus.monkeys.state.identifier.StateId
import ru.yandex.testopithecus.monkeys.state.identifier.StateIdGenerator
import ru.yandex.testopithecus.monkeys.state.model.StateModel
import ru.yandex.testopithecus.stateenricher.EmptyEnricher
import ru.yandex.testopithecus.stateenricher.Enricher
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState

class StateModelMonkey(private val enricher: Enricher = EmptyEnricher()) : Monkey {

    private val model = StateModel()
    private val stateIdGenerator: StateIdGenerator<StateId> = ElementsStateIdGenerator()
    private val stateActionsGenerator: StateActionsGenerator = StateActionsGeneratorImpl()

    private fun enrichState(uiState: UiState): UiState {
        return enricher.enrichState(uiState)
    }

    override fun generateAction(uiState: UiState): UiAction {
        val uiStateLocal: UiState = enrichState(uiState)
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