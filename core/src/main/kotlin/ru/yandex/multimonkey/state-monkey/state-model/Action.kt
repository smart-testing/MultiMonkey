package ru.yandex.multimonkey.`state-monkey`.`state-model`

import ru.yandex.multimonkey.net.NetAction

class Action(val from: State, var to: State?, val netAction : NetAction) {

//    val interest: Int
//        get() = to?.metric ?: 0

//    override fun compareTo(other: Action): Int {
//        return interest.compareTo(other.interest)
//    }

}