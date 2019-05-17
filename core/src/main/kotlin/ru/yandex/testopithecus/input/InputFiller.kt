package ru.yandex.testopithecus.input

import ru.yandex.testopithecus.rect.RectComparison
import ru.yandex.testopithecus.rect.TRectangle
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState
import java.util.stream.Collectors


object InputFiller : InputGenerator {
    private var configs: Map<String, Map<String, String>> = mapOf(
            "id" to mapOf("content9a9a1b96221c466eb24d2e48dadd4536" to "Hi there!"),
            "name" to mapOf("subj-16ab49096a46dad6e71262103da54c526a0026f9" to "Поэзия"),
            "class" to mapOf("cke_wysiwyg_div cke_reset cke_enable_context_menu cke_editable cke_editable_themed cke_contents_ltr cke_show_borders" to "Hi there"),
            "placeholder" to mapOf("Поиск" to "Анна Керн"),
            "text" to mapOf(
                    "To" to "pushkin@yandex.ru",
                    "Subject" to "Поэзия",
                    "Search emails" to "Анна Керн",
                    "Cc" to "jukovsky@yandex.ru",
                    "Bcc" to "voronzhov@yandex.ru",
                    "Кому" to "pushkin@yandex.ru",
                    "Скрытая" to "voronzhov@yandex.ru",
                    "Тема" to "Поэзия",
                    "Введите пароль" to "apktest",
                    "Введите логин, почту или телефон" to "apkTestAndroid",
                    "Phone number or login" to "apkTestAndroid",
                    "Password" to "apktest"))

    override fun suggestInput(input: UiElement, state: UiState): String {
        var res = suggestForMarkedInput(input)
        if (res == "") {
            val allTextLabels = state.elements
                    .parallelStream()
                    .filter { x -> x.attributes["isLabel"] == true }
                    .collect(Collectors.toList())
            res = suggestForUnmarkedInput(input, allTextLabels)
        }
        return res
    }

    private fun suggestForUnmarkedInput(unmarkedInput: UiElement, allTextLabels: Collection<UiElement>): String {
        val markedTextViews = findMarkedTextLabels(allTextLabels)
        val r = unmarkedInput.attributes["rect"] as TRectangle
        val nearestTextLabel = markedTextViews.stream().min { e1: UiElement, e2: UiElement ->
            val r1 = e1.attributes["rect"] as TRectangle
            val r2 = e2.attributes["rect"] as TRectangle
            val min1 = RectComparison.minDistance(r, r1)
            val min2 = RectComparison.minDistance(r, r2)
            when {
                min1 > min2 -> 1
                min1 < min2 -> -1
                else -> 0
            }
        }.orElse(null)
        return getFromConfig(nearestTextLabel)
    }

    private fun findMarkedTextLabels(allTextLabels: Collection<UiElement>): List<UiElement> {
        val markedTextViews = ArrayList<UiElement>()
        for (key in configs.keys) {
            markedTextViews.addAll(allTextLabels.stream()
                    .filter { x -> x.attributes[key] != null && configs.getValue(key).containsKey(x.attributes[key]) }
                    .collect(Collectors.toList()))
        }
        return markedTextViews
    }

    private fun suggestForMarkedInput(input: UiElement): String {
        return getFromConfig(input)
    }
    private fun getFromConfig(uiElement: UiElement) : String {
        for (attr in uiElement.attributes.keys) {
            if (configs.containsKey(attr) && configs.getValue(attr).containsKey(uiElement.attributes[attr])) {
                return configs[attr]?.get(uiElement.attributes[attr]) ?: "do not have a value for attribute: $attr"
            }
        }
        return ""
    }
}