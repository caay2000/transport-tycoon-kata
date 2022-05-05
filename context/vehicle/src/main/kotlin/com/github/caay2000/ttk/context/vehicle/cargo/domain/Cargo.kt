package com.github.caay2000.ttk.context.vehicle.cargo.domain

import com.github.caay2000.ttk.context.shared.domain.CargoId
import com.github.caay2000.ttk.context.shared.domain.StopId

data class Cargo(val id: CargoId, val sourceId: StopId, val targetId: StopId, val reserved: Boolean) {

    companion object {
        fun create(id: CargoId, sourceId: StopId, targetId: StopId): Cargo = Cargo(id, sourceId, targetId, false)
    }
}
