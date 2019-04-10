package ru.yandex.multimonkey

import org.json.JSONArray
import org.json.JSONObject
import ru.yandex.multimonkey.ui.UiAction
import ru.yandex.multimonkey.ui.UiElement
import ru.yandex.multimonkey.ui.UiState


fun deserializeState(json: JSONObject): UiState {
    val elements = deserializeElements(json.getJSONArray("elements"))
    val global = json.getString("global")
    return UiState(elements, global)
}

fun deserializeElements(jsonElements: JSONArray): List<UiElement> {
    val elements = mutableListOf<UiElement>()
    for (i in 0 until jsonElements.length()) {
        elements.add(deserializeElement(jsonElements[i] as JSONObject))
    }
    return elements
}

fun deserializeElement(json: JSONObject): UiElement {
    val id = json.getString("id")
    val attributes = deserializeJSONObject(json.getJSONObject("attributes"))
    val possibleActions = deserializeStringList(json.getJSONArray("possibleActions"))
    return UiElement(id, attributes, possibleActions)
}

fun deserializeJSONObject(jsonObject: JSONObject): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    for (key in jsonObject.keys()) {
        map[key] = deserialize(jsonObject[key])
    }
    return map
}

fun deserializeJSONArray(jsonArray: JSONArray): List<Any> {
    val list = mutableListOf<Any>()
    for (i in 0 until jsonArray.length()) {
        list.add(deserialize(jsonArray[i]))
    }
    return list
}

fun deserialize(value: Any) : Any {
    return when (value) {
        is JSONObject -> deserializeJSONObject(value)
        is JSONArray -> deserializeJSONArray(value)
        else -> value
    }
}

fun deserializeStringList(jsonArray: JSONArray): List<String> {
    val list = mutableListOf<String>()
    for (i in 0 until jsonArray.length()) {
        list.add(jsonArray[i] as String)
    }
    return list
}

fun serializeAction(action: UiAction): JSONObject {
    val json = JSONObject()
    json.put("id", action.id)
    json.put("action", action.action)
    json.put("attributes", serializeAttributes(action.attributes))
    return json
}

fun serializeAttributes(attributes: Map<String, String>): JSONObject {
    val json = JSONObject()
    for ((key, value) in attributes) {
        json.put(key, value)
    }
    return json
}