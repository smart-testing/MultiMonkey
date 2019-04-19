package ru.yandex.testopithecus.mbt.model

import org.apache.commons.lang3.builder.EqualsBuilder
import ru.yandex.testopithecus.mbt.interfaces.DraftTodoField
import ru.yandex.testopithecus.mbt.page.DraftTodoFieldActual


class DraftTodoFieldModel: DraftTodoField {

    private var text: String

    constructor() {
        text = ""
    }

    constructor(page: DraftTodoFieldActual) {
        text = page.getText()
    }

    override fun getText(): String {
        return text
    }

    override fun setText(text: String) {
        this.text = text
    }

    override fun equals(other: Any?): Boolean {
        if (other is DraftTodoFieldModel) {
            return EqualsBuilder()
                    .append(text, other.text)
                    .isEquals
        }
        return false
    }

}