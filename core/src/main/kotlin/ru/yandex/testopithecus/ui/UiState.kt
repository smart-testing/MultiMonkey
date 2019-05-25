package ru.yandex.testopithecus.ui

data class UiState(
        val elements: List<UiElement>,
        val global: MutableMap<String, Any>
)