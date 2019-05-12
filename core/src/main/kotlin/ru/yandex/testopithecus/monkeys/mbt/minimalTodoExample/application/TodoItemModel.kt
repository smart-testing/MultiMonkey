package ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application


data class TodoItemModel(val title: String = "", val description: String = "", val index: Int = -1) {

    fun setTitle(text: String): TodoItemModel {
        return this.copy(title = text)
    }

    fun isValid(): Boolean {
        return !title.isBlank()
    }

    fun isNewTodo(): Boolean {
        return index != -1
    }
}