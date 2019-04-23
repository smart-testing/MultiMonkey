package ru.yandex.testopithecus.monkeys.state.model.mbt.model

import ru.yandex.testopithecus.monkeys.state.model.mbt.interfaces.DraftTodo
import ru.yandex.testopithecus.monkeys.state.model.mbt.interfaces.TodoItem


class DraftTodoModel(override val todoItem: TodoItem): DraftTodo {

    override fun addTodo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rejectTodo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}