package net.netau.vasyoid

import org.json.JSONArray
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

import khttp.post as httpPost

class MonkeyController(private val driver: ChromeDriver) {

    fun generateAction(): Action {
        val headers = mapOf("Content-Type" to "application/json")
        val json = getElementsJSON()
        val response = httpPost(HOST, headers = headers, json = json)
        return Action(response.jsonObject)
    }

    private fun getElementsJSON(): JSONArray {
        val elements = driver.findElements(By.cssSelector("[class]:not([class=\"\"])"))
            .filter {
                it.isDisplayed && it.isEnabled && it.location != null && it.size != null
            }
            .map {
                Element(it.location, it.size)
            }
        println(elements)
        val json = JSONArray()
        elements.forEach {
            json.put(it.toJSON())
        }
        return json
    }

    private fun waitUntil(condition: ExpectedCondition<out Any>) {
        WebDriverWait(driver, 1).until(condition)
    }

    companion object {
        private const val HOST = "http://127.0.0.1:5000"
    }
}
