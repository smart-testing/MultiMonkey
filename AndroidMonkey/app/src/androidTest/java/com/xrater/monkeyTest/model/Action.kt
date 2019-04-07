package com.xrater.monkeyTest.model

import com.xrater.monkeyTest.net.NetAction

class Action(val netAction : NetAction): Comparable<Action> {
    var from: State? = null
    var to: State? = null

    val interest: Int
        get() = to?.metric ?: 0

    override fun compareTo(other: Action): Int {
        return interest.compareTo(other.interest)
    }

}