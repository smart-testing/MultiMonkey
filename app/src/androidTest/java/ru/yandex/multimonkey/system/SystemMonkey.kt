package ru.yandex.multimonkey.system

import androidx.test.uiautomator.*
import ru.yandex.multimonkey.SimpleUiTest
import ru.yandex.multimonkey.net.UiElement
import ru.yandex.multimonkey.net.Monkey
import ru.yandex.multimonkey.`state-monkey`.StateModelMonkey
import ru.yandex.multimonkey.net.UiState
import java.util.stream.Collectors


class SystemMonkey(private val device: UiDevice) : AndroidMonkey {

    private val model: Monkey = StateModelMonkey()

    override fun generateAction(): SystemAction? {
        val uiState: UiState
        try {
             uiState = buildState()
        } catch (e: StaleObjectException) { return null }
        val action = model.generateAction(uiState)
        return SystemAction(action, device)
    }

    private fun buildState() : UiState {
        return UiState(collectElements())
    }

    private fun collectElements(): MutableList<UiElement> {
        val elements = device.findObjects(By.pkg(SimpleUiTest.APPLICATION_PACKAGE))
        return elements.stream()
            .map { element -> SystemElement(element).buildUiElement() }
            .collect(Collectors.toList())
    }

}