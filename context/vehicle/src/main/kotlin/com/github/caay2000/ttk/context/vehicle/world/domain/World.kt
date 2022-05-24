package com.github.caay2000.ttk.context.vehicle.world.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId

data class World(val id: WorldId, val stops: Set<Stop>, val map: Map) {

    companion object {
        fun create(worldId: WorldId): World = World(worldId, emptySet(), Map())
    }

    fun createStop(stop: Stop): World =
        map.createStop(stop.id)
            .let { this.copy(stops = this.stops + stop) }

    fun createConnection(connection: Connection): World =
        map.createConnection(connection).let { this }

    fun distance(sourceStop: StopId, targetStop: StopId): Distance =
        map.distance(sourceStop, targetStop)

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
}
