package com.github.caay2000.ttk.context.vehicle.configuration.domain

import com.github.caay2000.ttk.context.shared.domain.VehicleType
import com.github.caay2000.ttk.lib.eventbus.domain.Aggregate

data class VehicleConfiguration internal constructor(val type: VehicleType, val loadTime: Int, val speed: Double, val capacity: Int) : Aggregate() {

    companion object {
        fun create(type: VehicleType, loadTime: Int, speed: Double, capacity: Int) = VehicleConfiguration(type, loadTime, speed, capacity)
    }
}
