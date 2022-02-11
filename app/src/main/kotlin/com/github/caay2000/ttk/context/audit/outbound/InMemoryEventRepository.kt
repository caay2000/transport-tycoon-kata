package com.github.caay2000.ttk.context.audit.outbound

import com.github.caay2000.ttk.context.audit.domain.repository.EventRepository
import com.github.caay2000.ttk.context.core.event.Event
import com.github.caay2000.ttk.context.vehicle.domain.ArrivedEvent
import com.github.caay2000.ttk.context.vehicle.domain.DepartedEvent
import com.github.caay2000.ttk.lib.database.InMemoryDatabase

class InMemoryEventRepository(private val database: InMemoryDatabase) : EventRepository {

    override fun save(event: Event) {
        database.save(DATABASE_TABLE, event.eventId.rawId, event)
    }

    override fun getAll() = database.getAll(DATABASE_TABLE)
        .map { (it as Event).mapToEvent() }

    private fun Event.mapToEvent(): Event = when (this) {
        is DepartedEvent -> this
        is ArrivedEvent -> this
        else -> throw IllegalArgumentException("Invalid event $this")
    }

    companion object {
        private const val DATABASE_TABLE = "event"
    }
}
