package com.github.caay2000.ttk.context.core.event

import com.github.caay2000.ttk.context.core.domain.EventId

interface Event {

    val eventId: EventId
    fun type(): String = this::class.java.simpleName
}
