package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.core.domain.Aggregate
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.time.domain.DateTime
import com.github.caay2000.ttk.context.vehicle.domain.Vehicle
import com.github.caay2000.ttk.context.vehicle.domain.VehicleType

class World(val id: WorldId, val stops: List<Stop>) : Aggregate() {

    private val vehicles: MutableList<Vehicle> = mutableListOf()

    fun getStop(location: Location) = this.stops.first { it.location == location }

    fun createVehicle(type: VehicleType, startingLocation: Location) {
        vehicles.add(Vehicle.create(this, type, startingLocation))
    }

    fun addCargo(cargo: Cargo) {
        val stop = stops.find { it.location == Location.FACTORY }!!
        stop.addCargo(cargo)
    }

    fun isCompleted(): Boolean = stops.all { it.cargo.isEmpty() } && vehicles.all { it.isEmpty() }

    fun update(dateTime: DateTime) {

        this.vehicles.forEach { vehicle ->
            vehicle.update(dateTime)
            pushEvents(vehicle.pullEvents())
        }
        pushEvent(WorldUpdatedEvent(this.id, dateTime, this.isCompleted()))
    }
}
