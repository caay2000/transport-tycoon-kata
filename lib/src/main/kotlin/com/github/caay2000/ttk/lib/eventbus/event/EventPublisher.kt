package com.github.caay2000.ttk.lib.eventbus.event

interface EventPublisher<E : Event> {
    fun publish(event: E)
    fun publish(events: List<E>)
}
