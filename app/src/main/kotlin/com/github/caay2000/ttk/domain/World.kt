package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.api.inbound.Cargo

data class World(val stops: List<Stop>, val vehicles: List<Vehicle>) {

    var time: Int = -1
        private set

    fun addCargo(cargos: List<Cargo>) {
        cargos.forEach { cargo ->
            val stop = stops.find { it.location == Location.FACTORY }!!
            stop.addCargo(cargo)
        }
    }

    fun isCompleted(): Boolean = stops.all { it.cargo.isEmpty() } && vehicles.all { it.isEmpty() }

    fun update() {

        vehicles.forEach {
            it.update()
        }
        time++
    }
}
