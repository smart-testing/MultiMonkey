package ru.yandex.testopithecus.monkeys.state.model.mbt.components.draftTodo

import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Action
import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Component
import ru.yandex.testopithecus.monkeys.state.model.mbt.model.ApplicationModel


class WriteTitleAction: Action {

    override fun canBePerformed(model: ApplicationModel): Boolean {
        return model.draftTodoModel.todoItem.title.isBlank()
    }

    override fun perform(model: ApplicationModel): Component {
        val draftTodoModel = model.draftTodoModel
        val text = generateText()
        draftTodoModel.todoItem.title = text
        return DraftTodoComponent()
    }

    private fun generateText(): String {
        return "Some text"
    }

}