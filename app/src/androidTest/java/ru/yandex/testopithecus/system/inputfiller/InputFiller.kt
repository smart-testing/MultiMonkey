package ru.yandex.testopithecus.system.inputfiller

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.yandex.testopithecus.rect.RectComparison
import java.io.File
import java.util.stream.Collectors


object InputFiller {
    private var configs: List<Config> = ArrayList()
    private const val RELATIVE_PATH_TO_PROPERTIES = "raw/config.json"


    init  {
        val mapper = ObjectMapper()
        configs = mapper.readValue(File(RELATIVE_PATH_TO_PROPERTIES))
    }
    fun fillInput(input:UiObject2, device: UiDevice) {
        if (!fillMarkedInput(input)) {
            fillUnmarkedInput(input, device)
        }
    }
    private fun fillUnmarkedInput(unmarkedInput : UiObject2, device:UiDevice) : Boolean {
        val markedTextViews = findMarkedTextViews(device)
        var minDistance = Integer.MAX_VALUE
        var nearestTextView: UiObject2? = null
        for (markedTextView in markedTextViews) {
            val curDistance = RectComparison.minDistance(RectAndroid(unmarkedInput.visibleBounds),
                    RectAndroid(markedTextView.visibleBounds))
            if (minDistance > curDistance) {
                minDistance = curDistance
                nearestTextView = markedTextView
            }
        }
        var fillValue = ""
        for (config in configs) {
            if (config.type.contains("text")) {
                if (nearestTextView != null && config.value == nearestTextView.text) {
                    fillValue = config.fillValue
                }
            } else if (config.type.contains("id")) {
                if (nearestTextView != null && config.value == nearestTextView.text) {
                    fillValue = config.fillValue
                }
            }
        }
        unmarkedInput.text = fillValue
        return fillValue != ""
    }

    private fun findMarkedTextViews(device: UiDevice): List<UiObject2> {
        val markedTextViews = ArrayList<UiObject2>()
        val allTextViews = device.findObjects(By.clazz("android.widget.EditText"))
        for (config in configs) {
            if (config.type.contains("id")) {
                markedTextViews.addAll(allTextViews.stream()
                        .filter { x -> x.resourceName == config.value }
                        .collect(Collectors.toList()))
            } else if (config.type.contains("text")) {
                markedTextViews.addAll(allTextViews.stream()
                        .filter { x -> x.text == config.value }
                        .collect(Collectors.toList()))
            }
        }
        return markedTextViews
    }

    private fun fillMarkedInput(input: UiObject2): Boolean {
        for (config in configs) {
            if (config.type.contains("id") && input.resourceName.contains(config.value)) {
                input.text = config.fillValue
                return true
            } else if (config.type.contains("text")&& input.text.contains(config.value)) {
                input.text = config.fillValue
                return true
            }
        }
        return false
    }
}