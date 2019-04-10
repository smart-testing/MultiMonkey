package ru.yandex.multimonkey.system

import androidx.test.uiautomator.*
import ru.yandex.multimonkey.ui.UiElement

class SystemElement(val obj: UiObject2) {

    fun buildUiElement(uid: String) : UiElement {
        return UiElement(uid, constructAttributes(), constructPossibleActions())
    }

    private fun constructAttributes() : Map<String, Any> {
        val attributes = mutableMapOf<String, Any>()
        val center = obj.visibleCenter
        attributes["position"] = mapOf(
                Pair("x", center.x),
                Pair("y", center.y)
        )
        return attributes
    }

    private fun constructPossibleActions(): List<String> {
        val possibleActions = mutableListOf<String>()
        if (obj.isClickable) {
            possibleActions.add("TAP")
        }
        return possibleActions
    }
}