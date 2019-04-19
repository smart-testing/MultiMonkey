package ru.yandex.testopithecus.mbt.model

import ru.yandex.testopithecus.mbt.interfaces.DraftTodoField
import ru.yandex.testopithecus.mbt.interfaces.TodoDraft


class TodoDraftModel: TodoDraft {

    override val title: DraftTodoField
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val description: DraftTodoField
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun addTodo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validTodo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rejectTodo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}