package ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application


data class ApplicationModel(
        val currentTodo: TodoItemModel? = null,
        val mainPage: MainPageModel = MainPageModel()
) {

    fun setCurrentTodo(currentTodo: TodoItemModel?): ApplicationModel {
        return this.copy(currentTodo = currentTodo)
    }

    fun setMainPage(mainPage: MainPageModel): ApplicationModel {
        return this.copy(mainPage = mainPage)
    }

}