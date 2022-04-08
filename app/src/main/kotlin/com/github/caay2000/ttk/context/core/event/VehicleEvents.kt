package com.github.caay2000.ttk.context.core.event

import com.github.caay2000.ttk.context.core.domain.EventId
import java.util.UUID

sealed class VehicleEvent : Event {
    override val eventId: EventId = EventId()
    abstract val vehicleId: UUID
}

data class VehiclePendingUpdateEvent(
    override val vehicleId: UUID
) : VehicleEvent()
