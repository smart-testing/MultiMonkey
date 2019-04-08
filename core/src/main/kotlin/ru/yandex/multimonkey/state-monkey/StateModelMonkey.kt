package ru.yandex.multimonkey.`state-monkey`

import ru.yandex.multimonkey.`state-monkey`.`state-model`.StateModel
import ru.yandex.multimonkey.net.Monkey
import ru.yandex.multimonkey.net.UiAction
import ru.yandex.multimonkey.net.UiState
import ru.yandex.multimonkey.`state-monkey`.`action-generator`.StateActionsGenerator
import ru.yandex.multimonkey.`state-monkey`.`action-generator`.StateActionsGeneratorImpl
import ru.yandex.multimonkey.`state-monkey`.`state-identifier`.DefaultStateIdGenerator
import ru.yandex.multimonkey.`state-monkey`.`state-identifier`.StateId
import ru.yandex.multimonkey.`state-monkey`.`state-identifier`.StateIdGenerator

class StateModelMonkey : Monkey {

    private val model = StateModel()
    private val stateIdGenerator : StateIdGenerator<StateId> = DefaultStateIdGenerator()
    private val stateActionsGenerator : StateActionsGenerator = StateActionsGeneratorImpl()

    override fun generateAction(uiState: UiState): UiAction {
        val stateId : StateId = stateIdGenerator.getId(uiState)
        if (!model.hasState(stateId)) {
            val uiActions = stateActionsGenerator.getActions(uiState)
            model.registerState(stateId, uiActions)
        }
        return model.generateAction(stateId)
    }

    override fun feedback() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}