package com.github.caay2000.ttk.domain

import com.github.caay2000.ttk.api.inbound.Cargo

data class Stop(val location: Location) {

    companion object {
        fun get(location: Location) = stops[location]!!
        fun all() = stops.values.toList()

        private val stops = mapOf(
            Location.FACTORY to Stop(Location.FACTORY),
            Location.PORT to Stop(Location.PORT),
            Location.WAREHOUSE_A to Stop(Location.WAREHOUSE_A),
            Location.WAREHOUSE_B to Stop(Location.WAREHOUSE_B)
        )
    }

    private var _cargo = mutableListOf<Cargo>()
    val cargo: List<Cargo>
        get() = _cargo.toList()

    fun hasCargo(): Boolean = _cargo.isEmpty().not()

    fun addCargo(cargo: Cargo): Stop {
        _cargo.add(cargo)
        return this
    }

    fun distanceTo(stop: Stop) = this.location.distanceTo(stop.location)

    fun retrieveCargo(): Cargo = _cargo.removeFirst()
    fun deliverCargo(cargo: Cargo) {
        if (cargo.destination != this.location) {
            addCargo(cargo)
        }
    }
}
