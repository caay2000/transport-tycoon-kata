package com.github.caay2000.ttk.context.world.stop.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.event.CargoProducedEvent
import com.github.caay2000.ttk.context.shared.event.ConnectionCreatedEvent
import com.github.caay2000.ttk.context.shared.event.StopCreatedEvent
import com.github.caay2000.ttk.context.world.cargo.domain.Cargo
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
}
