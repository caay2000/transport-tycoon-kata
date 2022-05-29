package com.github.caay2000.ttk.context.shared.event

import com.github.caay2000.ttk.lib.eventbus.event.Event
import java.util.UUID

sealed class VehicleEvent : Event {
    override val eventId: UUID = randomUUID()
    abstract val vehicleId: UUID
}

data class VehiclePendingUpdateEvent(
    val worldId: UUID,
    override val vehicleId: UUID
) : VehicleEvent()

data class VehicleUpdatedEvent(
    val worldId: UUID,
    override val vehicleId: UUID,
    val type: String,
    val cargoId: UUID?,
    val status: String,
    val taskFinished: Boolean
) : VehicleEvent()

data class VehicleLoadingEvent(
    val worldId: UUID,
    override val vehicleId: UUID,
    val cargoId: UUID,
    val stopId: UUID
) : VehicleEvent()

data class VehicleLoadedEvent(
    val worldId: UUID,
    override val vehicleId: UUID,
    val cargoId: UUID,
    val stopId: UUID,
    val sourceStopId: UUID,
    val targetStopId: UUID
) : VehicleEvent()

data class VehicleUnloadingEvent(
    val worldId: UUID,
    override val vehicleId: UUID,
    val cargoId: UUID,
    val stopId: UUID
) : VehicleEvent()

data class VehicleUnloadedEvent(
    val worldId: UUID,
    override val vehicleId: UUID,
    val cargoId: UUID,
    val stopId: UUID,
    val sourceStopId: UUID,
    val targetStopId: UUID
) : VehicleEvent()
