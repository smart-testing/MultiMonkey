package ru.yandex.testopithecus.mbt.components

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import ru.yandex.testopithecus.mbt.model.ApplicationModel
import ru.yandex.testopithecus.mbt.model.DraftTodoModel
import ru.yandex.testopithecus.mbt.page.DraftTodoActual


class DraftTodoComponent(override val page: DraftTodoActual): Component {

    override fun actions(): List<Action> {
        return listOf(
            WriteTitleAction()
        )
    }

    override fun assertMatches(model: ApplicationModel) {
        val actualModel = DraftTodoModel(page)
        val expectedModel = model.draftTodoModel

        if (actualModel != expectedModel) {
            fail()
        }
    }
}