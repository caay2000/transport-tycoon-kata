package com.github.caay2000.ttk.lib.eventbus.event

import com.github.caay2000.ttk.lib.eventbus.impl.KTEventPublisher

class EventPublisherImpl : EventPublisher<Event> {

    private val eventPublisher = KTEventPublisher<Event>()

    override fun publish(event: Event) = this.publish(listOf(event))
    override fun publish(events: List<Event>) =
        events.forEach { event ->

            println(event)
            eventPublisher.publish(event)
        }

}
