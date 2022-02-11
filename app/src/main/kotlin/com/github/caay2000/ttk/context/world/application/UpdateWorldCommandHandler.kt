package com.github.caay2000.ttk.context.world.application

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.command.WorldCommand
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.world.domain.service.WorldUpdaterService
import java.util.UUID

class UpdateWorldCommandHandler(private val worldUpdater: WorldUpdaterService) : CommandHandler<UpdateWorldCommand> {

    override fun invoke(command: UpdateWorldCommand) {
        worldUpdater.invoke(command.worldId.toDomainId())
    }
}

data class UpdateWorldCommand(override val worldId: UUID) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
