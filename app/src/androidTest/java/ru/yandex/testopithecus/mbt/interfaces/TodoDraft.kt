package ru.yandex.testopithecus.mbt.interfaces


interface TodoDraft {

    fun addTodo()

    fun validTodo()

    fun rejectTodo()

    val title: DraftTodoField

    val description: DraftTodoField

}