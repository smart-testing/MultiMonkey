package ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.draftTodo

import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.mbt.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application.ApplicationModel
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState
import java.lang.IllegalArgumentException


class WriteTitleAction: ModelAction {
    override fun canBePerformed(model: ApplicationModel): Boolean {
        return model.currentTodo?.title?.isBlank() ?: false
    }

    override fun perform(model: ApplicationModel): MbtElement {
        val text = generateText()
        val newDraftTodo = model.currentTodo?.setTitle(text) ?: throw IllegalArgumentException()
        return MbtElement(model.setCurrentTodo(newDraftTodo), DraftTodoComponent())
    }

    override fun getAction(state: UiState): UiAction {
        val element = state.elements.first { it.attributes["name"].toString().endsWith("userToDoEditText") }
        return UiAction(element.id, "INPUT", mapOf("text" to "Some text"))
    }

    private fun generateText(): String {
        return "Some text"
    }

}