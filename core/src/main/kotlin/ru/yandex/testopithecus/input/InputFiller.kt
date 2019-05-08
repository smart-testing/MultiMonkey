package ru.yandex.testopithecus.input

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
        val nearestTextLabel = markedTextViews.stream().min { _, e2 ->
            //            val r2 = e2.attributes["rect"] as TRectangle
            val r1 = unmarkedInput.attributes["rect"] as TRectangle
            val r2 = e2.attributes["rect"] as TRectangle
            var min = Integer.MAX_VALUE
            if (Math.abs(r1.top - r2.bottom) < min) {
                min = Math.abs(r1.top - r2.bottom)
            }
            if (Math.abs(r1.bottom - r2.top) < min)
                min = Math.abs(r1.bottom - r2.top)
            if (Math.abs(r1.left - r2.right) < min)
                min = Math.abs(r1.left - r2.right)
            if (Math.abs(r1.right - r2.left) < min)
                min = Math.abs(r1.right - r2.left)
            min
        }.orElse(null)
        var fillValue = ""
        if (nearestTextLabel != null) {
            for (attr in nearestTextLabel.attributes.keys) {
                if (configs.containsKey(attr) && configs.getValue(attr).containsKey(nearestTextLabel.attributes[attr])) {
                    fillValue = configs[attr]?.get(nearestTextLabel.attributes[attr])!!
                }
            }
        }
        return fillValue
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
        for (attr in input.attributes.keys) {
            if (configs.containsKey(attr) && configs.getValue(attr).containsKey(input.attributes[attr])) {
                return configs[attr]?.get(input.attributes[attr])!!
            }
        }
        return ""
    }
}