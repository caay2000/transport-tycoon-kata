package com.github.caay2000.ttk.context.vehicle.domain.world

import com.github.caay2000.ttk.context.shared.domain.Location
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId
import com.github.caay2000.ttk.context.vehicle.domain.cargo.Cargo

data class Stop(val worldId: WorldId, val id: StopId, val name: String, var cargo: List<Cargo>) {

    companion object {
        fun create(worldId: WorldId, id: StopId, name: String): Stop = Stop(worldId, id, name, mutableListOf())
    }

    fun hasCargo() = cargo.isNotEmpty()
//    fun retrieveCargo(): Cargo = cargo.removeFirst()

    fun addCargo(cargo: Cargo) {
//        this.cargo.add(cargo)
    }

    fun deliverCargo(cargo: Cargo) {
//        if (cargo.targetStopName != this.name) {
//            this.cargo.add(cargo)
//        }
    }

    fun distanceTo(stop: Stop) = Location.valueOf(this.name).distanceTo(Location.valueOf(stop.name))
}
