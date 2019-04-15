package ru.yandex.testopithecus.monkeys.state.model

import org.jgrapht.Graph
import org.jgrapht.graph.DirectedMultigraph
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.monkeys.state.identifier.StateId
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.DistanceToUnknownState
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.Metric
import ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy.MinimizeMetricStrategy
import ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy.WalkStrategy


class StateModel {

    private val states: MutableMap<StateId, State> = mutableMapOf()
    private val actions: MutableMap<Action, UiAction> = mutableMapOf()

    private val graph: Graph<State, Action> = DirectedMultigraph(null, { Action() }, false)

    private val strategy: WalkStrategy = MinimizeMetricStrategy()
    private val metric: Metric = DistanceToUnknownState()

    private var previousAction: Action? = null

    init {
        graph.addVertex(State.NULL_STATE)
    }

    fun hasState(id: StateId): Boolean {
        return states.contains(id)
    }

    fun registerState(id: StateId, uiActions: List<UiAction>) {
        if (states.contains(id)) {
            throw Exception("Duplicate state found")
        }
        println("Creating new state with id $id")
        val state = State(id)
        states[id] = state
        graph.addVertex(state)

        uiActions.forEach {
            val action = Action()
            actions[action] = it
            graph.addEdge(state, State.NULL_STATE, action)
        }
        metric.updateMetric(graph, state)
    }

    fun generateAction(id: StateId): UiAction {
        val state = states.getOrElse(id) { throw NoSuchElementException() }

        // temporary fix until feedback impl
        val previousEdgeSnapshot = previousAction
        if (previousEdgeSnapshot != null) {
            val previousSource = graph.getEdgeSource(previousEdgeSnapshot)
            val previousTarget = graph.getEdgeTarget(previousEdgeSnapshot)
            when {
                previousTarget == State.NULL_STATE -> graph.changeEdge(previousEdgeSnapshot, previousTarget, previousSource)
                previousTarget != state -> graph.changeEdge(previousEdgeSnapshot, previousTarget, State.NULL_STATE)
                else -> metric.updateMetric(graph, state)
            }
        }

        val action = strategy.getAction(graph, state)
        previousAction = action
        return actions.getOrElse(action) { throw NoSuchElementException() }
    }

}

fun <V, E> Graph<V, E>.changeEdge(edge: E, source: V, target: V) {
    removeEdge(edge)
    addEdge(source, target, edge)
}