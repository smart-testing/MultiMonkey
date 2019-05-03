package ru.yandex.testopithecus.monkeys.mbt.model.components.draftTodo

import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.mbt.model.components.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.model.components.Component
import ru.yandex.testopithecus.monkeys.mbt.model.components.mainPage.MainPageComponent
import ru.yandex.testopithecus.monkeys.mbt.model.application.ApplicationModel
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState


class RejectTodoAction: ModelAction {
    override fun canBePerformed(model: ApplicationModel): Boolean {
        return true
    }

    override fun perform(model: ApplicationModel): MbtElement {
        return MbtElement(model.setCurrentTodo(null), MainPageComponent())
    }

    override fun getAction(state: UiState): UiAction {
        val element = state.elements.first { it.attributes["name"] == null }
        return UiAction(element.id, "TAP", mapOf())
    }

}