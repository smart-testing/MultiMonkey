package ru.yandex.testopithecus.utils

import org.json.JSONArray
import org.json.JSONObject
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState

fun deserializeFeedback(json: JSONObject): UiFeedback {
    return UiFeedback(json.getString("status"), deserializeState(json.getJSONObject("state")))
}

fun deserializeState(json: JSONObject): UiState {
    val elements = deserializeElements(json.getJSONArray("elements"))
    val global = deserializeJSONObject(json.getJSONObject("global"))
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

fun deserializeAction(json: JSONObject): UiAction {
    val id: String? = json.optString("id", null)
    val action = json.getString("action")
    val attributes = deserializeAttributes(json.getJSONObject("attributes"))
    return UiAction(id, action, attributes)
}

fun deserializeAttributes(json: JSONObject): Map<String, String> {
    val attributes = mutableMapOf<String, String>()
    for (key in json.keys()) {
        attributes[key] = json.getString(key)
    }
    return attributes
}
