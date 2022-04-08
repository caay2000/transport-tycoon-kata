package com.github.caay2000.ttk.context.world.application.handler

import com.github.caay2000.ttk.context.core.command.Command
import java.util.UUID

interface VehicleCommand : Command {

    val worldId: UUID
}
