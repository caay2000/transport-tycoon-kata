package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.event.CargoProducedEvent
import com.github.caay2000.ttk.context.shared.event.ConnectionCreatedEvent
import com.github.caay2000.ttk.context.shared.event.StopCreatedEvent
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate

data class Stop internal constructor(val worldId: WorldId, val id: StopId, val name: String, val cargo: List<Cargo>, val connections: Set<Connection>) : Aggregate() {

    companion object {
        fun create(worldId: WorldId, stopId: StopId, name: String): Stop {
            val stop = Stop(worldId, stopId, name, emptyList(), emptySet())
            stop.pushEvent(StopCreatedEvent(worldId = worldId.uuid, stopId = stopId.uuid, stopName = name))
            return stop
        }
    }

    fun createConnection(targetStopId: StopId, distance: Distance, allowedVehicleTypes: Set<VehicleType>): Stop {
        val stop = this.copy(connections = connections + Connection(this.id, targetStopId, distance, allowedVehicleTypes))
        stop.pushEvent(ConnectionCreatedEvent(this.worldId.uuid, this.id.uuid, targetStopId.uuid, distance, allowedVehicleTypes.map { it.name }.toSet()))
        return stop
    }

    fun produceCargo(cargo: Cargo): Stop {
        val stop = this.copy(cargo = this.cargo + cargo)
        stop.pushEvent(CargoProducedEvent(this.worldId.uuid, cargo.id.uuid, cargo.sourceId.uuid, cargo.targetId.uuid))
        return stop
    }

    fun unloadCargo(cargo: Cargo): Stop =
        if (cargo.targetId == this.id) this
        else this.copy(cargo = this.cargo + cargo)

    fun consumeCargo(cargoId: CargoId): Stop = this.copy(cargo = this.cargo.filter { it.id != cargoId })

//    companion object {
//        fun get(location: Location) = stops[location]!!
//        fun all() = stops.values.toList()
//        fun cleanAll() = stops.forEach {
//            it.value._cargo.clear()
//        }
//
//        private val stops = mapOf(
//            Location.FACTORY to Stop(randomDomainId(), Location.FACTORY),
//            Location.PORT to Stop(randomDomainId(), Location.PORT),
//            Location.WAREHOUSE_A to Stop(randomDomainId(), Location.WAREHOUSE_A),
//            Location.WAREHOUSE_B to Stop(randomDomainId(), Location.WAREHOUSE_B)
//        )
//    }

//    private val _cargo = mutableListOf<Cargo>()
//    val cargo: List<Cargo>
//        get() = _cargo.toList()
//
//    fun hasCargo(): Boolean = _cargo.isEmpty().not()
//
//    fun addCargo(cargo: Cargo): Stop {
//        _cargo.add(cargo)
//        return this
//    }
//
//    fun removeCargo(cargoId: CargoId): Boolean = _cargo.removeIf { it.id == cargoId.uuid }
//
//    fun distanceTo(stop: Stop) = this.location.distanceTo(stop.location)
}
