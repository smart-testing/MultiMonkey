package ru.yandex.testopithecus.utils

import org.json.JSONArray
import org.json.JSONObject
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiFeedback
import ru.yandex.testopithecus.ui.UiState

fun serializeFeedback(uiFeedback: UiFeedback): JSONObject {
    val json = JSONObject()
    json.put("status", uiFeedback.status)
    json.put("state", serializeUiState(uiFeedback.state))
    return json
}

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

fun deserializeJSONObject(jsonObject: JSONObject): MutableMap<String, Any> {
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

fun deserialize(value: Any): Any {
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

fun serializeJSONObject(mapObject: Map<String, Any>): JSONObject {
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

fun serialize(value: Any): Any {
    @Suppress("UNCHECKED_CAST")
    return when (value) {
        is Map<*, *> -> serializeJSONObject(value as Map<String, Any>)
        is List<*> -> serializeJSONArray(value as List<Any>)
        else -> value
    }
}