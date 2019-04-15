package ru.yandex.testopithecus.monkeys.state.model.graph

import java.lang.Exception


class Graph {

    private val vertices: MutableSet<Vertex> = mutableSetOf()

    private val ingoingEdges: MutableMap<Vertex, MutableMap<Edge, Vertex>> = mutableMapOf()
    private val outcomingEdges: MutableMap<Vertex, MutableMap<Edge, Vertex?>> = mutableMapOf()

    fun addVertex(v: Vertex) {
        vertices.add(v)
        ingoingEdges[v] = mutableMapOf()
        outcomingEdges[v] = mutableMapOf()
    }

    fun addEdge(from: Vertex, to: Vertex?, edge: Edge) {
        outcomingEdges[from]?.put(edge, to)
        if (to !== null) {
            ingoingEdges[to]?.put(edge, from)
        }
    }

    fun getIncomingEdges(v: Vertex): MutableMap<Edge, Vertex> {
        return ingoingEdges.getOrElse(v) { throw NoSuchElementException() }
    }

    fun getOutgoingEdges(v: Vertex): MutableMap<Edge, Vertex?> {
        return outcomingEdges.getOrElse(v) { throw NoSuchElementException() }
    }

}