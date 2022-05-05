package com.github.caay2000.ttk.lib.eventbus.command

import java.util.UUID

interface Command {

    val commandId: UUID
    fun type(): String = this::class.java.simpleName

    fun randomUUID(): UUID = UUID.randomUUID()
}
