package ru.yandex.testopithecus.monkeys.state.model.graph


class Vertex(val graph: Graph) {

    fun getIncomingEdges(): MutableMap<Edge, Vertex> {
        return graph.getIncomingEdges(this)
    }

    fun getOutgoingEdges(): MutableMap<Edge, Vertex?> {
        return graph.getOutgoingEdges(this)
    }

}