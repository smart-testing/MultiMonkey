package ru.yandex.testopithecus.system

import androidx.test.uiautomator.By
import androidx.test.uiautomator.StaleObjectException
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState

abstract class AndroidMonkey(
        private val device: UiDevice,
        private val applicationPackage: String,
        private val apk: String) {

    fun performAction() {
        val (action, element) = generateActionImpl()
        AndroidActionPerformer(device, applicationPackage, apk, element).perform(action)
        performFeedbackImpl()
    }

    private fun generateActionImpl(): Pair<UiAction, UiObject2?> {
        while (true) {
            try {
                val elements = device.findObjects(By.pkg(applicationPackage))
                val uiState = AndroidElementParser.parse(elements)
                val action = generateAction(uiState)
                val id = action.id?.toInt()
                val element = id?.let { elements[id] }
                return action to element
            } catch (e: StaleObjectException) {
                System.err.println(e.localizedMessage)
            }
        }
    }

    private fun performFeedbackImpl() {
        var feedbackSend = false
        while (!feedbackSend) {
            try {
                val elements = device.findObjects(By.pkg(applicationPackage))
                val uiState = AndroidElementParser.parse(elements)
                feedback(UiFeedback("OK", uiState))
                feedbackSend = true
            } catch (e: StaleObjectException) {
                System.err.println(e.localizedMessage)
            }
        }
    }

    protected abstract fun generateAction(uiState: UiState): UiAction

    protected abstract fun feedback(feedback: UiFeedback)
}