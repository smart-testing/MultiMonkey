package ru.yandex.testopithecus.mbt.page

import androidx.test.uiautomator.UiObject2
import ru.yandex.testopithecus.mbt.interfaces.DraftTodoField


class DraftTodoFieldActual(private val textField: UiObject2): DraftTodoField {

    override fun getText(): String {
        return textField.text ?: ""
    }

    override fun setText(text: String) {
        textField.text = text
    }

}