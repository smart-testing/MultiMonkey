package ru.yandex.multimonkey.`state-monkey`.`action-generator`

import ru.yandex.multimonkey.net.NetAction
import ru.yandex.multimonkey.net.NetState


interface StateActionsGenerator {

    fun getActions(state: NetState) : List<NetAction>
}