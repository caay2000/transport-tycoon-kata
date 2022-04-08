package com.github.caay2000.ttk.context.world.application.handler

import com.github.caay2000.ttk.context.core.command.CommandHandler
import com.github.caay2000.ttk.context.core.domain.toDomainId
import com.github.caay2000.ttk.context.core.event.EventPublisher
import com.github.caay2000.ttk.context.world.application.repository.WorldRepository
import com.github.caay2000.ttk.context.world.application.service.WorldUpdaterService
import java.util.UUID

class UpdateWorldCommandHandler(eventPublisher: EventPublisher, worldRepository: WorldRepository) : CommandHandler<UpdateWorldCommand> {

    private val worldUpdater = WorldUpdaterService(eventPublisher, worldRepository)

    override fun invoke(command: UpdateWorldCommand) {
        worldUpdater.invoke(command.worldId.toDomainId())
    }
}

data class UpdateWorldCommand(override val worldId: UUID) : WorldCommand {
    override val commandId: UUID = UUID.randomUUID()
}
