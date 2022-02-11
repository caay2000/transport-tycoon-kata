package com.github.caay2000.ttk.lib.eventbus

class KTEventPublisher<in EVENT> {

    fun publish(event: EVENT) = KTEventBus.getInstance<Any, EVENT>().publishEvent(event)
}
