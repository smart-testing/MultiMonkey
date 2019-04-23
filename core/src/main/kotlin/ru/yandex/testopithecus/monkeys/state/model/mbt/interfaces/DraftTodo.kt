package ru.yandex.testopithecus.monkeys.state.model.mbt.interfaces


interface DraftTodo {

    fun addTodo()

    fun rejectTodo()

    val todoItem: TodoItem

}