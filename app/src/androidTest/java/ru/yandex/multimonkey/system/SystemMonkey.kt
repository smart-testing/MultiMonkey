package ru.yandex.multimonkey.system

import androidx.test.uiautomator.*
import ru.yandex.multimonkey.SimpleUiTest
import ru.yandex.multimonkey.ui.UiElement
import ru.yandex.multimonkey.ui.Monkey
import ru.yandex.multimonkey.monkeys.state.StateModelMonkey
import ru.yandex.multimonkey.ui.UiState
import java.util.stream.Collectors

class SystemMonkey(private val device: UiDevice) : AndroidMonkey {

    private val model: Monkey = StateModelMonkey()

    override fun generateAction(): SystemAction? {
        val uiState: UiState
        val elements: List<UiObject2>
        try {
            elements = device.findObjects(By.pkg(SimpleUiTest.APPLICATION_PACKAGE))
            uiState = UiState(constructElements(elements), buildGlobal())
        } catch (e: StaleObjectException) { return null }
        val action = model.generateAction(uiState)
        val id = action.id?.toInt()
        val element: UiObject2?
        if (id == null) {
            element = null
        } else {
            element = elements[id]
        }
        return SystemAction(action, element, device)
    }

    private fun buildGlobal(): Map<String, Any> {
        return mapOf()
    }

    private fun constructElements(elements: List<UiObject2>): MutableList<UiElement> {
        var id = 0
        return elements.stream()
                .map { element -> SystemElement(element).buildUiElement(id.toString()) }
                .peek { id++ }
                .collect(Collectors.toList())
    }
}