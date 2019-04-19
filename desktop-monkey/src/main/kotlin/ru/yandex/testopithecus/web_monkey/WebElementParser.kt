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
                .map { element -> parse(id.toString(), element) }
                .peek { id++ }
                .collect(Collectors.toList())
    }

    private fun parse(elementId: String, element: WebElement): UiElement {
        return UiElement(elementId, parseAttributes(element), parsePossibleActions(element))
    }

    private fun parseAttributes(element: WebElement): Map<String, Any> {
        val attributes = mutableMapOf<String, Any>()
        val center = element.location
        attributes["position"] = mapOf(
                Pair("x", center.x),
                Pair("y", center.y)
        )
        return attributes
    }

    private fun parsePossibleActions(element: WebElement): List<String> {
        val possibleActions = mutableListOf<String>()
        if (element.tagName == "a" || element.tagName == "button") {
            possibleActions.add("TAP")
        }
        return possibleActions
    }

    private fun buildGlobal(): Map<String, Any> {
        return mapOf()
    }
}