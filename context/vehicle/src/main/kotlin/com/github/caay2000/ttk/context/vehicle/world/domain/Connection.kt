package com.github.caay2000.ttk.context.vehicle.world.domain

import com.github.caay2000.ttk.context.shared.domain.Distance
import com.github.caay2000.ttk.context.shared.domain.StopId
import com.github.caay2000.ttk.context.shared.domain.VehicleTypeEnum

data class Connection(
    val sourceStopId: StopId,
    val targetStopId: StopId,
    val distance: Distance,
    val allowedVehicleTypes: Set<VehicleTypeEnum>
) {

    fun reverse(): Connection = this.copy(
        sourceStopId = this.targetStopId,
        targetStopId = this.sourceStopId
    )
}
