package ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.draftTodo

import ru.yandex.testopithecus.monkeys.mbt.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.Component


class DraftTodoComponent: Component {

    override fun actions(): List<ModelAction> {
        return listOf(
                WriteTitleAction(),
                RejectTodoAction(),
                AcceptTodoAction()
        )
    }

}