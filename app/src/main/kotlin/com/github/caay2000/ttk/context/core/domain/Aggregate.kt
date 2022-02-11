package com.github.caay2000.ttk.context.core.domain

import com.github.caay2000.ttk.context.core.event.Event

abstract class Aggregate {
    private var events: MutableList<Event> = mutableListOf()

    fun pullEvents(): List<Event> {
        val pulledEvents: MutableList<Event> = events
        events = mutableListOf()
        return pulledEvents.toList()
    }

    fun <E : Event> pushEvent(event: E) = this.events.add(event)

    fun <E : Event> pushEvents(events: List<E>) = this.events.addAll(events)
}
