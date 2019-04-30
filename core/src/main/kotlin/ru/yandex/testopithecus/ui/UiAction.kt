package ru.yandex.testopithecus.ui


data class UiAction(
        val id: String?,
        val action: String,
        val attributes: Map<String, String>
)

fun errorAction(message: String) = UiAction(null, "ERROR", mapOf("message" to message))

fun restartAction() = UiAction(null, "RESTART", mapOf())

fun tapAction(element: UiElement) = UiAction(element.id, "TAP", mapOf())

fun fillAction(element: UiElement) = UiAction(element.id, "FILL", mapOf())

fun skipAction() = UiAction(null, "SKIP", mapOf())

fun finishAction() = UiAction(null, "FINISH", mapOf())

fun screenshotAction(fake: Boolean) = UiAction(null, "SCREENSHOT", mapOf("fake" to fake.toString()))