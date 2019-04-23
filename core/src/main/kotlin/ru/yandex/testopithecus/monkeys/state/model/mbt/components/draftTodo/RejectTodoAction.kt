package ru.yandex.testopithecus.monkeys.state.model.mbt.components.draftTodo

import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Action
import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Component
import ru.yandex.testopithecus.monkeys.state.model.mbt.components.mainPage.MainPageComponent
import ru.yandex.testopithecus.monkeys.state.model.mbt.model.ApplicationModel


class RejectTodoAction: Action {

    override fun canBePerformed(model: ApplicationModel): Boolean {
        return true
    }

    override fun perform(model: ApplicationModel): Component {
        model.draftTodoModel.rejectTodo()
        return MainPageComponent()
    }

}