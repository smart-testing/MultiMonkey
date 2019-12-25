package ru.yandex.testopithecus.web_monkey

import org.openqa.selenium.WebElement
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.util.stream.Collectors


object WebElementParser {
    fun parse(elements: List<WebElement>): UiState {
        return UiState(parseElements(elements), buildGlobal())
    }

    private fun parseElements(elements: List<WebElement>): List<UiElement> {
        var id = 0
        return elements.stream()
                .peek{x -> x.isDisplayed}
                .map { element -> parse(id.toString(), element) }
                .peek { id++ }
                .collect(Collectors.toList())
    }

    private fun parse(elementId: String, element: WebElement): UiElement {
        return UiElement(elementId, parseAttributes(element), parsePossibleActions(element))
    }

    private fun parseAttributes(element: WebElement): MutableMap<String, Any> {
        val center = element.location
        return mutableMapOf("text" to element.text,
                "isLabel" to isLabelElement(element),
                "rect" to RectSelenium(element.rect),
                "placeholder" to element.getAttribute("placeholder"),
                "class" to element.getAttribute("class"),
                "center" to center)
    }

    private fun parsePossibleActions(element: WebElement): List<String> {
        val possibleActions = mutableListOf<String>()
        if (isTapElement(element)) {
            possibleActions.add("TAP")
        }
        if (isInputElement(element)) {
            possibleActions.add("INPUT")
        }
        return possibleActions
    }

    private fun buildGlobal(): Map<String, Any> {
        return mapOf()
    }
    private fun isLabelElement(element : WebElement) : Boolean {
        return (element.tagName == "div" || element.tagName == "span" || element.tagName == "label") && element.text.isNotEmpty()
    }
    private fun isInputElement(element : WebElement) : Boolean {
        val type = element.getAttribute("type")
        return (element.tagName == "input" &&  (type == "password" || type == "text")
                || element.tagName == "div" && (element.getAttribute("contenteditable") == "true"))
    }
    private fun isTapElement(element : WebElement) : Boolean {
        val type = element.getAttribute("type")
        return element.tagName == "a" || element.tagName == "button"
                || (element.tagName == "input" && (type == "submit" || type == "reset" || type == "radio" || type == "checkbox" || type == "button"))
    }
}