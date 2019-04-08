package ru.yandex.multimonkey.system

import androidx.test.uiautomator.*
import ru.yandex.multimonkey.SimpleUiTest
import ru.yandex.multimonkey.net.NetElement
import ru.yandex.multimonkey.net.Monkey
import ru.yandex.multimonkey.`state-monkey`.StateModelMonkey
import ru.yandex.multimonkey.net.NetState
import java.util.stream.Collectors


class SystemMonkey(private val device: UiDevice) : AndroidMonkey {

    private val model: Monkey = StateModelMonkey()

    override fun generateAction(): SystemAction? {
        val netState: NetState
        try {
             netState = buildState()
        } catch (e: StaleObjectException) { return null }
        val action = model.generateAction(netState)
        return SystemAction(action, device)
    }

    private fun buildState() : NetState {
        return NetState(collectElements())
    }

    private fun collectElements(): MutableList<NetElement> {
        val elements = device.findObjects(By.pkg(SimpleUiTest.APPLICATION_PACKAGE))
        return elements.stream()
            .map { element -> SystemElement(element).buildNetElement() }
            .collect(Collectors.toList())
    }

}