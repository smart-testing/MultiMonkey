package ru.yandex.testopithecus.monkeys.state.model

import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.monkeys.state.identifier.StateId
import ru.yandex.testopithecus.monkeys.state.model.graph.Edge
import ru.yandex.testopithecus.monkeys.state.model.graph.Graph
import ru.yandex.testopithecus.monkeys.state.model.graph.Turn
import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.DistanceToUnknownState
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.Metric
import ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy.RandomStrategy
import ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy.WalkStrategy


class StateModel {

    private val ids: MutableMap<StateId, Vertex> = mutableMapOf()
    private val states: MutableMap<Vertex, State> = mutableMapOf()
    private val actions: MutableMap<Edge, UiAction> = mutableMapOf()

    private val graph = Graph()

    private val strategy: WalkStrategy = RandomStrategy()
    private val metric: Metric = DistanceToUnknownState()

    private var previousTurn: Turn? = null

    fun hasState(id: StateId): Boolean {
        return ids.contains(id)
    }

    fun registerState(id: StateId, uiActions: List<UiAction>) {
        if (ids.contains(id)) {
            throw Exception("Duplicate state found")
        }
        println("Creating new state with id $id")
        val state = State(id)
        val vertex = Vertex(graph)
        ids[id] = vertex

        states[vertex] = state
        graph.addVertex(vertex)
        uiActions.forEach {
            val edge = Edge(graph)
            actions[edge] = it
            graph.addEdge(edge, vertex, null)
        }
        metric.updateMetric(vertex)
    }

    fun generateAction(id: StateId): UiAction {
        val vertex = ids.getOrElse(id) { throw NoSuchElementException() }

        // temporary fix until feedback impl
        val previousTurnSnapshot = previousTurn
        if (previousTurnSnapshot != null) {
            val previousFrom = previousTurnSnapshot.from
            val previousEdge = previousTurnSnapshot.edge
            when {
                previousTurnSnapshot.to == null -> graph.changeToVertex(previousEdge, previousFrom, vertex)
                previousTurnSnapshot.to != vertex -> graph.changeToVertex(previousEdge, previousFrom, null)
                else -> metric.updateMetric(vertex)
            }
        }

        val (edge, to) = strategy.getEdge(vertex)
        previousTurn = Turn(edge, vertex, to)
        return actions.getOrElse(edge) { throw NoSuchElementException() }
    }

}