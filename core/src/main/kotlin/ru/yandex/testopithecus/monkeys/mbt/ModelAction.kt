package ru.yandex.testopithecus.monkeys.mbt

import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application.ApplicationModel
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState


interface ModelAction {

    fun canBePerformed(model: ApplicationModel): Boolean

    fun perform(model: ApplicationModel): MbtElement

    fun getAction(state: UiState): UiAction
}