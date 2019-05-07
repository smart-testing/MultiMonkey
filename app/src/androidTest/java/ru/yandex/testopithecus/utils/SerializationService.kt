package ru.yandex.testopithecus.utils

import org.json.JSONArray
import org.json.JSONObject
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState


fun serializeUiState(uiState: UiState): JSONObject {
    val json = JSONObject()
    val jsonElements = serializeElements(uiState.elements)
    val jsonGlobal = serializeJSONObject(uiState.global)
    json.put("elements", jsonElements)
    json.put("global", jsonGlobal)
    return json
}

fun serializeElements(uiElements: List<UiElement>): JSONArray {
    val jsonElements = JSONArray()
    for (uiElement in uiElements) {
        jsonElements.put(serializeUiElement(uiElement))
    }
    return jsonElements
}

fun serializeUiElement(uiElement: UiElement): JSONObject {
    val json = JSONObject()
    json.put("id", uiElement.id)
    json.put("attributes", serializeJSONObject(uiElement.attributes))
    json.put("possibleActions", serializeJSONArray(uiElement.possibleActions))
    return json
}

fun serializeJSONObject(mapObject: Map<String, Any>): JSONObject  {
    val json = JSONObject()
    for ((key, value) in mapObject) {
        json.put(key, serialize(value))
    }
    return json
}

fun serializeJSONArray(list: List<Any>): JSONArray {
    val jsonArray = JSONArray()
    for (element in list) {
        jsonArray.put(serialize(element))
    }
    return jsonArray
}

fun serialize(value: Any) : Any {
    return when (value) {
        is Map<*, *> -> serializeJSONObject(value as Map<String, Any>)
        is List<*> -> serializeJSONArray(value as List<Any>)
        else -> value
    }
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