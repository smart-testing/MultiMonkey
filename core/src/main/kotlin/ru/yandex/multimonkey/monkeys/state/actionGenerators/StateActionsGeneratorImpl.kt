package ru.yandex.multimonkey.monkeys.state.actionGenerators

import org.json.JSONObject
import ru.yandex.multimonkey.net.UiAction
import ru.yandex.multimonkey.net.UiElement
import ru.yandex.multimonkey.net.UiState

class StateActionsGeneratorImpl : StateActionsGenerator {

    override fun getActions(state: UiState) : List<UiAction> {
        val actions = mutableListOf<UiAction>()
        state.elements.stream()
            .forEach  { element -> addActionsToList(actions, element) }
        actions.add(UiAction(JSONObject(mapOf("type" to "SKIP"))))
        return actions
    }

    private fun addActionsToList(actions: MutableList<UiAction>, element: UiElement) {
        if (element.isClickable) {
            val center = element.center
            val json = JSONObject()
            val positionJson = JSONObject()
            positionJson.put("x", center.x)
            positionJson.put("y", center.y)
            json.put("position", positionJson)
            json.put("type", "TAP")
            actions.add(UiAction(json))
        }
    }

}