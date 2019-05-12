package ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.draftTodo

import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.mbt.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.mainPage.MainPageComponent
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application.ApplicationModel
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import java.lang.IllegalArgumentException


class AcceptTodoAction: ModelAction {
    override fun canBePerformed(model: ApplicationModel): Boolean {
        return model.currentTodo?.isValid() ?: false
    }

    override fun perform(model: ApplicationModel): MbtElement {
        val draftTodo = model.currentTodo ?: throw IllegalArgumentException()
        val newMainPage = if (!draftTodo.isNewTodo()) {
            model.mainPage.addNewTodo(draftTodo)
        } else {
            model.mainPage.setTodoItem(draftTodo.index, draftTodo)
        }
        return MbtElement(model.setMainPage(newMainPage), MainPageComponent())
    }

    override fun getAction(state: UiState): UiAction {
        val element = state.elements.first { it.attributes["name"].toString().endsWith("makeToDoFloatingActionButton") }
        return UiAction(element.id, "TAP", mapOf())
    }

}