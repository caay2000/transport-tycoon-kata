// package com.github.caay2000.ttk.context.core.map
//
// import com.github.caay2000.ttk.context.world.domain.Location
// import com.github.caay2000.ttk.context.world.domain.Stop
// import com.github.caay2000.ttk.context.world.domain.Stop
// import org.jgrapht.alg.shortestpath.DijkstraShortestPath
// import org.jgrapht.graph.SimpleWeightedGraph
//
// class HardcodedPathAI : PathAI {
//
//    private val factory = Stop(name = "Factory")
//    private val port = Stop(name = "Port")
//    private val warehouseA = Stop(name = "Warehouse A")
//    private val warehouseB = Stop(name = "Warehouse B")
//
//    private val map = SimpleWeightedGraph.createBuilder<Stop, HardcodedPathAI.PathRoute>(HardcodedPathAI.PathRoute::class.java)
//        .addVertex(factory)
//        .addVertex(port)
//        .addVertex(warehouseA)
//        .addVertex(warehouseB)
//        .addEdge(factory, warehouseB, 5.0)
//        .addEdge(factory, port, 1.0)
//        .addEdge(port, warehouseA, 4.0)
//        .buildAsUnmodifiable()
//
//    override fun getRoute(start: Stop, end: Stop): Set<Stop> =
//        DijkstraShortestPath.findPathBetween(map, start, end).vertexList.toSet()
//
//    override fun getNextStop(start: Stop, end: Stop): Stop =
//        getRoute(start, end).elementAt(1)
//
//    class PathRoute {
//        var stops: Set<Stop> = emptySet()
//    }
// }
