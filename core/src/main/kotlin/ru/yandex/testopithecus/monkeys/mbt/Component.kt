package ru.yandex.testopithecus.monkeys.mbt

import ru.yandex.testopithecus.monkeys.mbt.ModelAction


interface Component {

    fun actions(): List<ModelAction>

//    fun assertMatches(actual: Page, model: ApplicationModel)

}