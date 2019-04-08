package ru.yandex.multimonkey

import org.json.JSONObject
import org.openqa.selenium.Dimension as DimensionS
import org.openqa.selenium.Point as PointS

data class Element(val position: Point, val size: Point) {

    fun toJSON(): JSONObject {
        return JSONObject(mapOf("position" to position.toJSON(), "size" to size.toJSON()))
    }

    constructor(position: PointS, size: DimensionS) :
            this(Point(position.x, position.y), Point(size.width, size.height))
}
