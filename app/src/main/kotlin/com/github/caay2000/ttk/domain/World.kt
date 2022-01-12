package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.Event

class World(val stops: List<Stop>) {

    private val _vehicles: MutableList<Vehicle> = mutableListOf()
    val vehicles: List<Vehicle>
        get() = _vehicles.toList()

    var events: List<Event> = emptyList()
        get() = field.toList()
        private set

    var time: Int = 0
        private set

    fun getStop(location: Location) = this.stops.first { it.location == location }

    fun createVehicle(type: VehicleType, startingLocation: Location) {
        _vehicles.add(Vehicle.create(this, type, startingLocation))
    }

    fun addCargo(cargos: List<Cargo>) {
        cargos.forEach { cargo ->
            val stop = stops.find { it.location == Location.FACTORY }!!
            stop.addCargo(cargo)
        }
    }

    fun isCompleted(): Boolean = stops.all { it.cargo.isEmpty() } && vehicles.all { it.isEmpty() }

    fun update(): List<Event> = vehicles.fold(
        initial = listOf<Event>(),
        operation = { list, vehicle ->
            list + vehicle.update()
        }
    ).also {
        this.events = this.events + it
        if (this.isCompleted().not()) time++
    }
}
