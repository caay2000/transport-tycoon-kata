package com.github.caay2000.ttk.context.vehicle.domain

import com.github.caay2000.ttk.context.core.domain.CargoId
import com.github.caay2000.ttk.context.core.domain.StopId

data class Cargo(val id: CargoId, val sourceStopId: StopId, val sourceStopName: String, val targetStopId: StopId, val targetStopName: String)
