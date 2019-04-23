package ru.yandex.testopithecus.monkeys.state.model.mbt.interfaces


interface TodoItem {

    var title: String

    var description: String

    fun isValid(): Boolean

}