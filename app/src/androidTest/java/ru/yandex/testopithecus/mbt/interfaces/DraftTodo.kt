package ru.yandex.testopithecus.mbt.interfaces


interface DraftTodo {

    fun addTodo()

    fun validTodo(): Boolean

    fun rejectTodo()

    val title: DraftTodoField

    val description: DraftTodoField

}