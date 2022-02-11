package com.github.caay2000.ttk.context.world.application

import com.github.caay2000.ttk.context.core.command.Command
import java.util.UUID

interface WorldCommand : Command {

    val worldId: UUID
}
