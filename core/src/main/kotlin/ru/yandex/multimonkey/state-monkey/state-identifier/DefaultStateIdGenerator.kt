package ru.yandex.multimonkey.`state-monkey`.`state-identifier`

import ru.yandex.multimonkey.net.NetState


class DefaultStateIdGenerator : StateIdGenerator<StateId> {

    override fun getId(state: NetState): StateId {
        return DefaultStateId(state)
    }

}

private class DefaultStateId(val netState: NetState) : StateId