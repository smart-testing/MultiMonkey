package ru.yandex.multimonkey.monkeys.state.model

import ru.yandex.multimonkey.ui.UiAction
import ru.yandex.multimonkey.monkeys.state.identifier.StateId
import java.lang.IllegalArgumentException


class StateModel {

    private val states: MutableMap<StateId, State> = mutableMapOf()

    private var previousAction: Action? = null

    fun hasState(id: StateId): Boolean {
        return states.containsKey(id)
    }

    fun registerState(id: StateId, uiActions: List<UiAction>) {
        if (states.containsKey(id)) {
            throw Exception("Duplicate state found")
        }
        val state = State()
        uiActions.forEach { state.addFromAction(Action(state, null, it)) }
        ModelConfig.METRIC.updateMetric(state)
        states[id] = state
    }

    fun generateAction(id: StateId): UiAction {
        val state = states.getOrElse(id) { throw IllegalArgumentException() }

        // temporary fix until feedback impl
        val previousActionSnapshot = previousAction
        if (previousActionSnapshot != null) {
            when {
                previousActionSnapshot.to == null -> linkStates(previousActionSnapshot.from, state, previousActionSnapshot)
                previousActionSnapshot.to != state -> unlink(previousActionSnapshot)
                else -> ModelConfig.METRIC.updateMetric(previousActionSnapshot.to)
            }
        }

        val action = ModelConfig.STRATEGY.generateAction(state)
        if (action == null) {
            throw Exception("No actions found")
        } else {
            previousAction = action
            return action.uiAction
        }
    }

    private fun unlink(action: Action) {
        action.to?.removeToAction(action)
        ModelConfig.METRIC.updateMetric(action.to)
        action.to = null
        ModelConfig.METRIC.updateMetric(action.from)
    }

    private fun linkStates(from: State, to: State, action: Action) {
        if (action.from != from) {
            throw Exception("Action has different from state")
        }
        if (action.to != null) {
            throw Exception("Action already have linked to sate")
        }
        action.to = to
        to.addToAction(action)
        ModelConfig.METRIC.updateMetric(action.from)
        ModelConfig.METRIC.updateMetric(action.to)
    }

}