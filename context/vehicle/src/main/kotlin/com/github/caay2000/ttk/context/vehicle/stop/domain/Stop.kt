package com.github.caay2000.ttk.context.vehicle.stop.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.cargo.domain.Cargo
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate

data class Stop(val worldId: WorldId, val id: StopId, val name: String, var cargo: List<Cargo>, val connections: Set<Connection>) : Aggregate() {

    companion object {
        fun create(worldId: WorldId, id: StopId, name: String): Stop = Stop(worldId, id, name, emptyList(), emptySet())
    }

    fun distanceTo(targetStopId: StopId) = this.connections.first { it.targetStopId == targetStopId }.distance

    fun createConnection(targetStopId: StopId, distance: Distance, allowedVehicleTypes: Set<VehicleType>): Stop =
        this.copy(connections = this.connections + Connection(this.id, targetStopId, distance, allowedVehicleTypes))

    fun produceCargo(cargo: Cargo): Stop = this.copy(cargo = this.cargo + cargo)
    fun unloadCargo(cargo: Cargo): Stop =
        if (cargo.targetId == this.id) this
        else this.copy(cargo = this.cargo + cargo)

    fun consumeCargo(cargoId: CargoId): Stop = this.copy(cargo = this.cargo.filter { it.id != cargoId })
}
