package ru.yandex.multimonkey.`state-monkey`.`state-model`

import khttp.delete
import java.lang.Exception

class State {

    private val toActions: MutableList<Action> = mutableListOf()
    private val fromActions: MutableList<Action> = mutableListOf()

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

    fun update() {}

//    private val actions: MutableList<Action> = actions.stream()
//        .map { action -> Action(action) }
//        .peek { action -> action.from = this }
//        .collect(Collectors.toList())

//    var metric = Int.MAX_VALUE
//
//    fun link(action: Action, to: State) {
//        if (!actions.contains(action) || action.from != this) {
//            throw Exception("No such action for state")
//        }
//        val toActionSnapshot = action.to
//        if (toActionSnapshot != null) {
//            if (toActionSnapshot != to) {
//                action.to = null
//                update()
//            }
//        } else {
//            action.to = to
//            to.fromActions.add(action)
//            update()
//        }
//    }
//
//    fun generateAction(): Action {
//        return actions.stream()
//            .min { a1, a2 -> a1.compareTo(a2) }
//            .orElseThrow { Exception("Action not found") }
//    }
//
//    private fun update() {
//        val prevMetric = metric
//        val newMetric = evaluateInterest()
//        if (newMetric != prevMetric) {
//            metric = newMetric
//            for (action in fromActions) {
//                action.from?.update()
//            }
//        }
//    }
//
//    private fun evaluateInterest(): Int {
//        var newMetric = Int.MAX_VALUE
//        actions.stream()
//            .forEach { action ->
//                run {
//                    val toActionSnapshot = action.to
//                    if (toActionSnapshot == null) {
//                        newMetric = 1
//                    } else {
//                        newMetric = Math.min(newMetric, toActionSnapshot.metric + 1)
//                    }
//                }
//            }
//        return newMetric
//    }

}