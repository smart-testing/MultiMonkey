package ru.yandex.testopithecus.mbt.components

import ru.yandex.testopithecus.mbt.model.ApplicationModel
import ru.yandex.testopithecus.mbt.page.Page


interface Action {

    fun canBePerformed(model: ApplicationModel): Boolean

    fun perform(page: Page, model: ApplicationModel): Component
}