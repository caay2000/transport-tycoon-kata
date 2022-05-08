package com.github.caay2000.ttk.context.vehicle.world.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.cargo.domain.Cargo
import com.github.caay2000.ttk.context.vehicle.stop.domain.Stop

data class World(val id: WorldId, val stops: Set<Stop>) {

    companion object {
        fun create(worldId: WorldId): World = World(worldId, emptySet())
    }

    fun addStop(stop: Stop): World = this.copy(stops = stops + stop)

    fun getStop(id: StopId) = stops.first { it.id == id }

    fun consumeCargo(stopId: StopId, cargoId: CargoId): World =
        this.getStop(stopId).consumeCargo(cargoId)
            .let { stop -> this.copy(stops = this.stops.filter { it.id != stop.id }.toSet() + stop) }

    fun produceCargo(stopId: StopId, cargo: Cargo): World =
        this.getStop(stopId).produceCargo(cargo)
            .let { stop -> this.copy(stops = this.stops.filter { it.id != stop.id }.toSet() + stop) }

    fun reserveCargo(stopId: StopId, cargoId: CargoId): World =
        this.getStop(stopId).reserveCargo(cargoId)
            .let { stop -> this.copy(stops = this.stops.filter { it.id != stop.id }.toSet() + stop) }

    fun unloadCargo(stopId: StopId, cargo: Cargo): World =
        this.getStop(stopId).unloadCargo(cargo)
            .let { stop -> this.copy(stops = this.stops.filter { it.id != stop.id }.toSet() + stop) }

    fun createConnection(stopId: StopId, targetStopId: StopId, distance: Distance, allowedVehicleTypes: Set<VehicleType>): World =
        this.getStop(stopId).createConnection(targetStopId, distance, allowedVehicleTypes)
            .let { stop -> this.copy(stops = this.stops.filter { it.id != stop.id }.toSet() + stop) }
}
