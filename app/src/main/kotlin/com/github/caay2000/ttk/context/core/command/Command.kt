package com.github.caay2000.ttk.context.core.command

import java.util.UUID

interface Command {

    val commandId: UUID
    fun type(): String = this::class.java.simpleName
}

interface WorldCommand : Command {

    val worldId: UUID
}
