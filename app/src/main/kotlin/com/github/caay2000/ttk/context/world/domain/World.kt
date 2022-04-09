package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.core.domain.Aggregate
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.core.event.StopCreatedEvent
import com.github.caay2000.ttk.context.core.event.WorldCreatedEvent
import com.github.caay2000.ttk.context.core.event.WorldUpdatedEvent
import com.github.caay2000.ttk.context.vehicle.domain.Vehicle

class World(val id: WorldId, val stops: Set<Stop>) : Aggregate() {

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

    private val vehicles: MutableList<Vehicle> = mutableListOf()

    fun addCargo(cargo: Cargo) {
        val stop = stops.find { it.location == Location.FACTORY }!!
        stop.addCargo(cargo)
    }

    fun getStop(location: Location) = this.stops.first { it.location == location }

    fun isCompleted(): Boolean = stops.all { it.cargo.isEmpty() } && vehicles.all { it.isEmpty() }

    fun update() {

        this.vehicles.forEach { vehicle ->
            vehicle.update()
            pushEvents(vehicle.pullEvents())
        }
        pushEvent(WorldUpdatedEvent(this.id.uuid, this.isCompleted()))
    }
}
