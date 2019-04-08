package ru.yandex.multimonkey.`state-monkey`

import ru.yandex.multimonkey.`state-monkey`.`state-model`.StateModel
import ru.yandex.multimonkey.net.Monkey
import ru.yandex.multimonkey.net.NetAction
import ru.yandex.multimonkey.net.NetState
import ru.yandex.multimonkey.`state-monkey`.`action-generator`.StateActionsGenerator
import ru.yandex.multimonkey.`state-monkey`.`action-generator`.StateActionsGeneratorImpl
import ru.yandex.multimonkey.`state-monkey`.`state-identifier`.DefaultStateIdGenerator
import ru.yandex.multimonkey.`state-monkey`.`state-identifier`.StateId
import ru.yandex.multimonkey.`state-monkey`.`state-identifier`.StateIdGenerator

class StateModelMonkey : Monkey {

    private val model = StateModel()
    private val stateIdGenerator : StateIdGenerator<StateId> = DefaultStateIdGenerator()
    private val stateActionsGenerator : StateActionsGenerator = StateActionsGeneratorImpl()

    override fun generateAction(netState: NetState): NetAction {
        val stateId : StateId = stateIdGenerator.getId(netState)
        if (!model.hasState(stateId)) {
            val netActions = stateActionsGenerator.getActions(netState)
            model.registerState(stateId, netActions)
        }
        return model.generateAction(stateId)
    }

    override fun feedback() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}