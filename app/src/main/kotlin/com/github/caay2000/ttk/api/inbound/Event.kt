package com.github.caay2000.ttk.api.inbound

import com.github.caay2000.ttk.domain.Location
import com.github.caay2000.ttk.domain.VehicleType
import java.util.UUID

sealed class Event(val id: UUID) {
    abstract val time: Int
}

sealed class VehicleEvent(id: UUID) : Event(id)

data class DepartEvent(
    override val time: Int,
    val vehicleId: UUID,
    val type: VehicleType,
    val location: Location,
    val destination: Location,
    val cargo: Cargo
) : VehicleEvent(UUID.randomUUID())

data class ArriveEvent(
    override val time: Int,
    val vehicleId: UUID,
    val type: VehicleType,
    val location: Location,
    val cargo: Cargo
) : VehicleEvent(UUID.randomUUID())
