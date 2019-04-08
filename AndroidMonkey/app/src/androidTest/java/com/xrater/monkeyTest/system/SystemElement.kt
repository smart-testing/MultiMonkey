package com.xrater.monkeyTest.system

import androidx.test.uiautomator.*
import ru.yandex.multimonkey.net.NetElement
import ru.yandex.multimonkey.net.Point

class SystemElement(val obj: UiObject2) {

    fun buildNetElement() : NetElement {
        val center = obj.visibleCenter
        return NetElement(
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