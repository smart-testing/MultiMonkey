package ru.yandex.testopithecus.monkeys.mbt.model.components.mainPage

import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.mbt.model.components.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.model.components.Component
import ru.yandex.testopithecus.monkeys.mbt.model.components.draftTodo.DraftTodoComponent
import ru.yandex.testopithecus.monkeys.mbt.model.application.ApplicationModel
import ru.yandex.testopithecus.monkeys.mbt.model.application.TodoItemModel
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