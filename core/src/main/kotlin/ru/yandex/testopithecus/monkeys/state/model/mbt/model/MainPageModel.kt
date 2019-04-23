package ru.yandex.testopithecus.monkeys.state.model.mbt.model

import ru.yandex.testopithecus.monkeys.state.model.mbt.interfaces.MainPage
import ru.yandex.testopithecus.monkeys.state.model.mbt.interfaces.TodoItem


class MainPageModel: MainPage {

    override val todoItems: MutableList<TodoItem> = mutableListOf()

    override fun openTodo(index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNewTodo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}