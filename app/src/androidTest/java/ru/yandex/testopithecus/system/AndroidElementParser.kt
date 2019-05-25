package ru.yandex.testopithecus.system

import android.util.Base64
import androidx.test.uiautomator.*
import ru.yandex.testopithecus.rect.TRectangle
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.io.File
import java.util.stream.Collectors

object AndroidElementParser {
    fun parse(elements: List<UiObject2>): UiState {
        return UiState(parseElements(elements), buildGlobal())
    }

    fun parseWithScreenshot(elements: List<UiObject2>, screenshot: String): UiState {
        return UiState(parseElements(elements), buildGlobalWithScreenshot(screenshot))
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
        val attributes = mutableMapOf<String, Any>()
        val center = element.visibleCenter
        val top = element.visibleBounds.top
        val bottom = element.visibleBounds.bottom
        val left = element.visibleBounds.left
        val right = element.visibleBounds.right
        attributes["position"] = mutableMapOf(
                Pair("x", center.x),
                Pair("y", center.y)
        )
        if (element.resourceName != null) {
            attributes["name"] = element.resourceName
            attributes["id"] = element.resourceName
        }
        if (element.text != null) {
            attributes["text"] = element.text
        }
        attributes["top"] = top.toString()
        attributes["bottom"] = bottom.toString()
        attributes["left"] = left.toString()
        attributes["right"] = right.toString()
        val rect = element.visibleBounds
        attributes["isLabel"] = isLabelElement(element)
        return attributes
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

    fun takeScreenshot(dir: File, device: UiDevice): String {
        val scrFile = File(dir, "scr.png")
        device.takeScreenshot(scrFile)
        val bytes = scrFile.readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP) ?: ""
        assert(base64 != "")
        return base64
    }

    private fun buildGlobal(): MutableMap<String, Any> {
        return mutableMapOf()
    }

    private fun buildGlobalWithScreenshot(screenshot: String): MutableMap<String, Any> {
        return mutableMapOf("screenshot" to screenshot)
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