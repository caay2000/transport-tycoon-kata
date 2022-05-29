package com.github.caay2000.ttk.context.world.world.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.shared.event.VehicleCreatedEvent
import com.github.caay2000.ttk.context.shared.event.VehiclePendingUpdateEvent
import com.github.caay2000.ttk.context.shared.event.WorldCreatedEvent
import com.github.caay2000.ttk.context.world.stop.domain.Stop
import com.github.caay2000.ttk.context.world.vehicle.domain.Vehicle
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate
import java.util.UUID

data class World(val id: WorldId, val stops: Set<Stop>, val vehicles: Set<Vehicle>) : Aggregate() {

    companion object {
        fun create(id: WorldId): World {
            val world = World(id, emptySet(), emptySet())
            world.pushEvent(WorldCreatedEvent(world.id.uuid))
            return world
        }
    }

    fun createVehicle(stopId: StopId, vehicle: Vehicle): World {
        val world = this.copy(vehicles = this.vehicles + vehicle)
        world.pushEvent(VehicleCreatedEvent(this.id.uuid, stopId.uuid, vehicle.id.uuid, vehicle.type.name))
        return world
    }

    fun updateVehicle(vehicleId: VehicleId, cargoId: CargoId?, dateTimeHash: String): World {
        val vehicle = this.vehicles.first { it.id == vehicleId }
        val world = copy(vehicles = vehicles.filter { it.id != vehicleId }.toSet())
        val updatedVehicle = vehicle.copy(cargoId = cargoId, updated = UUID.fromString(dateTimeHash))
        return world.copy(vehicles = world.vehicles + updatedVehicle)
    }

    fun isCompleted(): Boolean = stops.all { it.cargo.isEmpty() } && vehicles.all { it.isEmpty() }

    fun update() {
        this.vehicles.forEach { vehicle ->
            this.pushEvent(VehiclePendingUpdateEvent(this.id.uuid, vehicle.id.uuid))
        }
    }
}
