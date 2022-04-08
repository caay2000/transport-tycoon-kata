package com.github.caay2000.ttk.context.vehicle.domain

import com.github.caay2000.ttk.context.core.domain.StopId
import com.github.caay2000.ttk.context.world.domain.Location

data class Stop(val id: StopId, val name: String, var cargo: MutableList<Cargo>) {

    fun hasCargo() = cargo.isNotEmpty()
    fun retrieveCargo(): Cargo = cargo.removeFirst()

    fun deliverCargo(delivery: Cargo) {
        if (delivery.targetStopName != this.name) {
            cargo.add(delivery)
        }
    }


    fun distanceTo(stop: Stop) = Location.valueOf(this.name).distanceTo(Location.valueOf(stop.name))

}
