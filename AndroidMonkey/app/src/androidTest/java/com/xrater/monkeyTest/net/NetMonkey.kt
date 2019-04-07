package com.xrater.monkeyTest.net

interface NetMonkey {

    fun generateAction(netState: NetState): NetAction
}