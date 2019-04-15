package ru.yandex.testopithecus.monkeys.state.model.graph

class Graph {

    private val vertices: MutableSet<Vertex> = mutableSetOf()

    private val incomingEdges: MutableMap<Vertex, MutableMap<Edge, Vertex>> = mutableMapOf()
    private val outgoingEdges: MutableMap<Vertex, MutableMap<Edge, Vertex?>> = mutableMapOf()

    fun addVertex(v: Vertex) {
        println("Adding new vertex $v")
        vertices.add(v)
        incomingEdges[v] = mutableMapOf()
        outgoingEdges[v] = mutableMapOf()
    }

    fun addEdge(edge: Edge, from: Vertex, to: Vertex?) {
        println("Adding edge $edge from $from to $to")
        outgoingEdges[from]?.put(edge, to)
        if (to !== null) {
            incomingEdges[to]?.put(edge, from)
        }
    }

    fun changeToVertex(edge: Edge, from: Vertex, newTo: Vertex?) {
        val to = from.getOutgoingEdges()[edge]
        println("Changing end for $edge. Was $to; now $newTo")
        if (to != null) {
            incomingEdges[to]?.remove(edge)
        }
        addEdge(edge, from, newTo)
    }

    fun getIncomingEdges(v: Vertex): MutableMap<Edge, Vertex> {
        return incomingEdges.getOrElse(v) { throw NoSuchElementException() }
    }

    fun getOutgoingEdges(v: Vertex): MutableMap<Edge, Vertex?> {
        return outgoingEdges.getOrElse(v) { throw NoSuchElementException() }
    }

}