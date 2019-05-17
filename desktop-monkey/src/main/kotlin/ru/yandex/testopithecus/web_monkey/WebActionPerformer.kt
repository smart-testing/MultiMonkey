package ru.yandex.testopithecus.web_monkey

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import ru.yandex.testopithecus.ui.ActionPerformer
import ru.yandex.testopithecus.ui.UiAction


class WebActionPerformer(private val driver: WebDriver, private val element: WebElement): ActionPerformer {
    override fun perform(action: UiAction) {
        when (action.action) {
            "TAP" -> element.click()
//            "INPUT" -> InputFiller.fillInput(element, driver)
            else -> throw IllegalStateException("Unknown action '${action.action}'")
        }
    }
}