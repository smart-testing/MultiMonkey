package net.netau.vasyoid

import org.json.JSONObject

data class Point(val x: Int, val y: Int) {

    fun toJSON(): JSONObject {
        return JSONObject(mapOf("x" to x, "y" to y))
    }

    constructor(point: JSONObject) : this(point.getInt("x"), point.getInt("y"))
}
