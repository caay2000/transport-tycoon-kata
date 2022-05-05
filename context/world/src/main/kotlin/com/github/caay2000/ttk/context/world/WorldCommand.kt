package com.github.caay2000.ttk.context.world

import com.github.caay2000.ttk.lib.eventbus.command.Command
import java.util.UUID

interface WorldCommand : Command {

    val worldId: UUID
}
