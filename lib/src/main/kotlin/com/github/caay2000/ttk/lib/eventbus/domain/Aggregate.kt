package com.github.caay2000.ttk.lib.eventbus.domain

import com.github.caay2000.ttk.lib.eventbus.event.Event

abstract class Aggregate {
    private var events: MutableSet<Event> = mutableSetOf()

    fun pullEvents(): List<Event> {
        val pulledEvents: MutableSet<Event> = events
        events = mutableSetOf()
        return pulledEvents.toList()
    }

    fun <E : Event> pushEvent(event: E) = this.events.add(event)

    fun <E : Event> pushEvents(events: List<E>) = this.events.addAll(events)
}
