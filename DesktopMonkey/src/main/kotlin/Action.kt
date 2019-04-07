package net.netau.vasyoid

import org.json.JSONObject
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Actions

class Action(val type: Type, val position: Point) {

    constructor(action: JSONObject) : this(
        Type.valueOf(action.getString("type")), Point(action.getJSONObject("position"))
    )

    fun perform(driver: ChromeDriver) {
        when (type) {
            Type.TAP -> {
                Actions(driver)
                    .moveByOffset(position.x, position.y)
                    .click()
                    .moveByOffset(-position.x, -position.y)
                    .perform()
            }
        }
    }

    enum class Type {
        TAP
    }
}
