package ru.yandex.testopithecus.input

class Config {
    internal var type: String = ""
    internal var value: String = ""
    internal var fillValue: String = ""

    constructor(type: String, value: String, fillType: String) {
        this.type = type
        this.value = value
        this.fillValue = fillType
    }

    constructor() {}
}
