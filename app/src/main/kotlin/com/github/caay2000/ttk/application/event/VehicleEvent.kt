package com.github.caay2000.ttk.application.event

import com.github.caay2000.ttk.domain.Vehicle

sealed class VehicleEvent {
    abstract val eventTime: Int
    abstract val vehicle: Vehicle
}

data class RouteAssigned(override val eventTime: Int, override val vehicle: Vehicle) : VehicleEvent()
data class RouteStop(override val eventTime: Int, override val vehicle: Vehicle) : VehicleEvent()
data class RouteEnd(override val eventTime: Int, override val vehicle: Vehicle) : VehicleEvent()
