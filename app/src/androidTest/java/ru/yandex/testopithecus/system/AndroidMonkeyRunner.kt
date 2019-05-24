package ru.yandex.testopithecus.system

import androidx.test.uiautomator.*
import khttp.post as httpPost

import ru.yandex.testopithecus.businesslogictesting.ButtonLifeInspector
import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.stateenricher.CvClient
import ru.yandex.testopithecus.stateenricher.SimpleEnricher
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState
import ru.yandex.testopithecus.utils.deserializeAction
import ru.yandex.testopithecus.utils.serializeFeedback
import ru.yandex.testopithecus.utils.serializeUiState
import java.io.File

class AndroidMonkeyRunner(
        private val device: UiDevice,
        private val applicationPackage: String,
        private val apk: String,
        private val useHTTP: Boolean = false,
        private val screenshotDir: File? = null,
        url: String = "",
        useButtonLifeInspector: Boolean = false,
        mode: String = "",
        file: String? = null) {

    private val useScreenshots = screenshotDir != null
    private val model: Monkey = StateModelMonkey(SimpleEnricher(url))
    private val urlButtonLifeInspector = "http://$ANDROID_LOCALHOST:5000/button-alive"
    private val buttonLifeInspector: ButtonLifeInspector = ButtonLifeInspector(useButtonLifeInspector,
            ::takeScreenshot,
            CvClient(urlButtonLifeInspector))

    init {
        if (useHTTP && url == "") {
            httpPost("$URL$INIT$mode/${file ?: ""}")
        }
    }

    companion object {
        const val ANDROID_LOCALHOST = "10.0.2.2"
        const val LONG_WAIT = 1000.toLong()
        private const val URL = "http://10.0.2.2:8080/"
        private const val GENERATE_ACTION = "generate-action/"
        private const val FEEDBACK_URL = "feedback/"
        private const val INIT = "init/"
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
                if (useScreenshots) {
                    val screenshot = AndroidElementParser.takeScreenshot(screenshotDir!!, device)
                    uiState = AndroidElementParser.parseWithScreenshot(elements, screenshot)
                } else {
                    uiState = AndroidElementParser.parse(elements)
                }
                val action = generateAction(uiState)
                val id = action.id?.toInt()
                val element = id?.let { elements[id] }
                if (useScreenshots) {
                    //ToDo обработать SKIP
                    buttonLifeInspector.loadScreenshotBeforeAction()
                    AndroidActionPerformer(device, applicationPackage, apk, element).perform(action)
                    buttonLifeInspector.loadScreenshotAfterAction()
                    buttonLifeInspector.assertButtonLives()
                }
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

    private fun generateActionHTTP(uiState: UiState): UiAction {
        val response = httpPost(URL + GENERATE_ACTION, json = serializeUiState(uiState))
        return deserializeAction(response.jsonObject)
    }

    private fun feedbackHTTP(feedback: UiFeedback) {
        httpPost(URL + FEEDBACK_URL, json = serializeFeedback(feedback))
    }

    private fun generateAction(uiState: UiState): UiAction {
        if (useHTTP) return generateActionHTTP(uiState)
        return model.generateAction(uiState)
    }

    fun feedback(feedback: UiFeedback) {
        if (useHTTP) {
            feedbackHTTP(feedback)
        } else {
            model.feedback(feedback)
        }
    }
}