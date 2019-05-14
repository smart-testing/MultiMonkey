package ru.yandex.testopithecus.monkeys.state.model

import org.jgrapht.Graph
import org.jgrapht.graph.DirectedPseudograph
import org.json.JSONArray
import org.json.JSONObject
import ru.yandex.testopithecus.monkeys.mbt.MbtElement
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.application.ApplicationModel
import ru.yandex.testopithecus.monkeys.mbt.ModelAction
import ru.yandex.testopithecus.monkeys.mbt.minimalTodoExample.components.mainPage.MainPageComponent
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.monkeys.state.identifier.StateId
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.DistanceToUnknownState
import ru.yandex.testopithecus.monkeys.state.model.strategies.metric.Metric
import ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy.MBTPriorityWalkStrategy
import ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy.WalkStrategy
import ru.yandex.testopithecus.ui.UiState


class StateModel {

    private val states: MutableMap<StateId, State> = mutableMapOf()
    private val uiActions: MutableMap<Action, UiAction> = mutableMapOf()
    private val modelActions: MutableMap<Action, ModelAction> = mutableMapOf()

    private val graph: Graph<State, Action> = DirectedPseudograph(null, { Action(false) }, false)

    private val strategy: WalkStrategy = MBTPriorityWalkStrategy()
    private val metric: Metric = DistanceToUnknownState()

    private var previousAction: Action? = null

    init {
        graph.addVertex(State.NULL_STATE)
    }

    fun hasState(id: StateId): Boolean {
        return states.contains(id)
    }

    fun registerState(id: StateId, uiState: UiState, stateUiActions: List<UiAction>) {
        if (states.contains(id)) {
            throw Exception("Duplicate state found")
        }
        println("Creating new state with id $id")
        val state = State(id, uiState)
        states[id] = state
        graph.addVertex(state)

        stateUiActions.forEach {
            val action = Action(false)
            graph.addEdge(state, State.NULL_STATE, action)
            uiActions[action] = it
        }

        // temporary link native link
        if (states.size == 1) {
            state.setMbt(MbtElement(ApplicationModel(), MainPageComponent()))
            addMbtActions(state)
        }

        metric.updateMetric(graph, state)
    }

    fun generateAction(id: StateId): UiAction {
        val state = states.getOrElse(id) { throw NoSuchElementException() }
        val action = strategy.getAction(graph, state) ?: throw NoSuchElementException()
        previousAction = action
        return getUiAction(action, state.uiState)
    }

    fun feedback(id: StateId) {
        val state = states.getOrElse(id) { throw NoSuchElementException() }
        processFeedback(state)
        dumpGraph()
        metric.updateMetric(graph, state)
    }

    private fun processFeedback(state: State) {
        val previousEdgeSnapshot = previousAction
        if (previousEdgeSnapshot != null) {
            val previousSource = graph.getEdgeSource(previousEdgeSnapshot)
            val previousTarget = graph.getEdgeTarget(previousEdgeSnapshot)
            when {
                previousTarget == State.NULL_STATE -> graph.changeEdge(previousEdgeSnapshot, previousSource, state)
                previousTarget != state -> graph.changeEdge(previousEdgeSnapshot, previousSource, State.NULL_STATE)
                else -> metric.updateMetric(graph, state)
            }
            if (isModelAction(previousEdgeSnapshot) && previousTarget == State.NULL_STATE && !state.hasMbt()) {
                // update model
                val modelAction = modelActions.getOrElse(previousEdgeSnapshot) { throw Exception() }
                state.setMbt(modelAction.perform(previousSource.getMbt().model))

                addMbtActions(state)
                metric.updateMetric(graph, state)
            }
        }
    }

    private fun getUiAction(action: Action, state: UiState): UiAction {
        if (uiActions.contains(action)) {
            return uiActions.getOrElse(action) { throw Exception() }
        }
        val modelAction = modelActions.getOrElse(action) { throw NoSuchElementException() }
        return modelAction.getAction(state)
    }

    private fun isModelAction(action: Action): Boolean {
        return modelActions.contains(action)
    }

    private fun addMbtActions(state: State) {
        if (state.hasMbt()) {
            val mbt = state.getMbt()
            for (modelAction in mbt.component.actions().filter { it.canBePerformed(mbt.model) }) {
                val action = Action(true)
                graph.addEdge(state, State.NULL_STATE, action)
                modelActions[action] = modelAction
            }
        }
    }

    private fun dumpGraph(): JSONObject {
        val json = JSONObject()
        val vertices = JSONArray()
        for (state in graph.vertexSet()) {
            val vertex = JSONObject()
            vertex.put("index", state.index)
            vertex.put("isModel", state.hasMbt())
            vertices.put(vertex)
        }
        val edges = JSONArray()
        for (action in graph.edgeSet()) {
            val edge = JSONObject()
            edge.put("source", graph.getEdgeSource(action).index)
            edge.put("target", graph.getEdgeTarget(action).index)
            edge.put("isModel", isModelAction(action))
            edge.put("label", getLabelForAction(action))
            edges.put(edge)
        }
        json.put("vertices", vertices)
        json.put("edges", edges)
        println(json)
        return json
    }

    fun getLabelForAction(action: Action): String {
        if (uiActions.contains(action)) {
            val uiAction = uiActions.getOrElse(action) { throw Exception() }
            return uiAction.action
        }
        return ""
    }

}

fun <V, E> Graph<V, E>.changeEdge(edge: E, source: V, target: V) {
    removeEdge(edge)
    addEdge(source, target, edge)
}