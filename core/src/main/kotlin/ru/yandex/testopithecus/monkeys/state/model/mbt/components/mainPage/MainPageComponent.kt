package ru.yandex.testopithecus.monkeys.state.model.mbt.components.mainPage

import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Action
import ru.yandex.testopithecus.monkeys.state.model.mbt.components.Component


class MainPageComponent : Component {

    override fun actions(): List<Action> {
        return listOf(
                OpenTodoDraftAction(),
                OpenTodoAction()
        )
    }

}