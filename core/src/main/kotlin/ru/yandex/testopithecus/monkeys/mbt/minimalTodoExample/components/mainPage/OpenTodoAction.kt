package ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.mainPage

import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.mbt.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.draftTodo.DraftTodoComponent
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application.ApplicationModel
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application.TodoItemModel
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState


class OpenTodoAction: ModelAction {
    override fun canBePerformed(model: ApplicationModel): Boolean {
        return !model.mainPage.todoItems.isEmpty() && model.currentTodo == null
    }

    override fun perform(model: ApplicationModel): MbtElement {
        val index = model.mainPage.todoItems.indices.random()
        val todoItem = model.mainPage.todoItems[index]
        return MbtElement(model.setCurrentTodo(TodoItemModel(todoItem.title, todoItem.description, index)), DraftTodoComponent())
    }

    override fun getAction(state: UiState): UiAction {
        // action with argument
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}