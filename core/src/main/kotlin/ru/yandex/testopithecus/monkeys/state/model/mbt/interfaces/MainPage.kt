package ru.yandex.testopithecus.monkeys.state.model.mbt.interfaces


interface MainPage {

    val todoItems: List<TodoItem>

    fun openTodo(index: Int)

    fun createNewTodo()

}