package ru.yandex.testopithecus.ui


data class UiAction(
        val id: String?,
        val action: String,
        val attributes: Map<String, String>
)

fun errorAction(message: String) = UiAction(null, "ERROR", mapOf("message" to message))

fun restartAction() = UiAction(null, "RESTART", mapOf())

fun tapAction(element: UiElement) = UiAction(element.id, "TAP", mapOf())

fun fillAction(element: UiElement, suggestedText : String) = UiAction(element.id, "INPUT", mapOf("text" to suggestedText))

fun skipAction() = UiAction(null, "SKIP", mapOf())

fun finishAction() = UiAction(null, "FINISH", mapOf())

fun screenshotAction() = UiAction(null, "SCREENSHOT", mapOf())
fun fakeScreenshotAction() = UiAction(null, "FAKE_SCREENSHOT", mapOf())

fun testOkAction() = UiAction(null, "TEST_OK", mapOf())

fun testFailAction(message: String) = UiAction(null, "TEST_FAIL", mapOf("message" to message))

fun testFailAction(message: String, expected: String, actual: String)
        = UiAction(null, "TEST_FAIL", mapOf("message" to message, "expected" to expected, "actual" to actual))