package com.github.caay2000.ttk.context.vehicle.stop.domain

import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleType

data class Connection(
    val sourceStopId: StopId,
    val targetStopId: StopId,
    val distance: Distance,
    val allowedVehicleTypes: Set<VehicleType>
)
