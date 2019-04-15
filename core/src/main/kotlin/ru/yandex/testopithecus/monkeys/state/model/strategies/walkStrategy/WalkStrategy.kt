package ru.yandex.testopithecus.monkeys.state.model.strategies.walkStrategy

import ru.yandex.testopithecus.monkeys.state.model.graph.Edge
import ru.yandex.testopithecus.monkeys.state.model.graph.Vertex


interface WalkStrategy {

    fun getEdge(vertex: Vertex): Pair<Edge, Vertex?>

}