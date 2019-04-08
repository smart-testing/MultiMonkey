package ru.yandex.multimonkey.net

interface NetMonkey {

    fun generateAction(netState: NetState): NetAction
}