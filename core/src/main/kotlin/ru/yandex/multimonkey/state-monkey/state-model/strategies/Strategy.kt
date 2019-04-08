package ru.yandex.multimonkey.`state-monkey`.`state-model`.strategies

import ru.yandex.multimonkey.`state-monkey`.`state-model`.Action
import ru.yandex.multimonkey.`state-monkey`.`state-model`.State


interface Strategy {

    fun generateAction(state: State): Action

    fun initNewState(): State
}