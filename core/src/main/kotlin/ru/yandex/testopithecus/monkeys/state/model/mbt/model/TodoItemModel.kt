package ru.yandex.testopithecus.monkeys.state.model.mbt.model

import ru.yandex.testopithecus.monkeys.state.model.mbt.interfaces.TodoItem


class TodoItemModel(override var title: String = "", override var description: String = ""): TodoItem {

    override fun isValid(): Boolean {
        return !title.isBlank()
    }

}