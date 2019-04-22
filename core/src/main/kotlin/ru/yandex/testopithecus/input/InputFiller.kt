package ru.yandex.testopithecus.input

import ru.yandex.testopithecus.rect.RectComparison
import ru.yandex.testopithecus.rect.Rectangle
import ru.yandex.testopithecus.ui.UiElement
import java.util.stream.Collectors


object InputFiller {
    private var configs: List<Config> = listOf(Config("resource-id", "content9a9a1b96221c466eb24d2e48dadd4536", "Hi there!"),
            Config("text", "To", "pushkin@yandex.ru"),
            Config("text", "Subject", "Поэзия"),
            Config("text", "Search emails", "Анна Керн"),
            Config("text", "Cc", "jukovsky@yandex.ru"),
            Config("text", "Bcc", "voronzhov@yandex.ru"))
    fun fillInput(input:UiElement, allTextLabels: Collection<UiElement>) {
        if (!fillMarkedInput(input)) {
            fillUnmarkedInput(input, allTextLabels)
        }
    }
    private fun fillUnmarkedInput(unmarkedInput : UiElement, allTextLabels : Collection<UiElement>) : Boolean {
        val markedTextViews = findMarkedTextLabels(allTextLabels)
        var minDistance = Integer.MAX_VALUE
        var nearestTextLabel: UiElement? = null
        for (markedTextView in markedTextViews) {
            val curDistance = RectComparison.minDistance((unmarkedInput.attributes["rect"] as Rectangle),
            (markedTextView.attributes["rect"] as Rectangle))
            if (minDistance > curDistance) {
                minDistance = curDistance
                nearestTextLabel = markedTextView
            }
        }
        var fillValue = ""
        for (config in configs) {
            if (config.type.contains("text")) {
                if (nearestTextLabel != null && config.value == nearestTextLabel.attributes["text"]) {
                    fillValue = config.fillValue
                }
            } else if (config.type.contains("id")) {
                if (nearestTextLabel != null && config.value == nearestTextLabel.attributes["text"]) {
                    fillValue = config.fillValue
                }
            }
        }
        unmarkedInput.attributes["text"] = fillValue
        return fillValue != ""
    }

    private fun findMarkedTextLabels(allTextLabels: Collection<UiElement>): List<UiElement> {
        val markedTextViews = ArrayList<UiElement>()
        for (config in configs) {
            if (config.type.contains("id")) {
                markedTextViews.addAll(allTextLabels.stream()
                        .filter { x -> x.attributes["id"] == config.value }
                        .collect(Collectors.toList()))
            } else if (config.type.contains("text")) {
                markedTextViews.addAll(allTextLabels.stream()
                        .filter { x -> x.attributes["text"] == config.value }
                        .collect(Collectors.toList()))
            }
        }
        return markedTextViews
    }

    private fun fillMarkedInput(input: UiElement): Boolean {
        for (config in configs) {
            if (config.type.contains("id") && input.attributes["id"] == config.value) {
                input.attributes["text"] = config.fillValue
                return true
            } else if (config.type.contains("text")&& input.attributes["text"] == config.value) {
                input.attributes["text"] = config.fillValue
                return true
            }
        }
        return false
    }
}