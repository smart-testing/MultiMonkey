package ru.yandex.testopithecus.mbt.components

import ru.yandex.testopithecus.mbt.model.ApplicationModel
import ru.yandex.testopithecus.mbt.page.Page


interface Component {

    val page: Page

    fun actions(): List<Action>

    fun assertMatches(model: ApplicationModel)

}