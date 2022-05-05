package com.github.caay2000.ttk.context.shared.event

import com.github.caay2000.ttk.lib.eventbus.event.Event
import java.util.UUID

sealed class WorldEvent : Event {
    override val eventId: UUID = randomUUID()
    abstract val worldId: UUID
}

data class WorldCreatedEvent(
    override val worldId: UUID
) : WorldEvent()

data class StopCreatedEvent(
    override val worldId: UUID,
    val stopId: UUID,
    val stopName: String
) : WorldEvent()

data class ConnectionCreatedEvent(
    override val worldId: UUID,
    val sourceStopId: UUID,
    val targetStopId: UUID,
    val distance: Int,
    val allowedVehicleTypes: Set<String>
) : WorldEvent()

data class VehicleCreatedEvent(
    override val worldId: UUID,
    val stopId: UUID,
    val vehicleId: UUID,
    val type: String,
) : WorldEvent()

data class WorldUpdatedEvent(
    override val worldId: UUID,
    val completed: Boolean
) : WorldEvent()

data class CargoProducedEvent(
    override val worldId: UUID,
    val cargoId: UUID,
    val sourceId: UUID,
    val targetId: UUID
) : WorldEvent()
