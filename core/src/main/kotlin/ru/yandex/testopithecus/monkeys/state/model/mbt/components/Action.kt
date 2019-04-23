package ru.yandex.testopithecus.monkeys.state.model.mbt.components

import ru.yandex.testopithecus.monkeys.state.model.mbt.model.ApplicationModel


interface Action {

    fun canBePerformed(model: ApplicationModel): Boolean

    fun perform(model: ApplicationModel): Component

}