package com.github.caay2000.ttk.context.core.event

interface EventPublisher {
    fun <E : Event> publish(event: E)
    fun <E : Event> publish(events: List<E>)
}
