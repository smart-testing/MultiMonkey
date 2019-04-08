package ru.yandex.multimonkey.`state-monkey`.`state-identifier`

import ru.yandex.multimonkey.net.NetState


interface StateIdGenerator<out T : StateId> {

    fun getId(state: NetState) : T

}