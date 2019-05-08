package ru.yandex.testopithecus.system

import androidx.test.uiautomator.*
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.util.stream.Collectors

object AndroidElementParser {
    private val cache = HashMap<Int, UiState>()
    fun parse(elements: List<UiObject2>): UiState {
        val hashCode = elements.hashCode()
        if (!cache.containsKey(hashCode) || cache[hashCode] == null) {
            cache[hashCode] = UiState(parseElements(elements), buildGlobal())
        }
        return cache[hashCode]!!
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
        return mutableMapOf(
                "text" to element.text,
                "isLabel" to isLabelElement(element),
                "rect" to RectAndroid(element.visibleBounds),
                "id" to element.resourceName)
    }

    private fun parsePossibleActions(element: UiObject2): List<String> {
        val possibleActions = mutableListOf<String>()
        if (isInputElement(element)) {
            possibleActions.add("INPUT")
        }
        if (isTapElement(element)) {
            possibleActions.add("TAP")
        }
        if (element.className.contains("EditText")) {
            possibleActions.add("FILL")
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