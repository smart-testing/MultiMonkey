package ru.yandex.testopithecus.monkeys.mbt.model.application


data class MainPageModel(val todoItems: List<TodoItemModel> = listOf()) {

    fun addNewTodo(todoItem: TodoItemModel): MainPageModel {
        val newTodoItems = todoItems.toMutableList()
        newTodoItems.add(todoItem)
        return MainPageModel(newTodoItems)
    }

    fun setTodoItem(index: Int, todoItem: TodoItemModel): MainPageModel {
        val newTodoItems = todoItems.toMutableList()
        newTodoItems[index] = todoItem
        return MainPageModel(newTodoItems)
    }

}