package com.github.caay2000.ttk.lib.eventbus.event

import java.util.UUID

interface Event {

    val eventId: UUID
    fun type(): String = this::class.java.simpleName

    fun randomUUID(): UUID = UUID.randomUUID()
}
