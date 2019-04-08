package ru.yandex.multimonkey.`state-monkey`.`state-model`

import ru.yandex.multimonkey.net.UiAction
import ru.yandex.multimonkey.`state-monkey`.`state-identifier`.StateId
import ru.yandex.multimonkey.`state-monkey`.`state-model`.strategies.RandomStrategy
import ru.yandex.multimonkey.`state-monkey`.`state-model`.strategies.Strategy
import java.lang.IllegalArgumentException


class StateModel {

    private val strategy: Strategy = RandomStrategy()
    private val states: MutableMap<StateId, State> = mutableMapOf()

    private var previousAction: Action? = null

    fun hasState(id: StateId): Boolean {
        return states.containsKey(id)
    }

    fun registerState(id: StateId, uiActions: List<UiAction>) {
        if (states.containsKey(id)) {
            throw Exception("Duplicate state found")
        }
        val state = strategy.initNewState()
        uiActions.forEach { state.addFromAction(Action(state, null, it)) }
        state.update()
        states[id] = state
    }

    fun generateAction(id: StateId): UiAction {
        val state = states.getOrElse(id) { throw IllegalArgumentException() }

        // temporary fix until feedback impl
        val previousActionSnapshot = previousAction
        if (previousActionSnapshot != null) {
            if (previousActionSnapshot.to == null) {
                linkStates(previousActionSnapshot.from, state, previousActionSnapshot)
            } else if (previousActionSnapshot.to != state) {
                unlink(previousActionSnapshot)
            }
        }

        val action = strategy.generateAction(state)
        previousAction = action
        return action.uiAction
    }

    private fun unlink(action: Action) {
        action.to?.removeToAction(action)
        action.to?.update()
        action.to = null
        action.from.update()
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
        from.update()
        to.update()
    }

}