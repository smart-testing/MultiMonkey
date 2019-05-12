package ru.yandex.testopithecus.system

import androidx.test.uiautomator.UiDevice
import ru.yandex.testopithecus.monkeys.complex.ComplexMonkey
import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState

class AndroidMonkeyImpl(device: UiDevice, applicationPackage: String, apk: String) :
        AndroidMonkey(device, applicationPackage, apk) {
    private val model: Monkey = StateModelMonkey()

    override fun generateAction(uiState: UiState) = model.generateAction(uiState)

    override fun feedback(feedback: UiFeedback) {
        model.feedback(feedback)
    }
}