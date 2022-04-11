package com.github.caay2000.ttk.context.vehicle.domain.cargo

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId

data class Cargo(val id: CargoId, val sourceStopId: StopId, val sourceStopName: String, val targetStopId: StopId, val targetStopName: String)
