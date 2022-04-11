package com.github.caay2000.ttk.context.vehicle.domain

import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.world.domain.Location
import com.github.caay2000.ttk.lib.eventbus.event.Event
import java.util.UUID

sealed class VehicleEvent : Event {
    override val eventId: UUID = randomUUID()
    abstract val vehicleId: VehicleId
}

class ArrivedEvent(
    override val vehicleId: VehicleId,
    val type: VehicleType,
    val location: Location,
    val cargo: Cargo
) : VehicleEvent()

class DepartedEvent(
    override val vehicleId: VehicleId,
    val type: VehicleType,
    val location: Location,
    val destination: Location,
    val cargo: Cargo
) : VehicleEvent()
