package ru.yandex.testopithecus.stateenricher

import ru.yandex.testopithecus.businesslogictesting.ButtonLifeInspector
import ru.yandex.testopithecus.ui.UiState
import java.lang.RuntimeException

class ButtonLifeEnricher(cvServerAddress: String, enricher: Enricher) : EnricherDecorator(enricher) {

    private val buttonLifeInspector = ButtonLifeInspector(CvClient(cvServerAddress))
    private var buttonState = ButtonState.UNPRESSED

    private fun inspectButtonLife(uiState: UiState) {
        if ("screenshot" !in uiState.global) {
            throw RuntimeException("ButtonLifeEnricher: no \"screenshot\" in UiState")
        }
        val screenshot = uiState.global["screenshot"] as String
        if (screenshot == "") {
            throw RuntimeException("ButtonLifeEnricher: \"screenshot\" in UiState is empty")
        }
        buttonState = if (buttonState == ButtonState.UNPRESSED) {
            buttonLifeInspector.loadScreenshotBeforeAction(screenshot)
            ButtonState.PRESSED
        } else {
            buttonLifeInspector.loadScreenshotAfterAction(screenshot)
            buttonLifeInspector.assertButtonLives()
            ButtonState.UNPRESSED
        }
    }

    override fun enrichState(uiState: UiState): UiState {
        inspectButtonLife(uiState)
        return super.enrichState(uiState)
    }

    private enum class ButtonState {
        UNPRESSED, PRESSED
    }
}