package ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.mainPage

import ru.yandex.testopithecus.monkeys.mbt.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.Component


class MainPageComponent : Component {

    override fun actions(): List<ModelAction> {
        return listOf(
                OpenTodoDraftAction()
//                OpenTodoAction()
        )
    }

}