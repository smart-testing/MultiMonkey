package ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.mainPage

import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.mbt.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.draftTodo.DraftTodoComponent
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application.ApplicationModel
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application.TodoItemModel
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState


class OpenTodoDraftAction: ModelAction {
    override fun canBePerformed(model: ApplicationModel): Boolean {
        return model.currentTodo == null
    }

    override fun perform(model: ApplicationModel): MbtElement {
        return MbtElement(model.setCurrentTodo(TodoItemModel()), DraftTodoComponent())
    }

    override fun getAction(state: UiState): UiAction {
        val element = state.elements.first { it.attributes["name"].toString().endsWith("addToDoItemFAB") }
        return UiAction(element.id, "TAP", mapOf())
    }

}