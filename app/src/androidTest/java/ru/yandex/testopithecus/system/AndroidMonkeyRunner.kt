package ru.yandex.testopithecus.system

import androidx.test.uiautomator.By
import androidx.test.uiautomator.StaleObjectException
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState
import java.io.File
import khttp.post as httpPost

class AndroidMonkeyRunner(
        private val device: UiDevice,
        private val applicationPackage: String,
        private val apk: String,
        private val actionGenerator: AndroidActionGenerator,
        private val screenshotDir: File? = null) {

    private val useScreenshots = screenshotDir != null

    companion object {
        const val ANDROID_LOCALHOST = "10.0.2.2"
    }

    fun performAction() {
        val (action, element) = generateActionImpl()
        AndroidActionPerformer(device, applicationPackage, apk, element).perform(action)
        performFeedbackImpl()
    }

    private fun generateActionImpl(): Pair<UiAction, UiObject2?> {
        while (true) {
            try {
                val elements = device.findObjects(By.pkg(applicationPackage))
                var uiState: UiState
                uiState = if (useScreenshots) {
                    val screenshot = AndroidElementParser.takeScreenshot(screenshotDir!!, device)
                    AndroidElementParser.parseWithScreenshot(elements, screenshot)
                } else {
                    AndroidElementParser.parse(elements)
                }
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

    private fun takeScreenshot(): String {
        return AndroidElementParser.takeScreenshot(screenshotDir!!, device)
    }

    private fun generateAction(uiState: UiState): UiAction {
        return actionGenerator.generateAction(uiState)
    }

    private fun feedback(feedback: UiFeedback) {
        actionGenerator.feedback(feedback)
    }
}