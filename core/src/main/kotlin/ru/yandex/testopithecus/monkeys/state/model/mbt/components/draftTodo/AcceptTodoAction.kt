package ru.yandex.testopithecus.monkeys.state.model.mbt.components.draftTodo

import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Action
import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Component
import ru.yandex.testopithecus.monkeys.state.model.mbt.components.mainPage.MainPageComponent
import ru.yandex.testopithecus.monkeys.state.model.mbt.model.ApplicationModel


class AcceptTodoAction: Action {

    override fun canBePerformed(model: ApplicationModel): Boolean {
        return model.draftTodoModel.todoItem.isValid()
    }

    override fun perform(model: ApplicationModel): Component {
        model.draftTodoModel.addTodo()
        return MainPageComponent()
    }

}