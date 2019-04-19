package ru.yandex.testopithecus.mbt.components

import ru.yandex.testopithecus.mbt.model.ApplicationModel
import ru.yandex.testopithecus.mbt.page.DraftTodoActual
import ru.yandex.testopithecus.mbt.page.Page


class WriteTitleAction: Action {

    override fun canBePerformed(model: ApplicationModel): Boolean {
        return model.draftTodoModel.title.getText().isBlank()
    }

    override fun perform(page: Page, model: ApplicationModel): Component {
        val draftTodoElement = page as DraftTodoActual
        val draftTodoModel = model.draftTodoModel

        val text = generateText()

        draftTodoElement.title.setText(text)
        draftTodoModel.title.setText(text)

        return DraftTodoComponent(draftTodoElement)
    }

    private fun generateText(): String {
        return "Some text"
    }

}