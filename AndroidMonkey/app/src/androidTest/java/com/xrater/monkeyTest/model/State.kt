package com.xrater.monkeyTest.model

import com.xrater.monkeyTest.net.NetAction
import java.lang.Exception
import java.util.stream.Collectors

class State(actions: List<NetAction>) {

    private val toActions: MutableList<Action> = mutableListOf()

    private val actions: MutableList<Action> = actions.stream()
        .map { action -> Action(action) }
        .peek { action -> action.from = this }
        .collect(Collectors.toList())

    var metric = Int.MAX_VALUE

    fun link(action: Action, to: State) {
        if (!actions.contains(action) || action.from != this) {
            throw Exception("No such action for state")
        }
        val toActionSnapshot = action.to
        if (toActionSnapshot != null) {
            if (toActionSnapshot != to) {
                action.to = null
                update()
            }
        } else {
            action.to = to
            to.toActions.add(action)
            update()
        }
    }

    fun generateAction(): Action {
        return actions.stream()
            .min { a1, a2 -> a1.compareTo(a2) }
            .orElseThrow { Exception("Action not found") }
    }

    private fun update() {
        val prevMetric = metric
        val newMetric = evaluateInterest()
        if (newMetric != prevMetric) {
            metric = newMetric
            for (action in toActions) {
                action.from?.update()
            }
        }
    }

    private fun evaluateInterest(): Int {
        var newMetric = Int.MAX_VALUE
        actions.stream()
            .forEach { action ->
                run {
                    val toActionSnapshot = action.to
                    if (toActionSnapshot == null) {
                        newMetric = 1
                    } else {
                        newMetric = Math.min(newMetric, toActionSnapshot.metric + 1)
                    }
                }
            }
        return newMetric
    }

}