package ru.yandex.testopithecus.input

import ru.yandex.testopithecus.rect.RectComparison
import ru.yandex.testopithecus.rect.TRectangle
import ru.yandex.testopithecus.ui.UiElement
import java.util.stream.Collector
import java.util.stream.Collectors


object InputFiller {
    private var configs: List<Config> = listOf(Config("resource-id", "content9a9a1b96221c466eb24d2e48dadd4536", "Hi there!"),
            Config("text", "To", "pushkin@yandex.ru"),
            Config("text", "Subject", "Поэзия"),
            Config("text", "Search emails", "Анна Керн"),
            Config("text", "Cc", "jukovsky@yandex.ru"),
            Config("text", "Bcc", "voronzhov@yandex.ru"),
            Config("placeholder", "Поиск", "Анна Керн"),
            Config("name", "subj-16ab49096a46dad6e71262103da54c526a0026f9", "Поэзия"),
            Config("text", "Кому", "pushkin@yandex.ru"),
            Config("text", "Скрытая", "voronzhov@yandex.ru"),
            Config("text", "Копия", "jukovsky@yandex.ru"),
            Config("text", "Тема", "Поэзия"))
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
            val curDistance = RectComparison.minDistance((unmarkedInput.attributes["rect"] as TRectangle),
                    (markedTextView.attributes["rect"] as TRectangle))
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
                    break
                }
            } else if (config.type.contains("id")) {
                if (nearestTextLabel != null && config.value == nearestTextLabel.attributes["id"]) {
                    fillValue = config.fillValue
                    break
                }
            }
        }
        unmarkedInput.attributes["text"] = fillValue
        return fillValue != ""
    }

    private fun findMarkedTextLabels(allTextLabels: Collection<UiElement>): List<UiElement> {
        val markedTextViews = ArrayList<UiElement>()
        for (config in configs) {
            when {
                config.type.contains("id") -> markedTextViews.addAll(allTextLabels.stream()
                        .filter { x -> x.attributes["id"] == config.value }
                        .collect(Collectors.toList()))
                config.type.contains("text") -> markedTextViews.addAll(allTextLabels.stream()
                        .filter { x -> x.attributes["text"] == config.value }
                        .collect(Collectors.toList()))
                config.type.contains("placeholder") -> {
                    markedTextViews.addAll(allTextLabels.stream()
                            .filter{x->x.attributes["placeholder"]==config.value}
                            .collect(Collectors.toList()))
                }
            }
        }
        return markedTextViews
    }

    private fun fillMarkedInput(input: UiElement): Boolean {
        for (config in configs) {
            if (config.type.contains("id") && input.attributes["id"] ==config.value) {
                input.attributes["text"] = config.fillValue
                return true
            } else if (config.type.contains("text")&& input.attributes["text"] == config.value) {
                input.attributes["text"] = config.fillValue
                return true
            } else if (config.type.contains("placeholder")&& input.attributes["placeholder"] == config.value) {
                input.attributes["text"] = config.fillValue
                return true
            }
        }
        return false
    }
}