package com.github.caay2000.ttk.domain.event

import com.github.caay2000.ttk.domain.Location
import com.github.caay2000.ttk.domain.Vehicle

sealed class VehicleEvent {
    abstract val eventTime: Int
    abstract val location: Location
    abstract val vehicle: Vehicle
}

data class Departed(
    override val eventTime: Int,
    override val location: Location,
    override val vehicle: Vehicle
) : VehicleEvent()

data class Arrived(
    override val eventTime: Int,
    override val location: Location,
    override val vehicle: Vehicle
) : VehicleEvent()
