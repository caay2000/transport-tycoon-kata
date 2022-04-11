package com.github.caay2000.ttk.lib.eventbus.event

interface EventSubscriber<in EVENT : Event> {

    fun handle(event: EVENT)
}
