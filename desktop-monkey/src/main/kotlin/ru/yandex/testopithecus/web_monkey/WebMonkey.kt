package ru.yandex.testopithecus.web_monkey

import org.openqa.selenium.By
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.ui.Monkey

class WebMonkey(private val driver: WebDriver) {

    private val model: Monkey = StateModelMonkey()
    private val selector = listOf("input:not(hidden)", "label>div", "div>span", "div[contenteditable='true']",
            "label", "button", "a:link")
    private val strSelector = selector.joinToString()
    fun performAction() = try {
        performActionImpl()
    } catch (e: StaleElementReferenceException) {
        System.err.println(e.localizedMessage)
    }

    private fun performActionImpl() {
        val elements = driver.findElements(By.cssSelector(strSelector))
        val uiState = WebElementParser.parse(elements)
        val action = model.generateAction(uiState)
        val id = action.id?.toInt()
        val element = if (id != null) elements[id] else return
        WebActionPerformer(driver, element).perform(action)
    }
}