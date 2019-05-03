package ru.yandex.testopithecus.monkeys.mbt.model.components.mainPage

import ru.yandex.testopithecus.monkeys.mbt.model.components.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.model.components.Component


class MainPageComponent : Component {

    override fun actions(): List<ModelAction> {
        return listOf(
                OpenTodoDraftAction()
//                OpenTodoAction()
        )
    }

}