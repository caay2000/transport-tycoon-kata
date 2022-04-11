package com.github.caay2000.ttk.context.audit.domain.repository

import com.github.caay2000.ttk.lib.eventbus.event.Event

interface EventRepository {

    fun save(event: Event)
    fun getAll(): List<Event>
}
