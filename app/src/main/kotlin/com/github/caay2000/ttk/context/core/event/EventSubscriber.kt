package com.github.caay2000.ttk.context.core.event

interface EventSubscriber<in EVENT : Event> {

    fun handle(event: EVENT)
}
