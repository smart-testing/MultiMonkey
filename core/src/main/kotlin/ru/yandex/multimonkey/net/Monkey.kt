package ru.yandex.multimonkey.net

interface Monkey {

    fun generateAction(netState: NetState): NetAction

    fun feedback()
}