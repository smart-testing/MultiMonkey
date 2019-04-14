package ru.yandex.testopithecus.ui

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class UiElementTest : StringSpec({
    "should ignore id for equality" {
        val el1 = UiElement("1", mapOf("position" to mapOf("x" to 0, "y" to 0)), listOf("TAP"))
        val el2 = UiElement("2", mapOf("position" to mapOf("x" to 0, "y" to 0)), listOf("TAP"))
        el1 shouldBe el2
    }
})
