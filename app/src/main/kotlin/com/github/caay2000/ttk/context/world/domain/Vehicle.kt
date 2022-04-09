package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.core.domain.CargoId
import com.github.caay2000.ttk.context.core.domain.VehicleId

data class Vehicle(val id: VehicleId, val type: String, val cargoId: CargoId?, val status: String, val updated: Int = 0) {

    fun isEmpty() = cargoId == null
}
