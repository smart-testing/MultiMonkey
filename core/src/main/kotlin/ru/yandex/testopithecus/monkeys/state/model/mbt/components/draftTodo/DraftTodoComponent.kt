package ru.yandex.testopithecus.monkeys.state.model.mbt.components.draftTodo

import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Action
import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Component


class DraftTodoComponent: Component {

    override fun actions(): List<Action> {
        return listOf(
                WriteTitleAction(),
                RejectTodoAction(),
                AcceptTodoAction()
        )
    }

}