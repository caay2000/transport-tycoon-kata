package com.github.caay2000.ttk.context.vehicle.domain

import com.github.caay2000.ttk.context.core.domain.EventId
import com.github.caay2000.ttk.context.core.domain.VehicleId
import com.github.caay2000.ttk.context.core.event.Event
import com.github.caay2000.ttk.context.time.domain.DateTime
import com.github.caay2000.ttk.context.world.domain.Cargo
import com.github.caay2000.ttk.context.world.domain.Location

sealed class VehicleEvent : Event {
    override val eventId: EventId = EventId()
    abstract val vehicleId: VehicleId
}

class ArrivedEvent(
    override val vehicleId: VehicleId,
    override val time: DateTime,
    val type: VehicleType,
    val location: Location,
    val cargo: Cargo
) : VehicleEvent()

class DepartedEvent(
    override val vehicleId: VehicleId,
    override val time: DateTime,
    val type: VehicleType,
    val location: Location,
    val destination: Location,
    val cargo: Cargo
) : VehicleEvent()
