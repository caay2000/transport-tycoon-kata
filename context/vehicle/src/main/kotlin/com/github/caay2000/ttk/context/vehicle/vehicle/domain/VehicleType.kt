package com.github.caay2000.ttk.context.vehicle.vehicle.domain

import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum
import com.github.caay2000.ttk.context.vehicle.configuration.domain.VehicleConfiguration

data class VehicleType(
    val type: VehicleTypeEnum,
    val loadTime: Int,
    val speed: Double
) {
    companion object {
        fun from(configuration: VehicleConfiguration) =
            VehicleType(
                type = configuration.type,
                loadTime = configuration.loadTime,
                speed = configuration.speed
            )
    }
}
