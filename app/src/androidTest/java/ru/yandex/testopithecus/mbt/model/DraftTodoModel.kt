package ru.yandex.testopithecus.mbt.model

import org.apache.commons.lang3.builder.EqualsBuilder
import ru.yandex.testopithecus.mbt.interfaces.DraftTodoField
import ru.yandex.testopithecus.mbt.interfaces.DraftTodo
import ru.yandex.testopithecus.mbt.page.DraftTodoActual


class DraftTodoModel: DraftTodo {

    constructor() {
        title = DraftTodoFieldModel()
        description = DraftTodoFieldModel()
    }

    constructor(page: DraftTodoActual) {
        title = DraftTodoFieldModel(page.title)
        description = DraftTodoFieldModel(page.description)
    }

    override val title: DraftTodoField
    override val description: DraftTodoField

    init {

    }

    override fun addTodo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validTodo(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rejectTodo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun equals(other: Any?): Boolean {
        if (other is DraftTodoModel) {
            return EqualsBuilder()
                    .append(title, other.title)
                    .append(description, other.description)
                    .isEquals
        }
        return false
    }
}