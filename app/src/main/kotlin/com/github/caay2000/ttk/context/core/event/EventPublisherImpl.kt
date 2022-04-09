package com.github.caay2000.ttk.context.core.event

import com.github.caay2000.ttk.lib.eventbus.KTEventPublisher

class EventPublisherImpl : EventPublisher {

    private val eventPublisher = KTEventPublisher<Event>()

    override fun <E : Event> publish(event: E) = this.publish(listOf(event))
    override fun <E : Event> publish(events: List<E>) =
        events.forEach { event ->

            println(event)
            eventPublisher.publish(event)
        }
}
