package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.api.inbound.Cargo
import com.github.caay2000.ttk.api.inbound.Event

data class World(val stops: List<Stop>, val vehicles: List<Vehicle>) {

    var time: Int = 0
        private set

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
    ).also { if (this.isCompleted().not()) time++ }
}
