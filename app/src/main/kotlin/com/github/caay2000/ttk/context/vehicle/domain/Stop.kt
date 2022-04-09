package com.github.caay2000.ttk.context.vehicle.domain

import com.github.caay2000.ttk.context.core.domain.StopId
import com.github.caay2000.ttk.context.world.domain.Location

data class Stop(val id: StopId, val name: String, var cargo: MutableList<Cargo>) {

    fun hasCargo() = cargo.isNotEmpty()
    fun retrieveCargo(): Cargo = cargo.removeFirst()

    fun addCargo(cargo: Cargo) {
        this.cargo.add(cargo)
    }

    fun deliverCargo(cargo: Cargo) {
        if (cargo.targetStopName != this.name) {
            this.cargo.add(cargo)
        }
    }

    fun distanceTo(stop: Stop) = Location.valueOf(this.name).distanceTo(Location.valueOf(stop.name))
}
