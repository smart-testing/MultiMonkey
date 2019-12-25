package ru.yandex.testopithecus.web_monkey


import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import ru.yandex.testopithecus.ui.ActionPerformer
import ru.yandex.testopithecus.ui.UiAction


class WebActionPerformer(private val driver: WebDriver, private val element: WebElement): ActionPerformer {
    override fun perform(action: UiAction) {
        when (action.action) {
            "TAP" -> { Actions(driver).moveToElement(element).click().build().perform()}
            "INPUT" -> {
                Actions(driver).moveToElement(element).click().sendKeys(action.attributes["text"]).click().build().perform()
            }
            else -> throw IllegalStateException("Unknown action '${action.action}'")
        }
    }
}