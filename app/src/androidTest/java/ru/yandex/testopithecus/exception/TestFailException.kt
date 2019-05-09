package ru.yandex.testopithecus.exception

class TestFailException(attributes: Map<String, String>) :
        RuntimeException(attributes.getOrDefault("message", "")) {

    val expected = attributes.getOrDefault("expected", "")
    val actual = attributes.getOrDefault("actual", "")
}