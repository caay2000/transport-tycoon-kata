package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleType

data class Vehicle internal constructor(val id: VehicleId, val type: VehicleType, val cargoId: CargoId?, val updated: Int = 0) {

    companion object {
        fun create(id: VehicleId, type: VehicleType) = Vehicle(id, type, null)
    }

    fun isEmpty() = cargoId == null
}
