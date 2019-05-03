package ru.yandex.testopithecus.monkeys.mbt.model.components


interface Component {

    fun actions(): List<ModelAction>

//    fun assertMatches(actual: Page, model: ApplicationModel)

}