package com.github.caay2000.ttk.context.vehicle.world.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.WorldId

data class Stop(val worldId: WorldId, val id: StopId, val name: String, var cargo: List<Cargo>) {

    companion object {
        fun create(worldId: WorldId, id: StopId, name: String): Stop = Stop(worldId, id, name, emptyList())
    }

    fun produceCargo(cargo: Cargo): Stop = this.copy(cargo = this.cargo + cargo)
    fun unloadCargo(cargo: Cargo): Stop =
        if (cargo.targetId == this.id) this
        else this.copy(cargo = this.cargo + cargo)

    fun consumeCargo(cargoId: CargoId): Stop = this.copy(cargo = this.cargo.filter { it.id != cargoId })
    fun reserveCargo(cargoId: CargoId): Stop = this.copy(cargo = this.cargo.map { if (it.id == cargoId) it.copy(reserved = true) else it })
}
