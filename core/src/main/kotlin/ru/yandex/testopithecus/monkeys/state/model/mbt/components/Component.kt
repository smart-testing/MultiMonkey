package ru.yandex.testopithecus.monkeys.state.model.mbt.components


interface Component {

    fun actions(): List<Action>

//    fun assertMatches(actual: Page, model: ApplicationModel)

}