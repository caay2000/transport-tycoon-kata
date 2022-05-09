package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum

data class VehicleType(
    val type: VehicleTypeEnum,
    val loadTime: Int,
    val speed: Double
)
