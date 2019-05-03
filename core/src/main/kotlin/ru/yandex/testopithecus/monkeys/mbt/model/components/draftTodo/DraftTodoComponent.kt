package ru.yandex.testopithecus.monkeys.mbt.model.components.draftTodo

import ru.yandex.testopithecus.monkeys.mbt.model.components.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.model.components.Component


class DraftTodoComponent: Component {

    override fun actions(): List<ModelAction> {
        return listOf(
                WriteTitleAction(),
                RejectTodoAction(),
                AcceptTodoAction()
        )
    }

}