package com.xrater.monkeyTest.model

import com.xrater.monkeyTest.net.NetAction
import com.xrater.monkeyTest.net.NetState


class Model {

    private val states: MutableMap<NetState, State> = mutableMapOf()
    private var lastAction: Action? = null
    private var lastState: State? = null

    fun getState(state: NetState) : State? {
        return states[state]
    }

    fun generateAction(state: State) : Action {
        // process model due last step
        val lastActionSnapshot = lastAction
        val lastStateSnapshot = lastState
        if (lastActionSnapshot != null && lastStateSnapshot != null) {
            linkStates(lastStateSnapshot, state, lastActionSnapshot)
        }

        val action = state.generateAction()
        lastAction = action
        lastState = state
        return action
    }

    fun addState(state: NetState, actions: List<NetAction>) {
        if (states.containsKey(state)) {
            throw Exception("Duplicate state found")
        }
        states[state] = State(actions)
    }

    private fun linkStates(fromState: State, toState: State, action: Action) {
        fromState.link(action, toState)
    }

}