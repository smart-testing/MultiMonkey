package ru.yandex.multimonkey.system

import androidx.test.uiautomator.*
import ru.yandex.multimonkey.net.UiElement
import ru.yandex.multimonkey.net.Point

class SystemElement(val obj: UiObject2) {

    fun buildUiElement() : UiElement {
        val center = obj.visibleCenter
        return UiElement(
            Point(center.x, center.y),
            obj.isCheckable,
            obj.isClickable,
            obj.isFocusable,
            obj.isLongClickable,
            obj.resourceName,
            obj.className
        )
    }

}