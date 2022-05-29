package com.github.caay2000.ttk.context.world.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.VehicleId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import java.util.UUID

data class Vehicle internal constructor(val id: VehicleId, val type: VehicleTypeEnum, val cargoId: CargoId?, val taskFinished: Boolean, val updated: UUID = UUID.randomUUID()) {

    companion object {
        fun create(id: VehicleId, type: VehicleTypeEnum) = Vehicle(id, type, null, true)
    }

    fun isEmpty() = cargoId == null
}
