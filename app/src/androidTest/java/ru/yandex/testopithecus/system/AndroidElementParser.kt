package ru.yandex.testopithecus.system

import androidx.test.uiautomator.*
import ru.yandex.testopithecus.rect.TRectangle
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

    private fun parseAttributes(element: UiObject2): Map<String, Any> {
        val rect = element.visibleBounds
        return mapOf(
                "text" to element.text,
                "isLabel" to isLabelElement(element),
                "rect" to TRectangle(rect.top, rect.left, rect.right, rect.bottom),
                "id" to element.resourceName
        )
    }

    private fun parsePossibleActions(element: UiObject2): List<String> {
        val possibleActions = mutableListOf<String>()
        if (isInputElement(element)) {
            possibleActions.add("INPUT")
        }
        if (isTapElement(element)) {
            possibleActions.add("TAP")
        }
        return possibleActions
    }

    private fun buildGlobal(): Map<String, Any> {
        return mapOf()
    }

    private fun isLabelElement(element: UiObject2): Boolean {
        return element.text != null && element.text.isNotEmpty() && !element.isClickable
                && (element.className == "android.widget.TextView" || element.className == "TextInputLayout")
    }

    private fun isTapElement(element: UiObject2): Boolean {
        return element.isClickable && (element.className != "android.widget.EditText")
    }

    private fun isInputElement(element: UiObject2): Boolean {
        return element.className == "android.widget.EditText"
    }
}