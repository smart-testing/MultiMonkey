package ru.yandex.testopithecus.mbt.page

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import ru.yandex.testopithecus.mbt.interfaces.DraftTodo
import ru.yandex.testopithecus.mbt.interfaces.DraftTodoField
import java.util.regex.Pattern


class DraftTodoActual(private val device: UiDevice): DraftTodo, Page {

    override val title: DraftTodoFieldActual
        get() = DraftTodoFieldActual(device.findObject(By.res(Pattern.compile(".*id/userToDoEditText"))))
    override val description: DraftTodoFieldActual
        get() = DraftTodoFieldActual(device.findObject(By.res(Pattern.compile(".*id/userToDoDescription"))))

    override fun addTodo() {
        device.findObject(By.res(Pattern.compile(".*id/makeToDoFloatingActionButton"))).click()
    }

    override fun validTodo(): Boolean {
        return true
    }

    override fun rejectTodo() {
        device.pressBack()
    }

}