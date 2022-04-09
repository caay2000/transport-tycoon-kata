package com.github.caay2000.ttk.context.core.event

import com.github.caay2000.ttk.context.core.domain.EventId
import java.util.UUID

sealed class WorldEvent : Event {
    override val eventId: EventId = EventId()
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

data class WorldUpdatedEvent(
    override val worldId: UUID,
    val completed: Boolean
) : WorldEvent()

data class CargoAddedEvent(
    override val worldId: UUID,
    val cargoId: UUID,
    val sourceId: UUID,
    val targetId: UUID
) : WorldEvent()
