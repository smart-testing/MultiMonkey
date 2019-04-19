package ru.yandex.testopithecus.mbt

import ru.yandex.testopithecus.mbt.components.Component
import ru.yandex.testopithecus.mbt.model.ApplicationModel


class StateMachine(private val model: ApplicationModel) {

    fun run(start: Component) {
        var currentComponent = start
        while (true) {
            currentComponent = step(currentComponent)
        }
    }

    private fun step(start: Component): Component {
        start.assertMatches(this.model)
        val possibleActions = start.actions().filter { it.canBePerformed(this.model) }
        val action = possibleActions.random() // TODO use walk strategy
        return action.perform(start.page, model)
    }

}