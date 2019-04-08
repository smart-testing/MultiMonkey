package ru.yandex.multimonkey.`state-monkey`.`action-generator`

import org.json.JSONObject
import ru.yandex.multimonkey.net.NetAction
import ru.yandex.multimonkey.net.NetElement
import ru.yandex.multimonkey.net.NetState

class StateActionsGeneratorImpl : StateActionsGenerator {

    override fun getActions(state: NetState) : List<NetAction> {
        val actions = mutableListOf<NetAction>()
        state.elements.stream()
            .forEach  { element -> addActionsToList(actions, element) }
        actions.add(NetAction(JSONObject(mapOf("type" to "SKIP"))))
        return actions
    }

    private fun addActionsToList(actions: MutableList<NetAction>, element: NetElement) {
        if (element.isClickable) {
            val center = element.center
            val json = JSONObject()
            val positionJson = JSONObject()
            positionJson.put("x", center.x)
            positionJson.put("y", center.y)
            json.put("position", positionJson)
            json.put("type", "TAP")
            actions.add(NetAction(json))
        }
    }

}