package ru.yandex.testopithecus.monkeys.state.model

import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.monkeys.state.identifier.StateId
import ru.yandex.testopithecus.monkeys.state.model.graph.Edge
import ru.yandex.testopithecus.monkeys.state.model.graph.Graph
import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex
import ru.yandex.testopithecus.monkeys.state.model.strategies.RandomStrategy
import ru.yandex.testopithecus.monkeys.state.model.strategies.Strategy


class StateModel {

    private val ids: MutableMap<StateId, Vertex> = mutableMapOf()
    private val states: MutableMap<Vertex, State> = mutableMapOf()
    private val actions: MutableMap<Edge, UiAction> = mutableMapOf()

    private val graph = Graph()

    private val strategy: Strategy = RandomStrategy()

    private var previousEdge: Edge? = null

    fun hasState(id: StateId): Boolean {
        return ids.contains(id)
    }

    fun registerState(id: StateId, uiActions: List<UiAction>) {
        if (ids.contains(id)) {
            throw Exception("Duplicate state found")
        }
        val state = State(id)
        val vertex = Vertex(graph)
        ids[id] = vertex

        states[vertex] = state
        graph.addVertex(vertex)
        uiActions.forEach {
            val edge = Edge(graph)
            actions[edge] = it
            graph.addEdge(vertex, null, edge)
        }
    }

    fun generateAction(id: StateId): UiAction {
        val vertex = ids.getOrElse(id) { throw NoSuchElementException() }

        // temporary fix until feedback impl
//        val previousEdgeSnapshot = previousEdge
//        if (previousEdgeSnapshot != null) {
//            when {
//                previousEdgeSnapshot.to == null -> linkStates(previousEdgeSnapshot.from, state, previousEdgeSnapshot)
//                previousEdgeSnapshot.to != state -> unlink(previousEdgeSnapshot)
//                else -> ModelConfig.METRIC.updateMetric(previousEdgeSnapshot.to)
//            }
//        }

        val edge = strategy.getEdge(vertex)
        previousEdge = edge
        return actions.getOrElse(edge) { throw NoSuchElementException() }
    }

//    private fun unlink(action: Action) {
//        action.to?.removeToAction(action)
//        ModelConfig.METRIC.updateMetric(action.to)
//        action.to = null
//        ModelConfig.METRIC.updateMetric(action.from)
//    }
//
//    private fun linkStates(from: State, to: State, action: Action) {
//        if (action.from != from) {
//            throw Exception("Action has different from state")
//        }
//        if (action.to != null) {
//            throw Exception("Action already have linked to sate")
//        }
//        action.to = to
//        to.addToAction(action)
//        ModelConfig.METRIC.updateMetric(action.from)
//        ModelConfig.METRIC.updateMetric(action.to)
//    }

}