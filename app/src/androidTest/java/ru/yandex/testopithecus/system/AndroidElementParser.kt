package ru.yandex.testopithecus.system

import androidx.test.uiautomator.*
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.util.stream.Collectors

object AndroidElementParser {
    fun parse(elements: List<UiObject2>): UiState {
        return UiState(AndroidElementParser.parseElements(elements), buildGlobal())
    }

    private fun parseElements(elements: List<UiObject2>): List<UiElement> {
        var id = 0
        return elements.stream()
                .map { element -> parse(id.toString(), element) }
                .peek { id++ }
                .collect(Collectors.toList())
    }

    private fun parse(elementId: String, element: UiObject2): UiElement {
        return UiElement(elementId, parseAttributes(element), parsePossibleActions(element))
    }

    private fun parseAttributes(element: UiObject2): Map<String, Any> {
        val attributes = mutableMapOf<String, Any>()
        val center = element.visibleCenter
        attributes["position"] = mapOf(
                Pair("x", center.x),
                Pair("y", center.y)
        )
        if (element.resourceName != null) {
            attributes["name"] = element.resourceName
        }
        if (element.text != null) {
            attributes["text"] = element.text
        }
        return attributes
    }

    private fun parsePossibleActions(element: UiObject2): List<String> {
        val possibleActions = mutableListOf<String>()
        if (element.isClickable) {
            possibleActions.add("TAP")
        }
        return possibleActions
    }

    private fun buildGlobal(): Map<String, Any> {
        return mapOf()
    }
}