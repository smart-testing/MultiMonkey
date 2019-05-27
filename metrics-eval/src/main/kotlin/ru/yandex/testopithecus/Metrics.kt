package ru.yandex.testopithecus

data class Metrics(
        var crashes: Int = 0,
        var uniqueCrashes: Int = 0,
        var steps: Int = 0,
        var meanSteps: Int = 0,
        var time: Int = 0,
        var meanTime: Int = 0
)