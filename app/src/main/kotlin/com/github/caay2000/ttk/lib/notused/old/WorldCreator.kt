package com.github.caay2000.ttk.lib.notused.old // package com.github.caay2000.ttk.context.world
//
// import com.github.caay2000.ttk.context.core.domain.DomainId
// import com.github.caay2000.ttk.context.core.domain.WorldId
// import com.github.caay2000.ttk.context.core.event.EventPublisher
// import com.github.caay2000.ttk.context.core.eventbus.event.WorldCreatedEvent
// import org.jgrapht.graph.SimpleWeightedGraph
//
// class WorldCreator(private val eventPublisher: EventPublisher) {
//
//    private val factory = Stop(name = "Factory")
//    private val port = Stop(name = "Port")
//    private val warehouseA = Stop(name = "Warehouse A")
//    private val warehouseB = Stop(name = "Warehouse B")
//
//
//    fun create(request: CreateWorldRequest) {
//
//        World(
//            worldId = WorldId(),
//            stops = listOf(factory, port, warehouseA, warehouseB)
//        )
//
//        val map = SimpleWeightedGraph.createBuilder<Stop, Connection>(Connection::class.java)
//            .addVertex(factory)
//            .addVertex(port)
//            .addVertex(warehouseA)
//            .addVertex(warehouseB)
//            .addEdge(factory, warehouseB, Connection(factory, warehouseB, 5, Connection.ConnectionType.ROAD), 5.0)
//            .addEdge(factory, port, Connection(factory, port, 1, Connection.ConnectionType.ROAD), 1.0)
//            .addEdge(port, warehouseA, Connection(port, warehouseA, 4, Connection.ConnectionType.SEA), 4.0)
//            .buildAsUnmodifiable()
//
//
//        map.vertexSet().toList()
//
//
//
//        eventPublisher.publish(WorldCreatedEvent(WorldId()))
//    }
// }
//
//
// data class CreateWorldRequest(
//    val worldId: DomainId<WorldId> = WorldId()
// ) {
//
//
// }
