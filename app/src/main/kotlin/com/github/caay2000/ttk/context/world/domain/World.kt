package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.core.domain.CargoId
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.lib.event.StopCreatedEvent
import com.github.caay2000.ttk.lib.event.VehiclePendingUpdateEvent
import com.github.caay2000.ttk.lib.event.WorldCreatedEvent
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate

data class World(val id: WorldId, val stops: Set<Stop>) : Aggregate() {

    companion object {
        fun create(id: WorldId, stops: Set<Stop>): World {
            val world = World(id, stops)
            world.pushEvent(WorldCreatedEvent(world.id.uuid))
            world.stops.forEach { stop ->
                world.pushEvent(StopCreatedEvent(world.id.uuid, stop.id.uuid, stop.location.name))
            }
            return world
        }
    }

    val vehicles: MutableList<Vehicle> = mutableListOf()

    fun addCargo(cargo: Cargo) {
        val stop = stops.find { it.location == Location.FACTORY }!!
        stop.addCargo(cargo)
    }

    fun addVehicle(vehicle: Vehicle): World {
        this.vehicles.add(vehicle)
        return this
    }

    fun getStop(location: Location) = this.stops.first { it.location == location }

    fun updateVehicle(vehicleId: VehicleId, cargoId: CargoId?, status: String) {
        val vehicle = this.vehicles.first { it.id == vehicleId }
        this.vehicles.remove(vehicle)
        val updatedVehicle = vehicle.copy(
            cargoId = cargoId, status = status, updated = vehicle.updated + 1
        )
        this.vehicles.add(updatedVehicle)
    }

    fun removeCargo(cargoId: CargoId) {
        this.stops.forEach { stop ->
            stop.removeCargo(cargoId)
        }
    }

    fun isCompleted(): Boolean = stops.all { it.cargo.isEmpty() } && vehicles.all { it.isEmpty() }

    fun update() {

        this.vehicles.forEach { vehicle ->
            this.pushEvent(VehiclePendingUpdateEvent(vehicle.id.uuid))
        }
    }
}
