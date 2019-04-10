package ru.yandex.multimonkey.monkeys.state.model

import java.lang.Exception

class State {

    private val toActions: MutableList<Action> = mutableListOf()
    private val fromActions: MutableList<Action> = mutableListOf()

    var metric = ModelConfig.METRIC.getInitialMetric()

    fun getToActions() : List<Action> {
        return toActions
    }

    fun getFromActions() : List<Action> {
        return fromActions
    }

    fun addToAction(action: Action) {
        if (action.to != this) {
            throw Exception("Attempt to add action with another to state")
        }
        toActions.add(action)
    }

    fun addFromAction(action: Action) {
        if (action.from != this) {
            throw Exception("Attempt to add action with another from state")
        }
        fromActions.add(action)
    }

    fun removeToAction(action: Action) {
        if (action.to != this) {
            throw Exception("Attempt to remove action with another to state")
        }
        toActions.remove(action)
    }

}