package ru.yandex.testopithecus.system

import androidx.test.uiautomator.*
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.util.stream.Collectors

object AndroidElementParser {
    fun parse(elements: List<UiObject2>): UiState {
        return UiState(parseElements(elements), buildGlobal())
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

    private fun parseAttributes(element: UiObject2): MutableMap<String, Any> {
        val center = element.visibleCenter
        val attributes = mutableMapOf("location" to Pair(Pair("x", center.x), Pair("y", center.y)),
                "text" to element.text,
                "isLabel" to (element.className == "android.widget.TextView"),
                "rect" to RectAndroid(element.visibleBounds),
                "id" to element.resourceName)
        return attributes
    }

    private fun parsePossibleActions(element: UiObject2): List<String> {
        val possibleActions = mutableListOf<String>()
        println("-p ${element.className}")
        if (element.isClickable) {
            possibleActions.add("TAP")
        }
        if (element.className == "android.widget.EditText") {
            possibleActions.add("INPUT")
        }
        return possibleActions
    }

    private fun buildGlobal(): Map<String, Any> {
        return mapOf()
    }
}