package ru.yandex.testopithecus.ui

data class UiState(
        val elements: List<UiElement>,
        val global: Map<String, Any>
)