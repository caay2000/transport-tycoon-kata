package com.github.caay2000.ttk.context.vehicle.world.domain

import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.SimpleWeightedGraph

class Map {

    private val map: SimpleWeightedGraph<StopId, Connection> = SimpleWeightedGraph.createBuilder<StopId, Connection>(Connection::class.java).build()

    fun createStop(stopId: StopId): Map {
        map.addVertex(stopId)
        return this
    }

    fun createConnection(connection: Connection): Map {
        map.addEdge(connection.sourceStopId, connection.targetStopId, connection)
        map.addEdge(connection.targetStopId, connection.sourceStopId, connection.reverse())
        return this
    }

    fun route(sourceStop: StopId, targetStop: StopId): Set<Connection> =
        DijkstraShortestPath.findPathBetween(map, sourceStop, targetStop).edgeList.toSet()

    fun distance(sourceStop: StopId, targetStop: StopId): Distance =
        route(sourceStop, targetStop).fold(0) { distance, connection -> distance + connection.distance }
}
