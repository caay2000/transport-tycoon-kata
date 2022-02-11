package com.github.caay2000.ttk.context.core.event

import com.github.caay2000.ttk.context.core.domain.EventId
import com.github.caay2000.ttk.context.time.domain.DateTime

interface Event {

    val eventId: EventId
    val time: DateTime
    fun type(): String = this::class.java.simpleName
}
