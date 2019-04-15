package ru.yandex.testopithecus.monkeys.state.model.graph


class Vertex(val graph: Graph) {

    var metric: Int? = null

    fun getIncomingEdges(): Map<Edge, Vertex> {
        return graph.getIncomingEdges(this)
    }

    fun getOutgoingEdges(): Map<Edge, Vertex?> {
        return graph.getOutgoingEdges(this)
    }

}