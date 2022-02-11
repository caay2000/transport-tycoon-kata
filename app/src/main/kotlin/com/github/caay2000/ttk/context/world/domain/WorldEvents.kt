package com.github.caay2000.ttk.context.world.domain

import com.github.caay2000.ttk.context.core.domain.EventId
import com.github.caay2000.ttk.context.core.domain.WorldId
import com.github.caay2000.ttk.context.core.event.Event
import com.github.caay2000.ttk.context.time.domain.DateTime

sealed class WorldEvent : Event {
    override val eventId: EventId = EventId()
    abstract val worldId: WorldId
}

data class WorldUpdatedEvent(
    override val worldId: WorldId,
    override val time: DateTime,
    val completed: Boolean
) : WorldEvent()
