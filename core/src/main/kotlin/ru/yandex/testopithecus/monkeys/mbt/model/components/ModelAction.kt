package ru.yandex.testopithecus.monkeys.mbt.model.components

import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.mbt.model.application.ApplicationModel
import ru.yandex.testopithecus.monkeys.state.model.Action
import ru.yandex.testopithecus.monkeys.state.model.State
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState


interface ModelAction {

    fun canBePerformed(model: ApplicationModel): Boolean

    fun perform(model: ApplicationModel): MbtElement

    fun getAction(state: UiState): UiAction
}