package ru.yandex.multimonkey.ui


data class UiAction(
        val id: String?,
        val action: String,
        val attributes: Map<String, String>
)